package ru.todo100.social.vk.components;

/**
 * Created by igor on 23.03.15.
 */
public class GroupTypeDataComboBox {
    enum Type {
        event,
        group,
        page,
        all
    }
    private Type type;
    private String name;

    public GroupTypeDataComboBox(Type type,String name) {
        setType(type);
        setName(name);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
