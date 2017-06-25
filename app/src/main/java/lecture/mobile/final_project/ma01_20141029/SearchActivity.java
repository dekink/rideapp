
package lecture.mobile.final_project.ma01_20141029;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by DE on 2016-12-19.
 * 공원 검색 Activity
 * 작성자 :김다은
 * 학번: 20141029
 */

public class SearchActivity extends AppCompatActivity {

    EditText etTarget;
    ListView lvList;
    String address;
    String query;
    ParkAdapter adapter;

    ArrayList<Park> resultList;
    MyXmlParser parser;
    RelativeLayout searchLayout;
    InputMethodManager inputMM;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etTarget = (EditText)findViewById(R.id.editText2);
        lvList = (ListView)findViewById(R.id.lvList);
        searchBtn = (Button)findViewById(R.id.btnSearch);

        resultList = new ArrayList<Park>();
        //검색된 공원을 리스트뷰에 보여주는 커스텀 어댑터(ParkAdapter)
        adapter = new ParkAdapter(this, R.layout.park, resultList);

        lvList.setAdapter(adapter);
        //공원 검색 API URL
        address = getResources().getString(R.string.apiURL);
        lvList.setOnItemClickListener(onItemClickListener);

        //액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //화면클릭시 키보드 내리기
        searchLayout = (RelativeLayout)findViewById(R.id.activity_search);
        inputMM = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMM.hideSoftInputFromWindow(searchLayout.getWindowToken(), 0);
            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSearch:
                query="";
                try{
                    query = URLEncoder.encode(etTarget.getText().toString(), "utf-8"); //utf-8로 인코딩
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                address += query;
                new NetworkAsyncTask().execute(address);
                //버튼을 누르면 키보드 내려감
                inputMM.hideSoftInputFromWindow(searchBtn.getWindowToken(), 0);
                break;
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {

        public final static String TAG = "NetworkAsyncTask";
        public final static int TIME_OUT = 10000;

        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SearchActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            address = strings[0];
            StringBuilder resultBuilder = new StringBuilder();

            try {
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if (conn != null) {
                    conn.setConnectTimeout(TIME_OUT);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        for (String line = br.readLine(); line != null; line = br.readLine()) {
                            resultBuilder.append(line + '\n');
                        }

                        br.close();
                    }
                    conn.disconnect();
                }

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Toast.makeText(SearchActivity.this, "Malformed URL", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            address = getResources().getString(R.string.apiURL);
            return resultBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            parser = new MyXmlParser();
            resultList = parser.parse(result);

            adapter.setList(resultList);
            adapter.notifyDataSetChanged();
            progressDlg.dismiss();
        }
    }

    //리스트뷰에 검색된 공원을 클릭할때 사용하는 리스너(클릭하면 ParkMapActivity에서 공원지도를 보여줌)
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Park dto = resultList.get(position);
            Intent intentMap = new Intent(SearchActivity.this, ParkMapActivity.class);
            //지도띄우기 위해 경도위도 보내기
            intentMap.putExtra("longi", dto.getLONGITUDE().toString());
            intentMap.putExtra("lati", dto.getLATITUDE().toString());

            intentMap.putExtra("park", dto.getP_PARK().toString());
            startActivity(intentMap);
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

