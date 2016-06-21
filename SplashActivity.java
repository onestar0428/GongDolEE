package example.com.mptermui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by jy on 2016-06-09.
 */
public class SplashActivity extends AppCompatActivity {
    private static String TAG = SplashActivity.class.getName();
    private static long SLEEP_TIME = 1; // Sleep for some time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Removes title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
// Removes notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
// Start timer and launch main activity

        /*
        Resources res = getResources();
        TransitionDrawable transition = (TransitionDrawable) res.getDrawable(R.drawable.transition);
        ImageView image = (ImageView) findViewById(R.id.view_transition_drawable);
        image.setImageDrawable(transition);
        //transition.startTransition(1000); // transition will be run for 1 second
*/

        ImageView img = (ImageView) findViewById(R.id.view_transition_drawable);
        BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.g1);
        BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.g2);

       AnimationDrawable mframeAnimation;
        int reasonableDuration = 250;
        mframeAnimation = new AnimationDrawable();
        mframeAnimation.setOneShot(false); // loop continuously
        mframeAnimation.addFrame(frame1, reasonableDuration);
        mframeAnimation.addFrame(frame2, reasonableDuration);
        img.setBackgroundDrawable(mframeAnimation);
        mframeAnimation.setVisible(true, true);
        mframeAnimation.start();
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }

    private class IntentLauncher extends Thread {
        @Override
/**
 * Sleep for some time and than start new activity.
 */
        public void run() {
            try {
// Sleeping
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
// Start main activity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}