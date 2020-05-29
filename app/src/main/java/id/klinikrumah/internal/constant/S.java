package id.klinikrumah.internal.constant;

public class S {
    // request
    public static final String REQ_EMAIL = "email";
    public static final String REQ_GOOGLE = "google";
    public static final String REQ_FB = "facebook";
    // response
    public static final String RSPNS_STATUS = "status";
    public static final String RSPNS_SUCCESS = "success";
    public static final String RSPNS_DATA = "data";
    public static final String RSPNS_USER = "user";
    public static final String RSPNS_AUTH_TOKEN = "auth_token";
    // Country Code
    public static final String CC_ID = "+62";
    // Mime Types
    public static final String MIME_TYPE_JPG = "image/jpeg";
    public static final String MIME_TYPE_PNG = "image/png";
    public static final String MIME_TYPE_PDF = "application/pdf";
    // other
    public static final String DASH = "-";
    public static final String ZERO = "0";
    public static final String WA_LINK = "https://wa.me/%s";
    public static final String GMAP_LINK = "https://maps.google.com/maps?daddr=%s,%s";
    public static final String GMAP_SEARCH = "https://www.google.com/maps/search/%s";
    public static final String PKG_DRIVE = "com.google.android.apps.docs";
    public static final String TEMP_PHOTO_FILE_NAME = "klinikrumahid_ava.jpg";

    public static final class RequestCode {
        public static final int OPEN_GALLERY = 0x1;
        public static final int TAKE_PICTURE = 0x2;
        public static final int TAKE_PICTURE_WITH_CROP = 0x3;
        public static final int EXTERNAL_STORAGE = 0x4;
        public static final int DOC = 0X5;
        public static final int CAMERA = 0X6;
        public static final int PHONE_CALL = 0X7;
        public static final int LOCATION = 0X8;
        public static final int G_DRIVE = 0X9;
        public static final int SETTING_RESULT = 0X10;
    }
}