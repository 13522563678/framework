package com.kcwl.framework.rest.web.filter.reqeust;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;

/**
 * @author ckwl
 */
public class CustomServletInputStream extends ServletInputStream {
    private ByteArrayInputStream input;

    public CustomServletInputStream(byte[] bytes) {
        input = new ByteArrayInputStream(bytes);
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
    }
    @Override
    public int read() {
        return input.read();
    }
}
