package com.example.firebasedemo;

import java.io.Serializable;

/**
 * Created by admin1 on 24/5/18.
 */
public class chatPojo implements Serializable {

    String id;
    String message;
    String name;
    long date;
    String is_active;
    String profile_picture;
    String platform;
    boolean isplaying = false;
    String pin;
    String course_id;
    String firebase_id;
    String type;
    String erp_token;
    String is_chat_locked;
   // private Original original;

    public chatPojo() {
    }

    public chatPojo(String id, String message, String name, long date, String is_active, String profile_picture) {
        this.id = id;
        this.message = message;
        this.name = name;
        this.date = date;
        this.is_active = is_active;
        this.profile_picture = profile_picture;
    }


    public String getIs_chat_locked() {
        return is_chat_locked;
    }

    public void setIs_chat_locked(String is_chat_locked) {
        this.is_chat_locked = is_chat_locked;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }


    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }


    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


   /* public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }*/


    public String getErp_token() {
        return erp_token;
    }

    public void setErp_token(String erp_token) {
        this.erp_token = erp_token;
    }


    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    /*public chatPojo(String id, String message, String name, long date, String is_active, String profile_picture, String platform, boolean isplaying, String course_id, String firebase_id, String type, Original original, String erp_token) {
        this.id = id;
        this.message = message;
        this.name = name;
        this.date = date;
        this.is_active = is_active;
        this.profile_picture = profile_picture;
        this.platform = platform;
        this.isplaying = isplaying;
        this.course_id = course_id;
        this.firebase_id = firebase_id;
        this.type = type;
        this.original = original;
        this.erp_token = erp_token;
    }
*/

    public boolean isIsplaying() {
        return isplaying;
    }

    public void setIsplaying(boolean isplaying) {
        this.isplaying = isplaying;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
