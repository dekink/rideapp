package lecture.mobile.final_project.ma01_20141029;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by DE on 2016-12-20.
 * 지도에 내위치 표시 -> 이동하면 라인그리기 ->저장버튼누르면 라이딩정보저장
 * 작성자 :김다은
 * 학번: 20141029
 */

public class RideActivity extends AppCompatActivity {

    public final static String TAG = "MyGooglemapTest";
    public final static int ZOOM_LEVEL = 17;

    private GoogleMap mGoogleMap;
    private LocationManager locManager;

    private Marker centerMarker;
    private MarkerOptions markerOptions;
    private PolylineOptions lineOptions;

    RidingDBHelper mDBHelper;
    private Geocoder geocoder = new Geocoder(RideActivity.this);

    long startTime = 0;
    long endTime = 0;
    String currentTime = null;

    //거리구하기위한 변수들
    Location locationTmp = null; //이동전 location저장
    double distance; //거리저장
    long distanceTmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        mDBHelper = new RidingDBHelper(this);

//        위치관리자 준비
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

//        구글맵 준비
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallback);

//        마커를 생성하기 위한 옵션 지정
        markerOptions = new MarkerOptions();
        markerOptions.title("현재 위치");
        markerOptions.snippet("최종 위치");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.wheel));

//        라인을 그리기 위한 옵션 지정
        lineOptions = new PolylineOptions();
        lineOptions.color(Color.BLACK);
        lineOptions.width(6);

        //액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    public void onRClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                //시작시간 측정
                startTime = System.currentTimeMillis();
                currentTime = DateFormat.getDateTimeInstance().format(new Date());
                //위치 정보 수신 시작 - 5초 간격으로 0 m 이상 이동 시 수신
                locManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 0, locationListener);
                Toast.makeText(RideActivity.this, "START!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStop:
                //끝날때 시간 측정
                endTime = System.currentTimeMillis();
                //위치 정보 수신 종료
                locManager.removeUpdates(locationListener);
                //라이딩한 시간 (분:초)출력
                distanceTmp = Math.round(distance);
                Toast.makeText(RideActivity.this, "시간 " + (endTime - startTime) / (1000*60) +":"+ ((endTime - startTime) % (1000*60)) / 1000, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnSave:
                Toast.makeText(RideActivity.this, "거리: "+ distanceTmp + "m 속도: "+ distanceTmp / ((endTime - startTime) / 1000) + "m/s", Toast.LENGTH_SHORT).show();
                //라이딩 기록 DB저장
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues row = new ContentValues();
                row.put("ridingTime", " " + (endTime - startTime) / (1000*60) +":"+ ((endTime - startTime) % (1000*60)) / 1000);
                row.put("startTime", currentTime);
                row.put("distance", " "+ distanceTmp + "m");
                row.put("speed", " "+ distanceTmp / ((endTime - startTime) / 1000) + "m/s");
                db.insert(mDBHelper.TALBE_NAME, null, row);
                mDBHelper.close();
                finish();

                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
//        위치 정보 수신 종료 - 위치 정보 수신 종료를 누르지 않았을 경우를 대비
        locManager.removeUpdates(locationListener);
    }


    /*Google Map 준비 시 호출할 CallBack 인터페이스*/
    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
//            로딩한 구글맵을 보관
            mGoogleMap = googleMap;

//            설정한 provider에서 기록하고 있는 최종 위치 확인
            Location lastLocation = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

//            기록한 최종 위치가 있을 경우와 없을 경우를 구분하여 구현
            LatLng lastLatLng;
            if (lastLocation != null) {
                lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            } else {
                lastLatLng = new LatLng(37.606320, 127.041808);
            }

            Log.i(TAG, "Last location: " + lastLatLng.latitude + ", " + lastLatLng.longitude);

//            애니메이션 효과로 이동 시
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, ZOOM_LEVEL));

            // 현재 위치에 마커 지정
            markerOptions.position(lastLatLng);

            // 지도에 마커 추가 및 마커 윈도우 표시 - 윈도우 표시를 안 할 경우 마커를 터치할 때 표시됨
            centerMarker = mGoogleMap.addMarker(markerOptions);
            //centerMarker.showInfoWindow();

//            마커 윈도우 클릭 시 이벤트 처리
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    LatLng markerPosition = marker.getPosition();
                    String loc = String.format("윈도우 클릭 - 위도:%f, 경도:%f",  markerPosition.latitude, markerPosition.longitude);
                    Toast.makeText(RideActivity.this, loc, Toast.LENGTH_SHORT).show();
                }
            });

            //map 클릭하면 클릭한곳 경도위도를 주소로 바꿔줌
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                List<Address> addressList = null;
                @Override
                public void onMapClick(LatLng latLng) {
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;
                    Log.i(TAG, "##latitude ##longitude:" + latitude +" "+ longitude);

                    try {
                        addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList == null) {
                        Toast.makeText(RideActivity.this, "no result", Toast.LENGTH_SHORT).show();
                    } else {
                        StringBuffer result = new StringBuffer();
                        for (Address address : addressList) {
                            result.append(String.format("주소: %s", address.getAddressLine(0).toString()));
                            result.append(System.getProperty("line.separator"));  // == "\n"
                        }
                        Toast.makeText(RideActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    /*위치 정보 수신 LocationListener*/
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "Current Location : " + location.getLatitude() + ", " + location.getLongitude());

//            현재 수신한 위치 정보 Location을  LatLng 형태로 변환
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());

            //전 location과 이동후 location을 이용해 거리계산
            if(locationTmp != null) {
                distance += locationTmp.distanceTo(location);
                Log.i(TAG, "###거리: " + distance);
            }
//            새로운 위치로 지도 이동
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, ZOOM_LEVEL));

            // 새로운 위치로 마커의 위치 지정
            markerOptions.title("현재 위치");
            markerOptions.snippet("이동중");
            centerMarker.setPosition(currentLoc);

//            현재 위치를 라인 정보로 추가
            lineOptions.add(currentLoc);
            mGoogleMap.addPolyline(lineOptions);
            //거리계산을 위해 location저장
            locationTmp = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
        @Override
        public void onProviderEnabled(String s) {

        }
        @Override
        public void onProviderDisabled(String s) {

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