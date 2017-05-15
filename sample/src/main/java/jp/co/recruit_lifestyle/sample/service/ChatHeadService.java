package jp.co.recruit_lifestyle.sample.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import jp.co.recruit.floatingview.R;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;
import jp.co.recruit_lifestyle.sample.GtSharedPreferences;


/**
 * ChatHead Service
 */
public class ChatHeadService extends Service implements FloatingViewListener {
    Context context;
    /**
     * デバッグログ用のタグ
     */
    private static final String TAG = "ChatHeadService";

    /**
     * 通知ID
     */
    private static final int NOTIFICATION_ID = 9083150;

    /**
     * FloatingViewManager
     */
    private FloatingViewManager mFloatingViewManager;
    TextToSpeech t1;

    /**
     * {@inheritDoc}
     */
    boolean isopen =false;
    InterstitialAd mInterstitialAd;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: "+"in");
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-7019441527375550/9918121823");
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//
//        mInterstitialAd.loadAd(adRequest);
//
//        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        //包装需要执行Service的Intent
//        Intent intent = new Intent(this, this.getClass());
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //触发服务的起始时间
//        long triggerAtTime = SystemClock.elapsedRealtime();
//        Log.d(TAG, "onCreate: "+triggerAtTime+"");
//        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, 3000, pendingIntent);

//        if(mInterstitialAd.isLoaded()){
//            mInterstitialAd.show();
//            Log.d(TAG, "onCreate:"+"show");
//        }



    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // 既にManagerが存在していたら何もしない
        if (mFloatingViewManager != null) {
            return START_STICKY;
        }
        context = this;
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.CHINESE);
                }
            }
        });
        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ImageView iconView = (ImageView) inflater.inflate(R.layout.widget_chathead, null, false);

        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, getString(R.string.chathead_click_message));
                ListView listView = new ListView(getBaseContext());
                ArrayList<String> arrayList = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.bookname)));


                ArrayAdapter<String>  listAdapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(listAdapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatHeadService.this);
                builder.setTitle("佛經選擇");
                builder.setView(listView);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        takeScreenshot();
                        if(!GtSharedPreferences.getIsFirstUsed(getApplicationContext())){
                            Intent i= new Intent(getApplicationContext(), PhotoActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                            GtSharedPreferences.saveIsFirstUsed(getApplicationContext(),true);
                        }else{
                            Log.d(TAG, "onClick: "+"isopen");
                        }

//
//
//
//                        Toast.makeText(getApplicationContext(), "測試用的文字語音",Toast.LENGTH_SHORT).show();
//
//                        t1.speak("測試用的文字語音", TextToSpeech.QUEUE_FLUSH, null);
//                        if(t1.isSpeaking()){
//                            Log.d(TAG, "onClick: "+"isspeck");
//                        }
                    }
                });
                    AlertDialog alert = builder.create();
                    alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                    alert.show();

            }
        });

        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage(R.drawable.ic_trash_fixed);
        mFloatingViewManager.setActionTrashIconImage(R.drawable.ic_trash_action);
        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.overMargin = (int) (16 * metrics.density);
        mFloatingViewManager.addViewToWindow(iconView, options);

        // 常駐起動
        startForeground(NOTIFICATION_ID, createNotification());

        return START_REDELIVER_INTENT;
    }
    /**
     *
     * 1.START_STICKY:
     當Service在執行時被砍掉後，若沒有新的intent進來，
     Service會停留在started state，但intent資料不會被保留
     2.START_NOT_STICKY或START_REDELIVER_INTENT:
     當Service在執行時被砍掉後，若沒有新的intent進來，
     service會離開started  state，若沒有很明確的再啟動，
     將不會產生新的service物件
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        destroy();
        GtSharedPreferences.saveIsFirstUsed(getApplicationContext(),false);
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFinishFloatingView() {
        stopSelf();
        Log.d(TAG, getString(R.string.finish_deleted));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {
        if (isFinishing) {
            Log.d(TAG, getString(R.string.deleted_soon));
        } else {
            Log.d(TAG, getString(R.string.touch_finished_position, x, y));
        }
    }

    /**
     * Viewを破棄します。
     */
    private void destroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
    }

    /**
     * 通知を表示します。
     * クリック時のアクションはありません。
     */
    private Notification createNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getString(R.string.chathead_content_title));
        builder.setContentText(getString(R.string.content_text));
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE);

        return builder.build();
    }

    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT);
}

