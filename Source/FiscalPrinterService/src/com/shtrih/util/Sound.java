/*
 * Sound.java
 *
 * Created on 21 April 2008, 12:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Sound 
{
    /** Creates a new instance of Sound */
    public Sound() {
    }

    public static void beep(int freq, int timeToBeep)
    throws Exception 
    {
        // Allowable 8000,11025,16000,22050,44100
        float sampleRate = 16000.0F;
        // Prepare data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        int sampleCount = (int)sampleRate*timeToBeep/1000;
        for(int i=0;i<sampleCount;i++)
        {
            double time = i/sampleRate;
            double sinValue =
            (Math.sin(2*Math.PI*freq*time) +
            Math.sin(2*Math.PI*(freq/1.8)*time) +
            Math.sin(2*Math.PI*(freq/1.5)*time))/3.0;
            dos.writeShort((short)(16000*sinValue));
        }
        // play data
        byte[] audioData = baos.toByteArray();
        InputStream bais = new ByteArrayInputStream(audioData);
        AudioFormat audioFormat = new AudioFormat(sampleRate, 16, 1, true, true);
        AudioInputStream ais = new AudioInputStream(bais, audioFormat, 
            audioData.length/audioFormat.getFrameSize());

        DataLine.Info dataLineInfo = new DataLine.Info(
            SourceDataLine.class, audioFormat);

        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();
        byte playBuffer[] = new byte[16384];
        int cnt;
        while((cnt = ais.read(playBuffer, 0, playBuffer.length)) != -1)
        {
            if(cnt > 0)
            {
                sourceDataLine.write(playBuffer, 0, cnt);
            }
        }
        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();
    }
}

