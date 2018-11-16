package com.tommy.test;


/**
 * {@link java.nio.channels.SelectionKey}
 */
public class Test01 {
    public static void main(String[] args) {
        System.out.println("OP_READ    : " + (1 << 0));
        System.out.println("OP_WRITE   : " + (1 << 2));
        System.out.println("OP_CONNECT : " + (1 << 3));
        System.out.println("OP_ACCEPT  : " + (1 << 4));
        System.out.println("===================");
        int OP_READ = 1, OP_WRITE = 4, OP_CONNECT = 8, OP_ACCEPT = 16;
        System.out.println("readyOps() & OP_READ) != 0 :" + ((readyOps() & OP_READ) != 0));
        System.out.println("readyOps() & OP_WRITE) != 0 :" + ((readyOps() & OP_WRITE) != 0));
        System.out.println("readyOps() & OP_CONNECT) != 0 :" + ((readyOps() & OP_CONNECT) != 0));
        System.out.println("readyOps() & OP_ACCEPT) != 0 :" + ((readyOps() & OP_ACCEPT) != 0));
    }

    public static int readyOps() {
        return 4;
    }
}
