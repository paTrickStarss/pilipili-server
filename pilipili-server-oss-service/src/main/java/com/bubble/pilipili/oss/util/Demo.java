package com.bubble.pilipili.oss.util;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.internal.Mimetypes;
import com.aliyun.oss.model.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Demo {

//    public static void main(String[] args) throws Exception {
//        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
//        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
//        EnvironmentVariableCredentialsProvider credentialsProvider =
//                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "examplebucket";
//        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//        String objectName = "exampledir/exampleobject.txt";
//        // 待上传本地文件路径。
//        String filePath = "D:\\localpath\\examplefile.txt";
//        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
//        String region = "cn-hangzhou";
//
//        // 创建OSSClient实例。
//        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
//        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
//        OSS ossClient = OSSClientBuilder.create()
//                .endpoint(endpoint)
//                .credentialsProvider(credentialsProvider)
//                .clientConfiguration(clientBuilderConfiguration)
//                .region(region)
//                .build();
//
//        try {
//            // 创建InitiateMultipartUploadRequest对象。
//            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);
//
//            // 创建ObjectMetadata并设置Content-Type。
//            ObjectMetadata metadata = new ObjectMetadata();
//            if (metadata.getContentType() == null) {
//                metadata.setContentType(Mimetypes.getInstance().getMimetype(new File(filePath), objectName));
//            }
//            System.out.println("Content-Type: " + metadata.getContentType());
//
//            // 将metadata绑定到上传请求中。
//            request.setObjectMetadata(metadata);
//
//            // 初始化分片。
//            InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
//            // 返回uploadId。
//            String uploadId = upresult.getUploadId();
//
//            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
//            List<PartETag> partETags = new ArrayList<PartETag>();
//            // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
//            // 分片最小值为100 KB，最大值为5 GB。最后一个分片的大小允许小于100 KB。
//            // 设置分片大小为 1 MB。
//            final long partSize = 1 * 1024 * 1024L;
//
//            // 根据上传的数据大小计算分片数。以本地文件为例，说明如何通过File.length()获取上传数据的大小。
//            final File sampleFile = new File(filePath);
//            long fileLength = sampleFile.length();
//            int partCount = (int) (fileLength / partSize);
//            if (fileLength % partSize != 0) {
//                partCount++;
//            }
//            // 遍历分片上传。
//            for (int i = 0; i < partCount; i++) {
//                long startPos = i * partSize;
//                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
//                UploadPartRequest uploadPartRequest = new UploadPartRequest();
//                uploadPartRequest.setBucketName(bucketName);
//                uploadPartRequest.setKey(objectName);
//                uploadPartRequest.setUploadId(uploadId);
//                // 设置上传的分片流。
//                // 以本地文件为例说明如何创建FileInputStream，并通过InputStream.skip()方法跳过指定数据。
//                InputStream instream = new FileInputStream(sampleFile);
//                instream.skip(startPos);
//                uploadPartRequest.setInputStream(instream);
//                // 设置分片大小。
//                uploadPartRequest.setPartSize(curPartSize);
//                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
//                uploadPartRequest.setPartNumber(i + 1);
//                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
//                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
//                // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
//                partETags.add(uploadPartResult.getPartETag());
//
//                // 关闭流
//                instream.close();
//            }
//
//            // 创建CompleteMultipartUploadRequest对象。
//            // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
//            CompleteMultipartUploadRequest completeMultipartUploadRequest =
//                    new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);
//
//            // 完成分片上传。
//            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
//            System.out.println("上传成功，ETag：" + completeMultipartUploadResult.getETag());
//
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught a ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }
}