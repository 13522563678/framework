package com.kcwl.framework.rest.web.filter.reqeust;

import com.kcwl.framework.utils.StreamUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author 姚华成
 * @date 2018-01-30
 */
public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {
    private ByteArrayOutputStream cachedInputData;

    public ContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedInputData == null) {
            cacheInputStream();
        }
        return new CachedServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {
        cachedInputData = new ByteArrayOutputStream();
        StreamUtil.copy(super.getInputStream(), cachedInputData);
    }

    private class CachedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream input;

        public CachedServletInputStream() {
            input = new ByteArrayInputStream(cachedInputData.toByteArray());
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // 不需要实现
        }

        @Override
        public int read() {
            return input.read();
        }
    }
}
