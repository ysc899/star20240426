package kr.co.seesoft.nemo.starnemoapp.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GetByte {

    public static byte[] myGetBytes(final String data, final String charset) {

        if (data == null) {
            throw new IllegalArgumentException("data may not be null");
        }

        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }

        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return data.getBytes();
        }
    }

}
