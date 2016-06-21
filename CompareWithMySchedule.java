package example.com.mptermui;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by jy on 2016-06-08.
 */
public class CompareWithMySchedule extends AppCompatActivity {
    ClassroomDB db;

    ArrayList<TimeTable> tt = new ArrayList<TimeTable>(); // list for data from data
    ArrayList<String> building = new ArrayList<String>();
    ArrayList<String> room = new ArrayList<String>();
    TextView[] time;
    Spinner spin1;
    Spinner spin2;
    ArrayAdapter<String> list1;
    ArrayAdapter<String> list2;
    Button ok, close;
    String[] select = new String[2];

    int BGcolor;
    SharedPreferences prefs;
    final int mode = Activity.MODE_PRIVATE;
    SharedPreferences.Editor edit;
    final String MYPREFS = "MyPreferences_001";


    TextView textMon, textTue, textWed, textThu, textFri;
    int latest = 1700, click_count=0; //initialize latest time
    static int earliest = 800;
    int hourHeight, timeWidth, timeHeight, window_flag=0;
    int[] start = {earliest, earliest, earliest, earliest, earliest};


    LinearLayout.LayoutParams l;
    LinearLayout linearForTime, forTimeWidth;
    GridLayout layoutMon, layoutTue, layoutWed, layoutThu, layoutFri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparemy);
        TypefaceUtil.setGlobalFont(this, getWindow().getDecorView());

        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 타이틀 안보이게한다.

        linearForTime = (LinearLayout) findViewById(R.id.time);
        forTimeWidth = (LinearLayout) findViewById((R.id.forTimeWidth));
        layoutMon = (GridLayout) findViewById(R.id.monday);
        layoutTue = (GridLayout) findViewById(R.id.tuesday);
        layoutWed = (GridLayout) findViewById(R.id.wednesday);
        layoutThu = (GridLayout) findViewById(R.id.thursday);
        layoutFri = (GridLayout) findViewById(R.id.friday);
        ok = (Button) findViewById(R.id.makeOK);
        close= (Button) findViewById(R.id.close);



        textMon = (TextView) findViewById(R.id.cmp_mon);
        textTue = (TextView) findViewById(R.id.cmp_tue);
        textWed = (TextView) findViewById(R.id.cmp_wed);
        textThu = (TextView) findViewById(R.id.cmp_thu);
        textFri = (TextView) findViewById(R.id.cmp_fri);

        prefs = getSharedPreferences(MYPREFS, mode);
        BGcolor = prefs.getInt("Color", 0);

        Intent myLocalIntent = getIntent();
        Bundle myBundle = myLocalIntent.getExtras();
        latest = myBundle.getInt("latest");
        hourHeight = myBundle.getInt("hourHeight");
        timeHeight = myBundle.getInt("timeHeight");

        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      String result = select[0] + "-" + select[1];
                                      initArrayList(result, tt);
                                      if(click_count%2==0) {
                                          setFirstRow();
                                          makeTable();
                                      }
                                      else {
                                          initTable();
                                      }
                                      click_count++;
                                  }
                              }
        );

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myLocalIntent = getIntent();
                Bundle myBundle = myLocalIntent.getExtras();
                myLocalIntent.putExtras(myBundle);
                setResult(Activity.RESULT_OK, myLocalIntent);
                finish();
            }
          });

        int flag = 0;
        db = new ClassroomDB(this);
        db.open();
        Cursor c = db.getAllClassroom();
        if (c.moveToFirst()) {
            do {
                for (int i = 0; i < building.size(); i++) {//check if this subject is already existed in array
                    if (c.getString(c.getColumnIndex("building")).equals(building.get(i))) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    building.add(c.getString(c.getColumnIndex("building")));
                }
                flag = 0;
            } while (c.moveToNext());
        }
        db.close();

        setFirstRow();
        makeSpinner();
    }



    @Override
    public void onResume(){
        super.onResume();
        prefs =getSharedPreferences(MYPREFS, 0);
        BGcolor = prefs.getInt("Color", 0);
        setBGcolor();
    }



    public void setBGcolor(){
        float aph=(float)0.7;
        switch (BGcolor){
            case 1:
                textMon.setBackgroundResource(R.color.t1_1);
                textTue.setBackgroundResource(R.color.t1_1);
                textWed.setBackgroundResource(R.color.t1_1);
                textThu.setBackgroundResource(R.color.t1_1);
                textFri.setBackgroundResource(R.color.t1_1);

                linearForTime.setBackgroundResource(R.color.t1_1);

                break;
            case 2:

                textMon.setBackgroundResource(R.color.t2_1);
                textTue.setBackgroundResource(R.color.t2_1);
                textWed.setBackgroundResource(R.color.t2_1);
                textThu.setBackgroundResource(R.color.t2_1);
                textFri.setBackgroundResource(R.color.t2_1);

                linearForTime.setBackgroundResource(R.color.t2_1);

                break;
            case 3:


                textMon.setBackgroundResource(R.color.t3_1);
                textTue.setBackgroundResource(R.color.t3_1);
                textWed.setBackgroundResource(R.color.t3_1);
                textThu.setBackgroundResource(R.color.t3_1);
                textFri.setBackgroundResource(R.color.t3_1);

                linearForTime.setBackgroundResource(R.color.t3_1);

                break;
            case 4:

                textMon.setBackgroundResource(R.color.t4_1);
                textTue.setBackgroundResource(R.color.t4_1);
                textWed.setBackgroundResource(R.color.t4_1);
                textThu.setBackgroundResource(R.color.t4_1);
                textFri.setBackgroundResource(R.color.t4_1);

                linearForTime.setBackgroundResource(R.color.t4_1);
                break;
            case 5:

                textMon.setBackgroundResource(R.color.t5_2);
                textTue.setBackgroundResource(R.color.t5_2);
                textWed.setBackgroundResource(R.color.t5_2);
                textThu.setBackgroundResource(R.color.t5_2);
                textFri.setBackgroundResource(R.color.t5_2);

                linearForTime.setBackgroundResource(R.color.t5_2);
                break;
            case 6:

                textMon.setBackgroundResource(R.color.t6_2);
                textTue.setBackgroundResource(R.color.t6_2);
                textWed.setBackgroundResource(R.color.t6_2);
                textThu.setBackgroundResource(R.color.t6_2);
                textFri.setBackgroundResource(R.color.t6_2);


                linearForTime.setBackgroundResource(R.color.t6_2);
                break;
            case 7:


                textMon.setBackgroundResource(R.color.t7_1);
                textTue.setBackgroundResource(R.color.t7_1);
                textWed.setBackgroundResource(R.color.t7_1);
                textThu.setBackgroundResource(R.color.t7_1);
                textFri.setBackgroundResource(R.color.t7_1);


                linearForTime.setBackgroundResource(R.color.t7_1);
                break;
            case 8:

                textMon.setBackgroundResource(R.color.t8_3);
                textTue.setBackgroundResource(R.color.t8_3);
                textWed.setBackgroundResource(R.color.t8_3);
                textThu.setBackgroundResource(R.color.t8_3);
                textFri.setBackgroundResource(R.color.t8_3);

                linearForTime.setBackgroundResource(R.color.t8_3);
                break;
            case 9:


                textMon.setBackgroundResource(R.color.t9_3);
                textTue.setBackgroundResource(R.color.t9_3);
                textWed.setBackgroundResource(R.color.t9_3);
                textThu.setBackgroundResource(R.color.t9_3);
                textFri.setBackgroundResource(R.color.t9_3);


                linearForTime.setBackgroundResource(R.color.t9_3);
                break;

            case 10:

                textMon.setBackgroundResource(R.color.t10_2);
                textTue.setBackgroundResource(R.color.t10_2);
                textWed.setBackgroundResource(R.color.t10_2);
                textThu.setBackgroundResource(R.color.t10_2);
                textFri.setBackgroundResource(R.color.t10_2);


                linearForTime.setBackgroundResource(R.color.t10_2);
                break;

            default:
                //  timeTable.setBackgroundResource(R.color.defaultCop);
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
       //if(window_flag==1) {
            // the height will be set at this point
            hourHeight = (linearForTime.getMeasuredHeight()) / time.length;
            hourHeight = time[0].getMeasuredHeight();
            timeWidth = forTimeWidth.getMeasuredWidth() / 5;
            timeHeight = forTimeWidth.getMeasuredHeight();
       // }
        //Toast.makeText(getApplicationContext(), hourHeight + " " + linearForTime.getMeasuredHeight() + " " + time[0].getMeasuredHeight(), Toast.LENGTH_SHORT).show();
    }


    public void makeSpinner() {
        spin1 = (Spinner) findViewById(R.id.building_spinner);
        spin2 = (Spinner) findViewById(R.id.room_spinner);

        list1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, building){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TypefaceUtil.setGlobalFont(this.getContext(), parent);
                return super.getView(position, convertView, parent);
            }
            @Override
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "210APPL.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }
        };
        //list1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin1.setAdapter(list1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                room.clear();
                select[0] = building.get(position);

                db.open();
                Cursor c = db.getAllClassroom();
                if (c.moveToFirst()) {
                    do {
                        int flag = 0;
                        for (int i = 0; i < room.size(); i++) {//check if this subject is already existed in array
                            if (c.getString(c.getColumnIndex("classroom")).equals(room.get(i))) {
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            room.add(c.getString(c.getColumnIndex("classroom")));
                        }
                    } while (c.moveToNext());
                }
                db.close();

                list2 = new ArrayAdapter<String>(CompareWithMySchedule.this, android.R.layout.simple_spinner_dropdown_item, room){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TypefaceUtil.setGlobalFont(this.getContext(), parent);
                        return super.getView(position, convertView, parent);
                    }
                    @Override
                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                        View v =super.getDropDownView(position, convertView, parent);

                        Typeface externalFont=Typeface.createFromAsset(getAssets(), "210APPL.ttf");
                        ((TextView) v).setTypeface(externalFont);

                        return v;
                    }
                };
                spin2.setAdapter(list2);
                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                        select[1] = room.get(position);
                        click_count=0;
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                list2.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void initTable() {
       //remove all textViews
        layoutMon.removeAllViews();
        layoutTue.removeAllViews();
        layoutWed.removeAllViews();
        layoutThu.removeAllViews();
        layoutFri.removeAllViews();

        start[0] = earliest;
        start[1] = earliest;
        start[2] = earliest;
        start[3] = earliest;
        start[4] = earliest;
    }

    //Temporary class for inputting tables
    public ArrayList<TimeTable> initArrayList(String info, ArrayList<TimeTable> tt) {
        tt.clear();
        db.open();
        Cursor c = db.getAllClassroom();
        if (c.moveToFirst()) {
            do {
                if ((c.getString(c.getColumnIndex("building")) + "-" + c.getString(c.getColumnIndex("classroom"))).equals(info)) {
                    TimeTable t = new TimeTable();
                    t.setCourseId(c.getString(c.getColumnIndex("sid")));
                    t.setSubject(c.getString(c.getColumnIndex("subject")));
                    t.setProf(c.getString(c.getColumnIndex("prof")));
                    t.setTime(c.getString(c.getColumnIndex("time")));
                    t.setClassroom(c.getString((c.getColumnIndex("building"))) + "-" + c.getString((c.getColumnIndex("classroom"))));
                    checkingDate(t, c);
                    tt.add(t);
                }
            } while (c.moveToNext());
        }
        db.close();

        //display arraylist values
        /*
        for (TimeTable e : tt) {
            Log.w("TT", e.getSubject() + " " + e.getStart() + " " + e.getEnd() + " " + e.getDay() + " ");
        }*/
        arraySort();
        return tt;
    }

    public void checkingDate(TimeTable cur, Cursor cur_c) {
        String timeString = cur_c.getString(cur_c.getColumnIndex("time"));
        String arr[] = timeString.split(" ,");
        String date = arr[0].substring(0, 1);

        cur.setDay(date);

        //월1, 화2, 수3 형식일 때, 요일 별로 string 쪼개서 새 객체 생성한 뒤 저장
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].substring(0, 1).equals(date)) {//월1, 화2처럼 다를떄
                cur.setTime(timeString.substring(0, timeString.indexOf(arr[i].substring(0, 1))));//original 객체에 월1 저장
                timeString = timeString.substring(timeString.indexOf(arr[i].substring(0, 1)), timeString.length());//회2, 수3으로 저장
                date = arr[i].substring(0, 1);//새로운 요일 값으로 설정->화

                TimeTable new_t = new TimeTable();
                new_t.setCourseId(cur.getCourseID());
                new_t.setSubject(cur.getSubject());
                new_t.setProf(cur.getProf());
                new_t.setClassroom(cur.getClassroom());

                new_t.setDay(date);
                new_t.setTime(timeString);
                tt.add(new_t);

                cur = new_t;
            }
        }
    }

    //sorting arraylist in order of starting time
    public void arraySort() {
        Comparator<TimeTable> comp = new Comparator<TimeTable>() {
            @Override
            public int compare(TimeTable t1, TimeTable t2) {
                return (t1.getStart()) > (t2.getStart()) ? 1 : -1;
            }
        };
        Collections.sort(tt, comp);

        //checking order of arraylist<timetable>
        /*
        String str = "";
        for(int i=0; i<tt.size(); i++) {
            str += tt.get(i).getSubject();
        }
        Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();
        */
    }

    //creating first row
    public void setFirstRow() {
        window_flag=1;
        linearForTime.removeAllViews();;
        time = new TextView[(latest - earliest) / 100 + 1];
        l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight, 1);

        TextView temp = new TextView(this);
        temp.setText("");
        LinearLayout.LayoutParams l_blank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, timeHeight, 1);
        linearForTime.addView(temp, l_blank);

        for (int i = 0; i < time.length; i++) {
            time[i] = new TextView(this);
            time[i].setTypeface(Typeface.createFromAsset(getAssets(), "210APPL.ttf"));
            time[i].setText((earliest / 100 + i) + "");
            time[i].setTextColor(Color.WHITE);
            linearForTime.addView(time[i], l);
        }
    }

    //fill the timetable for calculating location and height of each subject
    public void makeTable() {
        LinearLayout.LayoutParams lForBlank;
        TextView blank; // blank between subjects
        TextView tempTable;
        Iterator<TimeTable> iterator = tt.iterator();

        initTable();
        if (tt.size() > 1)
            arraySort();

        while (iterator.hasNext()) {
            TimeTable t = iterator.next();
            if (t.getDraw() == 0) {
                tempTable = new TextView(this);
                //tempTable.setText(t.getSubject() + "\n" + t.getClassroom());
                tempTable.setBackgroundColor(Color.argb(170, 255, 255, 255));
                tempTable.setId(Integer.parseInt(t.getCourseID()));

                int hour = ((t.getEnd() - t.getStart())) / 100 * hourHeight;
                int minute = ((t.getEnd() - t.getStart())) % 100 * hourHeight / 100;
                l = new LinearLayout.LayoutParams(timeWidth, hour + minute);

                //calculating location and height of each subject
                switch (t.getDay()) {
                    case "월":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[0]) / 100 * hourHeight) + (t.getStart() - start[0]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutMon.addView(blank, lForBlank);
                        layoutMon.addView(tempTable, l);
                        start[0] = t.getEnd();
                        t.setDraw(1);
                        t.setTextView(tempTable);
                        break;
                    case "화":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[1]) / 100 * hourHeight) + (t.getStart() - start[1]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutTue.addView(blank, lForBlank);
                        layoutTue.addView(tempTable, l);
                        start[1] = t.getEnd();
                        break;
                    case "수":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[2]) / 100 * hourHeight) + (t.getStart() - start[2]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutWed.addView(blank, lForBlank);
                        layoutWed.addView(tempTable, l);
                        start[2] = t.getEnd();
                        break;
                    case "목":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[3]) / 100) * hourHeight + (t.getStart() - start[3]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutThu.addView(blank, lForBlank);
                        layoutThu.addView(tempTable, l);
                        start[3] = t.getEnd();
                        break;
                    case "금":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[4]) / 100 * hourHeight) + (t.getStart() - start[4]) % 100 * hourHeight / 100);
                        blank = new TextView(this);
                        layoutFri.addView(blank, lForBlank);
                        layoutFri.addView(tempTable, l);
                        start[4] = t.getEnd();
                        break;
                }
            }
        }
    }
}