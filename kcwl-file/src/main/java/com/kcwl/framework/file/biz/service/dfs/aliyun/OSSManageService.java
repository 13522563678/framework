package com.kcwl.framework.file.biz.service.dfs.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.kcwl.framework.file.biz.service.IFileService;
import com.kcwl.framework.file.biz.service.IMGConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * 对OSS服务器管理
 * @ClassName: OSSManageUtil
 * @Description:
 * @author liujh
 * @date 2015-3-26 上午10:47:00
 */
@Slf4j
@Service
@Data
public class OSSManageService implements IFileService {
//    @Autowired
//    private OSSProperties oSSConfig;
    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
    // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
    // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
    // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
    private  String endpoint;
    private  String imgEndpoint;
    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
    // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
    // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
    private  String accessKeyId ;
    private  String accessKeySecret;
    // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
    private  String bucketName;
    private  String ossAccessUrl;
    // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件。详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Object命名规范如下：使用UTF-8编码，长度必须在1-1023字节之间，不能以“/”或者“\”字符开头。
    private  String key;
    //过期时间
    private String expiresTime;

    public OSSManageService(){

    }

    public OSSManageService(String endpoint, String imgEndpoint, String accessKeyId, String accessKeySecret, String bucketName, String ossAccessUrl, String key, String expiresTime) {
        this.endpoint = endpoint;
        this.imgEndpoint = imgEndpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.ossAccessUrl = ossAccessUrl;
        this.key = key;
        this.expiresTime=expiresTime;
    }

