package ru.todo100.social.vk.controllers;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.todo100.social.AbstractController;
import ru.todo100.social.LogsService;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.GroupData;
import ru.todo100.social.vk.strategy.GroupsOperations;
import ru.todo100.social.vk.strategy.VideoOperations;
import ru.todo100.social.vk.strategy.WallOperations;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Igor Bobko
 */
public class UserGroupsController extends AbstractController implements Initializable{
    private final ObservableList<GroupData> data =
            FXCollections.observableArrayList(

            );
    public TableView groupsList;
    public TableColumn groupNameColumn;
    public TableColumn groupCanPostedColumn;
    public TableColumn groupIdColumn;
    public TextArea loggerArea;
    public Button publishButton;
    public TextArea messageArea;
    public TableColumn groupMemberCount;
    public TextField minMemberCount;
    public TextArea attachmentArea;
    public TextField sleepTime;
    public ComboBox pageGroup;

    public LogsService getLogsService() {
        return logsService;
    }

    public void setLogsService(LogsService logsService) {
        this.logsService = logsService;
    }

    public LogsService logsService;

    public ComboBox exitBy;

    public CheckBox inVideo;

    @SuppressWarnings("UnusedParameters")
    public void publish(ActionEvent actionEvent) {

        loggerArea.appendText("Start publish\n");
        String message = messageArea.getText();
        String attachment = attachmentArea.getText();

        Integer sleep = Integer.parseInt(sleepTime.getText());
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groups.get();

        System.out.println(pageGroup.getSelectionModel().getSelectedIndex());
        WallOperations wall = new WallOperations(Engine.accessToken);

        VideoOperations video = new VideoOperations(Engine.accessToken);


        Pattern p = Pattern.compile("video(\\d+)_(\\d+)");

        Thread tread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (GroupData gd : userGroups) {
                    if (getLogsService().checkLogs(gd.getId(),message,attachment)){
                        loggerArea.appendText("Duplicate message for: " + gd.getName() + " (" + gd.getId() + ")" + " \n");
                        continue;
                    }

                    if (inVideo.isSelected()) {
                        if (gd.getType().equals("group")) {
                            Matcher m = p.matcher(attachment);
                            boolean b = m.matches();
                            if (b) {
                                Integer owner_id = Integer.parseInt(m.group(1));
                                Integer video_id = Integer.parseInt(m.group(2));

                                System.out.println(owner_id + " " + video_id);
                                video.add(gd.getId() * -1, video_id, owner_id);
                            }
                            loggerArea.appendText("Publish in groups video: " + gd.getName() + " (" + gd.getId() + ")" + " \n");
                            getLogsService().insertLogs(gd.getId(),message,attachment);
                        }
                        continue;
                    }
                    if (pageGroup.getSelectionModel().getSelectedIndex() == 0) {
                        wall.post(gd.getId() * -1, 0, 0, message, attachment);
                        loggerArea.appendText("Publish in: " + gd.getName() + " (" + gd.getId() + ")" + " \n");
                        getLogsService().insertLogs(gd.getId(),message,attachment);
                    }
                    if (pageGroup.getSelectionModel().getSelectedIndex() == 1 && gd.getType().equals("page")) {
                        wall.post(gd.getId() * -1, 0, 0, message, attachment);
                        loggerArea.appendText("Publish in: " + gd.getName() + " (" + gd.getId() + ")" + " \n");
                        getLogsService().insertLogs(gd.getId(),message,attachment);
                    }
                    if (pageGroup.getSelectionModel().getSelectedIndex() == 2 && gd.getType().equals("group")) {
                        wall.post(gd.getId() * -1, 0, 0, message, attachment);
                        loggerArea.appendText("Publish in: " + gd.getName() + " (" + gd.getId() + ")" + " \n");
                        getLogsService().insertLogs(gd.getId(),message,attachment);
                    }

                }
                System.out.println("done");
            }
        });

        tread.start();


    }

    @SuppressWarnings("UnusedParameters")
    public void leaveGroups(ActionEvent actionEvent) {
        Integer count = Integer.parseInt(minMemberCount.getText());
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groupsList.getItems();
        for (GroupData gd : userGroups) {
            if (exitBy.getSelectionModel().getSelectedIndex() == 0) {
                groups.leave(gd.getId().intValue());
                continue;
            }
            if (exitBy.getSelectionModel().getSelectedIndex() == 1) {
                if (gd.getType().equals("page")) {
                } else {
                    continue;
                }
            }

            if (exitBy.getSelectionModel().getSelectedIndex() == 2) {
                if (gd.getType().equals("group")) {
                } else {
                    continue;
                }
            }
            if (gd.getMemberCount() < count) {
                groups.leave(gd.getId().intValue());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loggerArea.appendText("Window is opened\n");

        groupsList.setItems(data);


        groupIdColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));
        groupCanPostedColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("canPost"));
        groupMemberCount.setCellValueFactory(new PropertyValueFactory<GroupData, String>("memberCount"));


        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groups.get();


        data.addAll(userGroups);


        final ObservableList<String> country =
                FXCollections.observableArrayList(

                );
        country.add("Везде");
        country.add("По страница");
        country.add("По группа");
        pageGroup.setItems(country);
        pageGroup.getSelectionModel().select(0);

        final ObservableList<String> exitByData = FXCollections.observableArrayList();
        exitByData.add("Из всех");
        exitByData.add("Только из страниц");
        exitByData.add("Только из групп");
        exitBy.setItems(exitByData);

    }


    public void onGroupClick(javafx.scene.input.MouseEvent event) {
        if (event.getClickCount() == 2) {
            GroupData groupData = (GroupData) groupsList.getSelectionModel().getSelectedItem();
            HostServicesDelegate hostServices = HostServicesFactory.getInstance(Engine.application);
            String url = "http://vk.com/club" + groupData.getId();
            hostServices.showDocument(url);
        }
    }
}


