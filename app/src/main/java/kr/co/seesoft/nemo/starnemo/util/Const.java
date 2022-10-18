package kr.co.seesoft.nemo.starnemo.util;

import java.util.UUID;

public class Const {

    public static final int START_CAMERA_ACTIVITY = 2000;

    public static final int START_BLUETOOTH = 3000;

    public static final String HOSPITAL_CD = "hospitalCd";
    public static final String TODAY = "today";
    public static final String GO_ALBUM = "goAlbum";


    //블루투스 관련
    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_TOAST = 2;

    public static final UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    public static final UUID BT_UUID = UUID.fromString("00001124-0000-1000-8000-00805f9b34fb".toUpperCase());


    //핸들러 관련
    /** 핸들러 캘린더 선택 */
    public static final int HANDLER_CALENDAR = 8000;



    //일일방분계획 검색 부분
    /** 병원 이름으로 검색 */
    public static final int SEARCH_NAME = 0;
    /** 병원 코드로 검색 */
    public static final int SEARCH_CODE = 1;
    /** 담당자로 검색 */
    public static final int SEARCH_MANAGER = 2;
    /** 검색모드 내병원 */
    public static final int SEARCH_MODE_MY = 900;
    /** 검색모드 전체병원 */
    public static final int SEARCH_MODE_ALL = 901;


    //그리기 팝업 관련
//    public static final int OK = 1;
//    public static final int CANCEL = 2;
//    public static final int CLEAR = 3;

    /** 인계 모드 */
    public static final int DRAW_MODE_TAKING = 4000;
//    public static final int DRAW_MODE_TAKING_OK = 4001;
//    public static final int DRAW_MODE_TAKING_CANCEL = 4002;
//    public static final int DRAW_MODE_TAKING_CLEAR = 4003;



    /** 인수 모드 */
    public static final int DRAW_MODE_TAKE = 4100;
//    public static final int DRAW_MODE_TAKE_OK = 4101;
//    public static final int DRAW_MODE_TAKE_CANCEL = 4102;
//    public static final int DRAW_MODE_TAKE_CLEAR = 4103;


    /** 카메라 강제 플래시 */
    public static final int CAMERA_FLASH_MODE_ON = 5000;
    /** 카메라 강제 플래시 off */
    public static final int CAMERA_FLASH_MODE_OFF = 5001;
    /** 카메라 손전등 기능 */
    public static final int CAMERA_FLASH_MODE_ALL = 5002;

    public static final int CAMERA_FLASH_MODE_CLEAR = 5003;

    public static final int DEVICE_A90 = 90;
    public static final int DEVICE_A51 = 51;
    public static final int DEVICE_A52 = 52;
    public static final int DEVICE_A53 = 53;

    public static final int DEVOCE_OS_10 = 10;
    public static final int DEVECE_OS_11 = 11;
    public static final int DEVECE_OS_12 = 12;

    public static final String DEVICE_A90_MODEL_ID = "SM-A90";
    public static final String DEVICE_A51_MODEL_ID = "SM-A51";
    public static final String DEVICE_A52_MODEL_ID = "SM-A52";
    public static final String DEVICE_A53_MODEL_ID = "SM-A53";


}
