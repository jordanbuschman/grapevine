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


    public Posts(long date, String text, int views, String phone, String user, String title) {
        this.date = date;
        this.text = text;
        this.views = views;
        this.phone = phone;
        this.user = user;
        this.title = title;
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


}
