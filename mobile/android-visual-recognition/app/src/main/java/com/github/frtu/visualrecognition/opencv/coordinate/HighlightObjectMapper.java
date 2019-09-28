package com.github.frtu.visualrecognition.opencv.coordinate;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Take an image in constructor and draw coordinate on it
 */
public class HighlightObjectMapper implements CoordinateMapper {
    private Mat image;
    private Scalar color = new Scalar(0, 255, 0, 255);
    private int thickness = 3;


    @Override
    public void mapCoordinate(Rect objectCoordinate) {
        // If there are any faces found, draw a rectangle around it
        Imgproc.rectangle(image, objectCoordinate.tl(), objectCoordinate.br(), color, thickness);
    }

    public HighlightObjectMapper() {
    }

    public HighlightObjectMapper(Mat image) {
        this();
        this.image = image;
    }

    public HighlightObjectMapper(Scalar color, int thickness) {
        this();
        this.color = color;
        this.thickness = thickness;
    }

    /**
     * Image we should operate on
     *
     * @param image
     */
    public void setImage(Mat image) {
        this.image = image;
    }

    /**
     * Color we should draw the rectangle
     *
     * @param color
     */
    public void setColor(Scalar color) {
        this.color = color;
    }

    /**
     * How thick is the rectangle border
     *
     * @param thickness
     */
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    /**
     * Result of the drawed image
     *
     * @return
     */
    public Mat getResultImage() {
        return image;
    }
}
