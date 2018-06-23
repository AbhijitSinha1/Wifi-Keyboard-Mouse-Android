package com.abhijitsinha.smartinput.wifikeyboardandmouse.utils;

public class Settings {
    private Settings(){}
    private static final Settings instanceOf = new Settings();
    private String host;
    private int port;

    public static Settings getInstanceOf() {
        return instanceOf;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
