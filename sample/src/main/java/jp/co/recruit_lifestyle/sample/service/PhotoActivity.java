package jp.co.recruit_lifestyle.sample.service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import jp.co.recruit.floatingview.R;
import jp.co.recruit_lifestyle.sample.GtSharedPreferences;

import com.adbert.AdbertADView;
import com.adbert.AdbertListener;
import com.adbert.AdbertLoopADView;
import com.adbert.AdbertOrientation;
import com.adbert.ExpandVideoPosition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.jackPan.ReadtheBuddha.AdbertMediation;

public class PhotoActivity extends Activity implements  View.OnClickListener{
    private Button shotBtn;
    private  View mScreenView;
    private ImageView closeImg;
    private TextView mTextView;
    AdbertADView adbertView;
    String appId = "";  //Pleaser enter your appId
    String appKey = ""; //Pleaser enter your appKey
    String admob_banner = ""; //Pleaser enter your banner unit id
    RelativeLayout adLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_photo);
//
//        shotBtn = (Button) findViewById(R.id.shotbtn);
//        mScreenView = (RelativeLayout) findViewById(R.id.screenlayout);
//        findViewById(R.id.shotbtn).setOnClickListener(this);
        mTextView = (TextView)findViewById(R.id.text);
        mTextView.setTypeface(Typeface.createFromAsset(getAssets()
                , "fonts/wp010-05.ttf"));
        closeImg = (ImageView) findViewById(R.id.closeimg);
        findViewById(R.id.closeimg).setOnClickListener(this);
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        btnAction_banner_nonmediation();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.shotbtn:
//                shotBtn.setVisibility(View.GONE);
//                takeScreenshot();
//                break;
            case R.id.closeimg:
                GtSharedPreferences.saveIsFirstUsed(getApplicationContext(),false);
                this.finish();

                break;
        }

    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        View v1 =mScreenView.getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture


            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM

            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private static final String TAG = "PhotoActivity";
    public void btnAction_banner_nonmediation() {

        adbertView = (AdbertADView) findViewById(R.id.adbertADView);
        adbertView.setMode(AdbertOrientation.NORMAL);
        adbertView.setExpandVideo(ExpandVideoPosition.BOTTOM);
        adbertView.setFullScreen(false);
        adbertView.setBannerSize(AdSize.SMART_BANNER);
//        adbertView.setAPPID("20170427000002", "20170427000002");
        adbertView.setMediationAPPID("20170427000002|20170427000002");
        adbertView.setListener(new AdbertListener() {
            @Override
            public void onReceive(String msg) {
                Log.d(TAG, "onReceive: "+msg);

            }

            @Override
            public void onFailedReceive(String msg) {
                Log.d(TAG, "onFailedReceive: "+msg);


            }
        });

        adbertView.start();
    }
}
