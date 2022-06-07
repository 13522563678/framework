package com.kcwl.framework.file.biz.service.dfs.hwyun;

import com.kcwl.framework.file.FileProperties;
import com.kcwl.framework.file.biz.service.IFileService;
import com.kcwl.framework.file.biz.service.IMGConstant;
import com.kcwl.framework.utils.BeanMapUtil;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

/**
 * @Author: mengzr
 * @Date: 2019/9/12 10:52
 */
@Slf4j
public class HwyunOBSServiceImpl implements IFileService {

    private FileProperties.Hwyun hwyun;

    public HwyunOBSServiceImpl(FileProperties.Hwyun hwyun) {
        this.hwyun = hwyun;
    }

    @Override
    public String upload(String filename, InputStream is, Map<String, String> descriptions, boolean absolutePath) {
        ObsClient obsClient = createOBSClient();
        String fileUrl = null;
        try{
            String bucketName=getBucketName(descriptions);
            obsClient.putObject(bucketName, filename, is);
            setObjectAcl(obsClient, bucketName, filename, descriptions);
            fileUrl = getUrl(obsClient, filename, descriptions);
        } catch (Exception e) {
            log.error("InputStream upload error {}", e);
        } finally {
            closeOBSClient(obsClient);
        }
        return fileUrl;
    }

    @Override
    public String upload(String filename, File file, Map<String, String> descriptions, boolean absolutePath) {
        ObsClient obsClient = createOBSClient();
        String fileUrl = null;
        try{
            String bucketName=getBucketName(descriptions);
            obsClient.putObject(bucketName, filename, file);
            setObjectAcl(obsClient, bucketName, filename, descriptions);
            fileUrl = getUrl(obsClient, filename, descriptions);
        } catch (Exception e) {
            log.error("file upload error: {}", e);
        } finally {
            closeOBSClient(obsClient);
        }
        return fileUrl;
    }

    @Override
    public byte[] download(String filepath) {
        ObsClient obsClient = createOBSClient();
        byte[] data;
        InputStream input=null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            byte[] tempBuf = new byte[4096];
            ObsObject obsObject = obsClient.getObject(hwyun.getBucketName(), filepath);
            input = obsObject.getObjectContent();
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
            closeOBSClient(obsClient);
        }
        return data;
    }

    @Override
    public String getUrl(String  filename, Map<String, String> descriptions) {
        ObsClient obsClient = createOBSClient();
        String url = getUrl(obsClient, filename, descriptions);
        closeOBSClient(obsClient);
        return url;
    }

    @Override
    public List<String> getUrl(List<String> filenameList, Map<String, String> descriptions) {
        List<String> fileUrlList = new ArrayList<String>();
        ObsClient obsClient = createOBSClient();
        if ( fileUrlList != null ) {
            for (String fileName : filenameList) {
                fileUrlList.add(getUrl(obsClient, fileName, descriptions));
            }
        }
        closeOBSClient(obsClient);
        return fileUrlList;
    }


    @Override
    public Map<String, Object> getFileDescriptions(String filepath) {
        return null;
    }

    @Override
    public String getOriginalFilename(String filepath) {
        return filepath;
    }

    @Override
    public void deleteFile(String filepath) {
        ObsClient obsClient = createOBSClient();
        try{
            obsClient.deleteObject(hwyun.getBucketName(), filepath);
        } catch (Exception e) {
            log.error("{}", e);
        } finally {
            closeOBSClient(obsClient);
        }
    }

    private String getUrl(ObsClient obsClient, String  filename, Map<String, String> descriptions) {
        TemporarySignatureRequest request = new TemporarySignatureRequest();
        String bucketName=getBucketName(descriptions);
        request.setBucketName(bucketName);
        request.setObjectKey(filename);
        request.setMethod(HttpMethodEnum.GET);
        request.setRequestDate(new Date());
        request.setExpires(BeanMapUtil.getLong(descriptions, "expiredTime", hwyun.getExpiredTime()));

        int compressType = BeanMapUtil.getInteger(descriptions, "compressType", IMGConstant.IMG_COMPRESS_TYPE_YT);
        String compressTypeValue = getCompressTypeValue(compressType);

        if  ( compressTypeValue != null ) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("x-image-process",compressTypeValue);
            request.setQueryParams(queryMap);
        }

        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        String url = response.getSignedUrl();
        if ( isPublic(descriptions) ) {
            return  getPublicUrl(url);
        }
        return url;
    }

    private String getPublicUrl(String  url) {
        int i =url.indexOf("?");
        if ( i > 0 ) {
            return url.substring(0,i).replaceAll(":443", "");
        }
        return url;
    }

    private ObsClient createOBSClient() {
        return new ObsClient(hwyun.getAccessKey(),hwyun.getSecretKey(), hwyun.getEndpoint());
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

    private void closeOBSClient(ObsClient obsClient) {
        try {
            obsClient.close();
        } catch (IOException e) {
            log.error("{}", e);
        }
    }


    private String getBucketName(Map<String, String> descriptions) {
        String bucketName = null;
        if ( descriptions != null ) {
            bucketName = descriptions.get("bucketName");
        }
        if ( bucketName == null ) {
            bucketName = hwyun.getBucketName();
        }
        return bucketName;
    }

    private void setObjectAcl(ObsClient obsClient, String bucketName, String fileName, Map<String, String> descriptions) {
        if ( isPublic(descriptions) ) {
            obsClient.setObjectAcl(bucketName, fileName, AccessControlList.REST_CANNED_PUBLIC_READ);
        }
    }
}
