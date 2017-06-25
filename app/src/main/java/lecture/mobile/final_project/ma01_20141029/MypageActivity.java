package lecture.mobile.final_project.ma01_20141029;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by DE on 2016-12-19.
 * 마이페이지 액티비티 ( 알람설정버튼, 카메라찍기 버튼, 메모리스트뷰, 사진찍은거나오는 이미지뷰)
 * 작성자 :김다은
 * 학번: 20141029
 */

public class MypageActivity extends AppCompatActivity {

    private static final int CALL_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    ListView lvMemoList;
    ImageView imageView;
    Intent intent;
    private String saveFilePath;
    private Uri captureUri;
    private String absoultePath;
    MemoAdapter memoAdapter;
    MemoDBHelper memoDBHelper;
    ArrayList<MyMemoData> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        //액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        memoDBHelper = new MemoDBHelper(this);
        lvMemoList = (ListView)findViewById(R.id.lvMemo);
        imageView = (ImageView)findViewById(R.id.imageView5);
        //파일경로 지정 폴더생성
        saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/boardApp/";
        new File(saveFilePath).mkdir();

        lvMemoList.setOnItemLongClickListener(itemLongClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = memoDBHelper.getReadableDatabase();
        list = new ArrayList<MyMemoData>();

        Cursor cursor = db.query(MemoDBHelper.TALBE_NAME, null,null,null,null,null,null,null);

        while(cursor.moveToNext()){
            MyMemoData dto = new MyMemoData();
            dto.set_id(cursor.getInt(0));
            dto.setParkname(cursor.getString(1));
            dto.setTitle(cursor.getString(2));
            dto.setContent(cursor.getString(3));
            dto.setCurrentdate(cursor.getString(4));
            list.add(dto);
        }

        memoAdapter = new MemoAdapter(this, R.layout.memo, list);
        lvMemoList.setAdapter(memoAdapter);
        cursor.close();
        memoDBHelper.close();
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnCamera:
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doCamera();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doAlbum();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                new AlertDialog.Builder(this)
                        .setTitle("선택")
                        .setPositiveButton("취소", cancelListener)
                        .setNeutralButton("사진찍기", cameraListener)
                        .setNegativeButton("앨범에서", albumListener)
                        .show();
                break;
            case R.id.btnAlarm:
                intent = new Intent(this, AlarmActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void doCamera() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String uri = "board_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        captureUri = Uri.fromFile(new File(saveFilePath + uri));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, captureUri);
        startActivityForResult(intent, CALL_CAMERA);
    }
    public void doAlbum() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent receivedData) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_FROM_ALBUM:
                    captureUri = (Uri) receivedData.getExtras().get("data");
                    intent.putExtra("outputX", 250);
                    intent.putExtra("outputY", 150);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("crop", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CROP_FROM_IMAGE);
                    break;
                case CALL_CAMERA:
                    //이미지 크롭
                    intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(captureUri, "image/*");

                    intent.putExtra("outputX", 250);
                    intent.putExtra("outputY", 150);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("crop", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CROP_FROM_IMAGE);
                    break;
                case CROP_FROM_IMAGE:
                    if (resultCode != RESULT_OK) {
                        return;
                    }

                    final Bitmap extras = (Bitmap)receivedData.getExtras().get("data");

                    String filePath = saveFilePath + System.currentTimeMillis() + ".jpg";

                    if(extras != null)
                    {
                        Bitmap photo = extras;
                        imageView.setImageBitmap(photo);

                        storeCropImage(photo, filePath);
                        absoultePath = filePath;
                        break;
                    }
                    File f = new File(captureUri.getPath());
                    if (f.exists()) {
                        f.delete();
                    }
            }
        }
    }

    private void storeCropImage(Bitmap photo, String filePath) {

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;
        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    AdapterView.OnItemLongClickListener itemLongClickListener =
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final int pos = position;
                    final long iD = id;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MypageActivity.this);
                    builder.setTitle("삭제");
                    builder.setMessage("삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (list.remove(pos) != null) {
                                SQLiteDatabase db = memoDBHelper.getWritableDatabase();
                                db.delete(MemoDBHelper.TALBE_NAME, "_id" + "=" + iD, null);
                                Toast.makeText(MypageActivity.this, "삭제완료!", Toast.LENGTH_SHORT).show();
                                memoAdapter.notifyDataSetChanged();
                                memoDBHelper.close();
                            }
                        }
                    });
                    builder.setNegativeButton("취소", null);
                    builder.setCancelable(false);
                    builder.show();
                    return false;
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
    }
}