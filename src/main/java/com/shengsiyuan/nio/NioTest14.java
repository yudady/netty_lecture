package com.shengsiyuan.nio;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NioTest14 {

    public static void main(String[] args) throws Exception {



//        Charset en = Charset.forName("iso-8859-1");
        Charset en = Charset.forName("gbk");
        CharsetEncoder encoder = en.newEncoder();
        CharBuffer charBuffer = CharBuffer.wrap("abcdefghijkl-中文，測試".toCharArray());
        ByteBuffer encode = encoder.encode(charBuffer);
        byte[] mockHttpInputStream = encode.array();

//        byte[] error = new String(mockHttpInputStream,"iso-8859-1").getBytes();

        checkBytesEncoding(mockHttpInputStream, "gbk", "UTF-8");
        checkBytesEncoding(mockHttpInputStream, "big5", "UTF-8");
        checkBytesEncoding(mockHttpInputStream, "iso-8859-1", "iso-8859-1");
        checkBytesEncoding(mockHttpInputStream, "utf-8", "iso-8859-1");
        checkBytesEncoding(mockHttpInputStream, "iso-8859-1");
        checkBytesEncoding(mockHttpInputStream, "utf-8");
        checkBytesEncoding(mockHttpInputStream, "gb18030");
        checkBytesEncoding(mockHttpInputStream, "gbk");
        checkBytesEncoding(mockHttpInputStream, "big5");
    }

    private static void checkBytesEncoding(byte[] bytes, String decoding, String encoding) {
        System.out.println("-----------------------------------");
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
            System.out.println(Hex.encodeHex(bytes));
            byteBuffer.clear();

            Charset de = Charset.forName(decoding);
            CharsetDecoder decoder = de.newDecoder();
            CharBuffer decoderCharBuffer = decoder.decode(byteBuffer);
            char[] charArray = decoderCharBuffer.array();
            String str = new String(charArray);
            System.out.println("-->"+str);

            System.out.println(Hex.encodeHex(str.getBytes(encoding)));

            System.out.println("-----------------------------------");

            Charset en = Charset.forName(encoding);
            CharsetEncoder encoder = en.newEncoder();
            ByteBuffer encode = encoder.encode(decoderCharBuffer);

            System.out.println(new String(encode.array()));

        } catch (Exception e) {
            System.out.println(encoding + " ,出錯嚕");
        }

        System.out.println("-----------------------------------");
    }

    private static void checkBytesEncoding(byte[] bytes, String encoding) {
        System.out.println("-----------------------------------");
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
            System.out.println(Hex.encodeHex(bytes));
            System.out.println(bytes.length);
            System.out.println(encoding);
            byteBuffer.clear();
            Charset charset = Charset.forName(encoding);
            CharsetDecoder decoder = charset.newDecoder();
            CharsetEncoder encoder = charset.newEncoder();
            CharBuffer decoderCharBuffer = decoder.decode(byteBuffer);
            System.out.println(new String(decoderCharBuffer.array()));

        } catch (Exception e) {
            System.out.println(encoding + " ,出錯嚕");
        }

        System.out.println("-----------------------------------");
    }
}
