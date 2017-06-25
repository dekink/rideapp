package lecture.mobile.final_project.ma01_20141029;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DE on 2016-12-25.
 * 라이딩 기록 출력하는 엑티비티
 * 작성자 :김다은
 * 학번: 20141029
 */

public class RecordActivity extends AppCompatActivity{

    ListView lvRecord;
    RecordAdapter recordAdapter;
    RidingDBHelper ridingDBHelper;
    ArrayList<Riding> rList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lvRecord = (ListView)findViewById(R.id.lvRecord);
        ridingDBHelper = new RidingDBHelper(this);

        lvRecord.setOnItemLongClickListener(itemLongClickListener);
        lvRecord.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onResume() { //db에 있는 내용 어댑터를 이용해 리스트뷰에 출력
        super.onResume();
        SQLiteDatabase db = ridingDBHelper.getReadableDatabase();
        rList = new ArrayList<Riding>();

        Cursor cursor = db.query(RidingDBHelper.TALBE_NAME, null,null,null,null,null,null,null);

        while(cursor.moveToNext()){
            Riding dto = new Riding();
            dto.set_id(cursor.getInt(0));
            dto.setRidingTime(cursor.getString(1));
            dto.setStartTime(cursor.getString(2));
            dto.setDistance(cursor.getString(3));
            dto.setSpeed(cursor.getString(4));
            rList.add(dto);
        }

        recordAdapter = new RecordAdapter(this, R.layout.record, rList);
        lvRecord.setAdapter(recordAdapter);
        cursor.close();
        ridingDBHelper.close();
    }

    //리스트뷰 클릭시 해당정보 kakao톡에 공유
    AdapterView.OnItemClickListener itemClickListener =
            new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final int pos = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this); //다이알로그로 공유 물어보기
            builder.setTitle("공유");
            builder.setMessage("Kakao톡에 공유 하시겠습니까?");
            builder.setPositiveButton("공유", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        final KakaoLink kakaoLink = KakaoLink.getKakaoLink(RecordActivity.this);
                        final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                        kakaoBuilder.addText("Riding !\n" + rList.get(pos).getRidingTime() + rList.get(pos).getDistance() + rList.get(pos).getSpeed());
                        String url = "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSNbkZi9LIx5wR38llay2e0QVH7yDOdhN39fs3SL_wT6obPggwSeQ";
                        kakaoBuilder.addImage(url, 500, 600);
                        kakaoBuilder.addAppButton("타러가기");
                        kakaoLink.sendMessage(kakaoBuilder, RecordActivity.this);
                    } catch (KakaoParameterException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("취소", null);
            builder.setCancelable(false);
            builder.show();
        }
    };

    //리스트뷰 롱클릭시 db삭제 실행
    AdapterView.OnItemLongClickListener itemLongClickListener =
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final int pos = position;
                    final long iD = id;
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                    builder.setTitle("삭제");
                    builder.setMessage("삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (rList.remove(pos) != null) {
                                SQLiteDatabase db = ridingDBHelper.getWritableDatabase();
                                db.delete(ridingDBHelper.TALBE_NAME, "_id" + "=" + iD, null);
                                Toast.makeText(RecordActivity.this, "삭제완료!", Toast.LENGTH_SHORT).show();
                                recordAdapter.notifyDataSetChanged();
                                ridingDBHelper.close();
                            }
                        }
                    });
                    builder.setNegativeButton("취소", null);
                    builder.setCancelable(false);
                    builder.show();
                    return true;
                }
            };

    //액션바 뒤로가기
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}
