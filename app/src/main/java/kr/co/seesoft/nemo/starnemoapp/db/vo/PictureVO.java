package kr.co.seesoft.nemo.starnemoapp.db.vo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "pictures", indices = {@Index(value = {"ymd", "hospital_key"})})
public class PictureVO implements  Comparable<PictureVO>,Serializable {

    /** pk */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "seq")
    public long seq;

    /** 촬영 날짜 */
    @ColumnInfo(name = "ymd")
    public String ymd;

    /** 병원 키 */
    @ColumnInfo(name = "hospital_key")
    public String hospitalKey;

    /** 파일 경로 */
    @ColumnInfo(name = "file_path")
    public String filePath;

    /** 서버에 등록된 파일 명 */
    @ColumnInfo(name = "save_file_name")
    public String saveFileName;

    /** 전송 여부 */
    @ColumnInfo(name = "send_flag")
    public boolean sendFlag;

    @Override
    public String toString() {
        return "PictureVO{" +
                "seq=" + seq +
                ", ymd='" + ymd + '\'' +
                ", hospitalKey='" + hospitalKey + '\'' +
                ", filePath='" + filePath + '\'' +
                ", saveFileName='" + saveFileName + '\'' +
                ", sendFlag=" + sendFlag +
                '}';
    }

    @Override
    public int compareTo(PictureVO o) {
        return Long.compare(seq, o.seq);
//        return Integer.compare(order, ro.order);
//        return 0;
    }
}
