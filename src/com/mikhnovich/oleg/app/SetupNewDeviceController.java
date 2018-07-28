package com.mikhnovich.oleg.app;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URI;

public class SetupNewDeviceController {
    private final URI fullBuildURL = URI.create("https://github.com/olegmikhnovich/RpiControl#full-build");

    private final URI installScriptURL =
            URI.create("https://gist.github.com/olegmikhnovich/c73fa0e91fbaa15d15997fc901db895b");
    private final URI uninstallScriptURL =
            URI.create("https://gist.github.com/olegmikhnovich/a51544fb61ef9b3c6589376668d6d009");
    private final URI updateScriptURL =
            URI.create("https://gist.github.com/olegmikhnovich/84d8d0b4d31352c819de95ed343b4bfd");

    public void loadInstructions() {
        try {
            java.awt.Desktop.getDesktop().browse(fullBuildURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadScript(ActionEvent actionEvent) {
        String name = actionEvent.getSource().toString().split("]")[1];
        int installLabel = name.indexOf("Install script");
        int uninstallLabel = name.indexOf("Uninstall script");
        int updateLabel = name.indexOf("Update script");
        try {
            if (installLabel >= 0) java.awt.Desktop.getDesktop().browse(installScriptURL);
            else if (uninstallLabel >= 0) java.awt.Desktop.getDesktop().browse(uninstallScriptURL);
            else if (updateLabel >= 0) java.awt.Desktop.getDesktop().browse(updateScriptURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
