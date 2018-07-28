package com.mikhnovich.oleg.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RootController {
    @FXML
    public Button myDevicesTab;
    @FXML
    public Button setupDeviceTab;
    @FXML
    public AnchorPane tabsStage;

    public void myDevicesTabClick() throws IOException {
        Node pane = FXMLLoader.load(getClass().getResource("myDevices.fxml"));
        tabsStage.getChildren().setAll(pane);
    }

    public void setupDeviceTabClick() throws IOException {
        Node pane = FXMLLoader.load(getClass().getResource("setupNewDevice.fxml"));
        tabsStage.getChildren().setAll(pane);
    }
}
