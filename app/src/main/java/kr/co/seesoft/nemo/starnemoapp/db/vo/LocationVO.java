package kr.co.seesoft.nemo.starnemoapp.db.vo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "locations")
public class LocationVO implements Serializable {

    /** pk */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "seq")
    public long seq;

    /** 시간 */
    @ColumnInfo(name = "date")
    public Date date;

    /** 경도 */
    @ColumnInfo(name = "longitude")
    public Double longitude;

    /** 위도 */
    @ColumnInfo(name = "latitude")
    public Double latitude;

    @Override
    public String toString() {
        return "LocationVO{" +
                "seq=" + seq +
                ", date=" + date +
                ", longitude=" + longitude +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
