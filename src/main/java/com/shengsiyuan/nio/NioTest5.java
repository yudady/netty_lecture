package com.shengsiyuan.nio;

import java.nio.ByteBuffer;

/** ByteBuffer类型化的put与get方法 */
public class NioTest5 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(15); // 4 byte
        buffer.putLong(500000000L); // 8 byte
        buffer.putDouble(14.123456); // 8 byte
        buffer.putChar('你'); // 2 byte
        buffer.putShort((short) 2); // 2 byte
        buffer.putChar('我'); // 2 byte

        buffer.flip();

        byte[] bb = new byte[4];
        for (int i = 0; i < 4; i++) {
            bb[i] = buffer.get();
        }

        System.out.println(byteArrayToInt(bb)); // buffer.getInt()
        System.out.println(buffer.getLong()); // 呼叫2次 buffer.getInt()

        System.out.println(buffer.getDouble());

        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());

        System.out.println("---------");
        System.out.println(buffer.array().length);
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
    }

    public static int byteArrayToInt(byte[] b) {
        return ByteBuffer.wrap(b).getInt();
    }
}
