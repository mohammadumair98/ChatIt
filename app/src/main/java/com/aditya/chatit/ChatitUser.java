package com.aditya.chatit;

public class ChatitUser {
    private String id, username,imageURL,verification,status,search;

    public ChatitUser(String id, String username, String imageURL,String verification,String status,String search) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.verification = verification;
        this.status = status;
        this.search = search;
    }

    public ChatitUser()
    {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
