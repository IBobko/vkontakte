package ru.todo100.social.vk.controllers;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.services.LogsService;
import ru.todo100.social.vk.datas.LogData;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * @author Igor Bobko
 */
public class LogsController extends AbstractController implements Initializable {
    public TableColumn groupId;
    public TableColumn groupMessage;
    public TableColumn groupAttachment;
    public TableView logTableView;
    public TableColumn linkToPostColumn;
    public TableColumn date;
    public TableColumn groupName;
    public TextField countField;
    private LogsService logsService;

    public LogsService getLogsService() {
        return logsService;
    }

    public void setLogsService(LogsService logsService) {
        this.logsService = logsService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        groupId.setCellValueFactory(new PropertyValueFactory<LogData, String>("groupID"));
        groupName.setCellValueFactory(new PropertyValueFactory<LogData, String>("groupName"));
        groupMessage.setCellValueFactory(new PropertyValueFactory<LogData, String>("message"));
        groupAttachment.setCellValueFactory(new PropertyValueFactory<LogData, String>("attachment"));
        date.setCellValueFactory(new PropertyValueFactory<LogData,String>(""){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LogData, String> param) {

                return new ObservableValue<String>() {
                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }

                    @Override
                    public void addListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public String getValue() {
                        return dateFormat.format(param.getValue().getCreated().getTime());
                    }
                };
            }
        });

        linkToPostColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new ButtonTableCell<Boolean>();
            }
        });




        linkToPostColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<LogData, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<LogData, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        final ObservableList<LogData> data =
                FXCollections.observableArrayList(

                );
        data.addAll(getLogsService().getLogs());

        logTableView.setItems(data);


    }

    public void showAction(ActionEvent actionEvent) {
        Integer count = Integer.parseInt(countField.getText());
        final ObservableList<LogData> data =
                FXCollections.observableArrayList(

                );
        data.addAll(getLogsService().getLogs(count));

        logTableView.setItems(data);
    }

    public static class ButtonTableCell<S> extends TableCell<LogData, Boolean> {
        private final Button button;
        private ObservableValue<LogData> ov;
        public ButtonTableCell() {
            this.button = new Button();
            this.button.setAlignment(Pos.CENTER);
            setAlignment(Pos.CENTER);
            setGraphic(button);
            this.button.setText("Перейти");

            this.button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    LogData logData = getTableView().getItems().get(getIndex());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("http://vk.com/");
                    if (logData.getGroupType().equals("group")) {
                        stringBuilder.append("club");
                    } else if (logData.getGroupType().equals("page")) {
                        stringBuilder.append("public");
                    } else {
                        new Exception("Unknown group type");
                    }
                    stringBuilder.append(logData.getGroupID());
                    stringBuilder.append("?w=wall-").append(logData.getGroupID()).append("_").append(logData.getPostID());
                    HostServicesDelegate hostServices = HostServicesFactory.getInstance(Engine.application);
                    hostServices.showDocument(stringBuilder.toString());

                }
            });




        }
        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                if (item!=null)
                    //System.out.println(item.getGroupID());
                setText(null);
                setGraphic(null);
            } else {
                if (item!=null)
                //System.out.println(item.getGroupID());
                setGraphic(button);
            }
        }
    }
}
