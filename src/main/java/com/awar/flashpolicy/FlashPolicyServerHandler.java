package com.awar.flashpolicy;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutException;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Solverit
 * Date: 20.05.12
 * Time: 20:39
 */
public class FlashPolicyServerHandler extends SimpleChannelUpstreamHandler
{
    private static final Logger log = LoggerFactory.getLogger( FlashPolicyServerHandler.class );

    private static final String NEWLINE = "\r\n";

    @Override
    public void messageReceived( ChannelHandlerContext ctx, MessageEvent e ) throws Exception
    {
        Object msg = e.getMessage();
        ChannelFuture f = e.getChannel().write( this.getPolicyFileContents() );

        log.info( "send policy ok" );

        f.addListener( ChannelFutureListener.CLOSE );
    }

    private ChannelBuffer getPolicyFileContents() throws Exception
    {
        return ChannelBuffers.copiedBuffer(
                "<?xml version=\"1.0\"?>" + NEWLINE +
                        "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">" + NEWLINE +
                        "" + NEWLINE +
                        "<!-- Policy file for xmlsocket://socks.example.com -->" + NEWLINE +
                        "<cross-domain-policy> " + NEWLINE +
                        "" + NEWLINE +
                        "   <!-- This is a master socket policy file -->" + NEWLINE +
                        "   <!-- No other socket policies on the host will be permitted -->" + NEWLINE +
                        "   <site-control permitted-cross-domain-policies=\"master-only\"/>" + NEWLINE +
                        "" + NEWLINE +
                        "   <!-- Instead of setting to-ports=\"*\", administrator's can use ranges and commas -->" + NEWLINE +
                        "   <allow-access-from domain=\"*\" to-ports=\"7778\" />" + NEWLINE +
                        "" + NEWLINE +
                        "</cross-domain-policy>" + NEWLINE,
                CharsetUtil.US_ASCII );
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, ExceptionEvent e ) throws Exception
    {
        if ( e.getCause() instanceof ReadTimeoutException )
        {
            log.error( "Connection timed out." );

            e.getChannel().close();
        }
        else
        {
            log.error( e.getCause().getMessage() );

            e.getChannel().close();
        }
    }
}
