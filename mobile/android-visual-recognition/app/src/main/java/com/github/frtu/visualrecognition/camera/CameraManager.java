package com.github.frtu.visualrecognition.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Camera Manager for prior than Android 5 (API 21) that deprecate Camera in favor of camera2.
 * <p>
 * See : https://developer.android.com/guide/topics/media/camera.html <br/>
 * API : https://developer.android.com/reference/android/hardware/Camera.html <br/>
 * Controlling camera : https://developer.android.com/training/camera/cameradirect.html
 * </p>
 * <p>
 * For using new Camera 2 API see https://github.com/googlesamples/android-Camera2Basic
 * </p>
 * <p>
 * Created by fred on 11/03/2018.
 */
public class CameraManager {
    private static final Logger logger = LoggerFactory.getLogger(CameraManager.class);

    private Context mContext = null;

    public CameraManager(Context context) {
        mContext = context;
    }

    /**
     * Check if this device has a camera
     */
    public boolean hasCameraHardware() {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            logger.info("This device has a camera");
            return true;
        } else {
            logger.info("No camera on this device");
            return false;
        }
    }

    /**
     * Check if permission has been granted to this camera
     */
    public boolean hasCameraPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            logger.info("CAMERA Permission granted!");
            return true;
        } else {
            logger.info("CAMERA Permission NOT GRANTED!");
            return false;
        }
    }

    public void checkAndRequestCameraPermissionIfPossible(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            logger.info("Camera is mandatory but may be authorized later above android version>22. Current version = {}.", Build.VERSION.SDK_INT);

            // https://developer.android.com/training/permissions/requesting
            if (!hasCameraPermission()) {
                logger.warn("Ask user to authorize in app. Or authorize later by going to your device settings -> apps -> YOUR APP -> Permissions -> turn on camera permission!");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
    }

    /**
     * Proxy method for Camera.getNumberOfCameras()
     */
    public static int getNumberOfCameras() {
        int numberOfCameras = Camera.getNumberOfCameras();
        logger.info("Found {} camera on this device", numberOfCameras);
        return numberOfCameras;
    }

    /**
     * A safe way to get an instance of the Camera object.
     *
     * @return null if camera is unavailable or in use
     */
    public static Camera getCameraInstance(int index) {
        Camera c = null;
        int numberOfCameras = getNumberOfCameras();
        if (numberOfCameras > 0) {
            if (index >= numberOfCameras) {
                logger.warn("ATTENTION try to get a camera index={} higher than available={}. Return the latest index={} and continue.", index, numberOfCameras, numberOfCameras - 1, new IllegalArgumentException());
                index = numberOfCameras - 1;
            }
            try {
                c = Camera.open(index); // attempt to get a Camera instance
            } catch (Exception e) {
                // Camera is not available (in use or does not exist)
                logger.error(e.getMessage(), e);
            }
        } else {
            logger.error("Getting index={} is impossible, where total number of camera = {}", index, numberOfCameras);
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Getting Camera Parameters : https://developer.android.com/reference/android/hardware/Camera.Parameters.html
     *
     * @param index
     * @return
     */
    public static Camera.Parameters getCameraParameters(int index) {
        Camera camera = getCameraInstance(index);

        Camera.Parameters parameters = camera.getParameters();

        Camera.Size previewSize = parameters.getPreviewSize();
        int height = previewSize.height;
        int width = previewSize.width;
        logger.info("CAMERA PREVIEW height={} width={} max zoom={}", height, width, parameters.getMaxZoom());

        return parameters;
    }

    public static Camera releaseCamera(Camera camera) {
        if (camera != null) {
            logger.debug("Release the camera for other applications");
            camera.release();
        }
        return null;
    }
}
