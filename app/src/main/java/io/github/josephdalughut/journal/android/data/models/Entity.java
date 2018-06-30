package io.github.josephdalughut.journal.android.data.models;

import android.arch.persistence.room.ColumnInfo;

import java.util.Date;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Basic definition of models/entities used in the application,
 * for sharing common attributes and behavior.
 */
@android.arch.persistence.room.Entity
public class Entity {

    @ColumnInfo(name = "created_at") public Date createdAt;
    @ColumnInfo(name = "updated_at") public Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public Entity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Entity setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
