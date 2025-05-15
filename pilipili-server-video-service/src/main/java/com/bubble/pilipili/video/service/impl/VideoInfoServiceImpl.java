/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.component.EntityConverter;
import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.common.constant.UserVideoLevel;
import com.bubble.pilipili.common.constant.VideoStatus;
import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.service.InteractStatsAction;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.common.util.StringUtil;
import com.bubble.pilipili.feign.api.MQFeignAPI;
import com.bubble.pilipili.feign.api.OssFeignAPI;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.pojo.dto.OssTempSignDTO;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.feign.pojo.req.SendVideoStatsReq;
import com.bubble.pilipili.common.pojo.VideoStats;
import com.bubble.pilipili.video.pojo.dto.QueryCategoryDTO;
import com.bubble.pilipili.video.pojo.dto.QueryUserVideoDTO;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.entity.UserVideo;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import com.bubble.pilipili.video.pojo.param.QueryVideoInfoParam;
import com.bubble.pilipili.video.pojo.req.CreateVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.PageQueryVideoInfoReq;
import com.bubble.pilipili.feign.pojo.req.UpdateVideoInfoReq;
import com.bubble.pilipili.video.repository.CategoryRepository;
import com.bubble.pilipili.video.repository.UserVideoRepository;
import com.bubble.pilipili.video.repository.VideoInfoRepository;
import com.bubble.pilipili.video.service.VideoInfoService;
import com.bubble.pilipili.video.util.VideoRedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 视频信息业务层
 * @author Bubble
 * @date 2025/01/21 15:33
 */
