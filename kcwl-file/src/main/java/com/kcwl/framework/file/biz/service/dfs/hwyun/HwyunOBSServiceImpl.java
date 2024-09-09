package com.kcwl.framework.file.biz.service.dfs.hwyun;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.utils.JsonUtils;
import com.huaweicloud.sdk.mpc.v1.MpcClient;
import com.huaweicloud.sdk.mpc.v1.model.*;
import com.kcwl.framework.file.FileProperties;
import com.kcwl.framework.file.biz.service.IFileService;
import com.kcwl.framework.file.biz.service.IMGConstant;
import com.kcwl.framework.utils.BeanMapUtil;
import com.kcwl.framework.utils.CollectionUtil;
import com.kcwl.framework.utils.StringUtil;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.internal.Constants;
import com.obs.services.model.ObsObject;
import com.obs.services.model.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
        try {
            String bucketName = getBucketName(descriptions);
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
        try {
            String bucketName = getBucketName(descriptions);
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
        InputStream input = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] tempBuf = new byte[4096];
            ObsObject obsObject = obsClient.getObject(hwyun.getBucketName(), filepath);
            input = obsObject.getObjectContent();
            int len;
            while ((len = input.read(tempBuf)) != -1) {
                bos.write(tempBuf, 0, len);
            }
            data = bos.toByteArray();
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
    public String getUrl(String filename, Map<String, String> descriptions) {
        ObsClient obsClient = createOBSClient();
        String url = getUrl(obsClient, filename, descriptions);
        closeOBSClient(obsClient);
        return url;
    }

    @Override
    public List<String> getUrl(List<String> filenameList, Map<String, String> descriptions) {
        List<String> fileUrlList = new ArrayList<String>();
        ObsClient obsClient = createOBSClient();
        if (fileUrlList != null) {
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
        try {
            obsClient.deleteObject(hwyun.getBucketName(), filepath);
        } catch (Exception e) {
            log.error("{}", e);
        } finally {
            closeOBSClient(obsClient);
        }
    }

    @SneakyThrows
    private String getUrl(ObsClient obsClient, String filename, Map<String, String> descriptions) {
        TemporarySignatureRequest request = new TemporarySignatureRequest();
        String bucketName = getBucketName(descriptions);
        request.setBucketName(bucketName);
        request.setObjectKey(filename);
        request.setMethod(HttpMethodEnum.GET);
        request.setRequestDate(new Date());
        request.setExpires(BeanMapUtil.getLong(descriptions, "expiredTime", hwyun.getExpiredTime()));

        /*int compressType = BeanMapUtil.getInteger(descriptions, "compressType", IMGConstant.IMG_COMPRESS_TYPE_YT);
        String compressTypeValue = getCompressTypeValue(compressType);

        if  ( compressTypeValue != null ) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("x-image-process",compressTypeValue);
            request.setQueryParams(queryMap);
        }*/

        Map<String, Object> queryMap = new HashMap<>();
        String style = BeanMapUtil.getString(descriptions, "style", null);
        if (style != null) {
            queryMap.put("x-image-process", style);
        }

        String attachFileName = BeanMapUtil.getString(descriptions, "attachFileName", null);
        if (attachFileName != null) {
            queryMap.put(Constants.ObsRequestParams.RESPONSE_CONTENT_DISPOSITION, String.format("attachment;filename=%s", URLEncoder.encode(attachFileName, "UTF-8")));
        }
        if (!CollectionUtil.isEmpty(queryMap)) {
            request.setQueryParams(queryMap);
        }

        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        String url = response.getSignedUrl();
        if (isPublic(descriptions)) {
            return getPublicUrl(url);
        }
        return url;
    }

    private String getPublicUrl(String url) {
        return removeQueryParam(url, Arrays.asList("Expires", "Signature"));
        /*int i = url.indexOf("?");
        if (i > 0) {
            return url.substring(0, i).replaceAll(":443", "");
        }
        return url;*/
    }

    private ObsClient createOBSClient() {
        ObsConfiguration config = new ObsConfiguration();
        if (!StringUtil.isEmpty(hwyun.getImgEndpoint())) {
            config.setEndPoint(hwyun.getImgEndpoint());
            config.setCname(true);
        } else {
            config.setEndPoint(hwyun.getEndpoint());
        }

        return new ObsClient(hwyun.getAccessKey(), hwyun.getSecretKey(), config);
    }

    private String getCompressTypeValue(int type) {
        String compressTypeValue = null;
        switch (type) {
            case IMGConstant.IMG_COMPRESS_TYPE_DB:
                compressTypeValue = "@50p";
                break;
            case IMGConstant.IMG_COMPRESS_TYPE_W_H:
                compressTypeValue = "@120w_120h";
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
        if (descriptions != null) {
            bucketName = descriptions.get("bucketName");
        }
        if (bucketName == null) {
            bucketName = hwyun.getBucketName();
        }
        return bucketName;
    }

    private void setObjectAcl(ObsClient obsClient, String bucketName, String fileName, Map<String, String> descriptions) {
        if (isPublic(descriptions)) {
            obsClient.setObjectAcl(bucketName, fileName, AccessControlList.REST_CANNED_PUBLIC_READ);
        }
    }

    @Override
    public CreateTranscodingTaskResponse createTranscodingTask(String fileKey, List<Map<String, String>> watermarkObjects, Map<String, String> outFileInfo) {
        int index = fileKey.lastIndexOf("/");
        String outDirectory = index != -1 ? fileKey.substring(0, fileKey.lastIndexOf("/")) : "";
        String outName = fileKey.substring(index + 1);
        if (outFileInfo != null) {
            outDirectory = outFileInfo.get("directory") == null ? outDirectory : outFileInfo.get("directory");
            outName = outFileInfo.get("fileName") == null ? outName : outFileInfo.get("fileName");
        }
        ObsObjInfo input = new ObsObjInfo().withBucket(hwyun.getBucketName()).withLocation(hwyun.getRegion()).withObject(fileKey);
        ObsObjInfo output = new ObsObjInfo().withBucket(hwyun.getBucketName()).withLocation(hwyun.getRegion()).withObject(outDirectory);
        List<WatermarkRequest> watermarks = buildWatermarkRequest(watermarkObjects);
        AvParameters avParameters = buildAvParameters();
        //创建转码请求
        CreateTranscodingTaskRequest request = new CreateTranscodingTaskRequest().withBody(new CreateTranscodingReq()
                .withInput(input)
                .withOutput(output)
                .withWatermarks(watermarks)
                .withAvParameters(Collections.singletonList(avParameters))
                .withOutputFilenames(Collections.singletonList(outName))
        );

        CreateTranscodingTaskResponse response = createMpcClient().createTranscodingTask(request);
        return response;
    }

    private List<WatermarkRequest> buildWatermarkRequest(List<Map<String, String>> watermarkObjects) {
        List<WatermarkRequest> requestList = new ArrayList<>();
        for (Map<String, String> watermarkInfo : watermarkObjects) {
            if ("image".equals(watermarkInfo.get("watermarkType"))) {
                WatermarkRequest watermarkRequest = new WatermarkRequest();
                ObsObjInfo imageInput = new ObsObjInfo().withBucket(hwyun.getBucketName()).withLocation(hwyun.getRegion())
                        .withObject(watermarkInfo.get("watermarkImage"));
                watermarkRequest.setInput(imageInput);
                ImageWatermark imageWatermark = new ImageWatermark();
                watermarkRequest.setImageWatermark(imageWatermark);
                if (StringUtils.isNotBlank(watermarkInfo.get("x"))) {
                    imageWatermark.setDx(watermarkInfo.get("x"));
                }
                if (StringUtils.isNotBlank(watermarkInfo.get("y"))) {
                    imageWatermark.setDy(watermarkInfo.get("y"));
                }
                if (StringUtils.isNotBlank(watermarkInfo.get("referpos"))) {
                    imageWatermark.setReferpos(watermarkInfo.get("referpos"));
                }
                requestList.add(watermarkRequest);
            }
            if ("text".equals(watermarkInfo.get("watermarkType"))) {
                WatermarkRequest watermarkRequest = new WatermarkRequest();
                watermarkRequest.setTextContext(watermarkInfo.get("textContent"));
                TextWatermark textWatermark = new TextWatermark();
                watermarkRequest.setTextWatermark(textWatermark);

                if (StringUtils.isNotBlank(watermarkInfo.get("x"))) {
                    textWatermark.setDx(watermarkInfo.get("x"));
                }
                if (StringUtils.isNotBlank(watermarkInfo.get("y"))) {
                    textWatermark.setDy(watermarkInfo.get("y"));
                }
                if (StringUtils.isNotBlank(watermarkInfo.get("referpos"))) {
                    textWatermark.setReferpos(watermarkInfo.get("referpos"));
                }
                if (StringUtils.isNotBlank(watermarkInfo.get("fontName"))) {
                    textWatermark.setFontName(watermarkInfo.get("fontName"));
                }
                if (StringUtils.isNotBlank(watermarkInfo.get("color"))) {
                    textWatermark.setFontColor(watermarkInfo.get("color"));
                }
                if (StringUtils.isNotBlank(watermarkInfo.get("size"))) {
                    textWatermark.setFontSize(Integer.parseInt(watermarkInfo.get("size")));
                }
                requestList.add(watermarkRequest);
            }
        }

        return requestList;
    }

    @Override
    public ListTranscodingTaskResponse listTranscodingTask(List<Long> taskIds) {
        ListTranscodingTaskResponse listTranscodingTaskResponse = null;
        try {
            //按TaskId查询任务，TaskId是转码请求响应中返回的任务ID
            ListTranscodingTaskRequest req = new ListTranscodingTaskRequest().withTaskId(taskIds);
            //发送请求
            listTranscodingTaskResponse = createMpcClient().listTranscodingTask(req);
            log.info("ListTranscodingTaskResponse={}", JsonUtils.toJSON(listTranscodingTaskResponse));
        } catch (Exception e) {
            System.out.println(e);
        }
        return listTranscodingTaskResponse;
    }

    private MpcClient createMpcClient() {
        ObsConfiguration config = new ObsConfiguration();
        //设置httpConfig
        HttpConfig httpConfig = HttpConfig.getDefaultHttpConfig().withIgnoreSSLVerification(true).withConnectionTimeout(3).withIgnoreContentTypeForGetRequest(true);
        //根据实际需要，是否设置http代理
        //httpConfig.withProxyHost("xxxxxx").withProxyPort(xxxxxx).withProxyUsername("xxxxxx").
        //withProxyPassword("xxxxxx");
        BasicCredentials auth = new BasicCredentials().withAk(hwyun.getAccessKey()).withSk(hwyun.getSecretKey()).withProjectId(hwyun.getProjectId());
        return MpcClient.newBuilder()
                .withHttpConfig(httpConfig)
                .withCredential(auth)
                .withEndpoints(Arrays.asList(hwyun.getEndpoint().replace("obs", "mpc")))
                .build();
    }

    private AvParameters buildAvParameters() {
        return new AvParameters()
                .withVideo(new VideoParameters()
                        // 视频编码格式，1表示H264，2表示H265
                        .withCodec(1)
                        // 设置视频码率，单位：kbit/s
                        .withBitrate(6000)
                        // 编码档次，建议设为3
                        .withProfile(3)
                        .withLevel(15)
                        // 编码质量, 值越大质量越高，耗时越长
                        .withPreset(3)
                        .withMaxIframesInterval(5)
                        .withBframesCount(4)
                        .withHeight(1080)
                        .withWidth(1920))
                //设置音频参数
                .withAudio(new Audio()
                        //设置音频编码格式，1：AAC，2：HEAAC1，3：HEAAC2，4：MP3
                        .withCodec(1)
                        //采样率,1:AUDIO_SAMPLE_AUTO,2:22050Hz,3:32000Hz,4:44100Hz,5:48000Hz,6:96000Hz
                        .withSampleRate(4)
                        //音频码率，单位：kbit/s
                        .withBitrate(128)
                        //声道数
                        .withChannels(2))
                //设置公共参数
                .withCommon(new Common()
                        //高清低码开关
                        .withPvc(false)
                        //封装类型，1：HLS，2：DASH，3：HLS+DASH，4：MP4，5：MP3，6：ADTS
                        .withPackType(4));
    }
}
