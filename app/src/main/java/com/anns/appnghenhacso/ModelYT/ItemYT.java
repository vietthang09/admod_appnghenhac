package com.anns.appnghenhacso.ModelYT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemYT implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("lengthTextSimpleText")
    @Expose
    private  String lengthTextSimpleText;

    @SerializedName("viewCountText")
    @Expose
    private  int viewCountText;

    @SerializedName("publishedTimeText")
    @Expose
    private String publishedTimeText;

    @SerializedName("ownerChannelText")
    @Expose
    private String ownerChannelText;

    @SerializedName("thumbnailChannel")
    @Expose
    private String thumbnailChannel;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLengthTextSimpleText() {
        return lengthTextSimpleText;
    }

    public void setLengthTextSimpleText(String lengthTextSimpleText) {
        this.lengthTextSimpleText = lengthTextSimpleText;
    }

    public int getViewCountText() {
        return viewCountText;
    }

    public void setViewCountText(int viewCountText) {
        this.viewCountText = viewCountText;
    }

    public String getPublishedTimeText() {
        return publishedTimeText;
    }

    public void setPublishedTimeText(String publishedTimeText) {
        this.publishedTimeText = publishedTimeText;
    }

    public String getOwnerChannelText() {
        return ownerChannelText;
    }

    public void setOwnerChannelText(String ownerChannelText) {
        this.ownerChannelText = ownerChannelText;
    }

    public String getThumbnailChannel() {
        return thumbnailChannel;
    }

    public void setThumbnailChannel(String thumbnailChannel) {
        this.thumbnailChannel = thumbnailChannel;
    }
}
