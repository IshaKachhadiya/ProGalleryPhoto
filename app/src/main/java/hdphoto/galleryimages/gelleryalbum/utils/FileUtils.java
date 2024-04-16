package hdphoto.galleryimages.gelleryalbum.utils;

import android.content.Context;
import android.util.Log;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.constant.MediaScanners;
import hdphoto.galleryimages.gelleryalbum.appclass.GalleryAppClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;


public class FileUtils {
    public static int KILO = 1024;
    public static int MEGA = 1048576;

    public static String getFileExtension(String str) {
        return str.lastIndexOf(".") == -1 ? "" : str.substring(str.lastIndexOf(".") + 1, str.length());
    }

    public static String convertToHumanReadableSize(Context context, long j) {
        if (j == 0) {
            return "";
        }
        long j2 = KILO;
        if (j < j2) {
            return context.getString(R.string.bytes, String.valueOf(j));
        }
        long j3 = MEGA;
        return j < j3 ? context.getString(R.string.kilobytes, String.valueOf(j / j2)) : j < org.apache.commons.io.FileUtils.ONE_GB ? context.getString(R.string.megabytes, String.valueOf(j / j3)) : context.getString(R.string.gigabytes, new DecimalFormat("0.#").format(((float) j) / 1.07374182E9f));
    }

    public static boolean copy(InputStream inputStream, OutputStream outputStream) {
        try {
            IoUtils.copy(inputStream, outputStream);
            outputStream.flush();
            IoUtils.closeQuietly(inputStream);
            IoUtils.closeQuietly(outputStream);
            return true;
        } catch (IOException unused) {
            IoUtils.closeQuietly(inputStream);
            IoUtils.closeQuietly(outputStream);
            return false;
        } catch (Throwable th) {
            IoUtils.closeQuietly(inputStream);
            IoUtils.closeQuietly(outputStream);
            throw th;
        }
    }

    public static File CopyMoveFile(File file, File file2) throws Exception {
        try {
            if (!file.isDirectory()) {
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                FileChannel channel = new FileInputStream(file).getChannel();
                channel.transferTo(0L, channel.size(), new FileOutputStream(file2).getChannel());
                new MediaScanners(GalleryAppClass.getInstance(), file2);
                return file2;
            } else if (!file.getPath().equals(file2.getPath())) {
                File CopyMoveCreateDirectory = CopyMoveCreateDirectory(file2, file.getName());
                for (File file3 : file.listFiles()) {
                    CopyMoveFile(file3, CopyMoveCreateDirectory);
                }
                return CopyMoveCreateDirectory;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(String.format("Error copying %s", file.getName()));
        }
    }

    public static File CopyMoveCreateDirectory(File file, String str) throws Exception {
        File file2 = new File(file, str);
        if (file2.mkdirs()) {
            return file2;
        }
        if (file2.exists()) {
            throw new Exception(String.format("%s already exists", str));
        }
        throw new Exception(String.format("Error creating %s", str));
    }
}