@Slf4j
@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    @Autowired
    private VideoInfoRepository videoInfoRepository;
    @Autowired
    private UserVideoRepository userVideoRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InteractStatsAction interactStatsAction;
    @Autowired
    private EntityConverter entityConverter;

    @Autowired
    private MQFeignAPI MQFeignAPI;
    @Autowired
    private StatsFeignAPI statsFeignAPI;
    @Autowired
    private OssFeignAPI ossFeignAPI;

    @Autowired
    private VideoRedisHelper videoRedisHelper;

    /**
     * 新增视频信息
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean saveVideoInfo(CreateVideoInfoReq req) {
        VideoInfo videoInfo = entityConverter.copyFieldValue(req, VideoInfo.class);
        boolean videoTaskUploadSuccess = videoRedisHelper.isVideoTaskUploadSuccess(req.getTaskId());
        if (videoTaskUploadSuccess) {
            // OSS已经上传好了，状态设为上传完成审核中 1
            videoInfo.setStatus(VideoStatus.AUDITING.getValue());
        } else {
            // OSS还没处理好，状态设为上传中 0
            videoInfo.setStatus(VideoStatus.UPLOADING.getValue());
        }

        Boolean b = videoInfoRepository.saveVideoInfo(videoInfo);
        if (b) {
            // 清除视频信息缓存（若之前缓存过空值，几乎不可能）
            videoRedisHelper.removeCache(RedisKey.VIDEO_INFO, videoInfo.getVid());
            // 清除用户视频vid列表缓存
            videoRedisHelper.removeCacheByKeyPattern(RedisKey.USER_VIDEO_ID_LIST, videoInfo.getUid());

            if (videoTaskUploadSuccess) {
                // OSS已经上传好了，删除缓存的任务ID
                videoRedisHelper.removeVideoTask(req.getTaskId());
            } else {
                // OSS视频上传任务还没完成，缓存任务ID对应的vid，用于更新视频状态
//                videoRedisHelper.saveVideoTask(req.getTaskId(), videoInfo.getVid());
                videoRedisHelper.saveVideoTaskIdWithRLockTask(
                        req.getTaskId(), videoInfo.getVid(),
                        (cache) -> {
                            // 双重检查发现已被缓存，说明这段期间OSS任务完成了，删除缓存的任务ID
                            videoRedisHelper.removeVideoTask(req.getTaskId());
                            // 更新视频状态为审核中。
                            UpdateVideoInfoReq updateReq = new UpdateVideoInfoReq();
                            updateReq.setVid(videoInfo.getVid());
                            updateReq.setStatus(VideoStatus.AUDITING.getValue());
                            // 这里开启新事务
                            updateVideoInfo(updateReq);
                        }
                );
            }
        }
        return b;
    }

    /**
     * 更新视频信息
     * @param req
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Boolean updateVideoInfo(UpdateVideoInfoReq req) {
        VideoInfo videoInfo = entityConverter.copyFieldValue(req, VideoInfo.class);
        if (videoInfo.getVid() == null) {
            throw ServiceOperationException.emptyField("vid");
        }

        Boolean b = videoInfoRepository.updateVideoInfo(videoInfo);
        if (b) {
            videoRedisHelper.removeCache(RedisKey.VIDEO_INFO, videoInfo.getVid());
        }
        return b;
    }

    /**
     * 点赞视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Boolean favorVideoInfo(Integer vid, Integer uid) {
        Boolean b = updateVideoInteract(
                vid, uid,
                uv -> uv.setFavor(1),
                stats -> stats.setFavorCount(1L)
        );
//        revokeDewVideoInfo(vid, uid);
        return b;
    }

    /**
     * 取消点赞视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeFavorVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setFavor(0),
                stats -> stats.setFavorCount(-1L)
        );
    }

    /**
     * 投币视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean coinVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setCoin(1),
                stats -> stats.setCoinCount(1L)
        );
    }

    /**
     * 收藏视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean collectVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setCollect(1),
                stats -> stats.setCollectCount(1L)
        );
    }

    /**
     * 取消收藏
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeCollectVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setCollect(0),
                stats -> stats.setCollectCount(-1L)
        );
    }

    /**
     * 一键三连
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean tripleInteractVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> {
                    uv.setFavor(1);
                    uv.setCoin(1);
                    uv.setCollect(1);
                },
                stats -> {
                    stats.setFavorCount(1L);
                    stats.setCoinCount(1L);
                    stats.setCollectCount(1L);
                }
        );
    }

    /**
     * 转发视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean repostVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setRepost(1),
                stats -> stats.setRepostCount(1L)
        );
    }

    /**
     * 取消转发视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeRepostVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setRepost(0),
                stats -> stats.setRepostCount(-1L)
        );
    }

    /**
     * 点踩视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean dewVideoInfo(Integer vid, Integer uid) {
        Boolean b = updateVideoInteract(
                vid, uid,
                uv -> uv.setDew(1),
                stats -> stats.setDewCount(1L)
        );
//        revokeFavorVideoInfo(vid, uid);
        return b;
    }

    /**
     * 取消点踩视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeDewVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setDew(0),
                stats -> stats.setDewCount(-1L)
        );
    }

    /**
     * 删除视频信息
     * @param vid
     * @return
     */
    @Transactional
    @Override
    public Boolean deleteVideoInfo(Integer vid) {
        Boolean b = videoInfoRepository.deleteVideoInfo(vid);
        if (b) {
            videoRedisHelper.removeCache(RedisKey.VIDEO_INFO, vid);
        }
        return b;
    }

    /**
     * @param vid
     * @return
     */
    @Override
    public QueryVideoInfoDTO getVideoInfoById(Integer vid) {
        VideoInfo videoInfo = videoRedisHelper.queryViaCache(vid,
                RedisKey.VIDEO_INFO,
                videoInfoRepository::getVideoInfoById,
                VideoInfo.class
        );

        List<QueryVideoInfoDTO> dtoList = handleVideoInfo(Collections.singletonList(videoInfo));
        return ListUtil.isEmpty(dtoList) ? null : dtoList.get(0);
    }


    /**
     * 分页查询用户所有视频（用户个人空间用，上传中、审核中、审核通过状态的视频）
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryVideoInfoByUid(PageQueryVideoInfoReq req) {
        List<VideoStatus> statusList = Arrays.asList(
                VideoStatus.UPLOADING,
                VideoStatus.AUDITING,
                VideoStatus.AUDIT_PASSED
        );
        return doPageQueryVideoInfoByUid(req,
                (req1) ->
                    videoInfoRepository.pageQueryVideoInfoByUid(
                        req1.getUid(), req1.getPageNo(), req1.getPageSize(), statusList,
                            true, false, Collections.singletonList(VideoInfo::getUploadTime)
                    ),
                (uid) -> videoInfoRepository.getUserVideoCount(uid, statusList),
                UserVideoLevel.LEVEL_USER
        );
    }

    /**
     * 分页查询用户所有视频（管理员用，所有状态的视频）
     *
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryAllVideoInfoByUid(PageQueryVideoInfoReq req) {
        return doPageQueryVideoInfoByUid(req, (req1) ->
                        videoInfoRepository.pageQueryVideoInfoByUid(
                                req1.getUid(), req1.getPageNo(), req1.getPageSize(),
                                null,
                                true, false, Collections.singletonList(VideoInfo::getUploadTime)
                        ),
                (uid) -> videoInfoRepository.getUserVideoCount(uid, null),
                UserVideoLevel.LEVEL_ADMIN
        );
    }

    /**
     * 分页查询用户已上架视频（对外展示用）
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryPassedVideoInfoByUid(PageQueryVideoInfoReq req) {
        List<VideoStatus> statusList = Collections.singletonList(VideoStatus.AUDIT_PASSED);
        return doPageQueryVideoInfoByUid(req, (req1) ->
                        videoInfoRepository.pageQueryVideoInfoByUid(
                                req1.getUid(), req1.getPageNo(), req1.getPageSize(),
                                statusList,
                                true, false, Collections.singletonList(VideoInfo::getUploadTime)
                        ),
                (uid) -> videoInfoRepository.getUserVideoCount(uid, statusList),
                UserVideoLevel.LEVEL_PUBLIC
        );
    }

    /**
     * 执行分页查询用户视频
     * @param req 请求参数
     * @param queryFunc 查询repo方法
     * @param queryCountFunc 查询视频数量方法
     * @param level 用户视频缓存级别
     * @return
     */
    private PageDTO<QueryVideoInfoDTO> doPageQueryVideoInfoByUid(
            PageQueryVideoInfoReq req,
            Function<PageQueryVideoInfoReq, Page<VideoInfo>> queryFunc,
            Function<Integer, Long> queryCountFunc,
            UserVideoLevel level
    ) {
        List<Integer> vidList = videoRedisHelper.getUserVideoIdList(
                req.getUid(), req.getPageNo().intValue(), req.getPageSize().intValue(), level);
        List<VideoInfo> videoInfoList = new ArrayList<>();
        long total;
        // 若数据库查询结果为空列表，会缓存空列表，所以这里只需要判断是不是空对象就行
        if (vidList == null) {
            // 缓存未命中，查询数据库得到用户视频信息，缓存最新用户视频id列表，以及各个视频信息
            // 查询结果按投稿时间降序排序
            Page<VideoInfo> videoInfoPage = queryFunc.apply(req);
            List<VideoInfo> records = videoInfoPage.getRecords();
            videoInfoList.addAll(records);

            // 缓存最新的用户vid列表
            List<Integer> vidListNew = records.stream().map(VideoInfo::getVid).collect(Collectors.toList());
            videoRedisHelper.saveUserVideoIdList(
                    req.getUid(), req.getPageNo().intValue(), req.getPageSize().intValue(),
                    vidListNew, level
            );
            // 缓存最新用户视频数量
            videoRedisHelper.saveUserVideoCount(req.getUid(), videoInfoPage.getTotal(), level);
            total = videoInfoPage.getTotal();
            // 缓存查询到的视频信息
            records.forEach(videoInfo -> videoRedisHelper.saveVideoInfo(videoInfo.getVid(), videoInfo));

        } else {
            // 缓存命中，根据vid列表查询视频信息
            List<Integer> queryRepoVidList = new ArrayList<>();
            for (Integer vid : vidList) {
                VideoInfo videoInfo = videoRedisHelper.getVideoInfo(vid);
                if (videoInfo == null) {
                    // 缓存为空，则查询数据库。这里不做缓存空值判断，因为用户视频vid一定是已存在的视频
                    queryRepoVidList.add(vid);
                } else {
                    // 缓存命中
                    videoInfoList.add(videoInfo);
                }
            }

            // 查询未命中的vid视频信息
            if (!queryRepoVidList.isEmpty()) {
                List<VideoInfo> queryRepoVideoInfoList = videoInfoRepository.getVideoInfoById(queryRepoVidList);
                queryRepoVideoInfoList.forEach(videoInfo -> {
                    videoInfoList.add(videoInfo);
                    // 缓存最新的视频信息
                    videoRedisHelper.saveVideoInfo(videoInfo.getVid(), videoInfo);
                });
            }
            // 获取用户视频数量缓存值
            Long userVideoCount = videoRedisHelper.getUserVideoCount(req.getUid(), level);
            if (userVideoCount == null) {  // 这条分支不太可能会走，理想情况下，有缓存用户视频vid列表就一定会有缓存用户视频数量
                // 查询数据库获取用户视频数量
                Long queryRepoUserVideoCount = queryCountFunc.apply(req.getUid()); //videoInfoRepository.getUserVideoCount(req.getUid());
                total = queryRepoUserVideoCount == null ? 0L : queryRepoUserVideoCount;
                // 缓存最新的用户视频数量
                videoRedisHelper.saveUserVideoCount(req.getUid(), queryRepoUserVideoCount, level);
            } else {
                total = userVideoCount;
            }
        }

        List<QueryVideoInfoDTO> dtoList = handleVideoInfo(videoInfoList);
        return PageDTO.createPageDTO(
                req.getPageNo(),
                req.getPageSize(),
                total,
                dtoList
        );
    }


    /**
     * 分页条件查询所有已上架视频
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryVideoInfo(PageQueryVideoInfoReq req) {
        QueryVideoInfoParam param = entityConverter.copyFieldValue(req, QueryVideoInfoParam.class);
        Page<VideoInfo> videoInfoPage =
                videoInfoRepository.pageQueryVideoInfo(param, req.getPageNo(), req.getPageSize());

        List<QueryVideoInfoDTO> dtoList = handleVideoInfo(videoInfoPage.getRecords());
        return PageDTO.createPageDTO(
                videoInfoPage.getCurrent(),
                videoInfoPage.getSize(),
                videoInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * 查询用户视频互动状态
     *
     * @param vid
     * @param uid
     * @return
     */
    @Override
    public QueryUserVideoDTO getUserVideo(Integer vid, Integer uid) {
        UserVideo userVideo = videoRedisHelper.queryViaCache2(
                uid, vid,
                RedisKey.USER_VIDEO,
                userVideoRepository::getInteract,
                UserVideo.class
        );
        return entityConverter.copyFieldValue(userVideo, QueryUserVideoDTO.class);
    }

    /**
     * 查询分区列表
     * @return
     */
    @Override
    public List<QueryCategoryDTO> queryCategoryList() {
        List<QueryCategoryDTO> categoryList = videoRedisHelper.getCategoryList();
        if (categoryList == null) {
            List<QueryCategoryDTO> dtoList = categoryRepository.queryAllCategory()
                    .stream()
                    .map(entity ->
                            new QueryCategoryDTO(
                                    entity.getPrimaryCategoryId(), entity.getName())
                    )
                    .collect(Collectors.toList());
            videoRedisHelper.saveCategoryList(dtoList);
            return dtoList;
        }
        return categoryList;
    }

    /**
     * 获取视频统计数据、临时访问链接并返回DTO
     * @param videoInfoList
     * @return
     */
    List<QueryVideoInfoDTO> handleVideoInfo(List<VideoInfo> videoInfoList) {
        if (ListUtil.isEmpty(videoInfoList)) {
            return Collections.emptyList();
        }
        List<QueryVideoInfoDTO> dtoList =
                entityConverter.copyFieldValueList(
                        videoInfoList, QueryVideoInfoDTO.class);

        // feignAPI获取视频统计数据
        List<Integer> vidList = dtoList.stream().map(QueryVideoInfoDTO::getVid).collect(Collectors.toList());
        SimpleResponse<QueryStatsDTO<VideoStats>> statsResp = statsFeignAPI.getVideoStats(vidList);
        Map<Integer, VideoStats> statsMap = statsResp.getData().getStatsMap();

        // 获取临时访问链接
        List<String> contentUrlList = dtoList.stream()
                .map(QueryVideoInfoDTO::getContentUrl)
                .filter(ossUrl -> ossUrl != null && !ossUrl.startsWith("http"))
                .collect(Collectors.toList());
        List<String> coverUrlList = dtoList.stream()
                .map(QueryVideoInfoDTO::getCoverUrl)
                .filter(ossUrl -> ossUrl != null && !ossUrl.startsWith("http"))
                .collect(Collectors.toList());
        List<String> objectNameList =
                Stream.concat(contentUrlList.stream(), coverUrlList.stream())
                        .collect(Collectors.toList());
        SimpleResponse<OssTempSignDTO> signResp = ossFeignAPI.getTempSigns(objectNameList);
        Map<String, String> urlMap = signResp.getData().getUrlMap();

        // 装填DTO
        dtoList.forEach(dto -> {
            // 装填统计数据
            VideoStats stats = statsMap.get(dto.getVid());
            if (stats != null) {
                dto.setViewCount(stats.getViewCount());
                dto.setDanmakuCount(stats.getDanmakuCount());
                dto.setFavorCount(stats.getFavorCount());
                dto.setCoinCount(stats.getCoinCount());
                dto.setCollectCount(stats.getCollectCount());
                dto.setRepostCount(stats.getRepostCount());
                dto.setDewCount(stats.getDewCount());
            }

            // 装填临时访问链接
            String contentUrl = urlMap.get(dto.getContentUrl());
            if (StringUtil.isNotEmpty(contentUrl)) {
                dto.setContentUrl(contentUrl);
            }
            String coverUrl = urlMap.get(dto.getCoverUrl());
            if (StringUtil.isNotEmpty(coverUrl)) {
                dto.setCoverUrl(coverUrl);
            }

            // tag转数组
            dto.setTagList(getTagList(dto.getTag()));
        });

        return dtoList;
    }

    /**
     * 更新视频互动关系数据
     * @param vid
     * @param uid
     * @param interactConsumer
     * @param statsConsumer
     * @return
     */
    private Boolean updateVideoInteract(
            Integer vid, Integer uid,
            Consumer<UserVideo> interactConsumer,
            Consumer<VideoStats> statsConsumer
    ) {
        try {
            Boolean b = interactStatsAction.updateInteract(
                    UserVideo.class,
                    vid, uid,
                    UserVideo::setVid,
                    userVideoRepository,
                    interactConsumer
            );
            if (b) {
                // 删除用户视频互动状态缓存
                videoRedisHelper.removeCache(RedisKey.USER_VIDEO, uid, vid);

                VideoStats stats = new VideoStats();
                statsConsumer.accept(stats);

                // 这里不做统计数据的缓存更新，该部分缓存由统计数据管理服务去做，不需要保证统计数据更新的实时一致性

                // 推送统计数据更新消息
                SendVideoStatsReq req =
                        entityConverter.copyFieldValue(stats, SendVideoStatsReq.class);
                req.setVid(vid);
                MQFeignAPI.sendVideoStats(req);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new ServiceOperationException("更新视频互动关系数据异常");
        }
    }

    private VideoStats updateStatsValue(VideoStats oldStats, VideoStats stats) {
        VideoStats newStats = new VideoStats();
        if (stats.getViewCount() != null) {
            newStats.setViewCount(oldStats.getViewCount() + stats.getViewCount());
        }
        if (stats.getDanmakuCount() != null) {
            newStats.setDanmakuCount(oldStats.getDanmakuCount() + stats.getDanmakuCount());
        }
        if (stats.getCommentCount() != null) {
            newStats.setCommentCount(oldStats.getCommentCount() + stats.getCommentCount());
        }
        if (stats.getFavorCount() != null) {
            newStats.setFavorCount(oldStats.getFavorCount() + stats.getFavorCount());
        }
        if (stats.getCoinCount() != null) {
            newStats.setCoinCount(oldStats.getCoinCount() + stats.getCoinCount());
        }
        if (stats.getCollectCount() != null) {
            newStats.setCollectCount(oldStats.getCollectCount() + stats.getCollectCount());
        }
        if (stats.getRepostCount() != null) {
            newStats.setRepostCount(oldStats.getRepostCount() + stats.getRepostCount());
        }
        if (stats.getDewCount() != null) {
            newStats.setDewCount(oldStats.getDewCount() + stats.getDewCount());
        }
        return newStats;
    }

    private List<String> getTagList(String tag) {
        if (StringUtil.isEmpty(tag)) {
            return Collections.emptyList();
        }
        return Arrays
                .stream(tag.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
