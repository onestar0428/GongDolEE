package example.com.mptermui;

/**
 * Created by YY on 2016-05-14.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity    {

    SharedPreferences sh_Pref;
    int BGcolor;

    final int mode = Activity.MODE_PRIVATE;
    final String MYPREFS = "MyPreferences_001";

    Toolbar toolbar;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sh_Pref =getSharedPreferences(MYPREFS, 0);
        BGcolor =    sh_Pref.getInt("Color", 0);
       // TypefaceUtil.setGlobalFont(this, getWindow().getDecorView());

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //fragment 추가 하고싶으면 여기서
        tabLayout.addTab(tabLayout.newTab().setText("개인시간표"));
        tabLayout.addTab(tabLayout.newTab().setText("강의실"));

        tabLayout.addTab(tabLayout.newTab().setText("도서관"));
        tabLayout.addTab(tabLayout.newTab().setText("주변카페"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                   // Typeface mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "210APPL.ttf");
                    //((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setBGcolor(){
        float aph=(float)0.7;
        switch (BGcolor){
            case 1:
                toolbar.setBackgroundResource(R.color.t1_1);
                tabLayout.setBackgroundResource(R.color.t1_1);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;
            case 2:
                toolbar.setBackgroundResource(R.color.t2_3);
                tabLayout.setBackgroundResource(R.color.t2_3);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;
            case 3:
                toolbar.setBackgroundResource(R.color.t3_3);
                tabLayout.setBackgroundResource(R.color.t3_3);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;
            case 4:
                toolbar.setBackgroundResource(R.color.t4_3);
                tabLayout.setBackgroundResource(R.color.t4_3);
                tabLayout.setTabTextColors(Color.LTGRAY, Color.WHITE);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;
            case 5:
                toolbar.setBackgroundResource(R.color.t5_3);
                tabLayout.setBackgroundResource(R.color.t5_3);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;
            case 6:
                toolbar.setBackgroundResource(R.color.t6_3);
                tabLayout.setBackgroundResource(R.color.t6_3);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;
            case 7:
                toolbar.setBackgroundResource(R.color.t7_3);
                tabLayout.setBackgroundResource(R.color.t7_3);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;
            case 8:
                toolbar.setBackgroundResource(R.color.t8_1);
                tabLayout.setBackgroundResource(R.color.t8_1);
                //  tabLayout.setTabTextColors(Color.BLACK, Color.BLUE);
                //toolbar.setTitleTextColor(Color.BLACK);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;
            case 9:
                toolbar.setBackgroundResource(R.color.t9_2);
                tabLayout.setBackgroundResource(R.color.t9_2);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                // toolbar.setPopupTheme(R.style.MyActionBar);
                break;

            case 10:
                toolbar.setBackgroundResource(R.color.t10_3);
                tabLayout.setBackgroundResource(R.color.t10_3);
                tabLayout.setAlpha(aph);
                toolbar.setAlpha(aph);
                break;

            default :
                toolbar.setBackgroundResource(R.color.tool);
                tabLayout.setBackgroundResource(R.color.tool);
                tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        sh_Pref = getSharedPreferences(MYPREFS, 0);
        BGcolor =    sh_Pref.getInt("Color", 0);
        setBGcolor();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //TypefaceUtil.setGlobalFont(MainActivity.getContext(), menu);

       // Typeface externalFont=Typeface.createFromAsset(getAssets(), "210APPL.ttf");
       //((TextView) v).setTypeface(externalFont);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_res:
                startActivity(new Intent(this, Reservation.class));
                return true;
            case R.id.menu_color:
                startActivity(new Intent(this, ColorOption.class));
                return true;
            case R.id.menu_settings3:
                startActivity(new Intent(this, GradingCalculator.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

    }
}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}