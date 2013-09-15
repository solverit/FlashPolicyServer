package com.awar.flashpolicy;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Flash cross-domain policy server which responds on port 1843.
 * <p/>
 * See http://www.adobe.com/devnet/flashplayer/articles/socket_policy_files.html
 * for further details.
 * <p/>
 * User: Solverit
 * Date: 20.05.12
 * Time: 20:33
 */
public class FlashPolicyServer
{
    private static final Logger log = LoggerFactory.getLogger( FlashPolicyServer.class );

    public void start()
    {
        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool() ) );

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory( new FlashPolicyServerPipelineFactory() );

        bootstrap.setOption( "child.tcpNoDelay", true );
        bootstrap.setOption( "child.keepAlive",  true );

        // Bind and start to accept incoming connections.
        bootstrap.bind( new InetSocketAddress( 1843 ) );

        log.info( "Start FlashPolicyServer: OK" );
    }
}
