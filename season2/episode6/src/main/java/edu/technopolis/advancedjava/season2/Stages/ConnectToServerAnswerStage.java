package edu.technopolis.advancedjava.season2.Stages;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class ConnectToServerAnswerStage implements Stage
{
    private final SocketChannel server;
    private final SocketChannel client;

    public ConnectToServerAnswerStage(SocketChannel server, SocketChannel client)
    {
        this.server = server;
        this.client = client;
    }

    @Override
    public void process(SelectionKey key, Map<SocketChannel, Stage> connections)
    {

        if (!key.isWritable() || !client.isOpen())
        {
            return;
        }
        System.out.println("ConnectToServerAnswerStage");
        try
        {
            server.finishConnect();
            System.out.println(server.getRemoteAddress().toString());
            byte[] ip = Stage.getIP(server.getRemoteAddress().toString());
            short port = Stage.getPort(server.getRemoteAddress().toString());
            buffer.clear();
            buffer.put(SOCKS_VER).put(SUPPORT_METHOD).put(RESERVED_BYTE).put(ADRESS_TYPE_IPV4).put(ip).putShort(port);
            buffer.flip();
            client.write(buffer);
            client.register(key.selector(), SelectionKey.OP_READ);
            CommunicationStage clientStage = new CommunicationStage(client, server);
            CommunicationStage serverStage = new CommunicationStage(server, client);
            serverStage.setServerBuffer(clientStage.getBuffer());
            clientStage.setServerBuffer(serverStage.getBuffer());
            connections.put( server,  clientStage);
            connections.put( client, serverStage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
