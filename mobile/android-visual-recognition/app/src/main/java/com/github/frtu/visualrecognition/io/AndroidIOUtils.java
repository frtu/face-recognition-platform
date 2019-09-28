package com.github.frtu.visualrecognition.io;

import android.content.Context;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AndroidIOUtils {
    private static final Logger logger = LoggerFactory.getLogger(AndroidIOUtils.class);

    public static File getFile(Context context, String dirname, String filename) {
        File directory = context.getDir(dirname, Context.MODE_PRIVATE);
        logger.debug("Create file using directory={} filename={}", directory.getAbsolutePath(), filename);
        return new File(directory, filename);
    }

    public static File copyToFile(Context context, int sourceId, String dirname, String filename) {
        final File targetFile = getFile(context, dirname, filename);
        copy(context, sourceId, targetFile);
        return targetFile;
    }

    public static boolean copy(Context context, int sourceId, File targetFile) {
        InputStream inputStream = context.getResources().openRawResource(sourceId);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(targetFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            logger.error("Cannot find targetFile path={}", targetFile.getAbsolutePath(), e);
            return false;
        } catch (IOException e) {
            logger.error("Error in copying targetFile path={}", targetFile.getAbsolutePath(), e);
            return false;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        return true;
    }
}
