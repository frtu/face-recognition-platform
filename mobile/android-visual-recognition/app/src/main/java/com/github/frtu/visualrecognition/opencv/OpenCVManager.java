package com.github.frtu.visualrecognition.opencv;

import android.content.Context;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.github.frtu.visualrecognition.camera.CameraManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fred.
 */
public class OpenCVManager {
    private static final Logger logger = LoggerFactory.getLogger(OpenCVManager.class);

    private String opencvVersion = OpenCVLoader.OPENCV_VERSION;

    private Context mContext = null;

    private BaseLoaderCallback mLoaderCallback;

    private CameraBridgeViewBase mOpenCvCameraView;

    public OpenCVManager(Context context, int cameraId) {
        this(context, new JavaCameraView(context, cameraId));
    }

    public OpenCVManager(Context context, JavaCameraView openCvCameraView, Boolean isFrontCamera) {
        this(context, openCvCameraView);
        setIsFrontCamera(isFrontCamera);
    }

    public OpenCVManager(Context context, JavaCameraView openCvCameraView) {
        mContext = context;

        String arch = System.getProperty("os.arch");
        logger.info("OpenCV init for os.arch='{}'", arch);

        mOpenCvCameraView = openCvCameraView;
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mLoaderCallback = new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                        logger.info("OpenCV loaded successfully");
                        mOpenCvCameraView.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };

        // libraries
        startOrResume();
    }

    public void setIsFrontCamera(Boolean isFrontCamera) {
        if (CameraManager.getNumberOfCameras() == 1) {
            mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_ANY);
        } else {
            if (isFrontCamera) {
                mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
            } else {
                mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
            }
        }
    }

    public void setMaxFrameSize(int maxWidth, int maxHeight) {
        mOpenCvCameraView.setMaxFrameSize(maxWidth, maxHeight);
    }

    public void setOpencvVersion(String opencvVersion) {
        logger.info("Setting OpenCV version={}", opencvVersion);
        this.opencvVersion = opencvVersion;
    }

    public void startOrResume() {
        logger.info("Start OpenCV version={}", opencvVersion);

        boolean initDebug = OpenCVLoader.initDebug();
        if (mLoaderCallback != null) {
            if (initDebug) {
                logger.info("OpenCV library found inside package. Using it!");
                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            } else {
                logger.info("Internal OpenCV library not found. Using OpenCV Manager for initialization");
                initDebug = OpenCVLoader.initAsync(opencvVersion, mContext, mLoaderCallback);
            }
        }
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.enableView();
        }
        if (!initDebug) {
            // Handle initialization error
            logger.error("OpenCV initialization ERROR!");
        }
    }

    public void stop() {
        if (mOpenCvCameraView != null) {
            logger.info("Disable OpenCV view");
            mOpenCvCameraView.disableView();
        }
    }

    public void bindCvCameraViewListener(CameraBridgeViewBase.CvCameraViewListener cvCameraViewListener) {
        mOpenCvCameraView.setCvCameraViewListener(cvCameraViewListener);
    }

    public void bindCvCameraViewListener(CameraBridgeViewBase.CvCameraViewListener2 cvCameraViewListener) {
        mOpenCvCameraView.setCvCameraViewListener(cvCameraViewListener);
    }

    public void bindFrameLayout(FrameLayout frameLayout) {
        frameLayout.addView(mOpenCvCameraView);
    }
}