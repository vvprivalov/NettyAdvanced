package common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class JsonEncoder extends MessageToMessageEncoder<Message> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        byte[] value = OBJECT_MAPPER.writeValueAsBytes(msg);
        out.add(ctx.alloc().buffer().writeBytes(value));
    }
}
