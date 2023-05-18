package com.example.healthcare.model;

import java.io.Serializable;

public class Post implements Serializable {
    private String title;
    private String content;
    private String image;
    private String author;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Post() {
    }

    public Post(String title, String content, String author, String id) {
        this.title = title;
        this.content = content;
        this.author=author;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
