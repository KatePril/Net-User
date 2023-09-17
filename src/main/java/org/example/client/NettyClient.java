package org.example.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyClient {

    static final String HOST = "127.0.0.1";
    static final int PORT = 8001;

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringDecoder());

                            p.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture f = b.connect(HOST, PORT).sync();

            String input = "Mark";
            Channel channel = f.sync().channel();
            channel.writeAndFlush(input);
            channel.flush();

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
