package example.com.mptermui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TabFragment3 extends Fragment {


    WebView webView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.tab_fragement4, container, false);



        webView = (WebView) rootView.findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);




        if (savedInstanceState == null) {
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);



            webView.loadUrl("http://mlib.gachon.ac.kr");


        } else {
            webView.restoreState(savedInstanceState);
        }

        //뒤로가기 버튼 눌러도 어플 안 꺼지게 함
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //This is the filter
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;


                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        ((MainActivity)getActivity()).onBackPressed();
                    }

                    return true;
                }

                return false;
            }
        });
        return rootView;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        webView.saveState(outState);
    }

    /*
    public static boolean isNetworkStat( Context context ) {
     ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
     NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
     NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
     NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
     boolean blte_4g = false;
    if(lte_4g != null)
       blte_4g = lte_4g.isConnected();
       if( mobile != null ) {
       if (mobile.isConnected() || wifi.isConnected() || blte_4g)
       return true;
       } else {
      if ( wifi.isConnected() || blte_4g )
      return true;
      }
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
      dlg.setTitle("네트워크 오류");
      dlg.setMessage("네트워크 상태를 확인해 주십시요.");
      dlg.setIcon(R.drawable.applogo);
     dlg.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               dialog.dismiss();
               }
        });
        dlg.show();
        return false;
    }*/
}
