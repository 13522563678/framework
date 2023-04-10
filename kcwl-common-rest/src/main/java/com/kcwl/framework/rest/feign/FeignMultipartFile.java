package com.kcwl.framework.rest.feign;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ckwl
 */
public class FeignMultipartFile implements MultipartFile {
    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] content;

    public FeignMultipartFile(String name, @Nullable byte[] content) {
        this(name, "", (String)null, (byte[])content);
    }

    public FeignMultipartFile(String name, InputStream contentStream) throws IOException {
        this(name, "", (String)null, (byte[])FileCopyUtils.copyToByteArray(contentStream));
    }

    public FeignMultipartFile(String name, @Nullable String originalFilename, @Nullable String contentType, @Nullable byte[] content) {
        Assert.hasLength(name, "Name must not be empty");
        this.name = name;
        this.originalFilename = originalFilename != null ? originalFilename : "";
        this.contentType = contentType;
        this.content = content != null ? content : new byte[0];
    }

    public FeignMultipartFile(String name, @Nullable String originalFilename, @Nullable String contentType, InputStream contentStream) throws IOException {
        this(name, originalFilename, contentType, FileCopyUtils.copyToByteArray(contentStream));
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return 返回原始文件名
     */
    @Override
    @NonNull
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.content.length == 0;
    }

    @Override
    public long getSize() {
        return (long)this.content.length;
    }

    /**
     * @return 返回文件数据
     * @throws IOException
     */
    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    /**
     * @return 返回InputStream
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.content, dest);
    }
}
