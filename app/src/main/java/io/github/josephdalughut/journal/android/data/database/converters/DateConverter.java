package io.github.josephdalughut.journal.android.data.database.converters;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Helps room convert {@link java.util.Date} fields saved in
 * {@link io.github.josephdalughut.journal.android.data.database.Database}
 *
 * @see TypeConverter
 * @see io.github.josephdalughut.journal.android.data.models.Entity
 */
@SuppressLint("SimpleDateFormat")
public class DateConverter {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //room would use this to convert a string timestamp to a Date object
    @TypeConverter
    public static Date toDate(String timestamp){
        if(timestamp == null)
            return null;
        try {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(timestamp);
        } catch (ParseException e) {
            return null;
        }
    }

    //room would use this to convert a Date to string timestamp
    @TypeConverter
    public static String toTimestamp(Date date){
        if(date == null)
            return null;
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }

}
