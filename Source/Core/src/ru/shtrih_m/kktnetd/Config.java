/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.shtrih_m.kktnetd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Виталий
 */
public class Config {

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

    public Config() {
    }

    public String toJson() throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public Config fromJson(String jsonText) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return (Config) gson.fromJson(jsonText, Config.class);
    }
}
