package com.cn.xmf.base.request;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WapperedOutputStream extends ServletOutputStream {
    private ByteArrayOutputStream bos = null;

    public WapperedOutputStream(ByteArrayOutputStream stream)
            throws IOException {
        bos = stream;
    }

    @Override
    public void write(int b) throws IOException {
        bos.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        bos.write(b, 0, b.length);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}
