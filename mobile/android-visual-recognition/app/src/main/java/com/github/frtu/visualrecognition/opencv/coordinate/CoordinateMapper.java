package com.github.frtu.visualrecognition.opencv.coordinate;

import org.opencv.core.Rect;

/**
 * Pass all found object coordinate to the mapper.
 * Created by fred.
 */
public interface CoordinateMapper {
    void mapCoordinate(Rect objectCoordinate);
}
