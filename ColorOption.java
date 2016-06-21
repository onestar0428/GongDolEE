package example.com.mptermui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ColorOption extends AppCompatActivity   implements View.OnClickListener{
    Button[] btn  = new Button[10];
    TextView copWindow;
    Button copBtn ;
    int lastCheck =0;
    final int mode = Activity.MODE_PRIVATE;

    final String MYPREFS = "MyPreferences_001";
    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_option);
        TypefaceUtil.setGlobalFont(this, getWindow().getDecorView());


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        copBtn = (Button)findViewById(R.id.copBtn);

        btn[0]= (Button)findViewById(R.id.cop1);
        btn[1]= (Button)findViewById(R.id.cop2);
        btn[2]= (Button)findViewById(R.id.cop3);
        btn[3]= (Button)findViewById(R.id.cop4);
        btn[4]= (Button)findViewById(R.id.cop5);
        btn[5]= (Button)findViewById(R.id.cop6);
        btn[6]= (Button)findViewById(R.id.cop7);
        btn[7]= (Button)findViewById(R.id.cop8);
        btn[8]= (Button)findViewById(R.id.cop9);
        btn[9]= (Button)findViewById(R.id.cop10);


        copWindow = (TextView)findViewById(R.id.copWindow);
        for(int i = 0 ; i < 10 ; i ++)
            btn[i].setOnClickListener(this);
        copBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cop1:
                copWindow.setBackgroundResource(R.drawable.theme1);
                lastCheck =1;
                break;
            case R.id.cop2:
                copWindow.setBackgroundResource(R.drawable.theme2);
                lastCheck =2;
                break;
            case R.id.cop3:
                copWindow.setBackgroundResource(R.drawable.theme3);
                lastCheck =3;
                break;
            case R.id.cop4:
                copWindow.setBackgroundResource(R.drawable.theme4);
                lastCheck =4;
                break;
            case R.id.cop5:
                copWindow.setBackgroundResource(R.drawable.theme5);
                lastCheck =5;
                break;
            case R.id.cop6:
                copWindow.setBackgroundResource(R.drawable.theme6);
                lastCheck =6;
                break;
            case R.id.cop7:
                copWindow.setBackgroundResource(R.drawable.theme7);
                lastCheck =7;
                break;
            case R.id.cop8:
                copWindow.setBackgroundResource(R.drawable.theme8);
                lastCheck =8;
                break;
            case R.id.cop9:
                copWindow.setBackgroundResource(R.drawable.theme9);
                lastCheck =9;
                break;

            case R.id.cop10:
                copWindow.setBackgroundResource(R.drawable.theme10);
                lastCheck =10;
                break;
            case R.id.copBtn :
                savePreferences();

                finish();
                break;
            default :
                break;
        }
    }
    protected void savePreferences() {
//create the shared preferences object
        sh_Pref = getSharedPreferences(MYPREFS, mode);
//obtain an editor to add data to (my)SharedPreferences object
        toEdit  = sh_Pref.edit();
//put some <key/value> data in the preferences object
        toEdit.putInt("Color", lastCheck);
        toEdit.commit();
    }//savePreferences
}
