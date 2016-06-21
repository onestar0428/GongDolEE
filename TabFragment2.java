package example.com.mptermui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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


public class TabFragment2 extends Fragment {
    private View rootView;
    ClassroomDB db;

    ArrayList<TimeTable> tt = new ArrayList<TimeTable>(); // list for data from data
    ArrayList<String> building = new ArrayList<String>();
    ArrayList<String> room = new ArrayList<String>();
    TextView[] time;
    Spinner spin1;
    Spinner spin2;
    ArrayAdapter<String> list1;
    ArrayAdapter<String> list2;
    Button ok;
    String[] select = new String[2];
    //Typeface font;

    int latest = 1700; //initialize latest time
    static int earliest = 800;
    int hourHeight, timeWidth, timeHeight, window_flag=0; //timeHeight,timeWidth안되서 임의로 값을 적용
    int[] start = {earliest, earliest, earliest, earliest, earliest};

    TextView textMon, textTue, textWed, textThu, textFri;
    LinearLayout.LayoutParams l;
    LinearLayout linearForTime, forTimeWidth,timeTable;
    GridLayout layoutMon, layoutTue, layoutWed, layoutThu, layoutFri;


    SharedPreferences sh_Pref;
    int BGcolor;

    final String MYPREFS = "MyPreferences_001";

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.tab_fragment_2, container, false);
      //  font = Typeface.createFromAsset(getActivity().getAssets(), "210APPL.ttf");

        sh_Pref = getActivity().getSharedPreferences(MYPREFS, 0);
        BGcolor =    sh_Pref.getInt("Color", 0);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        sh_Pref = getActivity().getSharedPreferences(MYPREFS, 0);
        BGcolor =    sh_Pref.getInt("Color", 0);
        setBGcolor();
    }

    //myschedule.java onCreate()
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

        linearForTime = (LinearLayout) rootView.findViewById(R.id.time);
        forTimeWidth = (LinearLayout) rootView.findViewById((R.id.forTimeWidth));
        layoutMon = (GridLayout) rootView.findViewById(R.id.monday);
        layoutTue = (GridLayout) rootView.findViewById(R.id.tuesday);
        layoutWed = (GridLayout) rootView.findViewById(R.id.wednesday);
        layoutThu = (GridLayout) rootView.findViewById(R.id.thursday);
        layoutFri = (GridLayout) rootView.findViewById(R.id.friday);
        ok = (Button) rootView.findViewById(R.id.makeOK);
        timeTable = (LinearLayout) rootView.findViewById(R.id.table);



        textMon = (TextView) rootView.findViewById(R.id.t2_mon);
        textTue = (TextView) rootView.findViewById(R.id.t2_tue);
        textWed = (TextView) rootView.findViewById(R.id.t2_wed);
        textThu = (TextView) rootView.findViewById(R.id.t2_thu);
        textFri = (TextView) rootView.findViewById(R.id.t2_fri);

        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      String result = select[0] + "-" + select[1];
                                      initArrayList(result);
                                      makeTable();
                                  }
                              }
        );

        db = new ClassroomDB(getActivity());
        db.open();
        int flag = 0;
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

    //onWindowFocusChanged 대신 사용 (Fragment에선)
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            hourHeight = (linearForTime.getMeasuredHeight()) / time.length;
            hourHeight = time[0].getMeasuredHeight();
            timeWidth = forTimeWidth.getMeasuredWidth() / 5;
            timeHeight = forTimeWidth.getMeasuredHeight();
        }
    };


    public void makeSpinner() {
        spin1 = (Spinner) rootView.findViewById(R.id.building_spinner);
        spin2 = (Spinner) rootView.findViewById(R.id.room_spinner);

        list1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, building){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
         //       TypefaceUtil.setGlobalFont(this.getContext(), parent);
                return super.getView(position, convertView, parent);
            }
            @Override
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

               // Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), "210APPL.ttf");
           //     ((TextView) v).setTypeface(externalFont);

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

                list2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, room){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                     //   TypefaceUtil.setGlobalFont(this.getContext(), parent);
                        return super.getView(position, convertView, parent);
                    }
                    @Override
                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                        View v =super.getDropDownView(position, convertView, parent);

                      //  Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), "210APPL.ttf");
                        //((TextView) v).setTypeface(externalFont);

                        return v;
                    }
                };
                spin2.setAdapter(list2);
                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                        select[1] = room.get(position);
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
        Iterator<TimeTable> iterator = tt.iterator();

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
    public void initArrayList(String info) {
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

        checkLatestTime();
        arraySort();
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
    }

    //find max value for creating first row of timetable
    public void checkLatestTime() {
        latest = 1700;
        for (TimeTable e : tt) {
            if (e.getEnd() > latest)
                latest = e.getEnd() + 100;
        }
        setFirstRow();
    }

    //creating first row
    public void setFirstRow() {
        //window_flag=1;
        linearForTime.removeAllViews();;
        time = new TextView[(latest - earliest) / 100 + 1];
        l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight, 1);

        TextView temp = new TextView(getActivity());
        temp.setText("");
        LinearLayout.LayoutParams l_blank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, timeHeight, 1);
        linearForTime.addView(temp, l_blank);

        for (int i = 0; i < time.length; i++) {
            time[i] = new TextView(getActivity());
           // time[i].setTypeface(font);
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
                tempTable = new TextView(getActivity());
                tempTable.setText(t.getSubject());
                tempTable.setBackgroundColor(Color.WHITE);
                tempTable.setId(Integer.parseInt(t.getCourseID()));
                tempTable.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "210APPL.ttf"));

                int hour = ((t.getEnd() - t.getStart())) / 100 * hourHeight;
                int minute = ((t.getEnd() - t.getStart())) % 100 * hourHeight / 100;
                l = new LinearLayout.LayoutParams(timeWidth, hour + minute);

                //calculating location and height of each subject
                switch (t.getDay()) {
                    case "월":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[0]) / 100 * hourHeight) + (t.getStart() - start[0]) % 100 * hourHeight / 100);
                        blank = new TextView(getActivity());
                        layoutMon.addView(blank, lForBlank);
                        layoutMon.addView(tempTable, l);
                        start[0] = t.getEnd();
                        t.setDraw(1);
                        t.setTextView(tempTable);
                        break;
                    case "화":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[1]) / 100 * hourHeight) + (t.getStart() - start[1]) % 100 * hourHeight / 100);
                        blank = new TextView(getActivity());
                        layoutTue.addView(blank, lForBlank);
                        layoutTue.addView(tempTable, l);
                        start[1] = t.getEnd();
                        break;
                    case "수":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[2]) / 100 * hourHeight) + (t.getStart() - start[2]) % 100 * hourHeight / 100);
                        blank = new TextView(getActivity());
                        layoutWed.addView(blank, lForBlank);
                        layoutWed.addView(tempTable, l);
                        start[2] = t.getEnd();
                        break;
                    case "목":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[3]) / 100) * hourHeight + (t.getStart() - start[3]) % 100 * hourHeight / 100);
                        blank = new TextView(getActivity());
                        layoutThu.addView(blank, lForBlank);
                        layoutThu.addView(tempTable, l);
                        start[3] = t.getEnd();
                        break;
                    case "금":
                        lForBlank = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ((t.getStart() - start[4]) / 100 * hourHeight) + (t.getStart() - start[4]) % 100 * hourHeight / 100);
                        blank = new TextView(getActivity());
                        layoutFri.addView(blank, lForBlank);
                        layoutFri.addView(tempTable, l);
                        start[4] = t.getEnd();
                        break;
                }
            }
        }
    }

    public void setBGcolor(){
        switch (BGcolor){
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

            default :
                timeTable.setBackgroundResource(R.color.defaultCop);
                break;
        }
    }

}
