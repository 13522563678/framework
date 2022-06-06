package com.kcwl.framework.file.biz.service.dfs.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ResponseHeaderOverrides;
import com.kcwl.framework.file.FileConstants;
import com.kcwl.framework.file.FileProperties;
import com.kcwl.framework.file.biz.service.IFileService;
import com.kcwl.framework.file.biz.service.IMGConstant;
import com.kcwl.framework.utils.BeanMapUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Map;



/**
 * @Author: mengzr
 * @Date: 2019/6/28 11:07
 */
@Slf4j
public class AliyunOSSServiceImpl implements IFileService {

    private FileProperties.Aliyun aliyun;

    public AliyunOSSServiceImpl(FileProperties.Aliyun aliyun) {
        this.aliyun = aliyun;
    }

    @Override
    public String upload(String filename, InputStream is, Map<String, String> descriptions, boolean absolutePath) {
        OSSClient ossClient = createOSSClient();
        ossClient.putObject(getBucketName(descriptions), filename, is);
        String fileUrl = this.getUrl(ossClient, filename, descriptions);
        closeOSSClient(ossClient);
        return fileUrl;
    }

    @Override
    public String upload(String filename, File file, Map<String, String> descriptions, boolean absolutePath) {
        OSSClient ossClient = createOSSClient();
        ossClient.putObject(getBucketName(descriptions), filename, file);
        String fileUrl = this.getUrl(ossClient, filename, descriptions);
        closeOSSClient(ossClient);
        return fileUrl;    }

    @Override
    public byte[] download(String filepath) {
        OSSClient ossClient = createOSSClient();
        InputStream input=null;

        byte[] data = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            byte[] tempBuf = new byte[4096];
            OSSObject ossObject = ossClient.getObject(aliyun.getBucketName(), filepath);
            input = ossObject.getObjectContent();
            int len;
            while ((len=input.read(tempBuf)) != -1){
                bos.write(tempBuf, 0, len);
            }
            data  = bos.toByteArray();
        } catch (Exception e) {
            data = new byte[0];
            log.error("{}", e);
        } finally {
            safeCloseOutputStream(bos);
            safeCloseInputStream(input);
            closeOSSClient(ossClient);
        }
        return data;
    }

    @Override
    public String getUrl(String  filename, Map<String, String> descriptions) {
        OSSClient ossClient = createOSSClient();
        String url = getUrl(ossClient, filename, descriptions);
        closeOSSClient(ossClient);
        return url;
    }

    public String getUrl(OSSClient ossClient, String  filename, Map<String, String> descriptions) {

        int compressType = BeanMapUtil.getInteger(descriptions, "compressType", IMGConstant.IMG_COMPRESS_TYPE_YT);
        String attachFileName = BeanMapUtil.getString(descriptions, "attachFileName", null);
        String style = BeanMapUtil.getString(descriptions, "style", null);
        String compressTypeValue = getCompressTypeValue(compressType);
        String fileKey = filename;

        if (  compressTypeValue != null ) {
            fileKey += compressTypeValue;
        }

        Date expires = new Date (System.currentTimeMillis() + aliyun.getExpiredTime());
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(getBucketName(descriptions), fileKey);
        generatePresignedUrlRequest.setExpiration(expires);

        if ( style != null ) {
            generatePresignedUrlRequest.setProcess(style);
        }

        if ( attachFileName != null ) {
            ResponseHeaderOverrides header = new ResponseHeaderOverrides();
            header.setContentDisposition("attachment;filename=" + attachFileName);
            generatePresignedUrlRequest.setResponseHeaders(header);
        }
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        return isPublic(descriptions)  ? getPublicUrl(url) : url.toString();
    }

    @Override
    public Map<String, Object> getFileDescriptions(String filepath) {
        return null;
    }

    @Override
    public String getOriginalFilename(String filepath) {
        return null;
    }

    @Override
    public void deleteFile(String filepath) {
        OSSClient ossClient = createOSSClient();
        ossClient.deleteObject(aliyun.getBucketName(), filepath);
        closeOSSClient(ossClient);
    }

    private String getCompressTypeValue(int type) {
        String compressTypeValue = null;
        switch(type) {
            case IMGConstant.IMG_COMPRESS_TYPE_DB:
                compressTypeValue="@50p";
                break;
            case IMGConstant.IMG_COMPRESS_TYPE_W_H:
                compressTypeValue="@120w_120h";
                break;
            case IMGConstant.IMG_COMPRESS_TYPE_W_H_QZ:
                compressTypeValue = "@120w_120h_2e";
                break;
            default:
                break;
        }
        return compressTypeValue;
    }

    private OSSClient createOSSClient() {
        return new OSSClient(aliyun.getEndpoint(), aliyun.getAccessKey(), aliyun.getSecretKey());
    }

    private void closeOSSClient(OSSClient ossClient) {
        ossClient.shutdown();
    }

    private String getBucketName(Map<String, String> descriptions) {
        String bucketName = null;
        if ( descriptions != null ) {
            bucketName = descriptions.get("bucketName");
        }
        if ( bucketName == null ) {
            bucketName = aliyun.getBucketName();
        }
        return bucketName;
    }

    private String getPublicUrl(URL url) {
        StringBuilder sb = new StringBuilder();
        sb.append(url.getProtocol()).append("://").append(url.getHost()).append(url.getPath());
        return sb.toString();
    }
}
