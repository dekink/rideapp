package lecture.mobile.final_project.ma01_20141029;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DE on 2016-12-19.
 * 메모저장한것을 보기위한 커스텀어댑터
 * 작성자 :김다은
 * 학번: 20141029
 */

public class MemoAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    int layout;
    ArrayList<MyMemoData> list;

    public MemoAdapter(Context context, int resource, ArrayList<MyMemoData> list) {
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
    public MyMemoData getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final  int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView tvParkName = (TextView)convertView.findViewById(R.id.tvParkName);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView)convertView.findViewById(R.id.tvContent);

        MyMemoData dto = list.get(position);
        tvParkName.setText(dto.getParkname());
        tvTitle.setText(dto.getTitle() + " " + dto.getCurrentdate());
        tvContent.setText(dto.getContent());

        return convertView;
    }
}
