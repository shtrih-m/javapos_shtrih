package com.shtrih.util;

public class CircularBuffer {

    /** Size of the buffer */
    private final int size;

    /** The buffer */
    private final byte[] buffer;

    /** Index of the next data to be read from the buffer */
    private int readIndex;

    /** Index of the next data written in the buffer */
    private int writeIndex;

    public CircularBuffer(int size) {
        this.size = size;
        buffer = new byte[size];
    }

    /**
     * Tells if a new byte can be read from the buffer.
     */
    public synchronized int available()
    {
        if (writeIndex >= readIndex) {
            return writeIndex - readIndex;
        } else
        {
            return writeIndex + (size-readIndex);
        }
    }

    public synchronized void clear() {
        readIndex = 0;
        writeIndex = 0;
    }

    public synchronized int read() {
        if (available() > 0) {
            final int value = buffer[readIndex];
            readIndex = (readIndex + 1) % size;
            return value & 0xFF;
        }
        return -1;
    }


    public synchronized byte[] read(int len)
    {
        if (len > available()){
            len = available();
        }
        byte[] out = new byte[len];
        for (int i=0;i<len;i++){
            out[i] = (byte)read();
        }
        return out;
    }

    /**
     * Writes a byte to the buffer.
     */
    public void write(final int value)
    {
        buffer[writeIndex] = (byte) value;
        writeIndex = (writeIndex + 1) % size;
    }

    public synchronized void write(byte[] value) {
        for (int i=0;i<value.length;i++){
            write(value[i]);
        }
    }

    /**
     * Reads a byte from the buffer.
     */
    /**
     * Copy a previous interval in the buffer to the current position.
     *
     * @param distance the distance from the current write position
     * @param length   the number of bytes to copy
     */
    public void copy(final int distance, final int length) {
        final int pos1 = writeIndex - distance;
        final int pos2 = pos1 + length;
        for (int i = pos1; i < pos2; i++) {
            buffer[writeIndex] = buffer[(i + size) % size];
            writeIndex = (writeIndex + 1) % size;
        }
    }
}