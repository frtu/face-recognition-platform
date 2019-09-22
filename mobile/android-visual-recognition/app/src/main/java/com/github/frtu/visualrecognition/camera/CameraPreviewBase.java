package com.github.frtu.visualrecognition.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This is the Base class for Camera Preview for prior than Android 5 (API 21) that deprecate Camera in favor of camera2.
 * <p>
 * See : https://developer.android.com/guide/topics/media/camera.html
 * <p>
 * Created by fred on 11/03/2018.
 */
public class CameraPreviewBase extends SurfaceView implements SurfaceHolder.Callback {
    private static final Logger logger = LoggerFactory.getLogger(CameraPreviewBase.class);

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreviewBase(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            logger.error("Error setting camera preview: {}", e.getMessage(), e);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            logger.error("Error starting camera preview: {}", e.getMessage(), e);
        }
    }
}