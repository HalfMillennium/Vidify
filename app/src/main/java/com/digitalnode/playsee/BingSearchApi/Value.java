
package com.digitalnode.playsee.BingSearchApi;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("webSearchUrl")
    @Expose
    private String webSearchUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;
    @SerializedName("datePublished")
    @Expose
    private String datePublished;
    @SerializedName("publisher")
    @Expose
    private List<Publisher> publisher = null;
    @SerializedName("creator")
    @Expose
    private Creator creator;
    @SerializedName("isAccessibleForFree")
    @Expose
    private Boolean isAccessibleForFree;
    @SerializedName("contentUrl")
    @Expose
    private String contentUrl;
    @SerializedName("hostPageUrl")
    @Expose
    private String hostPageUrl;
    @SerializedName("encodingFormat")
    @Expose
    private String encodingFormat;
    @SerializedName("hostPageDisplayUrl")
    @Expose
    private String hostPageDisplayUrl;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("motionThumbnailUrl")
    @Expose
    private String motionThumbnailUrl;
    @SerializedName("embedHtml")
    @Expose
    private String embedHtml;
    @SerializedName("allowHttpsEmbed")
    @Expose
    private Boolean allowHttpsEmbed;
    @SerializedName("viewCount")
    @Expose
    private Integer viewCount;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("allowMobileEmbed")
    @Expose
    private Boolean allowMobileEmbed;
    @SerializedName("isSuperfresh")
    @Expose
    private Boolean isSuperfresh;

    public String getWebSearchUrl() {
        return webSearchUrl;
    }

    public void setWebSearchUrl(String webSearchUrl) {
        this.webSearchUrl = webSearchUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public List<Publisher> getPublisher() {
        return publisher;
    }

    public void setPublisher(List<Publisher> publisher) {
        this.publisher = publisher;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Boolean getIsAccessibleForFree() {
        return isAccessibleForFree;
    }

    public void setIsAccessibleForFree(Boolean isAccessibleForFree) {
        this.isAccessibleForFree = isAccessibleForFree;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getHostPageUrl() {
        return hostPageUrl;
    }

    public void setHostPageUrl(String hostPageUrl) {
        this.hostPageUrl = hostPageUrl;
    }

    public String getEncodingFormat() {
        return encodingFormat;
    }

    public void setEncodingFormat(String encodingFormat) {
        this.encodingFormat = encodingFormat;
    }

    public String getHostPageDisplayUrl() {
        return hostPageDisplayUrl;
    }

    public void setHostPageDisplayUrl(String hostPageDisplayUrl) {
        this.hostPageDisplayUrl = hostPageDisplayUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMotionThumbnailUrl() {
        return motionThumbnailUrl;
    }

    public void setMotionThumbnailUrl(String motionThumbnailUrl) {
        this.motionThumbnailUrl = motionThumbnailUrl;
    }

    public String getEmbedHtml() {
        return embedHtml;
    }

    public void setEmbedHtml(String embedHtml) {
        this.embedHtml = embedHtml;
    }

    public Boolean getAllowHttpsEmbed() {
        return allowHttpsEmbed;
    }

    public void setAllowHttpsEmbed(Boolean allowHttpsEmbed) {
        this.allowHttpsEmbed = allowHttpsEmbed;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Boolean getAllowMobileEmbed() {
        return allowMobileEmbed;
    }

    public void setAllowMobileEmbed(Boolean allowMobileEmbed) {
        this.allowMobileEmbed = allowMobileEmbed;
    }

    public Boolean getIsSuperfresh() {
        return isSuperfresh;
    }

    public void setIsSuperfresh(Boolean isSuperfresh) {
        this.isSuperfresh = isSuperfresh;
    }

}
