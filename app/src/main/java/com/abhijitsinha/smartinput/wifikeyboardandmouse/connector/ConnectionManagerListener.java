package com.abhijitsinha.smartinput.wifikeyboardandmouse.connector;

public interface ConnectionManagerListener {
    void onConnected();

    void onDisconnected();

    void onConnectionFailed();
}
