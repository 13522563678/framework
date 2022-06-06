package com.kcwl.framework.file.biz.service.dfs.fastdfs;

import com.kcwl.framework.exception.BizException;
import com.kcwl.framework.exception.ParamValidException;
import com.kcwl.framework.file.FileProperties;
import com.kcwl.framework.file.biz.service.IFileService;
import com.kcwl.framework.utils.StreamUtil;
import com.kcwl.framework.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kcwl.framework.file.FileConstants.*;

/**
 * @author 姚华成
 * @date 2018-06-12
 */
@Slf4j
@Validated
public class FastDfsServiceImpl implements IFileService {
    /**
     * 文件名称Key
     */
    private static final String FILENAME = "filename";

    private StorageClientPool storageClientPool;
    private FileProperties.Fastdfs fastdfs;

    public FastDfsServiceImpl(StorageClientPool storageClientPool, FileProperties.Fastdfs fastdfs) {
        this.storageClientPool = storageClientPool;
        this.fastdfs = fastdfs;
    }

    @Override
    public String upload(String filename, @NotNull InputStream is, Map<String, String> descriptions, boolean absolutePath) {
        try {
            if (is.available() > fastdfs.getMaxFileSize()) {
                throw new ParamValidException("上传文件超过最大大小！");
            }

            // 文件名后缀
            filename = formatPath(filename);
            String suffix = getFilenameSuffix(filename);

            // 文件描述
            NameValuePair[] nvps = null;
            List<NameValuePair> nvpsList = new ArrayList<>();
            // 文件名
            if (StringUtil.hasText(filename)) {
                nvpsList.add(new NameValuePair(filename, filename));
            }
            // 描述信息
            if (descriptions != null && descriptions.size() > 0) {
                descriptions.forEach((key, value) -> nvpsList.add(new NameValuePair(key, value)));
            }
            if (nvpsList.size() > 0) {
                nvps = new NameValuePair[nvpsList.size()];
                nvpsList.toArray(nvps);
            }

            StorageClient1 storageClient = storageClientPool.borrowObject();
            try {
                // 读取流
                byte[] fileData = StreamUtil.copyToByteArray(is);
                // 上传文件，返回路径
                String path = storageClient.upload_file1(fileData, suffix, nvps);

                if (StringUtil.isEmpty(path)) {
                    throw new BizException(API_RESULT_META_SUCCESS_FILE_UPLOAD_ERROR, "因服务器错误没有返回文件路径！");
                }
                if (absolutePath) {
                    path = fastdfs.getHttpUrlRoot() + path;
                }
                return path;
            } finally {
                // 返还对象
                storageClientPool.returnObject(storageClient);
            }
        } catch (Exception ex) {
            log.error("文件上传FastDFS服务器出错！", ex);
            throw new BizException(API_RESULT_META_SUCCESS_FILE_UPLOAD_ERROR, ex.getMessage());
        }
    }

    @Override
    public String upload(String filename, File file, Map<String, String> descriptions, boolean absolutePath) {
        throw new UnsupportedOperationException("暂时还不支持，有使用到请联系管理员！");
    }

    @Override
    public byte[] download(@NotBlank String filepath) {
        try {
            filepath = formatPath(filepath);
            StorageClient1 storageClient = storageClientPool.borrowObject();
            try {
                // 下载
                byte[] fileByte = storageClient.download_file1(filepath);
                if (fileByte == null) {
                    throw new BizException(API_RESULT_META_SUCCESS_FILE_DOWNLOAD_ERROR, "没有下载到文件内容！");
                }
                return fileByte;
            } finally {
                // 返还对象
                storageClientPool.returnObject(storageClient);
            }
        } catch (Exception ex) {
            log.error("从FastDFS服务器下载文件时出错！", ex);
            throw new BizException(API_RESULT_META_SUCCESS_FILE_DOWNLOAD_ERROR, ex.getMessage());
        }
    }

    /**
     * 获取文件描述信息
     *
     * @param filepath 文件路径
     * @return 文件描述信息
     */
    @Override
    public Map<String, Object> getFileDescriptions(String filepath) {
        try {
            StorageClient1 storageClient = storageClientPool.borrowObject();
            try {
                NameValuePair[] nvps = storageClient.get_metadata1(filepath);
                if (nvps == null) {
                    return null;
                }
                Map<String, Object> infoMap = new HashMap<>(nvps.length);
                for (NameValuePair nvp : nvps) {
                    infoMap.put(nvp.getName(), nvp.getValue());
                }
                return infoMap;
            } finally {
                storageClientPool.returnObject(storageClient);
            }
        } catch (Exception ex) {
            log.error("获取文件信息出错！", ex);
            throw new BizException(API_RESULT_META_SUCCESS_FILE_GET_INFO_ERROR, ex.getMessage());
        }
    }

    /**
     * 获取源文件的文件名称
     *
     * @param filepath 文件路径
     * @return 文件名称
     */
    @Override
    public String getOriginalFilename(String filepath) {
        Map<String, Object> descriptions = getFileDescriptions(filepath);
        if (descriptions.get(FILENAME) != null) {
            return (String) descriptions.get(FILENAME);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param filepath 文件路径
     * @return 删除成功返回 0, 失败返回其它
     */
    @Override
    public void deleteFile(@NotBlank String filepath) {
        try {
            StorageClient1 storageClient = storageClientPool.borrowObject();
            try {
                int status = storageClient.delete_file1(filepath);
                if (status != 0) {
                    throw new BizException(API_RESULT_META_SUCCESS_FILE_DELETE_ERROR, "返回的错误码为" + status);
                }
            } finally {
                // 返还对象
                storageClientPool.returnObject(storageClient);
            }
        } catch (Exception ex) {
            log.error("删除文件出错！", ex);
            throw new BizException(API_RESULT_META_SUCCESS_FILE_DELETE_ERROR, ex.getMessage());
        }
    }

    /**
     * 获取文件名称的后缀
     *
     * @param filename 文件名 或 文件路径
     * @return 文件后缀
     */
    private String getFilenameSuffix(String filename) {
        String suffix = null;
        if (StringUtil.hasText(filename)) {
            int index = filename.lastIndexOf(FILE_SEPARATOR);
            if (index >= 0) {
                filename = filename.substring(index + 1);
            }
            index = filename.lastIndexOf(POINT);
            if (index >= 0) {
                suffix = filename.substring(index + 1);
            }
        }
        return suffix;
    }

    /**
     * 转换路径中的 '\' 为 '/' <br>
     * 并把文件后缀转为小写
     *
     * @param path 路径
     * @return
     */
    private String formatPath(String path) {
        if (StringUtil.hasText(path)) {
            path = path.replaceAll("\\\\", FILE_SEPARATOR);
            int index = path.lastIndexOf(POINT);
            if (index > 0) {
                String pre = path.substring(0, index + 1);
                String suffix = path.substring(index + 1).toLowerCase();
                path = pre + suffix;
            }
        }
        return path;
    }
}
