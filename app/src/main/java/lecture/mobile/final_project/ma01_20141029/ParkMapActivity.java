package lecture.mobile.final_project.ma01_20141029;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by DE on 2016-12-19.
 * SearchActivity에서 리스트뷰에 검색된 공원클릭후 이 엑티비티에서 공원위치지도로 보여줌
 * 작성자 :김다은
 * 학번: 20141029
 */

public class ParkMapActivity extends AppCompatActivity {

    public final static int DEFAULT_ZOOM_LEVEL = 17;
    private GoogleMap mGoogleMap;
    private MarkerOptions options;
    private Marker centerMarker;
    double longi=0;
    double lati=0;
    String parkname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkmap);

        //인텐트로 위도 경도 공원이름 값 받기
        Intent intent = getIntent();
        longi = Double.parseDouble(intent.getStringExtra("longi"));
        lati = Double.parseDouble(intent.getStringExtra("lati"));
        //Log.d("test", "##LONGITUDE" + intent.getStringExtra("longi"));
        //Log.d("test", "********" + intent.getStringExtra("lati"));

        parkname = intent.getStringExtra("park");

        /*구글맵 준비*/
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        //액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMemo: //메모 버튼을 누르면 MemoActivity 로가고 거기서 메모를 할수있다
                Intent intent = new Intent(this, MemoActivity.class);
                intent.putExtra("park", parkname);
                startActivity(intent);
                break;
        }
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            LatLng parkLoc;
            parkLoc = new LatLng(lati, longi);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(parkLoc, DEFAULT_ZOOM_LEVEL));

            //마커 옵션및 마커등록
            options = new MarkerOptions();
            options.position(parkLoc);
            centerMarker = mGoogleMap.addMarker(options);
            centerMarker.showInfoWindow();
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
