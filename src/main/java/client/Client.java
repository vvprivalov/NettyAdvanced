package client;

import common.handler.JsonDecoder;
import common.handler.JsonEncoder;
import common.message.AuthMessage;
import common.message.DateMessage;
import common.message.Message;
import common.message.TextMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.time.LocalDateTime;
import java.util.Date;

public class Client {

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        final NioEventLoopGroup group = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 3, 0, 3),
                                    new LengthFieldPrepender(3),
                                    new JsonDecoder(),
                                    new JsonEncoder(),
                                    new SimpleChannelInboundHandler<Message>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
                                            System.out.println("Полученное сообщение: " + msg);
                                        }
                                    }
                            );
                        }
                    });

            System.out.println("Клиент стартовал");

            Channel channel = bootstrap.connect("localhost", 9000).sync().channel();

            while (channel.isActive()) {
                TextMessage textMessage = new TextMessage();
                textMessage.setText(String.format("[%s] %s", LocalDateTime.now(), Thread.currentThread().getName()));
                System.out.println("Пробую отправить сообщение: " + textMessage);
                channel.writeAndFlush(textMessage);

                DateMessage dateMessage = new DateMessage();
                dateMessage.setDate(new Date());
                channel.write(dateMessage);
                System.out.println("Пробую отправить сообщение: " + dateMessage);
                channel.flush();

                AuthMessage auth = new AuthMessage();
                auth.setLogin("Vitaliy");
                auth.setPassword("pass12345");
                channel.writeAndFlush(auth);
                System.out.println("Отправил сообщение об аутентификации");
                Thread.sleep(3000);

            }

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}