/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.util;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.bubble.pilipili.oss.config.OssClientConfig;
import com.bubble.pilipili.oss.config.OssClientPool;
import com.bubble.pilipili.oss.service.UploadProgressListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.21 20:09
 */
@Component
@Slf4j
public class OssUploadHelper {

    @Autowired
    private OssClientPool ossClientPool;

    /**
     * 分片大小 100MB
     * 用于计算文件有多少个分片。单位为字节。
     * 分片最小值为100 KB，最大值为5 GB。最后一个分片的大小允许小于100 KB。
     */
    public static final long PART_SIZE = 100 * 1024 * 1024L;

    private static final double MEGABYTES_TRANSFER_RATIO = 1.0 / (1024.0 * 1024.0);
    private static final BigDecimal MEGABYTES_PER_SECOND_TRANSFER_DIVISOR = BigDecimal.valueOf(1024.0 * 1024.0 / 1000);


    /**
     * 执行上传文件
     * @param file
     * @param objectName
     * @return
     */
    public String doUpload(MultipartFile file, String objectName) {
        OSS ossClient = ossClientPool.fetchClient();
        try {
            PutObjectResult result = ossClient.putObject(
                    ossClientPool.getOssClientConfig().getBucketName(),
                    objectName,
                    file.getInputStream()
            );
            log.info("OSS文件上传成功: {}", result.getETag());
            return objectName;

        } catch (IOException e) {
            log.error("文件流获取失败");
            throw new RuntimeException(e);
        }  catch (OSSException oe) {
            // 上传失败，处理异常
            log.error("OSS文件上传服务端异常: \n{}", oe.getMessage());
            throw oe;
        } catch (ClientException ce) {
            // 客户端异常，例如网络问题等
            log.error("OSS文件上传客户端异常: \n{}", ce.getMessage());
            throw ce;
        }
        finally {
            ossClientPool.releaseClient(ossClient);
        }
    }

    /**
     * 分片上传文件
     * @param file
     * @param objectName
     * @return
     */
    public String partUpload(MultipartFile file, String objectName) {
        OssClientConfig config = ossClientPool.getOssClientConfig();
        String bucketName = config.getBucketName();
        OSS ossClient = ossClientPool.fetchClient();

        try {
            // 创建InitiateMultipartUploadRequest对象。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);

            // 创建ObjectMetadata并设置Content-Type。
            ObjectMetadata metadata = new ObjectMetadata();
            if (metadata.getContentType() == null) {
                metadata.setContentType(file.getContentType());
            }
            log.debug("Content-Type: {}", metadata.getContentType());

            // 将metadata绑定到上传请求中。
            request.setObjectMetadata(metadata);

            // 初始化分片。
            InitiateMultipartUploadResult uploadResult = ossClient.initiateMultipartUpload(request);
            // 返回uploadId。
            String uploadId = uploadResult.getUploadId();

            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
            // 用于所有分片上传完毕后交给OSS校验并合并最终文件
            List<PartETag> partETags = new ArrayList<>();

            // 根据上传的数据大小计算分片数。
            long fileLength = file.getSize();
            int partCount = (int) (fileLength / PART_SIZE);
            if (fileLength % PART_SIZE != 0) {
                partCount++;
            }
            // 遍历分片上传。
            for (int i = 0; i < partCount; i++) {
                long startPos = i * PART_SIZE;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : PART_SIZE;
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                // 设置上传的分片流。 通过InputStream.skip()方法跳过指定数据。
                InputStream inputStream = file.getInputStream();
                inputStream.skip(startPos);
                uploadPartRequest.setInputStream(inputStream);
                // 设置分片大小。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
                uploadPartRequest.setPartNumber(i + 1);

                // 当前分片序号
                final int partNum = i + 1;
                // 分片上传进度监听器
                uploadPartRequest.setProgressListener(new UploadProgressListener(partNum));
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
                partETags.add(uploadPartResult.getPartETag());

                // 关闭流
                inputStream.close();
            }

            // 创建CompleteMultipartUploadRequest对象。
            // 在执行完成分片上传操作时，需要提供所有有效的partETags。
            // OSS收到提交的partETags后，会逐一验证每个分片的有效性。
            // 当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);

            // 完成分片上传。
            CompleteMultipartUploadResult completeUploadResult =
                    ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            log.info("上传成功，ETag：{}", completeUploadResult.getETag());

        } catch (OSSException oe) {
            log.error("OSS文件上传服务端异常: {}", oe.getMessage());
        } catch (ClientException ce) {
            log.error("OSS文件上传客户端异常: \n{}", ce.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ossClientPool.releaseClient(ossClient);
        }
        return objectName;
    }

    /**
     * 文件公共访问临时签名
     * @param objectName
     * @return
     */
    public String getTempSignUrl(String objectName) {
        OSS ossClient = ossClientPool.fetchClient();
        String bucketName = ossClientPool.getOssClientConfig().getBucketName();
        try {
            // 将图片缩放为固定宽高100 px后，再旋转90°。
            String style = "image/resize,m_fixed,w_100,h_100/rotate,90";
            // 指定签名URL过期时间为10分钟。(最长过期时间为32400秒)
            Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
            req.setExpiration(expiration);
            req.setProcess(style);
            URL signedUrl = ossClient.generatePresignedUrl(req);
            return signedUrl.toExternalForm();
        } catch (OSSException oe) {
            log.error("OSS文件上传服务端异常: {}", oe.getMessage());
        } catch (ClientException ce) {
            log.error("OSS文件上传客户端异常: \n{}", ce.getMessage());
        } finally {
            ossClientPool.releaseClient(ossClient);
        }
        return null;
    }


}
