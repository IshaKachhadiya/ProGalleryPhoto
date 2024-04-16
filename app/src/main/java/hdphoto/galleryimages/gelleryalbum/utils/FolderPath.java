package hdphoto.galleryimages.gelleryalbum.utils;

import java.io.File;


public class FolderPath {
    public static int Height = 0;
    public static String SDCARD_PATH_DELETE_IMAGE = AppUtilsClass.rootMainDir + File.separator + ".PhotoGallery1/TrashImage";
    public static final String LOGIN_PREFERENCE_DATA = "photogallerydata";
    public static final String LOGIN_PREFERENCE_FOLDER = "photogalleryfolder";
    public static String SDCARD_PATH_FOR11 = AppUtilsClass.rootMainDir + File.separator + ".PhotoGallery1/PrivateImage";
    public static String SDCARD_PATH_VIDEO = AppUtilsClass.rootMainDir + File.separator + ".PhotoGallery1/PrivateVideo";
    public static String SDCARD_PATH_DELETE_VIDEO = AppUtilsClass.rootMainDir + File.separator + ".PhotoGallery1/TrashVideo";
    public static String SDCARD_PATH_VIDEO_LOCK_BACKUP = AppUtilsClass.rootMainDir + File.separator + "RestoreLockVideo";
    public static String SDCARD_PATH_IMAGE_LOCK_BACKUP = AppUtilsClass.rootMainDir + File.separator + "RestoreLockImage";
    public static String SDCARD_PATH_IMAGE_TRASH_BACKUP = AppUtilsClass.rootMainDir + File.separator + "RestoreTrashImage";
    public static String SDCARD_PATH_VIDEO_TRASH_BACKUP = AppUtilsClass.rootMainDir + File.separator + "RestoreTrashVideo";
    public static String SAVE_IMAGE_PATH = AppUtilsClass.rootMainDir + File.separator + "SavePhotos";
    public static String SDCARD_PATH_IMAGE = AppUtilsClass.rootMainDir + File.separator + ".PhotoGallery1/PrivateImage";
    public static String SAVE_SECURITY_QA_IMAGE_PATH = AppUtilsClass.rootMainDir + File.separator + ".PhotoGallery1/SecurityData";
    public static int Width = 0;
    public static int lock_screen = 0;

    public static int getWidth(float f) {
        return (int) ((Width * f) / 100.0f);
    }

    public static int getHeight(float f) {
        return (int) ((Height * f) / 100.0f);
    }
}
