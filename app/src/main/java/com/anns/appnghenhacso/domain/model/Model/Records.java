package com.anns.appnghenhacso.domain.model.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Records {
    @SerializedName("records")
    @Expose
    private ArrayList<Item> items = null;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
