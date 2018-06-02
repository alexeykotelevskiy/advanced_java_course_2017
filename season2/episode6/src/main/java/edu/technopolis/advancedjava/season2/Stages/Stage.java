package edu.technopolis.advancedjava.season2.Stages;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;


public interface Stage
{
    byte SOCKS_VER = 0x05;
    byte SUPPORT_METHOD = 0x00;
    byte COMMAND = 0x01;
    byte ADRESS_TYPE_IPV4 = 0x01;
    byte UNSUPPORTED_METHOD = 0xF;
    byte RESERVED_BYTE = 0x00;
    ByteBuffer buffer = ByteBuffer.allocateDirect(512);

    void process(SelectionKey key, Map<SocketChannel, Stage> connections);

    static byte[] getIP(String address)
    {
        byte[] ans = new byte[4];
        String[] s = address.split("\\/|\\.|\\:");
        for (int i = 1; i < 5; i++)
        {
            ans[i - 1] = (byte) Integer.parseInt(s[i]);
        }
        return ans;
    }

    static short getPort(String address)
    {
        short ans;
        String[] s = address.split("\\/|\\.|\\:");
        ans = Short.parseShort(s[s.length - 1]);
        return ans;
    }
}