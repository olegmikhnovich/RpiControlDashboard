package com.mikhnovich.oleg.app;

import com.mikhnovich.oleg.utils.DeviceModel;
import com.mikhnovich.oleg.utils.IPAddressValidator;
import com.mikhnovich.oleg.utils.ScanSubnetExecutor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class MyDevicesController {
    @FXML
    public TableView<DeviceModel> devicesTable;
    @FXML
    public TextField filterField;
    @FXML
    public Button updateButton;
    @FXML
    private TableColumn<DeviceModel, String> nameColumn;
    @FXML
    private TableColumn<DeviceModel, String> typeColumn;
    @FXML
    private TableColumn<DeviceModel, String> osColumn;
    @FXML
    private TableColumn<DeviceModel, String> ipv4Column;

    private final ArrayList<InetAddress> ipSubnet = getIpSubnet();
    private ObservableList<DeviceModel> tableData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        this.scanAllDevices();
    }

    public void loadDevicePortal() {
        DeviceModel deviceModel = devicesTable.getSelectionModel().getSelectedItem();
        try {
            if(deviceModel != null) {
                java.awt.Desktop.getDesktop().browse(
                        URI.create("http://" + deviceModel.getIpv4().getHostName() + ":8080"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        this.scanAllDevices();
    }

    private void scanAllDevices() {
        this.updateButton.setDisable(true);
        this.updateButton.setText("Loading ...");
        Task<ArrayList<DeviceModel>> task = new Task<>() {
            @Override
            protected ArrayList<DeviceModel> call() {
                ScanSubnetExecutor executor = new ScanSubnetExecutor(ipSubnet);
                ArrayList<DeviceModel> devices = executor.execute();
                setTable(devices);
                return devices;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                updateButton.setDisable(false);
                updateButton.setText("Refresh");
            }

            private void setTable(ArrayList<DeviceModel> devices) {
                tableData.clear();
                tableData.addAll(devices);
                nameColumn.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().getName()));
                typeColumn.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().getType()));
                osColumn.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().getOs()));
                ipv4Column.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().getIpv4().getHostName()));
                FilteredList<DeviceModel> filteredData = new FilteredList<>(tableData, p -> true);
                filterField.textProperty().addListener((observable, oldValue, newValue) ->
                        filteredData.setPredicate(device -> {
                            if (newValue == null || newValue.isEmpty()) return true;
                            String lowerCaseFilter = newValue.toLowerCase();
                            return device.getName().toLowerCase().contains(lowerCaseFilter) ||
                                    (device.getIpv4().getHostName().toLowerCase().contains(lowerCaseFilter));
                        }));
                SortedList<DeviceModel> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(devicesTable.comparatorProperty());
                devicesTable.setItems(sortedData);
            }
        };
        Thread thread = new Thread(task);
        thread.setName("Load devices list");
        thread.start();
    }

    private ArrayList<InetAddress> getIpSubnet() {
        ArrayList<InetAddress> addr = new ArrayList<>();
        IPAddressValidator ipAddressValidator = new IPAddressValidator();
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) ias.nextElement();
                    if (ipAddressValidator.validateV4(ia.getHostAddress())) addr.add(ia);
                }
            }
        } catch (SocketException ignored) {}
        for (int i = 0; i < addr.size(); i++) {
            String sa = addr.get(i).getHostAddress();
            String[] saar = sa.split("\\.");
            saar[saar.length - 1] = "1";
            sa = String.join(".", saar);
            try {
                addr.set(i, InetAddress.getByName(sa));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        addr.removeIf(p -> p.getHostAddress().equals("127.0.0.1"));
        return addr;
    }
}
