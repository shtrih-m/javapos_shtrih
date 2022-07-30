/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.shtrih_m.kktnetd;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Виталий
 */
public class PPPConfig {

    // Constants
    public static final String TRANSPORT_TYPE_SERIAL = "serial";
    public static final String TRANSPORT_TYPE_FORWARDER = "forwarder";

    public class Resender {

        public String host = "localhost";
        public int port = 7778;
        public int remote_port = 7778;
        public int socks_port = 1080;

        public Resender() {
        }
    }

    public class Transport {

        public String type = "forwarder";
        public String path = "17778";

        public Transport() {
        }
    }

    public class PPP {

        public String our_ip = "192.168.1.169";
        public String peer_ip = "192.168.1.168";
        public String peer_dns = "8.8.8.8";
    }

    public boolean handle_signals = true;
    public Resender resender = new Resender();
    public Transport transport = new Transport();
    public PPP ppp = new PPP();

    public PPPConfig() {
    }

    public void load(String fileName) throws Exception {
        fromJson(FileUtils.load(fileName));
    }

    public void save(String fileName) throws Exception {
        FileUtils.save(fileName, toJson());
    }

    public String toJson() throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static PPPConfig fromJson(String jsonText) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return (PPPConfig) gson.fromJson(jsonText, PPPConfig.class);
    }
}
