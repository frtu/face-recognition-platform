# android-visual-recognition

Visual recognition for Android

## OpenCV in Android

### Download and import

* [Download OpenCV for Android SDK](https://github.com/opencv/opencv/releases)

To avoid very long build without response, it is recommendated to edit the file `unpacked_OpenCV_package/sdk/java/build.gradle` before importing Module.

```
    compileSdkVersion commonCompileSdkVersion
    buildToolsVersion commonBuildToolsVersion

    defaultConfig {
        minSdkVersion commonMinSdkVersion
        targetSdkVersion commonTargetSdkVersion
    }
```

Copy the folder ARCH/libopencv_java3.so corresponding to your mobile architecture into your application src/main/jniLibs/


### Preparing the target device

* Install the OpenCV Manager APK corresponding to your env (in subfolder /apk/*). Ex ARM processor :
```
adb install -r apk/OpenCV_3.4.1_Manager_3.41_armeabi-v7a.apk
```

* If you have pre-existing OpenCV Mgr version uninstall with :
```
adb uninstall -v org.opencv.engine
```

### OpenCV full guidelines

* [Follow the tutorial guide here](https://docs.opencv.org/3.4.1/d0/d6c/tutorial_dnn_android.html)
* [Simple guideline with Emulator](https://zami0xzami.wordpress.com/2016/03/17/opencv-for-mobile-devices-using-android-studio/)
* [opencv-in-android-studio](https://stackoverflow.com/questions/27406303/opencv-in-android-studio)

### License

This project includes OpenCV and its [license](https://opencv.org/license/).