    //上传路径
    public  OSSPath uploadFile(File file,String fileName){
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, key+fileName, file);
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return new OSSPath(ossAccessUrl+File.separator+key+File.separator+fileName,key+fileName);
    }

    //上传路径
    public  OSSPath uploadFile(File file,String fileName,String key){
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, key+fileName, file);
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return new OSSPath(ossAccessUrl+File.separator+key+File.separator+fileName,key+fileName);
    }

    /**
     * 上传操作
     * 如果是图片的话，则返回压缩后的图片地址，否则直接返回oss地址
     * @param input
     * @param fileName
     * @param isPic
     * @return
     * @throws IOException
     */
    public  OSSPath uploadFile(InputStream input,String fileName,boolean isPic) throws IOException{
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        //上传操作
        ossClient.putObject(bucketName, key+fileName, input);
        OSSPath osspath=new OSSPath(ossAccessUrl+File.separator+key+File.separator+fileName,key+fileName);
        String fileUrl=null;
        //如果是图片的话，则返回压缩后的图片地址，否则直接返回oss地址
        if(isPic)fileUrl=getCompressIMGUrl(key+fileName, IMGConstant.IMG_COMPRESS_TYPE_W_H);
        else fileUrl=getOSSUrl(key+fileName);
        osspath.setFileUrl(fileUrl);
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return osspath;
    }

    /**
     * 上传操作
     * 如果是图片的话，则返回压缩后的图片地址，否则直接返回oss地址
     * @param input
     * @param fileName
     * @param isPic
     * @return
     * @throws IOException
     */
    public  OSSPath uploadFile(InputStream input,String key,String fileName,boolean isPic) throws IOException{
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        //上传操作
        ossClient.putObject(bucketName, key+fileName, input);
        OSSPath osspath=new OSSPath(ossAccessUrl+File.separator+key+File.separator+fileName,key+fileName);
        String fileUrl=null;
        //如果是图片的话，则返回压缩后的图片地址，否则直接返回oss地址
        if(isPic)fileUrl=getCompressIMGUrl(key+fileName, IMGConstant.IMG_COMPRESS_TYPE_W_H);
        else fileUrl=getOSSUrl(key+fileName);
        osspath.setFileUrl(fileUrl);
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return osspath;
    }

    /**
     * oss服务
     * 获取oss上的访问地址
     * @param Objkey
     * @return
     */
    public  String getOSSUrl(String Objkey){
        //
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        Date expires = new Date (new Date().getTime() + 1000 * 60*Long.parseLong(expiresTime)); // 1*60 minute to expire
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, Objkey);
        generatePresignedUrlRequest.setExpiration(expires);
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        log.info(url.toString());
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return url.toString();
    }

    /**
     * oss服务
     * 获取IMG上的访问地址  signature
     * @param Objkey
     * @return
     */
    public  String getIMGUrl(String Objkey){
        //
        OSSClient ossClient = new OSSClient(imgEndpoint, accessKeyId, accessKeySecret);
        Date expires = new Date (new Date().getTime() + 1000 * 60*60); // 1*60 minute to expire
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, Objkey);
        generatePresignedUrlRequest.setExpiration(expires);
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        log.info(url.toString());
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return url.toString();
    }

    /**
     * oss服务
     * 获取压缩后的IMG上的访问地址
     * @param Objkey
     * @param type   1表示等比例压缩 （例如：@50p）    2 表示宽高压缩（例如：@120w_120h）  3 表示宽高且强制压缩（例如：@120w_120h_2e,2e表示强制）
     * @return
     */
    public  String getCompressIMGUrl(String Objkey,short type){
        //type   1表示等比例压缩 （例如：@50p）    2 表示宽高压缩（例如：@120w_120h）  3 表示宽高且强制压缩（例如：@120w_120h_2e,2e表示强制）
        switch(type) {
            case IMGConstant.IMG_COMPRESS_TYPE_DB:
                Objkey=Objkey+"@50p";
                break;
            case IMGConstant.IMG_COMPRESS_TYPE_W_H:
                Objkey=Objkey+"@120w_120h";
                break;
            case IMGConstant.IMG_COMPRESS_TYPE_W_H_QZ:
                Objkey=Objkey+"@120w_120h_2e";
                break;
            default:
                break;
        }
        //
        OSSClient ossClient = new OSSClient(imgEndpoint, accessKeyId, accessKeySecret);
        Date expires = new Date (new Date().getTime() + 1000 * 60*60); // 1*60 minute to expire
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, Objkey);
        generatePresignedUrlRequest.setExpiration(expires);
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        log.info(url.toString());
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return url.toString();
    }

    @Override
    public String upload(String filename, InputStream is, Map<String, String> descriptions, boolean absolutePath) {
        return null;
    }

    @Override
    public String upload(String filename, File file, Map<String, String> descriptions, boolean absolutePath) {
        return null;
    }

    @Override
    public byte[] download(String filepath) {
        return new byte[0];
    }

    @Override
    public Map<String, Object> getFileDescriptions(String filepath) {
        return null;
    }

    @Override
    public String getOriginalFilename(String filepath) {
        return null;
    }

    //删除附件
    @Override
    public  void deleteFile(String key){
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, key);
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     * @Version1.0
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String contentType(String FilenameExtension){
        if(FilenameExtension.equals("BMP")||FilenameExtension.equals("bmp")){return "image/bmp";}
        if(FilenameExtension.equals("GIF")||FilenameExtension.equals("gif")){return "image/gif";}
        if(FilenameExtension.equals("JPEG")||FilenameExtension.equals("jpeg")||
                FilenameExtension.equals("JPG")||FilenameExtension.equals("jpg")||
                FilenameExtension.equals("PNG")||FilenameExtension.equals("png")){return "image/jpeg";}
        if(FilenameExtension.equals("HTML")||FilenameExtension.equals("html")){return "text/html";}
        if(FilenameExtension.equals("TXT")||FilenameExtension.equals("txt")){return "text/plain";}
        if(FilenameExtension.equals("VSD")||FilenameExtension.equals("vsd")){return "application/vnd.visio";}
        if(FilenameExtension.equals("PPTX")||FilenameExtension.equals("pptx")||
                FilenameExtension.equals("PPT")||FilenameExtension.equals("ppt")){return "application/vnd.ms-powerpoint";}
        if(FilenameExtension.equals("DOCX")||FilenameExtension.equals("docx")||
                FilenameExtension.equals("DOC")||FilenameExtension.equals("doc")){return "application/msword";}
        if(FilenameExtension.equals("XML")||FilenameExtension.equals("xml")){return "text/xml";}
        return "text/html";
    }

    /**
     *
     * 检查gpx文件是否已经存在
     * @param ossKey
     * @return
     * @throws IOException
     */
    public  boolean checkGpx( String ossKey) throws IOException {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // Object是否存在
        boolean found = ossClient.doesObjectExist(bucketName, ossKey);
        // 关闭client
        try {
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return found;


    }
    /**
     *
     * 读取gpx文件的内容
     * @param ossKey
     * @return
     * @throws IOException
     */
    public  String readGpx( String ossKey) throws IOException {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // Object是否存在
        OSSObject ossObject = ossClient.getObject(bucketName, ossKey);
        // 读Object内容
        StringBuffer gpxBuffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                gpxBuffer.append(line);
            }
        }catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }finally {
            if(null!=reader){
                reader.close();
            }
            if(null!=ossClient){
                try {
                    // 关闭client
                    ossClient.shutdown();
                } catch (Exception e) {
                }
            }
        }
        return gpxBuffer.toString();

    }


   /* *//**
     * 上传操作
     * 如果是图片的话，则返回压缩后的图片地址，否则直接返回oss地址
     * @param input
     * @param fileName
     * @return
     * @throws IOException
     *//*
    public  OSSPath uploadAPKFile(InputStream input,String fileName) throws IOException{
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        //上传操作
        ossClient.putObject(apkBucketName, apkKey + fileName, input);
        OSSPath osspath = new OSSPath(ossAccessUrl + "/" + apkKey + fileName, apkKey + fileName);
        String fileUrl = null;
        fileUrl = getOSSAPKUrl(apkKey + fileName);
        osspath.setFileUrl(fileUrl);
        try {
            // 关闭client
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return osspath;
    }

    *//**
     * oss服务
     * 获取oss上的访问地址
     * @param Objkey
     * @return
     *//*
    public  String getOSSAPKUrl(String Objkey){
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        Date expires = new Date (new Date().getTime() + 1000 * 60*60); // 1*60 minute to expire
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(apkBucketName, Objkey);
        generatePresignedUrlRequest.setExpiration(expires);
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        logger.info(url.toString());
        logger.info(url.getPath().toString());
        try {
            // 关闭client
            ossClient.shutdown();
        } catch (Exception e) {
        }
        return ossAccessUrl + url.getPath().toString();
    }*/

}