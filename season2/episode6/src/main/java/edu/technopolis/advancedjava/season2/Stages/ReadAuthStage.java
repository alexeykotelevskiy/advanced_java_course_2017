package edu.technopolis.advancedjava.season2.Stages;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import static edu.technopolis.advancedjava.season2.LogUtils.logException;

public class ReadAuthStage implements Stage
{
    @Override
    public void process(SelectionKey key, Map<SocketChannel, Stage> connections)
    {
        SocketChannel channel = (SocketChannel) key.channel();
        if (!key.isReadable() || !channel.isOpen())
        {
            return;
        }
        System.out.println("ReadAuthStage");
        boolean methodFound = false;
        try
        {
            buffer.clear();
            int countBytes = channel.read(buffer);
            buffer.flip();
            if (countBytes < 3 || buffer.get() != SOCKS_VER)
            {
                channel.close();
                return;
            }
            int countMethods = buffer.get();
            for (int i = 0; i < countMethods; i++)
            {
                if (buffer.get() == SUPPORT_METHOD)
                {
                    methodFound = true;
                    break;
                }
            }
        }
        catch (IOException e)
        {
            logException("IO error", e);
            return;
        }
        try
        {
            connections.put(channel, new WriteAuthStage(methodFound ? SUPPORT_METHOD : UNSUPPORTED_METHOD));
            channel.register(key.selector(), SelectionKey.OP_WRITE);
        }
        catch (ClosedChannelException e)
        {
            e.printStackTrace();
        }
    }
}
