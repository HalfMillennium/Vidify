
package com.digitalnode.playsee.BingSearchApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggestion {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("displayText")
    @Expose
    private String displayText;
    @SerializedName("webSearchUrl")
    @Expose
    private String webSearchUrl;
    @SerializedName("searchLink")
    @Expose
    private String searchLink;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail__ thumbnail;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getWebSearchUrl() {
        return webSearchUrl;
    }

    public void setWebSearchUrl(String webSearchUrl) {
        this.webSearchUrl = webSearchUrl;
    }

    public String getSearchLink() {
        return searchLink;
    }

    public void setSearchLink(String searchLink) {
        this.searchLink = searchLink;
    }

    public Thumbnail__ getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail__ thumbnail) {
        this.thumbnail = thumbnail;
    }

}
