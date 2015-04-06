package ru.todo100.social;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;



public class SpringFXMLLoader {

    private static final ApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext("beans.xml");
    private static Logger LOG = Logger.getLogger(SpringFXMLLoader.class);

    public static Controller load(String url) {
        InputStream fxmlStream = null;
        try {
            fxmlStream = SpringFXMLLoader.class.getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return APPLICATION_CONTEXT.getBean(aClass);
                }
            });

            Node view = (Node) loader.load(fxmlStream);

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