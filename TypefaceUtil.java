package example.com.mptermui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jy on 2016-06-10.
 */
public class TypefaceUtil {

    public static void setGlobalFont(Context context, View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int len = vg.getChildCount();
                for (int i = 0; i < len; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "210APPL.ttf"));
                    }
                    setGlobalFont(context, v);
                }
            }
        } else {
        }
    }
}