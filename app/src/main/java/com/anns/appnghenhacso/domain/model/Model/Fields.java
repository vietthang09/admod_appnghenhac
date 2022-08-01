package com.anns.appnghenhacso.domain.model.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Fields implements Serializable {
    @SerializedName("Image_Url")
    @Expose
    private  String Image_Url;

    @SerializedName("Music_Size")
    @Expose
    private  String Music_Size;

    @SerializedName("Composed")
    @Expose
    private String Composed;

    @SerializedName("luot_thich")
    @Expose
    private String luot_thich;

    @SerializedName("thoi_gian")
    @Expose
    private String thoi_gian;

    @SerializedName("Music_Url")
    @Expose
    private String Music_Url;

    @SerializedName("Music_Title")
    @Expose
    private String Music_Title;

    @SerializedName("luot_nghe")
    @Expose
    private String luot_nghe;

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }

    public String getMusic_Size() {
        return Music_Size;
    }

    public void setMusic_Size(String music_Size) {
        Music_Size = music_Size;
    }

    public String getComposed() {
        return Composed;
    }

    public void setComposed(String composed) {
        Composed = composed;
    }

    public String getLuot_thich() {
        return luot_thich;
    }

    public void setLuot_thich(String luot_thich) {
        this.luot_thich = luot_thich;
    }

    public String getThoi_gian() {
        return thoi_gian;
    }

    public void setThoi_gian(String thoi_gian) {
        this.thoi_gian = thoi_gian;
    }

    public String getMusic_Url() {
        return Music_Url;
    }

    public void setMusic_Url(String music_Url) {
        Music_Url = music_Url;
    }

    public String getMusic_Title() {
        return Music_Title;
    }

    public void setMusic_Title(String music_Title) {
        Music_Title = music_Title;
    }

    public String getLuot_nghe() {
        return luot_nghe;
    }

    public void setLuot_nghe(String luot_nghe) {
        this.luot_nghe = luot_nghe;
    }
}
