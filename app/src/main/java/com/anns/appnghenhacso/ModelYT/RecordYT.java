package com.anns.appnghenhacso.ModelYT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecordYT {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("data")
    @Expose
    private ArrayList<ItemYT>  items = null;

    @SerializedName("message")
    @Expose
    private String message;


    public ArrayList<ItemYT> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemYT> items) {
        this.items = items;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
