package lecture.mobile.final_project.ma01_20141029;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by DE on 2016-12-19.
 * 알람설정하는 액티비티
 * 작성자 :김다은
 * 학번: 20141029
 */

public class AlarmActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    Calendar cal;
    PendingIntent pendingIntent;
    EditText etAlarmTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        cal = Calendar.getInstance();
        // 년월일시분 초기화
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        etAlarmTitle = (EditText)findViewById(R.id.etAlarmTitle);

        //액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onRClick(View v) {
        switch (v.getId()) {
            case R.id.btndate: //이버튼을 누르면 날짜 설정 다이알로그
                new DatePickerDialog(AlarmActivity.this, dateSetListener, mYear, mMonth, mDay).show();
                break;
            case R.id.btnTime://이버튼을 누르면 시간 설정 다이알로그
                new TimePickerDialog(AlarmActivity.this, timeSetListener, mHour, mMinute, false).show();
                break;
            case R.id.btnSetalarm: //알람설정 버튼
                cal.set(mYear, mMonth, mDay, mHour, mMinute, 0); //앞에 Picker로받은 시간과날짜

                Intent intent = new Intent(this, AlarmReceiver.class); //AlarmReceiver로보낼 인텐트생성
                intent.putExtra("alarmTitle", etAlarmTitle.getText().toString()); //사용자가 입력한 알람이름 같이 보냄
                pendingIntent
                        = PendingIntent.getBroadcast(this, 0, intent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 5*60000, pendingIntent); //알람등록
                showToast();
                break;

            case R.id.btnAlarmStop:
                if (pendingIntent != null) alarmManager.cancel(pendingIntent); //알람끄기
                Toast.makeText(AlarmActivity.this, "알람이 꺼졌습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
    //  다이알로그에서 설정한 년월일 미리생성한 변수에 넣어줌
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
        }
    };
    //  다이알로그에서 설정한 시분 미리생성한 변수에 넣어줌
    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;

        }
    };

    //
    public void showToast() { //설정한 알람시간 확인용
        String result = String.format("%d년 %d월 %d일 %d시 %d분", mYear, mMonth + 1, mDay, mHour, mMinute);
        Toast.makeText(AlarmActivity.this, result, Toast.LENGTH_LONG).show();
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
