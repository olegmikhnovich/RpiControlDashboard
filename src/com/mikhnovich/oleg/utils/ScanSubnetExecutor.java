package com.mikhnovich.oleg.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanSubnetExecutor {
    private final ExecutorService service;
    private ArrayList<Callable<ArrayList<DeviceModel>>> callableTasks;

    public ScanSubnetExecutor(ArrayList<InetAddress> ipSubnet) {
        this.callableTasks = new ArrayList<>();
        this.service = Executors.newCachedThreadPool();
        setCallableTasks(ipSubnet);
    }

    public ArrayList<DeviceModel> execute() {
        ArrayList<DeviceModel> devices = new ArrayList<>();
        try {
            var dataCallable = service.invokeAll(callableTasks);
            for (var dc: dataCallable) {
                var dev = dc.get();
                devices.addAll(dev);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return devices;
    }

    private void setCallableTasks(ArrayList<InetAddress> ipSubnet) {
        for (InetAddress ia : ipSubnet) {
            Callable<ArrayList<DeviceModel>> callableTask = () -> {
                ArrayList<DeviceModel> devices = new ArrayList<>();
                for (int i = 1; i < 255; i++) {
                    try {
                        DatagramSocket clientSocket = new DatagramSocket();
                        clientSocket.setSoTimeout(30);
                        String[] a = ia.getHostName().split("\\.");
                        a[a.length - 1] = i + "";
                        InetAddress IPAddress = InetAddress.getByName(String.join(".", a));
                        String udpAuthMask = "mikhnovich.oleg.rpicontrol";
                        byte[] sendData = udpAuthMask.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4822);
                        byte[] receiveData = new byte[1024];
                        clientSocket.send(sendPacket);
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);
                        String data = new String(receivePacket.getData());
                        clientSocket.close();
                        String[] res = data.split("\n");
                        DeviceModel dm = new DeviceModel(res[0], res[1], res[2], IPAddress);
                        devices.add(dm);
                    } catch (IOException ignored) {
                    }
                }
                return devices;
            };
            this.callableTasks.add(callableTask);
        }
    }
}
