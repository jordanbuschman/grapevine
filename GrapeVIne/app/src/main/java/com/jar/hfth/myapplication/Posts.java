package com.jar.hfth.myapplication;

/**
 * Created by August on 3/1/15.
 */
public class Posts {

    long date;
    String text;
    int views;
    String phone;
    String user;
    String title;
    int grove;
    String id;


    public Posts(long date, String text, int views, String phone, String user, String title, int grove, String id) {
        this.date = date;
        this.text = text;
        this.views = views;
        this.phone = phone;
        this.user = user;
        this.title = title;
        this.grove = grove;
        this.id = id;
    }
    public long getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public int getViews() {
        return views;
    }

    public String getPhone() {
        return phone;
    }

    public String getUser() {
        return user;
    }
    public String getTitle(){
        return title;
    }
    public int getGrove(){return grove;}
    public String getId(){return  id;}


}
