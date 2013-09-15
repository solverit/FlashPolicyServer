package com.awar.flashpolicy;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;
import org.jboss.netty.util.CharsetUtil;

/**
 * User: Solverit
 * Date: 20.05.12
 * Time: 20:37
 */
public class FlashPolicyServerDecoder extends ReplayingDecoder<VoidEnum>
{
    private final ChannelBuffer requestBuffer = ChannelBuffers.copiedBuffer( "<policy-file-request/>", CharsetUtil.US_ASCII );

    @Override
    protected Object decode( ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, VoidEnum state )
    {
        ChannelBuffer data = buffer.readBytes( requestBuffer.readableBytes() );

        if ( data.equals( requestBuffer ) )
        {
            return data;
        }

        channel.close();

        return null;
    }
}
