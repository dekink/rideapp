package lecture.mobile.final_project.ma01_20141029;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DE on 2016-12-25.
 * 작성자 :김다은
 * 학번: 20141029
 */

public class RidingDBHelper extends SQLiteOpenHelper{

    private final static String DB_NAME = "riding_db";
    public final static String TALBE_NAME = "riding_table";

    public RidingDBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TALBE_NAME + " ( _id integer primary key autoincrement,"
                + "ridingTime TEXT, startTime TEXT, distance TEXT, speed TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
