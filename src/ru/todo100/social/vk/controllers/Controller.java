package ru.todo100.social.vk.controllers;


import javafx.scene.Node;

public interface Controller {
    Node getView();

    void setView(Node view);
}