package edu.technopolis.advancedjava.season2.Stages;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class ReadConnectStage implements Stage
{
    @Override
    public void process(SelectionKey key, Map<SocketChannel, Stage> connections)
    {

        SocketChannel channel = (SocketChannel) key.channel();
        if (!channel.isOpen() || !key.isReadable())
        {
            return;
        }
        System.out.println("ReadConnectStage");
        try
        {
            buffer.clear();
            int countBytes = channel.read(buffer);
            buffer.flip();
            if (countBytes == -1 || countBytes < 10)
            {
                channel.close();
                return;
            }
            if (buffer.get() != SOCKS_VER || buffer.get() != COMMAND || buffer.get() != RESERVED_BYTE || buffer.get() != ADRESS_TYPE_IPV4)
            {
                channel.close();
                return;
            }
            byte[] ip = new byte[4];
            buffer.get(ip);
            short port = buffer.getShort();
            SocketChannel server = SocketChannel.open();
            server.configureBlocking(false);
            server.register(key.selector(), SelectionKey.OP_CONNECT);
            server.connect(new InetSocketAddress(InetAddress.getByAddress(ip), port));
            System.out.println("connecting to " + server.getRemoteAddress());
            connections.put(server, new ConnectToServerStage(server, channel));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
