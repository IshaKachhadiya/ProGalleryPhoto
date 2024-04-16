package hdphoto.galleryimages.gelleryalbum.duplicate;


public class ImageUtils {

    public static boolean isImage(String str) {
        String fileType = getFileType(str);
        if (fileType != null) {
            return fileType.equals("jpg") || fileType.equals("gif") || fileType.equals("png") || fileType.equals("jpeg") || fileType.equals("bmp") || fileType.equals("wbmp") || fileType.equals("ico") || fileType.equals("jpe");
        }
        return false;
    }

    public static String getFileType(String str) {
        int lastIndexOf;
        return (str == null || (lastIndexOf = str.lastIndexOf(".")) == -1) ? "" : str.substring(lastIndexOf + 1).toLowerCase();
    }
}
