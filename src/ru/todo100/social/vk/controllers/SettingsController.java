package ru.todo100.social.vk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import ru.todo100.social.AbstractController;
import ru.todo100.social.Settings;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Igor Bobko
 */
public class SettingsController extends AbstractController implements Initializable {

    @FXML
    private TextField antiCaptchaKeyField;
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        antiCaptchaKeyField.setText(getSettings().getAntiCaptchaKey());
    }

    public void saveButtonAction() {
        getSettings().setAntiCaptchaKey(antiCaptchaKeyField.getText());
    }
}
