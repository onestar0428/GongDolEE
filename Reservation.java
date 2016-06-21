package example.com.mptermui;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Reservation extends Activity {
    ImageButton btn;
    //connet tast;
    connet_S tast2;
    int year, month, day;
    Spinner spin1;
    Spinner spin2;
    String msg;
    ArrayList<String> arraylist1;
    ArrayList<String> arraylist2;
    String date="월화수목금";
    int datenum=0;//요일선택

    String[] select= new String[2];
    int[] able= new int[12];
    ListView listview;
    String dtime, time, classroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TypefaceUtil.setGlobalFont(this, getWindow().getDecorView());

        setContentView(R.layout.activity_reservation);

        //EditText k2= (EditText)findViewById(R.id.phoneN);
        // TelephonyManager phone=(TelephonyManager)getSystemService(getBaseContext().TELEPHONY_SERVICE);
        // String phonenum=phone.getLine1Number();
        //Log.d("번호",phonenum);
        //k2.setHint(phonenum);

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        select[0]="IT대학";
        select[1]="304";
        btn = (ImageButton) findViewById(R.id.day_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(Reservation.this, dateSetListener, year, month, day).show();
            }
        });
        makeSpinner();
        Button btn2 = (Button) findViewById(R.id.send);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                send();
            }
        });
    }
    public void send()
    {
        EditText k1= (EditText)findViewById(R.id.name);
        EditText k2= (EditText)findViewById(R.id.phoneN);
        String a1=k1.getText().toString();
        String a2=k2.getText().toString();

        if(a1.length()<1||a2.length()<5)
        {
            Toast.makeText(getBaseContext(), "이름이나 전화번호를 확인하세요", Toast.LENGTH_LONG).show();
        }
        else
        {
            time = "";
            SparseBooleanArray k = listview.getCheckedItemPositions();
            for (int i = 0; i < 12; i++) {
                if (k.get(i)) {
                    time += date.charAt(datenum) + i + " ,";
                }
            }
            classroom = select[0] + "-" + select[1];
            tast2 = new connet_S();
            tast2.execute("http://cjf9028.cafe24.com/insert1.php");
            Toast.makeText(getBaseContext(), "예약성공 ", Toast.LENGTH_LONG).show();
        }
    }
    private class connet_S extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    Log.w("데이터 값 ", time + " " + dtime + " " + classroom);
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("time").append("=").append(time).append("&");
                    buffer.append("dtime").append("=").append(dtime).append("&");
                    buffer.append("classroom").append("=").append(classroom).append("&");

                    OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();
                }
                else
                    Log.w("서버 연결 실패:"," ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }
        protected void onPostExecute(String str){

        }
    }
    public void makeSpinner() {
        spin1 = (Spinner) findViewById(R.id.spinner1);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        arraylist1 = new ArrayList<String>();
        arraylist1.add("IT대학");
        arraylist1.add("data1");
        arraylist1.add("data2");
        arraylist1.add("data3");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraylist1);
        //sp.setPrompt("골라봐"); // 스피너 제목
        spin1.setAdapter(adapter);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if(arraylist1.get(position).toString()=="IT대학")
                {
                    select[0] = arraylist1.get(position).toString();
                    arraylist2 = new ArrayList<String>();
                    arraylist2.add("304");
                    arraylist2.add("305");
                    arraylist2.add("412");
                    arraylist2.add("413");
                    arraylist2.add("601");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_dropdown_item, arraylist2);
                    spin2.setAdapter(adapter);
                }
                else
                {
                    arraylist2 = new ArrayList<String>();
                    arraylist2.add(" ");
                    arraylist2.add(" ");
                    arraylist2.add(" ");
                    arraylist2.add(" ");
                    arraylist2.add(" ");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_dropdown_item, arraylist2);
                    spin2.setAdapter(adapter);
                }
                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                        select[1] = arraylist2.get(position);
                        for(int i=0;i<12;i++)
                            able[i]=1;
                        //tast=new connet();
                        //tast.execute("http://cjf9028.cafe24.com/showDB.php");
                        final ArrayList<String> items = new ArrayList<String>() ;
                        for(int i=0;i<12;i++) {
                            if(able[i]==1) {
                                items.add((i + 9) + ":00~" + (i + 10) + ":00");
                            }
                        }
                        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
                        final ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_multiple_choice, items) ;

                        // listview 생성 및 adapter 지정.
                        listview = (ListView) findViewById(R.id.listview) ;
                        listview.setAdapter(adapter) ;
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    private class connet extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    String[] dat= new String[3];
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        int i=0;
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                            // 학수번호 교수명 수업시간 대학,강의실 순으로 한줄씩 저장
                            dat[i++]=line;
                            if(i==3)
                            {
                                String[] da=dat[1].split("-");
                                if(da[0]==select[0])
                                    if(da[1]==select[1])
                                    {
                                        String k[]= dat[2].split(",");
                                        int t=0;
                                        for(int j=0;j<12;j++)
                                        {
                                            if(k[t].charAt(0)==date.charAt(datenum))
                                            {
                                                if(j>=10)
                                                {
                                                    if(k[t].charAt(1)==1)
                                                        if(k[t].charAt(2)==1)
                                                        {
                                                            able[11]=0;
                                                        }
                                                        else if(k[t].charAt(1)==2)
                                                        {
                                                            able[12]=0;
                                                        }
                                                }
                                                else if(k[t].charAt(1)==i)
                                                    able[j]=0;
                                            }
                                        }
                                    }
                            }
                            i=i%3;
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }
        protected void onPostExecute(String str){

        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            Date date = new Date();
            int y=date.getYear();
            int m=date.getMonth();
            int d=date.getDay();
            y=(y-100+2000)*10000+m*100+d;
            int k=(year)*10000+(monthOfYear+1)*100+dayOfMonth;
            if(k>y) {
                msg = String.format("%d%d%d", year, monthOfYear + 1, dayOfMonth);
                dtime=Integer.toString(k);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear + 1);
                cal.set(Calendar.DATE, dayOfMonth);
                datenum = cal.get(Calendar.DAY_OF_WEEK) - 1;
            }
            else
            {
                Toast.makeText(getBaseContext(),"날짜를 다시 선택하세요",Toast.LENGTH_LONG).show();
                new DatePickerDialog(Reservation.this, dateSetListener, year, month, day).show();
            }
        }
    };
}