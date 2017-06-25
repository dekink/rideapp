package lecture.mobile.final_project.ma01_20141029;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DE on 2016-12-19.
 *작성자 :김다은
 * 학번: 20141029
 */

public class MemoDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "memo_db";
    public final static String TALBE_NAME = "memo_table";

    public MemoDBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TALBE_NAME + " ( _id integer primary key autoincrement,"
                + "parkname TEXT, title TEXT, content TEXT, currentdate TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
