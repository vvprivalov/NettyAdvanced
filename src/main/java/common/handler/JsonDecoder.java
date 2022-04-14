package common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonDecoder extends MessageToMessageDecoder<String> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        final byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        Message message = OBJECT_MAPPER.readValue(bytes, Message.class);
        System.out.println("Конвертация объекта типа String в объект типа Message");
        out.add(message);
    }
}
