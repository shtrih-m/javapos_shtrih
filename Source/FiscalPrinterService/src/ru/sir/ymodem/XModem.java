package ru.sir.ymodem;

import java.io.*;

/**
 * XModem <br/>
 * <p>
 * Created by Anton Sirotinkin (aesirot@mail.ru), Moscow 2014 <br/>
 * I hope you will find this program useful.<br/>
 * You are free to use/modify the code for any purpose, but please leave a reference to me.<br/>
 */
public class XModem {
    private Modem modem;

    /**
     * Constructor
     *
     * @param inputStream  stream for reading received data from other side
     * @param outputStream stream for writing data to other side
     */
    public XModem(InputStream inputStream, OutputStream outputStream) {
        this.modem = new Modem(inputStream, outputStream);
    }

    /**
     * Send a file. <br/>
     * <p>
     * This method support correct thread interruption, when thread is interrupted "cancel of transmission" will be send.
     * So you can move long transmission to other thread and interrupt it according to your algorithm.
     *
     * @param file
     * @throws java.io.IOException
     */
    public void send(File file) throws IOException, InterruptedException {
        modem.send(file, false);
    }

    /**
     * Receive file <br/>
     * <p>
     * This method support correct thread interruption, when thread is interrupted "cancel of transmission" will be send.
     * So you can move long transmission to other thread and interrupt it according to your algorithm.
     *
     * @param file file path for storing
     * @throws java.io.IOException
     */
    public void receive(File file) throws IOException {
        modem.receive(file, false);
    }
}
