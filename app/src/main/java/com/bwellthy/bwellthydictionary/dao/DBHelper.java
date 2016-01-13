package com.bwellthy.bwellthydictionary.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.bwellthy.bwellthydictionary.models.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miteshp on 13/01/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    public static final String DATABASE_NAME = "dictionary.db";
    public static final String TABLE_NAME = "words";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_VARIANT = "variant";
    public static final String COLUMN_MEANING = "meaning";
    public static final String COLUMN_RATIO = "ratio";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        "(" + COLUMN_ID + " integer primary key, " +
                        COLUMN_WORD + " text, " +
                        COLUMN_VARIANT + " integer, " +
                        COLUMN_MEANING + " text, " +
                        COLUMN_RATIO + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    static public String createInsert(final String tableName, final String[] columnNames) {
        if (tableName == null || columnNames == null || columnNames.length == 0) {
            throw new IllegalArgumentException();
        }
        final StringBuilder s = new StringBuilder();
        s.append("INSERT INTO ").append(tableName).append(" (");
        for (String column : columnNames) {
            s.append(column).append(" ,");
        }
        int length = s.length();
        s.delete(length - 2, length);
        s.append(") VALUES( ");
        for (int i = 0; i < columnNames.length; i++) {
            s.append(" ? ,");
        }
        length = s.length();
        s.delete(length - 2, length);
        s.append(")");
        return s.toString();
    }

    final static String INSERT_QUERY = createInsert(TABLE_NAME, new String[]{COLUMN_ID,
            COLUMN_WORD, COLUMN_VARIANT, COLUMN_MEANING, COLUMN_RATIO});

    /**
     * Bulk insert a list of data in the database.
     *
     * @param wordList
     * @return
     */
    public int bulkInsert(final List<Word> wordList) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final SQLiteStatement statement = db.compileStatement(INSERT_QUERY);
        db.beginTransaction();
        try {
            for (Word word : wordList) {
                statement.clearBindings();
                statement.bindLong(1, word.getId());
                statement.bindString(2, word.getWord());
                statement.bindLong(3, word.getVariant());
                statement.bindString(4, word.getMeaning());
                statement.bindDouble(5, word.getRatio());

                statement.execute();
            }
            db.setTransactionSuccessful();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Get all the words in the dictionary
     *
     * @return
     */
    public List<Word> getWords() {
        final List<Word> wordList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            final Word word = new Word();

            if (cursor.getDouble(cursor.getColumnIndex(COLUMN_RATIO)) > 0) {
                word.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                word.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
                word.setVariant(cursor.getInt(cursor.getColumnIndex(COLUMN_VARIANT)));
                word.setMeaning(cursor.getString(cursor.getColumnIndex(COLUMN_MEANING)));
                word.setRatio(cursor.getDouble(cursor.getColumnIndex(COLUMN_RATIO)));

                wordList.add(word);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return wordList;
    }
}
