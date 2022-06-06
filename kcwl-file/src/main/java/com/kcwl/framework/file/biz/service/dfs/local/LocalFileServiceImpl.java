package com.kcwl.framework.file.biz.service.dfs.local;
import com.kcwl.framework.file.biz.service.IFileService;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @author 姚华成
 * @date 2018-06-12
 */
public class LocalFileServiceImpl implements IFileService {

    @Override
    public String upload(String filename, InputStream is, Map<String, String> descriptions, boolean absolutePath) {
        throw new UnsupportedOperationException("暂时还不支持，有使用到请联系管理员！");
    }

    @Override
    public String upload(String filename, File file, Map<String, String> descriptions, boolean absolutePath) {
        throw new UnsupportedOperationException("暂时还不支持，有使用到请联系管理员！");

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

    @Override
    public void deleteFile(String filepath) {

    }
}
