package server;

import common.message.AuthMessage;
import common.message.DateMessage;
import common.message.Message;
import common.message.TextMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.sctp.SctpOutboundByteStreamHandler;


public class FirstServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Новый канал активирован");
        TextMessage answer = new TextMessage();
        answer.setText("Успешное соединение");
        ctx.writeAndFlush(answer);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        if (msg instanceof TextMessage) {
            TextMessage message = (TextMessage) msg;
            System.out.println("Входящее сообщение типа Текст: " + message.getText());
            ctx.writeAndFlush(msg);
        }
        if (msg instanceof DateMessage) {
            DateMessage message = (DateMessage) msg;
            System.out.println("Входящее сообщение типа Дата: " + message.getDate());
            ctx.writeAndFlush(msg);
        }
        if (msg instanceof AuthMessage) {
            AuthMessage auth = (AuthMessage) msg;
            System.out.println("Входящее сообщение типа Auth: [ Логин: " + auth.getLogin() + "] [ Пароль: " + auth.getPassword() + " ]");
            ctx.writeAndFlush(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        System.out.println("Клиент отключился");
    }
}