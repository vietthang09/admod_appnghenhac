package com.anns.appnghenhacso.domain.model.ModelYT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecordDetailVideoYT {
    @SerializedName("success")
    @Expose
    private  boolean success;

    @SerializedName("data")
    @Expose
    private  ArrayList<ItemDetailVideoYT> itemDetailVideoYTS = null;

    @SerializedName("message")
    @Expose
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<ItemDetailVideoYT> getItemDetailVideoYTS() {
        return itemDetailVideoYTS;
    }

    public void setItemDetailVideoYTS(ArrayList<ItemDetailVideoYT> itemDetailVideoYTS) {
        this.itemDetailVideoYTS = itemDetailVideoYTS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
