package example.com.mptermui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

/**
 * Created by jy on 2016-06-05
 */
public class Pop2 extends AppCompatActivity {
    Button delete;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow2);
      //  TypefaceUtil.setGlobalFont(this, getWindow().getDecorView());


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        delete = (Button) findViewById(R.id.delete);
        cancel = (Button) findViewById(R.id.cancel);

        delete.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent myLocalIntent = getIntent();
                                          Bundle myBundle = myLocalIntent.getExtras();
                                          String id = myBundle.getString("table");
                                          myBundle.putString("table2", id);
                                          myLocalIntent.putExtras(myBundle);
                                          setResult(Activity.RESULT_OK, myLocalIntent);
                                          finish();
                                      }
                                  }
        );
        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          finish();
                                      }
                                  }
        );

        getWindow().setLayout((int) (width * .90), (int) (height * .2));
    }
}