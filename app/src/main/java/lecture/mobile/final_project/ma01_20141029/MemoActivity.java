package lecture.mobile.final_project.ma01_20141029;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by DE on 2016-12-19.
 * Memo 작성 -> DB입력
 * 저장된 메모는 MypageActivity에서 출력
 * 작성자 :김다은
 * 학번: 20141029
 */

public class MemoActivity extends AppCompatActivity {

    MemoDBHelper mDBHelper;
    String parkName;
    RelativeLayout memoLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        mDBHelper = new MemoDBHelper(this);

        //액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        parkName = intent.getStringExtra("park"); //공원이름 인텐트로받음(내가 클릭한 공원)

        //화면클릭시 키보드 내리기
        memoLayout = (RelativeLayout)findViewById(R.id.activity_memo);
        memoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMM = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMM.hideSoftInputFromWindow(memoLayout.getWindowToken(), 0);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave: //save버튼
                EditText etTitle = (EditText)findViewById(R.id.etMemoTitle);
                EditText etContent = (EditText)findViewById(R.id.etMemoContent);
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();

                if (title.length() ==0 || content.length() ==0) { //제목과 내용을 모두입력해야만 저장이됨
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
                    builder.setTitle("경고")
                            .setMessage("모든 항목을 채워주세요")
                            .setPositiveButton("확인", null)
                            .show();
                }

                //내용 60자 미만 작성
                else if (content.length() > 60) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
                    builder.setTitle("경고")
                            .setMessage("내용 60자 미만 작성")
                            .setPositiveButton("확인", null)
                            .show();
                }
                else {
                    //DB저장을 위한
                    SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    ContentValues row = new ContentValues();
                    row.put("parkname", parkName);
                    row.put("title", title);
                    row.put("content", content);
                    row.put("currentdate", DateFormat.getDateTimeInstance().format(new Date()));
                    db.insert(mDBHelper.TALBE_NAME, null, row);
                    mDBHelper.close();
                    etTitle.setText("");
                    etContent.setText("");
                    finish();
                }
                break;
        }
    }
    //액션바 뒤로가기
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
