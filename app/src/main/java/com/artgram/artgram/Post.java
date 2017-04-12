package com.artgram.artgram;

/**
 * Created by sonal on 12-04-2017.
 */
public class Post {
    private String caption;
    private String image;
    public Post()
    {

    }
    public Post(String caption, String image) {
        this.caption = caption;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
