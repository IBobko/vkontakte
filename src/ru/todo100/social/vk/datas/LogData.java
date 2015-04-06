package ru.todo100.social.vk.datas;

/**
 * @author Igor Bobko <limit-speed@yandex.ru>
 */

public class LogData {
    private Integer groupID;
    private String message;
    private String attachment;

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
