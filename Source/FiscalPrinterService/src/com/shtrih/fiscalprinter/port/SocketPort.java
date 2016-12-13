/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.port;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.StringTokenizer;

import com.shtrih.util.CompositeLogger;

import com.shtrih.util.Localizer;
import gnu.io.SerialPort;

/**
 * @author V.Kravtsov
 */
public class SocketPort implements PrinterPort {

    private boolean connected = false;
    private int timeout = 1000;
    private Socket socket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private String portName = "";
    static CompositeLogger logger = CompositeLogger.getLogger(SocketPort.class);

    public SocketPort() throws Exception 
    {
    }

    
    public void open() throws Exception {
        open(0);
    }
            
    public void open(int timeout) throws Exception 
    {
        if (connected) return; 
        
        socket = new Socket();
        socket.setReuseAddress(true);
        socket.setSoTimeout(this.timeout);
        StringTokenizer tokenizer = new StringTokenizer(portName, ":");
        String host = tokenizer.nextToken();
        int port = Integer.parseInt(tokenizer.nextToken());
        socket.connect(new InetSocketAddress(host, port), timeout);
        in = socket.getInputStream();
        if (in == null){
            throw new Exception("socket.getInputStream() return null");
        }
        out = socket.getOutputStream();
        if (out == null){
            throw new Exception("socket.getOutputStream() return null");
        }
        SharedObjects.getInstance().addref(portName);
        connected = true;
    }

    public void close() throws Exception 
    {
        if (!connected) return;
        
        connected = false;
        if (SharedObjects.getInstance().release(portName))
        {
            
            in.close();
            out.close();
            in = null;
            out = null;
            socket.close();
            socket = null;
            Thread.sleep(100);
        }
    }

    public int readByte() throws Exception 
    {
        open();
        
        int b = doReadByte();
        return b;
    }

    public int doReadByte() throws Exception {
        open();
        
        int result;
        long startTime = System.currentTimeMillis();
        for (;;) {
            Thread.sleep(0, 001);
            long currentTime = System.currentTimeMillis();
            if (in.available() > 0) {
                result = in.read();
                if (result >= 0) {
                    return result;
                }
            }
            if ((currentTime - startTime) > timeout) {
                noConnectionError();
            }
        }
    }

    private void noConnectionError() throws Exception {
        connected = false;
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    public byte[] readBytes(int len) throws Exception {
        open();
        
        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            int count = in.read(data, offset, len);
            if (count == -1) {
                break;
            }
            len -= count;
            offset += count;
        }
        return data;
    }

    public void connect() throws Exception {
        if (!connected) {
            open(timeout);
        }
    }

    public void write(byte[] b) throws Exception {
        
        open();
        
        for (int i = 0; i < 2; i++) {
            try {
                connect();
                out.write(b);
                out.flush();
                return;
            } catch (IOException e) {
                socket.close();
                if (i == 1) {
                    throw e;
                }
                logger.error(e);
            }
        }
    }

    public void write(int b) throws Exception {
        open();
        byte[] data = new byte[1];
        data[0] = (byte) b;
        write(data);
    }

    public void setBaudRate(int baudRate) throws Exception {
    }

    public void setTimeout(int timeout) throws Exception {
        this.timeout = timeout;
    }

    public String getPortName(){
        return portName;
    }
    
    public void setPortName(String portName) throws Exception 
    {
        if (portName.equalsIgnoreCase(this.portName)){
            return;
        }
        
        this.portName = portName;
        socket = (Socket)SharedObjects.getInstance().findObject(portName);
        if (socket == null){
            socket = new Socket();
            SharedObjects.getInstance().add(socket, portName);
        }
    }

    public InputStream getInputStream() throws Exception {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws Exception {
        return socket.getOutputStream();
    }

    public Object getSyncObject() throws Exception {
        return socket;
    }

    public boolean isSearchByBaudRateEnabled() {
        return false;
    }
}
