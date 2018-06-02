package edu.technopolis.advancedjava.season2.Stages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class CommunicationStage implements Stage
{
    private final SocketChannel server;
    private final SocketChannel client;
    private ByteBuffer serverBuffer;

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setServerBuffer(ByteBuffer serverBuffer) {
        this.serverBuffer = serverBuffer;
    }

    public CommunicationStage(SocketChannel server, SocketChannel client)
    {
        this.server = server;
        this.client = client;
    }

    @Override
    public void process(SelectionKey key, Map<SocketChannel, Stage> connections)
    {
        System.out.println("communication stage");
        try
        {
            if (!server.isOpen())
            {
                client.close();
                return;
            }
            if (!client.isOpen())
            {
                server.close();
                return;
            }
            if (key.isReadable())
            {
                buffer.clear();
                int countBytes = client.read(buffer);
                buffer.flip();
                System.out.println(countBytes);
                if (countBytes > 0)
                {
                    server.register(key.selector(), SelectionKey.OP_WRITE);
                }
                else
                {
                    server.close();
                    client.close();
                }
            }
            else if (key.isWritable())
            {
                while (serverBuffer.hasRemaining())
                {
                    client.write(serverBuffer);
                }
                client.register(key.selector(), SelectionKey.OP_READ);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
