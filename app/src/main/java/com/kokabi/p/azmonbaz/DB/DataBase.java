package com.kokabi.p.azmonbaz.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kokabi.p.azmonbaz.Help.Constants;
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
    static final String KEY_answerList = "answerList";
    static final String createHistory = "CREATE TABLE " +
            tableHistory + "(" + KEY_idHistory + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_idTestHistory + " INTEGER," + KEY_testName + " TEXT," + KEY_testTime + " TEXT,"
            + KEY_percentage + " TEXT," + KEY_answered + " INTEGER," + KEY_incorrect + " INTEGER," + KEY_unAnswered + " INTEGER,"
            + KEY_updateTime + " TEXT," + KEY_answerList + " TEXT  );";
    /*Favored Question Table Info*/
    static final String tableFavoredQuestion = "favoredQuestion";
    static final String KEY_idFavoredQuestion = "idFavoredQuestion";
    static final String KEY_favoredTestName = "favoredTestName";
    static final String KEY_idQuestion = "idQuestion";
    static final String KEY_questionImage = "questionImage";
    static final String KEY_answerImage = "answerImage";
    static final String KEY_key = "key";
    static final String createFavoredQuestion = "CREATE TABLE " +
            tableFavoredQuestion + "(" + KEY_idFavoredQuestion + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_favoredTestName + " TEXT," + KEY_idQuestion + " INTEGER," + KEY_questionImage + " TEXT," + KEY_answerImage + " TEXT,"
            + KEY_key + " INTEGER );";
    /*Question State Table Info*/
    static final String tableQuestionState = "questionState";
    static final String KEY_idQuestionState = "idQuestionState";
    static final String KEY_question = "question";
    static final String KEY_state = "state";
    static final String createQuestionState = "CREATE TABLE " +
            tableQuestionState + "(" + KEY_idQuestionState + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_question + " INTEGER," + KEY_state + " INTEGER );";
    /*Saved Test Table Info*/
    static final String tableSavedTest = "savedTest";
    static final String KEY_id = "idRow";
    static final String KEY_idSavedTest = "idSavedTest";
    static final String KEY_time = "time";
    static final String KEY_answers = "answers";
    static final String KEY_hasNegativePoint = "hasNegativePoint";
    static final String KEY_savedTestName = "savedTestName";
    static final String createSavedTest = "CREATE TABLE " +
            tableSavedTest + "(" + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_idSavedTest + " INTEGER," + KEY_time + " INTEGER," + KEY_answers + " TEXT,"
            + KEY_hasNegativePoint + " INTEGER," + KEY_savedTestName + " TEXT );";

    public DataBase(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createFavoriteTests);
        db.execSQL(createHistory);
        db.execSQL(createFavoredQuestion);
        Log.i("===================", createFavoredQuestion);
        db.execSQL(createQuestionState);
        db.execSQL(createSavedTest);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + tableFavoriteTests);
        db.execSQL("DROP TABLE IF EXISTS " + tableHistory);
        db.execSQL("DROP TABLE IF EXISTS " + tableFavoredQuestion);
        db.execSQL("DROP TABLE IF EXISTS " + tableQuestionState);
        db.execSQL("DROP TABLE IF EXISTS " + createSavedTest);
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
        historyValue.put(KEY_answerList, historyObj.getAnswerList());
        return db.insert(tableHistory, null, historyValue);
    }

    public long favoredQuestionInsert(TestObj testObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues favoredValue = new ContentValues();

        favoredValue.put(KEY_favoredTestName, testObj.getTestName());
        favoredValue.put(KEY_idQuestion, testObj.getIdQuestion());
        favoredValue.put(KEY_questionImage, testObj.getQuestionImage());
        favoredValue.put(KEY_answerImage, testObj.getAnswerImage());
        favoredValue.put(KEY_key, testObj.getKey());
        return db.insert(tableFavoredQuestion, null, favoredValue);
    }

    public long questionStateInsert(int id, int state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues questionStateValue = new ContentValues();

        questionStateValue.put(KEY_question, id);
        questionStateValue.put(KEY_state, state);
        return db.insert(tableQuestionState, null, questionStateValue);
    }

    public long savedTestInsert(int id, int time, String answers, int hasNegativePoint, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues savedTestValue = new ContentValues();

        savedTestValue.put(KEY_idSavedTest, id);
        savedTestValue.put(KEY_time, time);
        savedTestValue.put(KEY_answers, answers);
        savedTestValue.put(KEY_hasNegativePoint, hasNegativePoint);
        savedTestValue.put(KEY_savedTestName, name);
        return db.insert(tableSavedTest, null, savedTestValue);
    }

    /*Update Methods*/
    public long questionStateUpdate(int id, int state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues questionStateValue = new ContentValues();

        questionStateValue.put(KEY_state, state);

        String whereClause = KEY_question + " = " + id;
        return db.update(tableQuestionState, questionStateValue, whereClause, null);
    }

    public long savedTestUpdate(int id, int time, String answers, int hasNegativePoint, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues savedTestValue = new ContentValues();

        savedTestValue.put(KEY_time, time);
        savedTestValue.put(KEY_answers, answers);
        savedTestValue.put(KEY_hasNegativePoint, hasNegativePoint);
        savedTestValue.put(KEY_savedTestName, name);

        String whereClause = KEY_idSavedTest + " = " + id;
        return db.update(tableSavedTest, savedTestValue, whereClause, null);
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

    public long savedTestDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = KEY_idSavedTest + " = " + id;
        return db.delete(tableSavedTest, whereClause, null);
    }

    /*Select Methods*/
    public ArrayList<Integer> selectAllFavorites() {
        String query = "SELECT * FROM " + tableFavoriteTests
                + " ORDER BY " + KEY_idFavoriteTest;

        ArrayList<Integer> testsArrayList = new ArrayList<>();
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    testsArrayList.add(cursor.getInt(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return testsArrayList;
    }

    public ArrayList<HistoryObj> selectAllHistory() {
        String query = "SELECT * FROM " + tableHistory
                + " ORDER BY " + KEY_idHistory;

        ArrayList<HistoryObj> historyArrayList = new ArrayList<>();
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    historyArrayList.add(new HistoryObj(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3)
                            , cursor.getString(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return historyArrayList;
    }

    public String selectTestHistory(int id) {
        String answerList = "";
        String query = "SELECT * FROM " + tableHistory
                + " WHERE " + KEY_idTestHistory + " = " + id;

        try {
            Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    answerList = cursor.getString(9);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return answerList;
    }

    public ArrayList<TestObj> selectAllFavoredQuestion() {
        String query = "SELECT * FROM " + tableFavoredQuestion
                + " ORDER BY " + KEY_idQuestion;

        ArrayList<TestObj> testObjArrayList = new ArrayList<>();
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    testObjArrayList.add(new TestObj(cursor.getString(1)
                            , cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return testObjArrayList;
    }

    public TestObj selectFavoredQuestion(int id) {
        String query = "SELECT * FROM " + tableFavoredQuestion
                + " WHERE " + KEY_idQuestion + " = " + id;

        TestObj testObj = new TestObj();
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    testObj = new TestObj(cursor.getString(1), cursor.getInt(2)
                            , cursor.getString(3), cursor.getString(4), cursor.getInt(5));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return testObj;
    }

    public int selectQuestionState(int id) {
        int state = 3;
        String query = "SELECT * FROM " + tableQuestionState
                + " WHERE " + KEY_question + " = " + id;

        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    state = cursor.getInt(2);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return state;
    }

    public String selectSavedTestAnswers(int id) {
        String savedTest = "";
        String query = "SELECT * FROM " + tableSavedTest
                + " WHERE " + KEY_idSavedTest + " = " + id;

        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    savedTest = cursor.getString(3);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return savedTest;
    }

    public ArrayList<TestsTitleObj> selectAllSavedTest() {
        String query = "SELECT * FROM " + tableSavedTest
                + " ORDER BY " + KEY_id;

        ArrayList<TestsTitleObj> testsTitleObjArrayList = new ArrayList<>();
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    boolean hasNegative = cursor.getInt(4) == 1;
                    testsTitleObjArrayList.add(new TestsTitleObj(cursor.getInt(1), cursor.getString(5), hasNegative, cursor.getInt(2)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return testsTitleObjArrayList;
    }

    public boolean isTestFavored(int id) {
        boolean isFavored = false;
        String query = "SELECT * FROM " + tableFavoriteTests +
                " WHERE " + KEY_idTest + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
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
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return isFavored;
    }

    public boolean isQuestionFavored(int id, String testName) {
        boolean isFavored = false;
        String query = "SELECT * FROM " + tableFavoredQuestion
                + " WHERE (" + KEY_idQuestion + " = " + id + " AND "
                + KEY_favoredTestName + " = " + testName + ")";

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(2) != 0) {
                        isFavored = true;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return isFavored;
    }

    public boolean isQuestionStateCreated(int id) {
        boolean isCreated = false;
        String query = "SELECT * FROM " + tableQuestionState +
                " WHERE " + KEY_question + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    if (id == cursor.getInt(1)) {
                        isCreated = true;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return isCreated;
    }

    public boolean isSavedTestCreated(int id) {
        boolean isCreated = false;
        String query = "SELECT * FROM " + tableSavedTest +
                " WHERE " + KEY_idSavedTest + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    if (id == cursor.getInt(1)) {
                        isCreated = true;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(Constants.TAG, e.toString());
        }
        return isCreated;
    }
}