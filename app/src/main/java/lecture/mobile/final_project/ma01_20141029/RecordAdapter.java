package lecture.mobile.final_project.ma01_20141029;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DE on 2016-12-26.
 * 라이딩기록 출력을 위한 커스텀 어댑터
 * 작성자 :김다은
 * 학번: 20141029
 */

public class RecordAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    int layout;
    ArrayList<Riding> list;

    public RecordAdapter(Context context, int resource, ArrayList<Riding> list) {
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
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public Riding getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final  int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView tvStartTime = (TextView)convertView.findViewById(R.id.tvStartTime);
        TextView tvDistance = (TextView)convertView.findViewById(R.id.tvDistance);
        TextView tvSpeed = (TextView)convertView.findViewById(R.id.tvSpeed);
        TextView tvRidingTime = (TextView)convertView.findViewById(R.id.tvRidingTime);

        Riding dto = list.get(position);
        tvStartTime.setText(dto.getStartTime());
        tvRidingTime.setText(dto.getRidingTime());
        tvSpeed.setText(dto.getSpeed());
        tvDistance.setText(dto.getDistance());

        return convertView;
    }
}
