package io.github.josephdalughut.journal.android.data.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import io.github.josephdalughut.journal.android.data.database.converters.DateConverter;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;
import io.github.josephdalughut.journal.android.data.models.entry.EntryDao;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Default database for the application
 */
@android.arch.persistence.room.Database(
        entities = {Entry.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({ DateConverter.class })
public abstract class Database extends RoomDatabase {

    private static final String LOG_TAG = Database.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static final String DB_NAME = "journalbase";
    private static Database sInstance;

    /**
     * @return a singleton {@link Database} instance.
     */
    public static Database getInstance(Context context){
        if(sInstance == null){
            Log.d(LOG_TAG, "Db instance is null, creating new");
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(), Database.class,
                        Database.DB_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract EntryDao getEntryDao();

}
