package com.compilercharisma.chameleonbusinessstudio.webconfig;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayHelper {
    private InputStream inputStream;
    private byte[] buf;
    private final int bufLen = 4 * 0x400;

    public ByteArrayHelper() {
        buf = new byte[bufLen];
    }

    public ByteArrayHelper(InputStream inputStream) {
        this.inputStream = inputStream;
        buf = new byte[bufLen];
    }

    public byte[] toByteArray() throws IOException {
        IOException exception = null;
        int readLen;
        try {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                    outputStream.write(buf, 0, readLen);

                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
            }
        }
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
