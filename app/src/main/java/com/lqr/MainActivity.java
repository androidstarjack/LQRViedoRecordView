package com.lqr;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lqr.example.takevideo.record.landscapevideocamera.RecodePAlerMainActivity;
import com.lqr.view.LQRVideoRecordView;


public class MainActivity extends AppCompatActivity implements LQRVideoRecordView.OnRecordStausChangeListener {

    private LQRVideoRecordView mVrvVideo;
    private Button mBtnVideo;
    private TextView mTvTipOne;
    private TextView mTvTipTwo;
    private RecordProgress mRp;
    private static final String TAG = "RxPermissionTest";
    public  static  final int RECODE_VIEDEO_TIMER = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mVrvVideo = (LQRVideoRecordView) findViewById(R.id.vrvVideo);
        mBtnVideo = (Button) findViewById(R.id.btnVideo);
        mTvTipOne = (TextView) findViewById(R.id.tvTipOne);
        mTvTipTwo = (TextView) findViewById(R.id.tvTipTwo);
        mRp = (RecordProgress) findViewById(R.id.rp);
        mRp.setRecordTime(RECODE_VIEDEO_TIMER);
        mVrvVideo.setRecordMaxTime(RECODE_VIEDEO_TIMER);
    }

    private void initListener() {
        mBtnVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mRp.start();
                        mRp.setProgressColor(Color.parseColor("#1AAD19"));
                        mTvTipOne.setVisibility(View.VISIBLE);
                        mTvTipTwo.setVisibility(View.GONE);
                        //开始录制
                        mVrvVideo.record(MainActivity.this);
                        break;
                    case MotionEvent.ACTION_UP:
                        mRp.stop();
                        mTvTipOne.setVisibility(View.GONE);
                        mTvTipTwo.setVisibility(View.GONE);
                        //判断时间
                        if (mVrvVideo.getTimeCount() > 3) {
                            if (!isCancel(v, event)) {
                                onRecrodFinish();
                            }
                        } else {
                            if (!isCancel(v, event)) {
                                Toast.makeText(getApplicationContext(), "视频时长太短", Toast.LENGTH_SHORT).show();
                                if (mVrvVideo.getVecordFile() != null)
                                    mVrvVideo.getVecordFile().delete();
                            }
                        }
                        resetVideoRecord();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isCancel(v, event)) {
                            mTvTipOne.setVisibility(View.GONE);
                            mTvTipTwo.setVisibility(View.VISIBLE);
                            mRp.setProgressColor(Color.parseColor("#FF1493"));
                        } else {
                            mTvTipOne.setVisibility(View.VISIBLE);
                            mTvTipTwo.setVisibility(View.GONE);
                            mRp.setProgressColor(Color.parseColor("#1AAD19"));
                        }
                        break;
                }
                return true;
            }
        });
    }

    private boolean isCancel(View v, MotionEvent event) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + v.getWidth() || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }

    @Override
    public void onRecrodFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvTipOne.setVisibility(View.GONE);
                mTvTipTwo.setVisibility(View.GONE);
                resetVideoRecord();
                //打开播放界面
                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("text", mVrvVideo.getVecordFile().toString());
                intent.putExtras(bundle);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "录制成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRecording(int timeCount, int recordMaxTime) {

    }

    @Override
    public void onRecordStart() {
    }

    /**
     * 停止录制（释放相机后重新打开相机）
     */
    public void resetVideoRecord() {
        mVrvVideo.stop();
        mVrvVideo.openCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void requestPermissions() {
//        RxPermissions rxPermission = new RxPermissions(MainActivity.this);
//        rxPermission.requestEach(Manifest.permission.CAMERA,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.RECORD_AUDIO,
//                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                . subscribe(new Consumer<Permission>() {
//                    @Override
//                    public void accept(Permission permission) throws Exception {
//                        if (permission.granted) {
//                            // 用户已经同意该权限
//                            Log.d(TAG, permission.name + " is granted.");
//                            initListener();
//                        } else if (permission.shouldShowRequestPermissionRationale) {
//                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
//                        } else {
//                            // 用户拒绝了该权限，并且选中『不再询问』
//                            Log.d(TAG, permission.name + " is denied.");
//                        }
//                    }
//                });
//        rxPermission .requestEachCombined(Manifest.permission.CAMERA,
//                        Manifest.permission.READ_PHONE_STATE);

    }
    public void palyRecode(View view){
        startActivity(new Intent(MainActivity.this, RecodePAlerMainActivity.class));
    }
    public void compressRecode(View view){
        startActivity(new Intent(MainActivity.this, VideoCompressActivity.class));
    }
}
