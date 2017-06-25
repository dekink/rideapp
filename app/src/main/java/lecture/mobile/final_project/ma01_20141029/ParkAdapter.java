package lecture.mobile.final_project.ma01_20141029;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by DE on 2016-10-13.
 * 공원정보 출력을 위한 커스텀 어댑터
 * 작성자 :김다은
 * 학번: 20141029
 */

public class ParkAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    int layout;
    ArrayList<Park> list;


    public ParkAdapter(Context context, int resource, ArrayList<Park> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Park getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }
    public void setList(ArrayList<Park> list) {
        this.list = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String ivParkIMG;
        final int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView parkTitle = (TextView) convertView.findViewById(R.id.tvParkTitle);
        TextView parkAddr = (TextView) convertView.findViewById(R.id.tvParkAddr);
        final ImageView parkImage = (ImageView) convertView.findViewById(R.id.ivParkIMG);

        Park dto = list.get(position);
        parkTitle.setText(dto.getP_PARK());
        parkAddr.setText(dto.getP_ADDR());
        ivParkIMG = dto.getP_IMG();

        //URL이용해 이미지 불러오기 스레드
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(ivParkIMG); // URL 주소를 이용해서 URL 객체 생성

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    parkImage.setImageBitmap(bitmap); //   이미지 뷰에 지정할 Bitmap을 생성
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        mThread.start();

        return convertView;

    }


}
