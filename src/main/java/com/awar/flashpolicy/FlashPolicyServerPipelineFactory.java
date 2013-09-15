package com.awar.flashpolicy;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

/**
 * User: Solverit
 * Date: 20.05.12
 * Time: 20:41
 */
public class FlashPolicyServerPipelineFactory implements ChannelPipelineFactory
{
    private final Timer timer = new HashedWheelTimer();

    public ChannelPipeline getPipeline() throws Exception
    {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();
        pipeline.addLast( "timeout", new ReadTimeoutHandler( timer, 30 ) );
        pipeline.addLast( "decoder", new FlashPolicyServerDecoder() );
        pipeline.addLast( "handler", new FlashPolicyServerHandler() );

        return pipeline;
    }
}
