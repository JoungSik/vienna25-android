package com.joung.vienna.note.model;

import androidx.annotation.Keep;

@Keep
public class Note {

    private long key;
    private String title, content, date, author;

    public Note() {
    }

    public Note(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Note(String title, String content, String date, String author) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isEmpty() {
        return title.isEmpty() || content.isEmpty() || date.isEmpty();
    }
}
