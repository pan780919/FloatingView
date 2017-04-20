package jp.co.recruit_lifestyle.sample.service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import jp.co.recruit.floatingview.R;
import jp.co.recruit_lifestyle.sample.GtSharedPreferences;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
public class PhotoActivity extends Activity implements  View.OnClickListener{
    private Button shotBtn;
    private  View mScreenView;
    private ImageView closeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_photo);
//
//        shotBtn = (Button) findViewById(R.id.shotbtn);
//        mScreenView = (RelativeLayout) findViewById(R.id.screenlayout);
//        findViewById(R.id.shotbtn).setOnClickListener(this);
        closeImg = (ImageView) findViewById(R.id.closeimg);
        findViewById(R.id.closeimg).setOnClickListener(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.shotbtn:
//                shotBtn.setVisibility(View.GONE);
//                takeScreenshot();
//                break;
            case R.id.closeimg:
                this.finish();
                GtSharedPreferences.saveIsFirstUsed(getApplicationContext(),false);
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


}
