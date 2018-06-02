package edu.technopolis.advancedjava.season2.Stages;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class ConnectToServerStage implements Stage
{
    private final SocketChannel server;
    private final SocketChannel client;

    ConnectToServerStage(SocketChannel server, SocketChannel key)
    {
        this.server = server;
        this.client = key;
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
            connections.put(channel, new ConnectToServerAnswerStage(server, channel));
            client.register(key.selector(), SelectionKey.OP_WRITE);
        }
        catch (ClosedChannelException e)
        {
            e.printStackTrace();
        }
    }
}
