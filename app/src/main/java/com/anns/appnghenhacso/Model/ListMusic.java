package com.anns.appnghenhacso.Model;

public class ListMusic {
    private  String id;
    private  int imageListMusic;
    private  String titleListMusic;

    public ListMusic(String id, int imageListMusic, String titleListMusic) {
        this.id = id;
        this.imageListMusic = imageListMusic;
        this.titleListMusic = titleListMusic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageListMusic() {
        return imageListMusic;
    }

    public void setImageListMusic(int imageListMusic) {
        this.imageListMusic = imageListMusic;
    }

    public String getTitleListMusic() {
        return titleListMusic;
    }

    public void setTitleListMusic(String titleListMusic) {
        this.titleListMusic = titleListMusic;
    }
}
