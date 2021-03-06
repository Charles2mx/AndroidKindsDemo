package com.fenqile.functionvideorecordingandfacerecognition.vivodetection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fenqile.functionvideorecordingandfacerecognition.R;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DetectionActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = "DetectionActivity";
    private Button mBtnStartDetect;
    private Button mBtnStopDetect;
    private SurfaceView mSurfaceViewDetect;
    private TextView mTvDetectTime;
    private TextView mTvActionState;
    private TextView mTvVivoState;
    private TextView mTvActionTips;
    private Camera.Parameters mCameraParas;

    private Camera mCamera;
    private SurfaceView mCameraSurface = null;
    private int mCameraWidth = 480;
    private int mCameraHeight = 270;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "bright#onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        initView();
    }

    private void initView() {
        mBtnStartDetect = findViewById(R.id.btn_start_detect);
        mBtnStopDetect = findViewById(R.id.btn_stop_detect);
        mSurfaceViewDetect = findViewById(R.id.surface_view_detect);
        mTvDetectTime = findViewById(R.id.tv_detect_time);
        mTvActionState = findViewById(R.id.tv_action_state);
        mTvVivoState = findViewById(R.id.tv_vivo_state);
        mTvActionTips = findViewById(R.id.tv_action_tips);

        //开启
        mBtnStartDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.setPreviewCallback(DetectionActivity.this);
            }
        });

        //关闭
        mBtnStopDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.setPreviewCallback(null);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        callCamera(mCameraSurface.getHolder());
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    //判断权限是否开启
    public void callCamera(SurfaceHolder surfaceHolder) {
        if(Build.VERSION.SDK_INT > 21) {
            String callCamera = Manifest.permission.CAMERA;
            String callRecord = Manifest.permission.RECORD_AUDIO;
            String[] permissions = new String[]{callCamera, callRecord};
            int selfSound = ActivityCompat.checkSelfPermission(this, callRecord);
            int selfPermission = ActivityCompat.checkSelfPermission(this, callCamera);

            if(selfPermission != PackageManager.PERMISSION_GRANTED || selfSound != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
                initCamera(surfaceHolder);
            }
        } else {
            initCamera(surfaceHolder);
        }
    }

    //处理申请权限的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCamera(mCameraSurface.getHolder());
                } else {
                    Toast.makeText(this, "you refused the camera function", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //初始化Camera
    private void initCamera(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "bright#initCamera");
        mCamera = Camera.open(1);
        //获取窗口的管理器
        WindowManager windowManager = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
        Display display = windowManager.getDefaultDisplay();
        mCameraParas = mCamera.getParameters();
        List<Camera.Size> sizeList = mCameraParas.getSupportedPreviewSizes();
        Camera.Size size = getOptimalPreviewSize(sizeList, display.getWidth(), display.getHeight());
        if(sizeList.size() > 1) {
            Iterator<Camera.Size> iterator = sizeList.iterator();
            while (iterator.hasNext()) {
                Camera.Size cur = iterator.next();
                if(cur.height <= 360) {
                    mCameraHeight = cur.height;
                    mCameraWidth = cur.width;
                    break;
                }
            }
        }

        mCameraParas.setPreviewFpsRange(30000, 30000);
        mCameraParas.setVideoStabilization(true);

        mCameraParas.setPreviewSize(mCameraWidth, mCameraHeight);
        try {
            //把摄像头获得画面显示在SurfaceView控件里面
            mCamera.setPreviewDisplay(mCameraSurface.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> focusModes = mCameraParas.getSupportedFocusModes();
        if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mCameraParas.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mCameraParas.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        mCamera.setParameters(mCameraParas);
        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }

    //?
    private static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
