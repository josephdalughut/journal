package io.github.josephdalughut.journal.android.data.models.entry;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.github.josephdalughut.journal.android.data.models.EntityDao;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Data Access Object for {@link Entry} model.
 */
@Dao
public interface EntryDao extends EntityDao{

    /**
     * @return a collection of all {@link Entry}(ies) in the database.
     */
    @Query("SELECT * FROM entries ")
    public LiveData<List<Entry>> loadEntries();

    @Query("SELECT * FROM entries where id == :entryId")
    public Entry getEntry(Long entryId);

    /**
     * Inserts an {@link Entry} into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insert(Entry entry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(List<Entry> entries);

    /**
     * Deletes an {@link Entry} from the database
     */
    @Delete
    public void delete(Entry entry);

    @Query("DELETE FROM entries")
    public void delete();

    /**
     * @return a collection of all {@link Entry}(ies) which haven't been synced
     */
    @Query("SELECT * FROM entries where synced == 0")
    public List<Entry> loadUnSyncedEntries();

}
