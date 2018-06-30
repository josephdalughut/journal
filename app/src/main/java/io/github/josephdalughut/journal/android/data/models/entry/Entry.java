package io.github.josephdalughut.journal.android.data.models.entry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Represents a journal entry made by a user of the application
 */
@Entity(tableName = "entries")
public class Entry extends io.github.josephdalughut.journal.android.data.models.Entity {

    @PrimaryKey(autoGenerate = true) private Long id;
    private String title;
    private String content;
    private Long user_id;

    @Ignore
    public Entry() {
    }

    public Entry(Long id, String title, String content, Long user_id) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
    }

    public Long getId() {
        return id;
    }

    public Entry setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Entry setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Entry setContent(String content) {
        this.content = content;
        return this;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Entry setUser_id(Long user_id) {
        this.user_id = user_id;
        return this;
    }

    /**
     * @return true if this entry has no title
     */
    public boolean isTitleEmpty(){
        return title == null || title.trim().isEmpty();
    }

    /**
     * @return true if this entry has no content
     */
    public boolean isContentEmpty(){
        return content == null || content.isEmpty();
    }

}
