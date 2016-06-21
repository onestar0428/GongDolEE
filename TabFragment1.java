package example.com.mptermui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class TabFragment1 extends Fragment implements View.OnClickListener {
    private View rootView;
    ClassroomDB db;

    ArrayList<TimeTable> mtt = new ArrayList<TimeTable>(); // list for data from data
    TextView[] time;
    int latest = 1700; //initialize latest time
    int[] start = {earliest, earliest, earliest, earliest, earliest};
    static int earliest = 800;
    int hourHeight, timeWidth, timeHeight;

    LinearLayout.LayoutParams l;
    LinearLayout linearForTime, forTimeWidth, timeTable;
    GridLayout layoutMon, layoutTue, layoutWed, layoutThu, layoutFri;
    TextView textMon, textTue, textWed, textThu, textFri;
    int BGcolor, initial_flag=0;

    SharedPreferences prefs;
    final int mode = Activity.MODE_PRIVATE;
    SharedPreferences.Editor edit;
    final String MYPREFS = "MyPreferences_001";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.tab_fragment_1, container, false);
      //  font = Typeface.createFromAsset(getActivity().getAssets(), "210APPL.ttf");

        prefs = getActivity().getSharedPreferences(MYPREFS, mode);
        BGcolor = prefs.getInt("Color", 0);
        setHasOptionsMenu(true);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        forTimeWidth = (LinearLayout) rootView.findViewById(R.id.forTimeWidth);
        linearForTime = (LinearLayout) rootView.findViewById(R.id.time);
        layoutMon = (GridLayout) rootView.findViewById(R.id.monday);
        layoutTue = (GridLayout) rootView.findViewById(R.id.tuesday);
        layoutWed = (GridLayout) rootView.findViewById(R.id.wednesday);
        layoutThu = (GridLayout) rootView.findViewById(R.id.thursday);
        layoutFri = (GridLayout) rootView.findViewById(R.id.friday);
        timeTable = (LinearLayout) rootView.findViewById(R.id.table);



        textMon = (TextView) rootView.findViewById(R.id.t1_mon);
        textTue = (TextView) rootView.findViewById(R.id.t1_tue);
        textWed = (TextView) rootView.findViewById(R.id.t1_wed);
        textThu = (TextView) rootView.findViewById(R.id.t1_thu);
        textFri = (TextView) rootView.findViewById(R.id.t1_fri);

        timeTable.setOnClickListener(this);

        return rootView;
    }

    //for add in Option menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tab1, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tab1:
                /*
                Iterator<TimeTable> iterator = mtt.iterator();

                while (iterator.hasNext()) {
                    TimeTable temp = iterator.next();
                    temp.getTextView().setText("");
                }*/
                Intent intent = new Intent(getActivity(), CompareWithMySchedule.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle myData = new Bundle();
                myData.putInt("latest", latest);
                myData.putInt("hourHeight", hourHeight);
                myData.putInt("timeHeight", timeHeight);
                intent.putExtras(myData);
                startActivityForResult(intent, 103);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //onWindowFocusChanged 대신 사용 (Fragment에선)
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            hourHeight = (linearForTime.getMeasuredHeight()) / time.length;
            hourHeight = time[0].getMeasuredHeight();
            timeWidth = forTimeWidth.getMeasuredWidth() / 5;
            timeHeight = forTimeWidth.getMeasuredHeight();

            if(initial_flag==0 && hourHeight!=0){
                makeTable();
                initial_flag=1;
            }
        }
    };

    //myschedule.java onCreate()
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new ClassroomDB(getActivity());
        db.open();
        Cursor c = db.getAllClassroom();
        if (c.getCount() == 0) {
            db.insertContact("06480001", "데이터마이닝 (원어강의)", "정옥란", "금A ,금B", "IT대학", "412","3");
            db.insertContact("10176001", "컴퓨터그래픽스 (원어강의)", "최진우", "화4 ,화5 ,화6 ,화7", "IT대학", "304","3");
            db.insertContact("10176002", "컴퓨터그래픽스 (원어강의)", "최진우", "목4 ,목5 ,목6 ,목7", "IT대학", "304","3");
            db.insertContact("10177001", "소프트웨어공학 (원어강의)", "최아영", "금A ,금B", "IT대학", "304","3");
            db.insertContact("10178001", "모바일프로그래밍 (원어강의)", "최재혁", "월3 ,월4 ,수2 ,수3", "IT대학", "412", "3");
            db.insertContact("10178002", "모바일프로그래밍 (원어강의)", "최재혁", "월5 ,월6 ,수5 ,수6", "IT대학", "412", "3");
            db.insertContact("10375001", "경영학원론", "김학진", "월7 ,월8 ,월9", "IT대학", "305", "3");
            db.insertContact("10622001", "기술경영", "조형래", "수6 ,수7 ,수8", "IT대학", "305","3");
            db.insertContact("11649001", "졸업작품3(캡스톤디자인) (팀티칭)", "최재혁", "수9 ,수10", "IT대학", "305","1");
            db.insertContact("11852001", "졸업작품1 (캡스톤디자인)(팀티칭)", "정옥란", "목9 ,목10", "IT대학", "304","1");
            db.insertContact("11853001", "소프트웨어산업세미나", "이전영", "수8 ,수9 ,수10", "IT대학", "601","2");
            db.insertContact("09031001", "멀티미디어및실습 (원어강의)", "정용주", "화1 ,화2 ,화3 ,화4", "IT대학", "305","3");
            db.insertContact("09031002", "멀티미디어및실습 (원어강의)", "정용주", "목1 ,목2 ,목3 ,목4", "IT대학", "305","3");
            db.insertContact("11653002", "에듀에코세미나 (1)", "유준", "월2", "IT대학", "305","1");
            db.insertContact("06440001", "자료구조및실습 (원어강의)", "김원", "목1 ,목2 ,목3 ,목4", "IT대학", "601","3");
            db.insertContact("06440002", "자료구조및실습 (원어강의)", "김원", "금1 ,금2 ,금3 ,금4", "IT대학", "601","3");
            db.insertContact("09392001", "이산수학 (원어강의)", "최재혁", "월E ,금D", "IT대학", "412","3");
            db.insertContact("09392002", "이산수학 (원어강의)", "유준", "수C ,금D", "IT대학", "413","3");
            db.insertContact("09805001", "객체지향프로그래밍 (원어강의)", "노웅기", "월1 ,월2 ,월3 ,월4", "IT대학", "601","3");
            db.insertContact("09805002", "객체지향프로그래밍 (원어강의)", "노웅기", "수1 ,수2 ,수3 ,수4", "IT대학", "601","3");
            db.insertContact("09806001", "운영체제 (원어강의)", "유준", "수2 ,수3 ,금2 ,금3", "IT대학", "413","3");
            db.insertContact("09806002", "운영체제 (원어강의)", "유준", "화6 ,화7 ,목6 ,목7", "IT대학", "412","3");
            db.insertContact("11672001", "컴퓨터프로그래밍 (원어강의)", "최아영", "화2 ,화3 ,목2 ,목3", "IT대학", "412","3");
            db.insertContact("11672002", "컴퓨터프로그래밍 (원어강의)", "최아영", "화5 ,화6 ,목5 ,목6", "IT대학", "601","3");
            db.insertContact("09836002", "경제학개론", "윤종문", "금10 ,금11", "IT대학", "304","2");
        }
        db.close();

        setFirstRow();
        getSavedData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == timeTable.getId()) {
            Intent intent = new Intent(getActivity(), Pop.class);
            Bundle myData = new Bundle();
            intent.putExtras(myData);
            startActivityForResult(intent, 101);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //try {
        //handling activity result for making tables
        if ((requestCode == 101) && (resultCode == Activity.RESULT_OK)) {
            // Bundle myResults = data.getExtras();
            String result = data.getExtras().getString("selected");
            initArrayList(result);
        }

        //handling activity result for deleting table
        if ((requestCode == 102) && (resultCode == Activity.RESULT_OK)) {
            Bundle myResults = data.getExtras();
            String id = myResults.getString("table2");
            Iterator<TimeTable> iterator = mtt.iterator();

            while (iterator.hasNext()) {//for array mtt
                TimeTable tt = iterator.next();
                //Toast.makeText(getApplicationContext(), tt.getSubject() + " " + s + " " + tt.getDay(), Toast.LENGTH_SHORT).show();
                if (tt.getCourseID().equals(id)) {//intent result로 받은 값이 그려진 테이블의 과목과 일치하면 지운다
                    switch (tt.getDay()) {
                        case "월":
                            layoutMon.removeView(tt.getTextView());
                            start[0] = earliest;
                            break;
                        case "화":
                            layoutTue.removeView(tt.getTextView());
                            start[1] = earliest;
                            break;
                        case "수":
                            layoutWed.removeView(tt.getTextView());
                            start[2] = earliest;
                            break;
                        case "목":
                            layoutThu.removeView(tt.getTextView());
                            start[3] = earliest;
                            break;
                        case "금":
                            layoutFri.removeView(tt.getTextView());
                            start[4] = earliest;
                            break;
                    }
                    iterator.remove();
                }
            }
            setFirstRow();
            makeTable();
        }

        //handling activity result for making tables
        if ((requestCode == 103) && (resultCode == Activity.RESULT_OK)) {
            Iterator iterator = mtt.iterator();
            while (iterator.hasNext()) {
                TimeTable temp = (TimeTable)iterator.next();
                temp.getTextView().setText(temp.getSubject() + "\n" + temp.getClassroom());
                makeTable();
            }
        }
// } catch (Exception e) {
//    Log.e("d", e+"");
//  }
    }

    //Get data from sharedpreference
    public void getSavedData() {
        if ((String) prefs.getString("mySubject", null) != null) {
            try {
                JSONArray array = new JSONArray((String) prefs.getString("mySubject", null));
                mtt.clear();
                db.open();
                Cursor cl = db.getAllClassroom();

                for (int i = 0; i < array.length(); i++) {
                    String s = array.optString(i);
                    if (cl.moveToFirst()) {
                        Log.w("sid", s);
                        do {
                            if (cl.getString(cl.getColumnIndex("sid")).equals(s)) {
                                TimeTable t = new TimeTable();
                                t.setSubject(cl.getString(cl.getColumnIndex("subject")));
                                t.setProf(cl.getString(cl.getColumnIndex("prof")));
                                t.setTime(cl.getString(cl.getColumnIndex("time")));
                                t.setBuilding(cl.getString(cl.getColumnIndex("building")));
                                t.setRoom(cl.getString(cl.getColumnIndex("classroom")));
                                t.setClassroom(cl.getString(cl.getColumnIndex("building")) + "-" + cl.getString(cl.getColumnIndex("classroom")));
                                t.setCourseId(cl.getString(cl.getColumnIndex("sid")));
                                checkingDate(cl.getString(cl.getColumnIndex("time")), t);
                                mtt.add(t);

                                checkLatestTime();
                            }
                        } while (cl.moveToNext());
                    }
                }
                db.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Temporary class for inputting tables
    public void initArrayList(String s) {
        String arr[] = s.split("&");
        TimeTable t = new TimeTable();
        t.setSubject(arr[0]);
        t.setProf(arr[1]);
        t.setTime(arr[2]);
        t.setBuilding(arr[3]);
        t.setRoom(arr[4]);
        t.setClassroom(arr[3] + "-" + arr[4]);
        t.setCourseId(arr[5]);
        checkingDate(arr[2], t);
        mtt.add(t);

        checkLatestTime();
        makeTable();
    }

    public void checkingDate(String arr, TimeTable cur) {
        String d[] = arr.split(" ,");//월1/화2/수3으로 분리
        String date = d[0];//월1
        String day = d[0].substring(0, 1);//월

        cur.setDay(day);
        cur.setTime(arr);

        //월1, 화2, 수3 형식일 때, 요일 별로 string 쪼개서 새 객체 생성한 뒤 저장
        for (int i = 0; i < d.length; i++) {
            if (!d[i].substring(0, 1).equals(day)) {//월1, 화2처럼 다를떄
                cur.setTime(date);//original 객체에 월1 저장

                day = d[i].substring(0, 1);
                TimeTable new_t = new TimeTable();
                new_t.setCourseId(cur.getCourseID());
                new_t.setSubject(cur.getSubject());
                new_t.setProf(cur.getProf());
                new_t.setClassroom(cur.getClassroom());

                new_t.setDay(day);
                new_t.setTime(arr.substring(arr.indexOf(day), arr.length()));
                date = arr.substring(arr.indexOf(day), arr.length());
                //checkingDate(date.split(" ,"), new_t);
                mtt.add(new_t);

                cur = new_t;
            } else if (i != 0)
                date += " ," + d[i];
        }
    }

    public void arraySort() {
        Comparator<TimeTable> comp = new Comparator<TimeTable>() {
            @Override
            public int compare(TimeTable t1, TimeTable t2) {
                return (t1.getStart()) >= (t2.getStart()) ? 1 : -1;
            }
        };
        Collections.sort(mtt, comp);
    }

    public void checkLatestTime() {
        latest = 1700;
        for (TimeTable e : mtt) {
            if (e.getEnd() > latest) {
                latest = e.getEnd() + 100;
                setFirstRow();
            }
        }
    }

    public void setFirstRow() {
        linearForTime.removeAllViews();
        time = new TextView[(latest - earliest) / 100 + 1];
        l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight, 1);

        TextView temp = new TextView(getActivity());
        LinearLayout.LayoutParams l_blank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, timeHeight, 1);
        linearForTime.addView(temp, l_blank);

        for (int i = 0; i < time.length; i++) {
            time[i] = new TextView(getActivity());
          //  time[i].setTypeface(font);
            time[i].setText(Integer.toString(earliest / 100 + i));
            time[i].setTextColor(Color.WHITE);
            linearForTime.addView(time[i], l);
        }
    }

    public void initTable() {
        Iterator<TimeTable> iterator = mtt.iterator();

        layoutMon.removeAllViews();
        layoutTue.removeAllViews();
        layoutWed.removeAllViews();
        layoutThu.removeAllViews();
        layoutFri.removeAllViews();

        while (iterator.hasNext()) {
            TimeTable t = iterator.next();
            t.setDraw(0);
        }
        start[0] = earliest;
        start[1] = earliest;
        start[2] = earliest;
        start[3] = earliest;
        start[4] = earliest;
    }

    //fill the timetable for calculating location and height of each subject
    public void makeTable() {
        //hourHeight = time[0].getHeight()/((latest - earliest)/100 + 1);
        LinearLayout.LayoutParams lForBlank;
        TextView blank; // blank between subjects
        TextView tempTable;
        Iterator<TimeTable> iterator = mtt.iterator();

        initTable();
        if (mtt.size() > 1)
            arraySort();

        while (iterator.hasNext()) {
            TimeTable t = iterator.next();
            if (t.getDraw() == 0) {
                tempTable = new TextView(getActivity());
                tempTable.setText(t.getSubject() + "\n" + t.getClassroom());
                tempTable.setBackgroundColor(Color.WHITE);
                tempTable.setId(Integer.parseInt(t.getCourseID()));
                tempTable.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "210APPL.ttf"));
                tempTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(getActivity(), Pop2.class);
                        Bundle b = new Bundle();
                        String id = v.getId()+"";
                        if(id.length()==7)
                            id = "0"+id;
                        b.putString("table",id);
                        mIntent.putExtras(b);
                        startActivityForResult(mIntent, 102);
                    }
                });

                int hour = ((t.getEnd() - t.getStart())) / 100 * hourHeight;
                int minute = ((t.getEnd() - t.getStart())) % 100 * hourHeight / 100;
                l = new LinearLayout.LayoutParams(timeWidth, hour + minute);

                //calculating location and height of each subject
                switch (t.getDay()) {
                    case "월":
                        if (t.getStart() > start[0]) {
                            lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    ((t.getStart() - start[0]) / 100 * hourHeight) + (t.getStart() - start[0]) % 100 * hourHeight / 100);
                            blank = new TextView(getActivity());
                            layoutMon.addView(blank, lForBlank);
                            layoutMon.addView(tempTable, l);
                            start[0] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getActivity(), "중복입니다!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                    case "화":
                        if (t.getStart() > start[1]) {
                            lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    ((t.getStart() - start[1]) / 100 * hourHeight) + (t.getStart() - start[1]) % 100 * hourHeight / 100);
                            blank = new TextView(getActivity());
                            layoutTue.addView(blank, lForBlank);
                            layoutTue.addView(tempTable, l);
                            start[1] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getActivity(), "중복입니다!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                    case "수":
                        if (t.getStart() > start[2]) {
                            lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    ((t.getStart() - start[2]) / 100 * hourHeight) + (t.getStart() - start[2]) % 100 * hourHeight / 100);
                            blank = new TextView(getActivity());
                            layoutWed.addView(blank, lForBlank);
                            layoutWed.addView(tempTable, l);
                            start[2] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getActivity(), "중복입니다!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                    case "목":
                        if (t.getStart() > start[3]) {
                            lForBlank = new LinearLayout.LayoutParams(timeWidth,
                                    ((t.getStart() - start[3]) / 100) * hourHeight + (t.getStart() - start[3]) % 100 * hourHeight / 100);
                            blank = new TextView(getActivity());
                            layoutThu.addView(blank, lForBlank);
                            layoutThu.addView(tempTable, l);
                            start[3] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getActivity(), "중복입니다!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                    case "금":
                        if (t.getStart() > start[4]) {
                            lForBlank = new LinearLayout.LayoutParams(timeWidth,
                                    ((t.getStart() - start[4]) / 100 * hourHeight) + (t.getStart() - start[4]) % 100 * hourHeight / 100);
                            blank = new TextView(getActivity());
                            layoutFri.addView(blank, lForBlank);
                            layoutFri.addView(tempTable, l);
                            start[4] = t.getEnd();
                            t.setDraw(1);
                            t.setTextView(tempTable);
                        } else {
                            Toast.makeText(getActivity(), "중복입니다!", Toast.LENGTH_SHORT).show();
                            iterator.remove();
                        }
                        break;
                }
            }
        }
        savePreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        prefs = getActivity().getSharedPreferences(MYPREFS, 0);
        BGcolor = prefs.getInt("Color", 0);
        setBGcolor();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        savePreferences();
    }

    //create the shared preferences object
    protected void savePreferences() {
        //sh_Pref = getActivity().getSharedPreferences(MYPREFS, mode);
        edit = prefs.edit();

        JSONArray array = new JSONArray();
        for (int i = 0; i < mtt.size(); i++) {
            int flag = 0;

            for (int j = 0; j < array.length(); j++) {
                if (mtt.get(i).getCourseID().equals(array.optString(j))) {
                    flag = 1;
                    continue;
                }
            }
            if (flag == 0) {
                array.put(mtt.get(i).getCourseID());
            }
        }
        edit.putString("mySubject", array.toString());
        edit.commit();
    }//savePreferences

    public void setBGcolor() {
        switch (BGcolor) {
            case 1:
                layoutMon.setBackgroundResource(R.color.t1_2);
                layoutTue.setBackgroundResource(R.color.t1_2);
                layoutWed.setBackgroundResource(R.color.t1_2);
                layoutThu.setBackgroundResource(R.color.t1_2);
                layoutFri.setBackgroundResource(R.color.t1_2);


                textMon.setBackgroundResource(R.color.t1_1);
                textTue.setBackgroundResource(R.color.t1_1);
                textWed.setBackgroundResource(R.color.t1_1);
                textThu.setBackgroundResource(R.color.t1_1);
                textFri.setBackgroundResource(R.color.t1_1);

                linearForTime.setBackgroundResource(R.color.t1_1);

                break;
            case 2:
                layoutMon.setBackgroundResource(R.color.t2_2);
                layoutTue.setBackgroundResource(R.color.t2_2);
                layoutWed.setBackgroundResource(R.color.t2_2);
                layoutThu.setBackgroundResource(R.color.t2_2);
                layoutFri.setBackgroundResource(R.color.t2_2);


                textMon.setBackgroundResource(R.color.t2_1);
                textTue.setBackgroundResource(R.color.t2_1);
                textWed.setBackgroundResource(R.color.t2_1);
                textThu.setBackgroundResource(R.color.t2_1);
                textFri.setBackgroundResource(R.color.t2_1);

                linearForTime.setBackgroundResource(R.color.t2_1);

                break;
            case 3:
                layoutMon.setBackgroundResource(R.color.t3_2);
                layoutTue.setBackgroundResource(R.color.t3_2);
                layoutWed.setBackgroundResource(R.color.t3_2);
                layoutThu.setBackgroundResource(R.color.t3_2);
                layoutFri.setBackgroundResource(R.color.t3_2);


                textMon.setBackgroundResource(R.color.t3_1);
                textTue.setBackgroundResource(R.color.t3_1);
                textWed.setBackgroundResource(R.color.t3_1);
                textThu.setBackgroundResource(R.color.t3_1);
                textFri.setBackgroundResource(R.color.t3_1);

                linearForTime.setBackgroundResource(R.color.t3_1);

                break;
            case 4:
                layoutMon.setBackgroundResource(R.color.t4_2);
                layoutTue.setBackgroundResource(R.color.t4_2);
                layoutWed.setBackgroundResource(R.color.t4_2);
                layoutThu.setBackgroundResource(R.color.t4_2);
                layoutFri.setBackgroundResource(R.color.t4_2);


                textMon.setBackgroundResource(R.color.t4_1);
                textTue.setBackgroundResource(R.color.t4_1);
                textWed.setBackgroundResource(R.color.t4_1);
                textThu.setBackgroundResource(R.color.t4_1);
                textFri.setBackgroundResource(R.color.t4_1);

                linearForTime.setBackgroundResource(R.color.t4_1);
                break;
            case 5:
                layoutMon.setBackgroundResource(R.color.t5_1);
                layoutTue.setBackgroundResource(R.color.t5_1);
                layoutWed.setBackgroundResource(R.color.t5_1);
                layoutThu.setBackgroundResource(R.color.t5_1);
                layoutFri.setBackgroundResource(R.color.t5_1);


                textMon.setBackgroundResource(R.color.t5_2);
                textTue.setBackgroundResource(R.color.t5_2);
                textWed.setBackgroundResource(R.color.t5_2);
                textThu.setBackgroundResource(R.color.t5_2);
                textFri.setBackgroundResource(R.color.t5_2);

                linearForTime.setBackgroundResource(R.color.t5_2);
                break;
            case 6:
                layoutMon.setBackgroundResource(R.color.t6_1);
                layoutTue.setBackgroundResource(R.color.t6_1);
                layoutWed.setBackgroundResource(R.color.t6_1);
                layoutThu.setBackgroundResource(R.color.t6_1);
                layoutFri.setBackgroundResource(R.color.t6_1);


                textMon.setBackgroundResource(R.color.t6_2);
                textTue.setBackgroundResource(R.color.t6_2);
                textWed.setBackgroundResource(R.color.t6_2);
                textThu.setBackgroundResource(R.color.t6_2);
                textFri.setBackgroundResource(R.color.t6_2);


                linearForTime.setBackgroundResource(R.color.t6_2);
                break;
            case 7:
                layoutMon.setBackgroundResource(R.color.t7_2);
                layoutTue.setBackgroundResource(R.color.t7_2);
                layoutWed.setBackgroundResource(R.color.t7_2);
                layoutThu.setBackgroundResource(R.color.t7_2);
                layoutFri.setBackgroundResource(R.color.t7_2);


                textMon.setBackgroundResource(R.color.t7_1);
                textTue.setBackgroundResource(R.color.t7_1);
                textWed.setBackgroundResource(R.color.t7_1);
                textThu.setBackgroundResource(R.color.t7_1);
                textFri.setBackgroundResource(R.color.t7_1);


                linearForTime.setBackgroundResource(R.color.t7_1);
                break;
            case 8:
                layoutMon.setBackgroundResource(R.color.t8_2);
                layoutTue.setBackgroundResource(R.color.t8_2);
                layoutWed.setBackgroundResource(R.color.t8_2);
                layoutThu.setBackgroundResource(R.color.t8_2);
                layoutFri.setBackgroundResource(R.color.t8_2);


                textMon.setBackgroundResource(R.color.t8_3);
                textTue.setBackgroundResource(R.color.t8_3);
                textWed.setBackgroundResource(R.color.t8_3);
                textThu.setBackgroundResource(R.color.t8_3);
                textFri.setBackgroundResource(R.color.t8_3);


                linearForTime.setBackgroundResource(R.color.t8_3);
                break;
            case 9:
                layoutMon.setBackgroundResource(R.color.t9_1);
                layoutTue.setBackgroundResource(R.color.t9_1);
                layoutWed.setBackgroundResource(R.color.t9_1);
                layoutThu.setBackgroundResource(R.color.t9_1);
                layoutFri.setBackgroundResource(R.color.t9_1);


                textMon.setBackgroundResource(R.color.t9_3);
                textTue.setBackgroundResource(R.color.t9_3);
                textWed.setBackgroundResource(R.color.t9_3);
                textThu.setBackgroundResource(R.color.t9_3);
                textFri.setBackgroundResource(R.color.t9_3);


                linearForTime.setBackgroundResource(R.color.t9_3);
                break;

            case 10:
                layoutMon.setBackgroundResource(R.color.t10_1);
                layoutTue.setBackgroundResource(R.color.t10_1);
                layoutWed.setBackgroundResource(R.color.t10_1);
                layoutThu.setBackgroundResource(R.color.t10_1);
                layoutFri.setBackgroundResource(R.color.t10_1);


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

}