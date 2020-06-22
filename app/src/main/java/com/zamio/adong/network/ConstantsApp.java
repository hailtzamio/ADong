package com.zamio.adong.network;


import com.zamio.adong.model.LinesAddNew;
import com.zamio.adong.model.Product;
import com.zamio.adong.model.Transport;
import com.zamio.adong.model.Worker2;

import java.util.ArrayList;

public class ConstantsApp {
    public static final boolean DEBUG = true;

    public static enum FilterType {
        CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN, BLEND_DIFFERENCE,
        BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
        GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON, SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, HALFTONE, TRANSFORM2D
    }


    public static final String DB_NAME = "quizupelcom";
    public static String BASE64_AUTH_TOKEN = "Bearer eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJhZG1pbiIsImF1ZCI6InNpbXBsZS1jbGllbnQiLCJpc3MiOiJTaW1wbGVBUEkiLCJyb2xlcyI6WyJBZG1pbmlzdHJhdG9yIl0sInVzZXJJZCI6MSwiZW1haWwiOiJhZG1pbiJ9.YVk6B1qksEk9noTkV8ArurNVXhBOPKyNyHFM-Nu9oRCnmjg2dR7dnZJ8s8gk7xX_3dGPZAA2L3JjBz_48P0-hA";
    public static String BASE64_HEADER_LANG = "vi";
        public static String SERVER_URL = "http://adong-api.zamio.net/api/"; // UAT
//    public static String SERVER_URL = "http://adong-api-dev.zamio.net/api/";
//    public static String SERVER_URL = "http://adong-api-test.zamio.net/api/";

    public static int REQUEST_CODE_START_ACTIVITY = 1000;
    public static int START_ACTIVITY_TO_PLAY_GAME_FROM_QUIZUPACTIVITY = 100;
    public static int START_ACTIVITY_TO_GO_TO_QUESTION_INFO = 101;
    public static int START_ACTIVITY_TO_HISTORY = 102;
    public static int RESULT_CODE_TO_STOP_GAME_FROM_QUIZUPACTIVITY = 10;
    public static int RESULT_CODE_TO_CONTINUE_TO_PLAY_GAME_FROM_QUIZUPACTIVITY = 9;


    public static int REQUEST_CODE_START_FACEBOOK_LOGIN = 64206;

    public static int START_ACTIVITY_TO_MOVE_FROM_LIVECHALLENGE = 100;
    public static int START_ACTIVITY_TO_MOVE_FROM_LIVECHALLENGE_EXIT = 101;
    public static String KEY_LIVECHALLENGE_SHOWID = "KEY_LIVECHALLENGE_SHOWID";
    public static String KEY_LIVECHALLENGE_TOTAL = "KEY_LIVECHALLENGE_TOTAL";
    public static String TOAST = "Không lấy được dữ liệu";

    // SoloQuestionIntro
    public static String KEY_TYPE_OF_GAME = "KEY_TYPE_OF_GAME";
    public static String KEY_VALUES_ID = "KEY_VALUES_ID";
    public static String KEY_VALUES_OBJECT = "KEY_VALUES_OBJECT";
    public static String KEY_VALUES_REG_APPROVED = "KEY_VALUES_REG_APPROVED";
    public static String KEY_VALUES_TITLE = "KEY_VALUES_TITLE";
    public static String KEY_VALUES_STATUS = "KEY_VALUES_STATUS";
    public static String KEY_VALUES_HIDE = "KEY_VALUES_HIDE";
    public static String KEY_VALUES_NEW_PROJECT = "KEY_VALUES_NEW_PROJECT";
    public static String KEY_VALUES_LAT= "KEY_VALUES_LAT";
    public static String KEY_VALUES_LONG= "KEY_VALUES_LONG";
    public static String ChooseTeamWorkerActivity = "ChooseTeamWorkerActivity";
    public static String ProjectWorkersFragment = "ProjectWorkersFragment";
    public static String KEY_PERMISSION = "KEY_PERMISSION";
    public static String PERMISSION = "c-r-u-d";
    public static ArrayList<Worker2> workers = new ArrayList<Worker2>();
    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<LinesAddNew> lines = new ArrayList<LinesAddNew>();
    public static ArrayList<Product> productsToGooodReceied = new ArrayList<Product>();
    public static ArrayList<Transport> transportsChoose = new ArrayList<Transport>();
}
