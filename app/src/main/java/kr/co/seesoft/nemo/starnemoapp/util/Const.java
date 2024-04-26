package kr.co.seesoft.nemo.starnemoapp.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Const {

    ////////////////////////////////////////////////////////////////////////////////////
    // Smatro VCAT Start
    // 사업자 번호
    // REAL
    public static final String VCAT_BIZ_NO = "2188201101";
    // TEST
    //public static final String VCAT_BIZ_NO = "2178114493";

    // 단말기번호(TID)
    // REAL
    public static final String VCAT_CAT_ID = "2178759014";
    // TEST
    //public static final String VCAT_CAT_ID = "3113782030";

    // 단말기일련번호
    // V-CAT 프로그램에서 관리 됨
    public static final String VCAT_MID = "POS0111740";

    /* 주의(!) 아래 문자열을 변경하지 마세요!
     * 아래 두 항목의 문자열을 변경하면, 서비스 호출에 문제가 발생됩니다.
     */
    public static final String VCAT_SERVER_PACKAGE = "service.vcat.smartro.com.vcat";
    public static final String VCAT_SERVER_ACTION = "smartro.vcat.action";

    public static final int VCAT_SCHEME = 100;

    // 결제 수단
    public static final String VCAT_TRAN_TYPE = "credit";

    // 카드 결제
    public static final String VCAT_TRAN_PAYMENT = "approval";

    // 카드 취소
    public static final String VCAT_TRAN_PAYMENT_CANCEL = "cancellation";

    // 카드 취소
    public static final String VCAT_TRAN_PAYMENT_NO_CARD_CANCEL = "no-card-cancel";

    // 가맹형태
    public static final String VCAT_MEM_TYPE = "VAN";

    // 서버 정보
    final static List<String> serverTest = Arrays.asList("eth", "test");
    final static List<String> serverReal = Arrays.asList("eth", "real");

    // 스마트로 포르타
    final static List<String> serverFortaTest = Arrays.asList("eth", "211.196.246.168", "20101");
    final static List<String> serverFortaReal = Arrays.asList("eth", "211.196.50.236", "20101");

    final static List<String> keyServerTest = Arrays.asList("eth", "test");
    final static List<String> keyServerReal = Arrays.asList("eth", "real");

    // 서버 설정
    // REAL
    public static final List<String> vcatServerType = serverReal;
    public static final List<String> vcatPgServerType = serverReal;
    public static final List<String> vcatKeyServerType = keyServerReal;
    // TEST
//    public static final List<String> vcatServerType = serverTest;
//    public static final List<String> vcatPgServerType = serverTest;
//    public static final List<String> vcatKeyServerType = keyServerTest;

    // Smatro VCAT End
    ////////////////////////////////////////////////////////////////////////////////////


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

    /** 핸들러 전자 서명 완료 */
    public static final int HANDLER_SIGN = 8100;

    /** 핸들러 전자 서명 완료 */
    public static final int HANDLER_SIGN_CANCEL = 8110;

    /** 핸들러 확인 완료 */
    public static final int HANDLER_CONFIRM = 8200;



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

    // 페이지 사이즈
    public static final int PAGE_SIZE = 100;

    // 거래 약관 코드
    public static final String CUSTOMER_TERMS_CODE = "CUST01";

    // 청구서 발송 docDivCd
    public static final String DOC_DIV_CD = "BILL"; // TAXBIL: 세금계산서, BILL: 청구서

    // 메모 코드 요청
    public static final String MEMO_CTGR_ID = "CUST_MEMO_DIV_CD";

    // 메인메뉴 코드 요청
    public static final String MAIN_MENU_CTGR_ID = "STAR_APP_MENU_CD";

    // 고객지원요청코드목록
    public static final String CUSTOMER_TYPE_CTGR_ID = "CUST_SUPT_UPDT_RQST_TYPE_CD";




    // API URL
    /////////// Real
    //public static final String API_BASE_URL = "https://smfstar.seegenemedical.com/";
    ////////// QA
    public static final String API_BASE_URL = "https://smfstar-qa.seegenemedical.com";
    ////////// DEV
    //public static final String API_BASE_URL =  "https://smfstar-dev.seegenemedical.com/";


    // 검사 결과 URL
    // REAL
    //public static final String URL_REGISTER_RESULTS = "https://trms.seegenemedical.com/mobileStarRstUserPopup.do";
    // QA
    //public static final String URL_REGISTER_RESULTS = "http://100.100.100.95:8082/mobileStarRstUserPopup.do";
    // DEV
    public static final String URL_REGISTER_RESULTS = "http://100.100.100.170:9001/mobileStarRstUserPopup.do";

    // 항목 조회 URL
    // REAL
    //public static final String URL_ITEM_SEARCH = "https://trms.seegenemedical.com/mobileStarTestItemView.do";
    // QA
    //public static final String URL_ITEM_SEARCH = "http://100.100.100.95:8082/mobileStarTestItemView.do";
    // DEV
    public static final String URL_ITEM_SEARCH = "http://100.100.100.170:9001/mobileStarTestItemView.do";


    // 결과 조회 URL
    // REAL
    //public static final String URL_RESULTS_SEARCH = "https://trms.seegenemedical.com/mobileStarRstUserPopup.do";
    // QA
    //public static final String URL_RESULTS_SEARCH = "http://100.100.100.95:8082/mobileStarRstUserPopup.do";
    // DEV
    public static final String URL_RESULTS_SEARCH = "http://100.100.100.170:9001/mobileStarRstUserPopup.do";


    // 신규 버전 안내 페이지
    public static final String URL_NEW_VERSION = "https://starinfo.seegenemedical.com";
    public static final String STR_NEW_VERSION = "새 버전의 App이 있습니다.\n새 버전을 다운로드 하십시요.";


}
