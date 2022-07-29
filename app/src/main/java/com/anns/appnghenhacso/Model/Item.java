package com.anns.appnghenhacso.Model;

import android.os.Parcel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;


public class Item implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("fields")
    @Expose
    private Fields fields;

    @SerializedName("createdTime")
    @Expose
    private Date createdTime;


//    protected Item(Parcel in) {
//        id = in.readString();
//        createdTime = in.readString();
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
