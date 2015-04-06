package ru.todo100.social.vk.datas;

import java.net.URI;

public class GroupData {
    private Long id;
    private String name;
    private String screenName;
    private Integer isClosed;
    private String type;
    private Boolean isAdmin;
    private Boolean isMember;
    private Integer canPost;
    private URI photo;
    private URI photoMedium;
    private URI photoBig;

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    private Integer memberCount;

    public URI getPhotoBig() {
        return photoBig;
    }

    public void setPhotoBig(URI photoBig) {
        this.photoBig = photoBig;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Integer getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Integer isClosed) {
        this.isClosed = isClosed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(Boolean isMember) {
        this.isMember = isMember;
    }

    public URI getPhoto() {
        return photo;
    }

    public void setPhoto(URI photo) {
        this.photo = photo;
    }

    public URI getPhotoMedium() {
        return photoMedium;
    }

    public void setPhotoMedium(URI photoMedium) {
        this.photoMedium = photoMedium;
    }

    public Integer getCanPost() {
        return canPost;
    }

    public void setCanPost(Integer canPost) {
        this.canPost = canPost;
    }
}
