package kr.co.seesoft.nemo.starnemoapp.db.vo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "takingover", indices = {@Index(value = {"ymd", "hospital_key"})})
public class TakingOverVO implements Serializable {

    /** pk */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "seq")
    public long seq;

    /** 날짜 */
    @ColumnInfo(name = "ymd")
    public String ymd;

    /** 병원 키 */
    @ColumnInfo(name = "hospital_key")
    public String hospitalKey;

    /** 온도 */
    @ColumnInfo(name = "temperature")
    public float temperature;

    /** sst 갯수 */
    @ColumnInfo(name = "sst")
    public int sst;

    /** edta 갯수 */
    @ColumnInfo(name = "edta")
    public int edta;

    /** urine 갯수 */
    @ColumnInfo(name = "urine")
    public int urine;

    /** 조직 갯수 */
    @ColumnInfo(name = "bio")
    public int bio;

    /** 기타 갯수 */
    @ColumnInfo(name = "other")
    public int other;

    /** 특이사항 */
    @ColumnInfo(name = "issue")
    public String issue;

    /** 인계 사인 파일 경로로 */
    @ColumnInfo(name = "taking_over_path")
    public String takingOverPath;

    /** 인수 사인 파일 경로로 */
    @ColumnInfo(name = "take_over_path")
    public String takeOverPath;

    /** 시간 */
    @ColumnInfo(name = "date")
    public Date date;

    @Override
    public String toString() {
        return "TakingOverVO{" +
                "seq=" + seq +
                ", ymd='" + ymd + '\'' +
                ", hospitalKey='" + hospitalKey + '\'' +
                ", temperature=" + temperature +
                ", sst=" + sst +
                ", edta=" + edta +
                ", urine=" + urine +
                ", bio=" + bio +
                ", other=" + other +
                ", issue='" + issue + '\'' +
                ", takingOverPath='" + takingOverPath + '\'' +
                ", takeOverPath='" + takeOverPath + '\'' +
                ", date=" + date +
                '}';
    }
}
