package com.kcwl.framework.file;

import com.kcwl.framework.entity.ApiResultMeta;

/**
 * @author 姚华成
 * @date 2018-06-12
 */
public class FileConstants {
    public static ApiResultMeta API_RESULT_META_SUCCESS_FILE_UPLOAD_ERROR = new ApiResultMeta(100301, "上传文件错误！错误原因：${reason}");
    public static ApiResultMeta API_RESULT_META_SUCCESS_FILE_DOWNLOAD_ERROR = new ApiResultMeta(100302, "下载文件错误！错误原因：${reason}");
    public static ApiResultMeta API_RESULT_META_SUCCESS_FILE_DELETE_ERROR = new ApiResultMeta(100302, "删除文件错误！错误原因：${reason}");
    public static ApiResultMeta API_RESULT_META_SUCCESS_FILE_GET_INFO_ERROR = new ApiResultMeta(100302, "获取文件信息错误！错误原因：${reason}");

    /**
     * 路径分隔符
     */
    public static final String FILE_SEPARATOR = "/";
    /**
     * Point
     */
    public static final String POINT = ".";

    public static final String IS_PUBLIC = "1";

}
