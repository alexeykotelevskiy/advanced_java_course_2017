package edu.technopolis.advancedjava.season2.Stages;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class ConnectToServerStage implements Stage
{
    private final SocketChannel server;
    private final SocketChannel client;

    ConnectToServerStage(SocketChannel server, SocketChannel client)
    {
        this.server = server;
        this.client = client;
    }

    @Override
    public void process(SelectionKey key, Map<SocketChannel, Stage> connections)
    {
        if (!key.isConnectable() || !client.isOpen())
        {
            return;
        }
        System.out.println("ConnectToServerStage");
        SocketChannel channel = (SocketChannel) key.channel();
        try
        {
            try {
                server.finishConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.register(key.selector(), SelectionKey.OP_WRITE);
            connections.put(channel, new ConnectToServerAnswerStage(server, client));

        }
        catch (ClosedChannelException e)
        {
            e.printStackTrace();
        }
    }
}
