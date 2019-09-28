package com.github.frtu.visualrecognition.opencv;

import com.github.frtu.visualrecognition.opencv.coordinate.CoordinateMapper;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.io.File;

/**
 * Object detector using Clasifier
 * Created by fred.
 */
public class ObjectDetector {
    private CascadeClassifier cascadeClassifier;

    // https://stackoverflow.com/questions/36218385/parameters-of-detectmultiscale-in-opencv-using-python
    double scaleFactor = 1.1;
    int minNeighbors = 2;
    Size minOjbSize = new Size(30, 30);
    Size maxOjbSize = new Size();

    public ObjectDetector(File filePath) {
        // Load the cascade classifier
        this(new CascadeClassifier(filePath.getAbsolutePath()));
    }

    public ObjectDetector(CascadeClassifier cascadeClassifier) {
        if (cascadeClassifier.empty()) {
            throw new IllegalStateException("The cascade is empty!");
        }
        this.cascadeClassifier = cascadeClassifier;
    }

    public MatOfRect detectObject(Mat gray) {
        MatOfRect faces = new MatOfRect();
        // Use the classifier to detect faces
        cascadeClassifier.detectMultiScale(gray, faces, scaleFactor, minNeighbors,
                Objdetect.CASCADE_SCALE_IMAGE, minOjbSize, maxOjbSize);
        return faces;
    }

    public MatOfRect detectObject(Mat gray, CoordinateMapper coordinateMapper) {
        MatOfRect matOfRect = detectObject(gray);
        for (Rect objCoordinate : matOfRect.toArray()) {
            coordinateMapper.mapCoordinate(objCoordinate);
        }
        return matOfRect;
    }

    public void setMinOjbSize(int minObjSize) {
        setMinOjbSize(new Size(minObjSize, minObjSize));
    }

    public void setMinOjbSize(Size minOjbSize) {
        this.minOjbSize = minOjbSize;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public void setMinNeighbors(int minNeighbors) {
        this.minNeighbors = minNeighbors;
    }

    public void setMaxOjbSize(Size maxOjbSize) {
        this.maxOjbSize = maxOjbSize;
    }
}
