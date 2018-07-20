package com.mikhnovich.oleg.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DeviceModel {
    private String name;
    private String type;
    private String os;
    private InetAddress ipv4;

    DeviceModel(String name, String type, String os, InetAddress ipv4) {
        this.name = name;
        this.type = type;
        this.os = os;
        this.ipv4 = ipv4;
    }

    public DeviceModel(String name, String type, String os, String ipv4) {
        this.name = name;
        this.type = type;
        this.os = os;
        try {
            this.ipv4 = InetAddress.getByName(ipv4);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public InetAddress getIpv4() {
        return ipv4;
    }

    public void setIpv4(InetAddress ipv4) {
        this.ipv4 = ipv4;
    }
}
