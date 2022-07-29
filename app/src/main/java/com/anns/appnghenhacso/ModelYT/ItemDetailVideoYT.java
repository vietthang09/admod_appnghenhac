package com.anns.appnghenhacso.ModelYT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDetailVideoYT {
    @SerializedName("itag")
    @Expose
    private int itag;

    @SerializedName("mimeType")
    @Expose
    private String mimeType;

    @SerializedName("width")
    @Expose
    private int width;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("contentLength")
    @Expose
    private String contentLength;

    @SerializedName("quality")
    @Expose
    private String quality;

    @SerializedName("qualityLabel")
    @Expose
    private String qualityLabel;

    @SerializedName("audioQuality")
    @Expose
    private String audioQuality;

    @SerializedName("audioSampleRate")
    @Expose
    private String audioSampleRate;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("signatureCipher")
    @Expose
    private String signatureCipher;

    public int getItag() {
        return itag;
    }

    public void setItag(int itag) {
        this.itag = itag;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getQualityLabel() {
        return qualityLabel;
    }

    public void setQualityLabel(String qualityLabel) {
        this.qualityLabel = qualityLabel;
    }

    public String getAudioQuality() {
        return audioQuality;
    }

    public void setAudioQuality(String audioQuality) {
        this.audioQuality = audioQuality;
    }

    public String getAudioSampleRate() {
        return audioSampleRate;
    }

    public void setAudioSampleRate(String audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSignatureCipher() {
        return signatureCipher;
    }

    public void setSignatureCipher(String signatureCipher) {
        this.signatureCipher = signatureCipher;
    }
}
