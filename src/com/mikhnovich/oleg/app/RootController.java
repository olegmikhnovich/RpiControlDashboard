package com.mikhnovich.oleg.app;

import javafx.event.ActionEvent;
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
    public Button examplesTab;
    @FXML
    public AnchorPane tabsStage;

    public void myDevicesTabClick(ActionEvent actionEvent) throws IOException {
        Node pane = FXMLLoader.load(getClass().getResource("myDevices.fxml"));
        tabsStage.getChildren().setAll(pane);
    }

    public void setupDeviceTabClick(ActionEvent actionEvent) {
    }

    public void examplesTabClick(ActionEvent actionEvent) {
    }
}
