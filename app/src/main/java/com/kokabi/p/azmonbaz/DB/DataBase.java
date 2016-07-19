package com.kokabi.p.azmonbaz.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kokabi.p.azmonbaz.Objects.HistoryObj;
import com.kokabi.p.azmonbaz.Objects.TestObj;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    static final int version = 1;
    static final String name = "AzmonBaz";
    /*Favorite Test Table Info*/
    static final String tableFavoriteTests = "favoriteTests";
    static final String KEY_idFavoriteTest = "idFavoriteTest";
    static final String KEY_idTest = "idTest";
    static final String createFavoriteTests = "CREATE TABLE " +
            tableFavoriteTests + "(" + KEY_idFavoriteTest + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_idTest + " INTEGER );";
    /*History Table Info*/
    static final String tableHistory = "history";
    static final String KEY_idHistory = "idHistory";
    static final String KEY_idTestHistory = "idTestHistory";
    static final String KEY_testName = "testName";
    static final String KEY_testTime = "testTime";
    static final String KEY_percentage = "percentage";
    static final String KEY_answered = "answered";
    static final String KEY_incorrect = "incorrect";
    static final String KEY_unAnswered = "unAnswered";
    static final String KEY_updateTime = "updateTime";
    static final String createHistory = "CREATE TABLE " +
            tableHistory + "(" + KEY_idHistory + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_idTestHistory + " INTEGER," + KEY_testName + " TEXT," + KEY_testTime + " TEXT,"
            + KEY_percentage + " TEXT," + KEY_answered + " INTEGER," + KEY_incorrect + " INTEGER," + KEY_unAnswered + " INTEGER,"
            + KEY_updateTime + " TEXT );";
    /*Favored Question Table Info*/
    static final String tableFavoredQuestion = "favoredQuestion";
    static final String KEY_idFavoredQuestion = "idFavoredQuestion";
    static final String KEY_idQuestion = "idQuestion";
    static final String KEY_questionImage = "questionImage";
    static final String KEY_answerImage = "answerImage";
    static final String KEY_key = "key";
    static final String createFavoredQuestion = "CREATE TABLE " +
            tableFavoredQuestion + "(" + KEY_idFavoredQuestion + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_idQuestion + " INTEGER," + KEY_questionImage + " TEXT," + KEY_answerImage + " TEXT,"
            + KEY_key + " INTEGER );";

    public DataBase(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createFavoriteTests);
        db.execSQL(createHistory);
        db.execSQL(createFavoredQuestion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + tableFavoriteTests);
        db.execSQL("DROP TABLE IF EXISTS " + tableHistory);
        db.execSQL("DROP TABLE IF EXISTS " + tableFavoredQuestion);
        onCreate(db);
    }

    /*Insert Methods*/
    public long favoriteTestInsert(TestsTitleObj testsTitleObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues favoriteTestValue = new ContentValues();

        favoriteTestValue.put(KEY_idTest, testsTitleObj.getIdTest());
        return db.insert(tableFavoriteTests, null, favoriteTestValue);
    }

    public long historyInsert(HistoryObj historyObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues historyValue = new ContentValues();

        historyValue.put(KEY_idTestHistory, historyObj.getIdTest());
        historyValue.put(KEY_testName, historyObj.getTestName());
        historyValue.put(KEY_testTime, historyObj.getTestTime());
        historyValue.put(KEY_percentage, historyObj.getTestPercentage());
        historyValue.put(KEY_answered, historyObj.getAnsweredQuestion());
        historyValue.put(KEY_incorrect, historyObj.getIncorrectQuestion());
        historyValue.put(KEY_unAnswered, historyObj.getUnAnsweredQuestion());
        historyValue.put(KEY_updateTime, historyObj.getUpdateTime());
        return db.insert(tableHistory, null, historyValue);
    }

    public long favoredQuestionInsert(TestObj testObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues favoredValue = new ContentValues();

        favoredValue.put(KEY_idQuestion, testObj.getIdQuestion());
        favoredValue.put(KEY_questionImage, testObj.getQuestionImage());
        favoredValue.put(KEY_answerImage, testObj.getAnswerImage());
        favoredValue.put(KEY_answerImage, testObj.getKey());
        favoredValue.put(KEY_key, testObj.getKey());
        return db.insert(tableFavoredQuestion, null, favoredValue);
    }

    /*Update Methods*/
    public long favoriteTestUpdate(TestsTitleObj testsTitleObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues favoriteTestValue = new ContentValues();

        favoriteTestValue.put(KEY_idTest, testsTitleObj.getIdTest());

        String whereClause = KEY_idTest + " = " + testsTitleObj.getIdTest();
        return db.update(tableFavoriteTests, favoriteTestValue, whereClause, null);
    }

    /*Delete Methods*/
    public long favoriteTestDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = KEY_idTest + " = " + id;
        return db.delete(tableFavoriteTests, whereClause, null);
    }

    public long historyDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = KEY_idHistory + " = " + id;
        return db.delete(tableHistory, whereClause, null);
    }

    public long favoredQuestionDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = KEY_idQuestion + " = " + id;
        return db.delete(tableFavoredQuestion, whereClause, null);
    }

    /*Select Methods*/
    public ArrayList<Integer> selectAllFavorites() {
        String query = "SELECT * FROM " + tableFavoriteTests +
                " ORDER BY " + KEY_idFavoriteTest;

        ArrayList<Integer> testsArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                testsArrayList.add(cursor.getInt(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return testsArrayList;
    }

    public ArrayList<HistoryObj> selectAllHistory() {
        String query = "SELECT * FROM " + tableHistory +
                " ORDER BY " + KEY_idHistory;

        ArrayList<HistoryObj> historyArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                historyArrayList.add(new HistoryObj(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3)
                        , cursor.getString(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyArrayList;
    }

    public ArrayList<TestObj> selectAllFavoredQuestion() {
        String query = "SELECT * FROM " + tableFavoredQuestion +
                " ORDER BY " + KEY_idQuestion;

        ArrayList<TestObj> testObjArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                testObjArrayList.add(new TestObj(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return testObjArrayList;
    }

    public boolean isTestFavored(int id) {
        boolean isFavored = false;
        String query = "SELECT * FROM " + tableFavoriteTests +
                " WHERE " + KEY_idTest + " = "
                + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                TestsTitleObj testsTitleObj = new TestsTitleObj();
                testsTitleObj.setIdTest(cursor.getInt(1));
                if (testsTitleObj.getIdTest() != 0) {
                    isFavored = true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return isFavored;
    }

    public boolean isQuesttionFavored(int id) {
        boolean isFavored = false;
        String query = "SELECT * FROM " + tableFavoredQuestion +
                " WHERE " + KEY_idQuestion + " = "
                + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                TestObj testsObj = new TestObj();
                testsObj.setIdQuestion(cursor.getInt(1));
                if (testsObj.getIdQuestion() != 0) {
                    isFavored = true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return isFavored;
    }
}