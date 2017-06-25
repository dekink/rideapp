package lecture.mobile.final_project.ma01_20141029;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Created by DE on 2016-12-19.
 * Main화면
 * 작성자 :김다은
 * 학번: 20141029
 */

public class MainActivity extends AppCompatActivity {
    Intent intent;
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뒤로가기 두번클릭시 종료때 사용 핸들러
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnMSearch: //공원 검색 액티비티로 이동 (SearchActivity)
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMBoard: //라이드 액티비티로 이동 (RideActivity)
                intent = new Intent(this, RideActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMypage: //마이페이지로 이동 (MypageActivity)
                intent = new Intent(this, MypageActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRecord: //라이딩 기록 페이지로 이동
                intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    //뒤로가기 두번클릭시 종료
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
