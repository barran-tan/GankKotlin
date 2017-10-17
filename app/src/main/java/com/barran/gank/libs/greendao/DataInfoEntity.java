package com.barran.gank.libs.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * test greendao
 *
 * <P>TODO greendao不识别kotlin类！！！</P>
 *
 * Created by tanwei on 2017/10/17.
 */
@Entity
public class DataInfoEntity {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String infoId;
    private String type;
    private long createTime, publishTime;
    private String desc;
    private String linkUrl;
    private String author;
    private String image;
    
    // custom field
    private boolean read;

    @Generated(hash = 1126986289)
    public DataInfoEntity(Long id, String infoId, String type, long createTime,
            long publishTime, String desc, String linkUrl, String author,
            String image, boolean read) {
        this.id = id;
        this.infoId = infoId;
        this.type = type;
        this.createTime = createTime;
        this.publishTime = publishTime;
        this.desc = desc;
        this.linkUrl = linkUrl;
        this.author = author;
        this.image = image;
        this.read = read;
    }

    @Generated(hash = 1921053336)
    public DataInfoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImages() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getImage() {
        return this.image;
    }

    public boolean getRead() {
        return this.read;
    }
}
