package ru.todo100.social.vk.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.todo100.social.Settings;

/**
 * Created by igor on 18.04.15.
 */
public class ConfirmAntiCaptchaKeyController extends AbstractController {
    public void saveButtonAction(ActionEvent actionEvent) {

    }

    @FXML
    private TextField antiCaptchaKeyField;
    private Settings settings;



    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
