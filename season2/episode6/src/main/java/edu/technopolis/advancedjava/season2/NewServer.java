package edu.technopolis.advancedjava.season2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.istack.internal.NotNull;

import edu.technopolis.advancedjava.season2.Stages.ReadAuthStage;
import edu.technopolis.advancedjava.season2.Stages.Stage;

/**
 * Сервер, построенный на API java.nio.* . Работает единственный поток,
 * обрабатывающий события, полученные из селектора.
 * Нельзя блокировать или нагружать долгоиграющими действиями данный поток, потому что это
 * замедлит обработку соединений.
 */
public class NewServer
{
    public static void main(String[] args)
    {
        Map<SocketChannel, Stage> connections = new HashMap<>();
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open())
        {
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(10001));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true)
            {
                selector.select(); //блокирующий вызов
                @NotNull
                Set<SelectionKey> keys = selector.selectedKeys();
                if (keys.isEmpty())
                {
                    continue;
                }
                //сначала обработаем новые соединения и запись, чтобы освободить буфферы
                keys.removeIf(key -> {
                    if (key.isAcceptable())
                    {
                        accept(connections, key);
                        return true;
                    }
                    if (key.isWritable()) {
                        process(key, connections);
                        return true;
                    }
                 return false;
                });
                //затем все остальное
                keys.removeIf(key -> {
                    process(key, connections);
                    return true;
                });
                connections.keySet().removeIf(channel -> !channel.isOpen());
            }
        }
        catch (IOException e)
        {
            LogUtils.logException("Unexpected error on processing incoming connection", e);
        }
    }

    private static void process(SelectionKey key, Map<SocketChannel, Stage> connections)
    {
        Stage connection = connections.get(key.channel());
        connection.process(key, connections);
    }

    private static void accept(Map<SocketChannel, Stage> connections, SelectionKey key)
    {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = null;
        try
        {
            channel = serverChannel.accept(); //non-blocking call
            channel.configureBlocking(false);
            channel.register(key.selector(), SelectionKey.OP_READ);
            connections.put(channel, new ReadAuthStage());
        }
        catch (IOException e)
        {
            LogUtils.logException("Failed to process channel " + channel, e);
            if (channel != null)
            {
                closeChannel(channel);
            }
        }
    }


    private static void closeChannel(SocketChannel accept)
    {
        try
        {
            accept.close();
        }
        catch (IOException e)
        {
            System.err.println("Failed to close channel " + accept);
        }
    }
}
