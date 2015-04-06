package ru.todo100.social.vk.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.todo100.social.LogsService;
import ru.todo100.social.vk.datas.LogData;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Igor Bobko
 */
public class LogsController extends AbstractController implements Initializable {
    public TableColumn groupId;
    public TableColumn groupMessage;
    public TableColumn groupAttachment;
    public TableView logTableView;
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

        groupId.setCellValueFactory(new PropertyValueFactory<LogData, String>("groupID"));
        groupMessage.setCellValueFactory(new PropertyValueFactory<LogData, String>("message"));
        groupAttachment.setCellValueFactory(new PropertyValueFactory<LogData, String>("attachment"));

        final ObservableList<LogData> data =
                FXCollections.observableArrayList(

                );
        data.addAll(getLogsService().getLogs());

        logTableView.setItems(data);


    }
}
