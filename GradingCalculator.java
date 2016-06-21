package example.com.mptermui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;

public class GradingCalculator extends AppCompatActivity implements View.OnClickListener {
    ClassroomDB db;

    Spinner[] spin = new Spinner[11];
    String[] grade;
    SharedPreferences sh_Pref;
    int subjectCnt = 0;
    TextView[] subject = new TextView[11];
    TextView[] score = new TextView[11];
    final String MYPREFS = "MyPreferences_001";
    ArrayList<String> mySubject = new ArrayList<String>();
    Button calBtn;
    TextView totalGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading_calculator);

        subject[1] = (TextView) findViewById(R.id.subject1);
        subject[2] = (TextView) findViewById(R.id.subject2);
        subject[3] = (TextView) findViewById(R.id.subject3);
        subject[4] = (TextView) findViewById(R.id.subject4);
        subject[5] = (TextView) findViewById(R.id.subject5);
        subject[6] = (TextView) findViewById(R.id.subject6);
        subject[7] = (TextView) findViewById(R.id.subject7);
        subject[8] = (TextView) findViewById(R.id.subject8);
        subject[9] = (TextView) findViewById(R.id.subject9);
        subject[10] = (TextView) findViewById(R.id.subject10);

        score[1] = (TextView) findViewById(R.id.score1);
        score[2] = (TextView) findViewById(R.id.score2);
        score[3] = (TextView) findViewById(R.id.score3);
        score[4] = (TextView) findViewById(R.id.score4);
        score[5] = (TextView) findViewById(R.id.score5);
        score[6] = (TextView) findViewById(R.id.score6);
        score[7] = (TextView) findViewById(R.id.score7);
        score[8] = (TextView) findViewById(R.id.score8);
        score[9] = (TextView) findViewById(R.id.score9);
        score[10] = (TextView) findViewById(R.id.score10);

        totalGrade = (TextView) findViewById(R.id.totalGrade);

        db = new ClassroomDB(this);
        db.open();
     /*   Cursor c = db.getAllClassroom();

        db.close();*/

        sh_Pref = getSharedPreferences(MYPREFS, 0);
        try {
            int j = 1;
            JSONArray array = new JSONArray((String) sh_Pref.getString("mySubject", null));

            db.open();
            Cursor c = db.getAllClassroom();
            if (c.moveToFirst()) {
                while (c.moveToNext()) {
                    Log.w("db", c.getString(c.getColumnIndex("sid")));
                }
            }
            subjectCnt=array.length();
            for (int i = 0; i < subjectCnt; i++) {
                String r = array.optString(i);

                Cursor cl = db.getAllClassroom();

                if(cl.moveToFirst()) {
                    do {
                        if (j >= 1 && j <= array.length() && cl.getString(cl.getColumnIndex("sid")).equals(r)) {
                            Log.w("loSS", " " + cl.getString(1) + "    " + r + " J J J " + j);
                                subject[j].setText(cl.getString(cl.getColumnIndex("subject")));
                                score[j].setText(cl.getString(cl.getColumnIndex("score")));
                            j++;
                        }
                    }
                    while (cl.moveToNext());
                }
            }
            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        calBtn = (Button) findViewById(R.id.calBtn);
        spin[1] = (Spinner) findViewById(R.id.grade1);
        spin[2] = (Spinner) findViewById(R.id.grade2);
        spin[3] = (Spinner) findViewById(R.id.grade3);
        spin[4] = (Spinner) findViewById(R.id.grade4);
        spin[5] = (Spinner) findViewById(R.id.grade5);
        spin[6] = (Spinner) findViewById(R.id.grade6);
        spin[7] = (Spinner) findViewById(R.id.grade7);
        spin[8] = (Spinner) findViewById(R.id.grade8);
        spin[9] = (Spinner) findViewById(R.id.grade9);
        spin[10] = (Spinner) findViewById(R.id.grade10);

        grade = getResources().getStringArray(R.array.grade);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, grade);

        for (int i = 1; i < 11; i++) {
            spin[i].setAdapter(adapter);
            spin[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int arg2, long arg3) {
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
        calBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        double total = 0;
        double result = 0;
        double grade = 1;
        for (int i = 1; i <= subjectCnt; i++) {
            switch (spin[i].getSelectedItem().toString()) {
                case "A+":
                    grade = Integer.parseInt(score[i].getText().toString()) * 4.5;
                    break;
                case "A":
                    grade = Integer.parseInt(score[i].getText().toString()) * 4.0;
                    break;
                case "B+":
                    grade = Integer.parseInt(score[i].getText().toString()) * 3.5;
                    break;
                case "B":
                    grade = Integer.parseInt(score[i].getText().toString()) * 3.0;
                    break;
                case "C+":
                    grade = Integer.parseInt(score[i].getText().toString()) * 2.5;
                    break;
                case "C":
                    grade = Integer.parseInt(score[i].getText().toString()) * 2.0;
                    break;
                case "D+":
                    grade = Integer.parseInt(score[i].getText().toString()) * 1.5;
                    break;
                case "D":
                    grade = Integer.parseInt(score[i].getText().toString()) * 1.0;
                    break;
                case "F":
                    grade = Integer.parseInt(score[i].getText().toString());
                    break;
            }
            total += grade;
            result += Integer.parseInt(score[i].getText().toString());
        }

        double tg = total / result;
        String str = "" + Math.round(tg * 100) / 100.0;
        totalGrade.setText(str);
    }
}
