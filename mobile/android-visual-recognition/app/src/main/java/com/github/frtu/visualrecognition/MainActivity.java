package com.github.frtu.visualrecognition;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.github.frtu.visualrecognition.io.AndroidIOUtils;
import com.github.frtu.visualrecognition.opencv.Colors;
import com.github.frtu.visualrecognition.opencv.ObjectDetector;
import com.github.frtu.visualrecognition.opencv.OpenCVManager;
import com.github.frtu.visualrecognition.opencv.coordinate.HighlightObjectMapper;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    private OpenCVManager openCVManager;

    private ObjectDetector faceDetector;
    private HighlightObjectMapper highlightFaceMapper = new HighlightObjectMapper(Colors.GREEN, 3);

    private ObjectDetector eyeDetector;
    private HighlightObjectMapper highlightEyeMapper = new HighlightObjectMapper(Colors.YELLOW, 3);

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        logger.info("called onCreate");

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        // Create our Preview view and set it as the content of our activity.
        FrameLayout preview = findViewById(R.id.container);
        openCVManager = new OpenCVManager(this, 0);
        openCVManager.bindFrameLayout(preview);
        openCVManager.bindCvCameraViewListener(this);

        File faceCascade = null;
        File eyeCascade = null;
        try {
            // Copy the resource into a temp file so OpenCV can load it
            faceCascade = AndroidIOUtils.copyToFile(this, R.raw.lbpcascade_frontalface,
                    "cascade", "lbpcascade_frontalface.xml");

            logger.info("Loading cascacde file : {}", faceCascade.getAbsolutePath());
            faceDetector = new ObjectDetector(faceCascade);

            eyeCascade = AndroidIOUtils.copyToFile(this, R.raw.haarcascade_lefteye_2splits,
                    "cascade", "haarcascade_lefteye.xml");

            logger.info("Loading cascacde file : {}", eyeCascade.getAbsolutePath());
            eyeDetector = new ObjectDetector(eyeCascade);
        } catch (Exception e) {
            logger.error("Failed to load cascade. Exception thrown: ", e);
        } finally {
            if (faceCascade != null) {
                // Delete folder after copy
                faceCascade.getParentFile().delete();
            }
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        // The faces will be a 20% of the height of the screen
        double objectFactor = 0.2;
        faceDetector.setMinOjbSize((int) (height * objectFactor));
        eyeDetector.setMinOjbSize((int) (height * objectFactor * objectFactor));
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final Mat rgba = inputFrame.rgba();
        final Mat gray = inputFrame.gray();

        highlightFaceMapper.setImage(rgba);
        faceDetector.detectObject(gray, highlightFaceMapper);

        highlightEyeMapper.setImage(rgba);
        eyeDetector.detectObject(gray, highlightEyeMapper);

        return highlightFaceMapper.getResultImage();
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public void onPause() {
        super.onPause();
        openCVManager.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        openCVManager.startOrResume();
    }

    public void onDestroy() {
        super.onDestroy();
        openCVManager.stop();
    }
}
