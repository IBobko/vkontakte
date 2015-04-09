package ru.todo100.social;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.todo100.social.vk.controllers.Controller;

import java.io.IOException;
import java.io.InputStream;


public class SpringFXMLLoader {

    public static final ApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext("beans.xml");
    private static Logger LOG = Logger.getLogger(SpringFXMLLoader.class);

    public static Controller load(String url) {
        InputStream fxmlStream = null;
        try {
            fxmlStream = SpringFXMLLoader.class.getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(APPLICATION_CONTEXT::getBean);

            Node view = loader.load(fxmlStream);

            Controller controller = loader.getController();
            controller.setView(view);

            return controller;
        } catch (IOException e) {
            LOG.error("Can't load resource", e);
            throw new RuntimeException(e);
        } finally {
            if (fxmlStream != null) {
                try {
                    fxmlStream.close();
                } catch (IOException e) {
                    LOG.error("Can't close stream", e);
                }
            }
        }
    }
}