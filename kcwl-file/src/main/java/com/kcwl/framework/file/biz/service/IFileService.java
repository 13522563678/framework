package com.kcwl.framework.file.biz.service;
import com.kcwl.framework.file.FileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 姚华成
 * @date 2018-06-09
 */
public interface IFileService {
    Logger log = LoggerFactory.getLogger(IFileService.class);

    /**
     * 文件上传方法
     *
     * @param is       文件输入流
     * @param filename 文件名
     * @return 文件路径
     */
    default String upload(String filename, InputStream is) {
        return upload(filename, is, null);
    }

    /**
     * 上传通用方法
     *
     * @param is           文件输入流
     * @param filename     文件名
     * @param descriptions 文件描述信息
     * @return 组名+文件路径，如：group1/M00/00/00/wKgz6lnduTeAMdrcAAEoRmXZPp870.jpeg
     */
    default String upload(String filename, InputStream is, Map<String, String> descriptions) {
        return upload(filename, is, descriptions, false);
    }

    /**
     * 上传通用方法
     *
     * @param is           文件输入流
     * @param filename     文件名
     * @param descriptions 文件描述信息
     * @param absolutePath 返回绝对路径还是相对路径
     * @return 组名+文件路径，如：group1/M00/00/00/wKgz6lnduTeAMdrcAAEoRmXZPp870.jpeg
     */
    String upload(String filename, InputStream is, Map<String, String> descriptions, boolean absolutePath);


    /**
     * 文件上传方法
     *
     * @param file       文件输入流
     * @param filename 文件名
     * @return 文件路径
     */
    default String upload(String filename, File file) {
        return upload(filename, file, null);
    }

    /**
     * 上传通用方法
     *
     * @param file           文件输入流
     * @param filename     文件名
     * @param descriptions 文件描述信息
     * @return 组名+文件路径，如：group1/M00/00/00/wKgz6lnduTeAMdrcAAEoRmXZPp870.jpeg
     */
    default String upload(String filename, File file, Map<String, String> descriptions) {
        return upload(filename, file, descriptions, false);
    }

    /**
     * 上传通用方法
     *
     * @param file           文件输入流
     * @param filename     文件名
     * @param descriptions 文件描述信息
     * @param absolutePath 返回绝对路径还是相对路径
     * @return 组名+文件路径，如：group1/M00/00/00/wKgz6lnduTeAMdrcAAEoRmXZPp870.jpeg
     */
    String upload(String filename, File file, Map<String, String> descriptions, boolean absolutePath);

    /**
     * 下载文件 输出文件
     *
     * @param filepath 文件路径
     * @param os       输出流
     */
    default void download(String filepath, OutputStream os) {
        try {
            os.write(download(filepath));
        } catch (Exception ex) {
            log.error("文件下载出错！", ex);
            //throw new BizException(FileConstants.API_RESULT_META_SUCCESS_FILE_DOWNLOAD_ERROR.getCode(), ex.getMessage());
        }
    }

    /**
     * @param filename
     * @return
     */
    default String getUrl(String  filename, Map<String, String> descriptions) {
        return filename;
    }

    /**
     * @param filenameList
     * @return
     */
    default List<String> getUrl(List<String> filenameList, Map<String, String> descriptions) {
        List<String> fileUrlList = new ArrayList<String>();
        if ( fileUrlList != null ) {
            for (String fileName : filenameList ) {
                fileUrlList.add(getUrl(fileName, descriptions));
            }
        }
        return fileUrlList;
    }

    /**
     * @param filename
     * @return
     */
    default String getUrl(String  filename) {
        return getUrl(filename, null);
    }

    /**
     * 下载文件
     *
     * @param filepath 文件路径
     * @return 二进制文件内容
     */
    byte[] download(String filepath);

    /**
     * 获取文件描述信息
     *
     * @param filepath 文件路径
     * @return 文件描述信息
     */
    Map<String, Object> getFileDescriptions(String filepath);

    /**
     * 获取源文件的文件名称
     *
     * @param filepath 文件路径
     * @return 文件名称
     */
    String getOriginalFilename(String filepath);

    /**
     * 删除文件
     *
     * @param filepath 文件路径
     */
    void deleteFile(String filepath);

    default void safeCloseInputStream(InputStream in) {
        if ( in != null ) {
            try {
                in.close();
            } catch (IOException e) {
                log.error("{}", e);
            }
        }
    }

    default void safeCloseOutputStream(OutputStream out) {
        if ( out != null ) {
            try {
                out.close();
            } catch (IOException e) {
                log.error("{}", e);
            }
        }
    }

    default boolean isPublic(Map<String, String> descriptions) {
        if ( descriptions != null ) {
            String isPublic = descriptions.get("public");
            if (FileConstants.IS_PUBLIC.equals(isPublic) ) {
                return true;
            }
        }
        return false;
    }
}
