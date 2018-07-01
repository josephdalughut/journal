package io.github.josephdalughut.journal.android.data.models.entry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.auth.FirebaseUser;

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

    public static final String FIREBASE_COLLECTION_NAME = "entries";

    @PrimaryKey(autoGenerate = true) private Long id;
    private String title;
    private String content;
    private String firebase_user_id;
    private boolean synced = false;

    @Ignore
    public Entry() {
    }

    public Entry(Long id, String title, String content, String firebase_user_id, boolean synced) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.firebase_user_id = firebase_user_id;
        this.synced = synced;
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

    public String getFirebase_user_id() {
        return firebase_user_id;
    }

    public void setFirebase_user_id(String firebase_user_id) {
        this.firebase_user_id = firebase_user_id;
    }

    public boolean isSynced() {
        return synced;
    }

    public Entry setSynced(boolean synced) {
        this.synced = synced;
        return this;
    }

    /**
     * Converts this Entry's id to a unique id for the firebase user.
     *
     * We can't use the entry's default ID, since it is auto-generated we might have conflicts from
     * other users. To solve this, we add the users (unique) id before each entry before saving to firebase.
     *
     * @return a unique id for saving this {@link Entry} in firestore.
     */
    public static String getFirebaseId(FirebaseUser user, Long entryId){
        return user.getUid() + "_" + entryId;
    }

    /**
     * @see #getFirebaseId(FirebaseUser, Long)
     */
    public static String getFirebaseId(FirebaseUser user, Entry entry){
        return getFirebaseId(user, entry.getId());
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
