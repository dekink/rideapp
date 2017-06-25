package lecture.mobile.final_project.ma01_20141029;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by DE on 2016-12-26.
 * 알람 리시버
 * 작성자 :김다은
 * 학번: 20141029
 */

public class AlarmReceiver extends BroadcastReceiver {
    static final int NAPNOTI = 1;
    NotificationManager notiManager;
    String alarmTitle;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        alarmTitle = intent.getStringExtra("alarmTitle");
        notiManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, AlarmEndActivity.class); //AlarmEndActivity로 보내는 인텐트생성
        intent1.putExtra("alarmTitle", alarmTitle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews denotiView = new RemoteViews(context.getPackageName(), R.layout.denotiview);

        Notification.Builder builder = new Notification.Builder(context)
                .setTicker("**알람시간**")
                .setSmallIcon(R.drawable.wheel)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] {500,500})
                .setWhen(System.currentTimeMillis())
                .setContent(denotiView); //custom notification사용

        notiManager.notify(NAPNOTI, builder.build());
    }

    public static class AlarmEndActivity extends AppCompatActivity {

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.alarmview);

            Intent intent = getIntent();
            String alarmTitle = intent.getStringExtra("alarmTitle");
            NotificationManager notiManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            notiManager.cancel(NAPNOTI); //notification눌러서 여기로오면 notification취소됨

            TextView etAlarmTItle = (TextView) findViewById(R.id.tvTitleview);
            etAlarmTItle.setText(alarmTitle);
            ImageButton btn = (ImageButton)findViewById(R.id.btnAlarmEnd);
            btn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

}