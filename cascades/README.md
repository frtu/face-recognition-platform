# OpenCV cascades

Links :

* [Object detection module from OpenCV](https://docs.opencv.org/3.4/d5/d54/group__objdetect.html)
* [Cascade classfier tutorial](https://docs.opencv.org/3.4.1/db/d28/tutorial_cascade_classifier.html)
* [Cascade class reference](https://docs.opencv.org/3.4.1/d1/de5/classcv_1_1CascadeClassifier.html#ab3e572643114c43b21074df48c565a27)

## haar cascades

Haar features are inherently slow - they make extensive use of floating point operations, which are a bit slow on mobile devices.

## lbp cascades

For LBP, the performance gain is significant, and the loss in accuracy is minimal