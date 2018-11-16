package com.shengsiyuan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap();

    public static void main(String[] args) throws IOException {
        int jdkVersion = 8;
        boolean isNetty = true;
        SelectorProvider provider = SelectorProvider.provider();
        ServerSocketChannel serverSocketChannel;
        Selector selector;
        if (isNetty) {
            serverSocketChannel = provider.openServerSocketChannel();
        } else {
            serverSocketChannel = ServerSocketChannel.open();
        }

        serverSocketChannel.configureBlocking(false);

        if (isNetty) {
            selector = provider.openSelector();
        } else {
            selector = Selector.open();
        }

        serverSocketChannel.register(selector, 0, serverSocketChannel);

        if (jdkVersion >= 7) {
            serverSocketChannel.bind(new InetSocketAddress(8899));
        } else {
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(8899));
        }


        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, serverSocketChannel);

        System.out.println("serverSocketChannel.register#OP_ACCEPT");
        while (true) {
            try {
                int select = selector.select();

                if (select <= 0) {
                    TimeUnit.SECONDS.sleep(1);
                    return;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;
                    ServerSocketChannel server;
                    try {
                        if (selectionKey.isAcceptable()) {
                            if (jdkVersion >= 7) {
                                server = (ServerSocketChannel) selectionKey.attachment();
                            } else {
                                server = (ServerSocketChannel) selectionKey.channel();
                            }

                            client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);

                            String key = "【" + UUID.randomUUID().toString() + "】";

                            clientMap.put(key, client);

                        } else if (selectionKey.isReadable()) {
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                            int count = client.read(readBuffer);

                            if (count > 0) {
                                readBuffer.flip();

                                Charset charset = Charset.forName("utf-8");
                                String receivedMessage = String.valueOf(charset.decode(readBuffer).array());

                                System.out.println(client + ": " + receivedMessage);

                                String senderKey = null;

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    if (client == entry.getValue()) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    SocketChannel value = entry.getValue();

                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                                    writeBuffer.put((senderKey + ": " + receivedMessage).getBytes());
                                    writeBuffer.flip();

                                    value.write(writeBuffer);
                                }
                            }
                        } else {
                            System.out.println("-------> interestOps : " + selectionKey.interestOps());
                        }

                        selectionKeys.remove(selectionKey);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

//                selectionKeys.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
