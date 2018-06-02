package edu.technopolis.advancedjava.season2.Stages;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;


public class WriteAuthStage implements Stage
{
    private final byte answer;

    public WriteAuthStage(byte answer)
    {
        this.answer = answer;
    }

    @Override
    public void process(SelectionKey key, Map<SocketChannel, Stage> connections)
    {
        SocketChannel channel = (SocketChannel) key.channel();
        try
        {
            if (!channel.isOpen() || !key.isWritable())
            {
                return;
            }
            System.out.println("WriteAuthStage");
            buffer.clear();
            buffer.put(SOCKS_VER).put(answer);
            buffer.flip();
            while (buffer.hasRemaining())
            {
                channel.write(buffer);
            }
            buffer.clear();

            if (answer == SUPPORT_METHOD)
            {
                channel.register(key.selector(), SelectionKey.OP_READ);
                connections.put(channel, new ReadConnectStage());
            }
            else
            {
                channel.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
