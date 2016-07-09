package com.develop.netty;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
    	
    	ByteBuf in = (ByteBuf) msg;
    	try {
    		System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
//			while (in.isReadable()) {
//				System.out.println((char)in.readChar());
//				System.out.flush();
//			}
    		ctx.write(msg);
    		ctx.flush();
		} catch (Exception e) {
			ReferenceCountUtil.release(msg);
		}
       // ((ByteBuf) msg).release(); // (3)
    	//System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
    	//in.release();
    	
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        final ByteBuf time = ctx.alloc().buffer(4); // (2)
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        }); // (4)
    }
}
