package kr.co.seesoft.nemo.starnemoapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.db.vo.LocationVO;

/**
 * https://medium.com/harrythegreat/%EB%B2%88%EC%97%AD-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-room-7%EA%B0%80%EC%A7%80-%EC%9C%A0%EC%9A%A9%ED%95%9C-%ED%8C%81-18252a941e27
 * 참고
 */
@Dao
public interface LocationDAO extends BaseDAO<LocationVO> {
    @Query("SELECT * FROM locations ORDER BY seq DESC")
    LiveData<List<LocationVO>> findAll();

    @Query("SELECT * FROM locations ORDER BY seq DESC LIMIT 1")
    LiveData<LocationVO> findLastOne();

//    @Query("SELECT * FROM pictures WHERE hospital_key = :hospitalKey And ymd = :ymd ORDER BY seq DESC")
//    LiveData<List<PictureVO>> findByHospitalAndYmd(String hospitalKey, String ymd);
//
//
    @Query("DELETE FROM locations WHERE date < :beforeDate ")
    int deleteByBeforeDate(Date beforeDate);
//
//    @Query("DELETE FROM pictures WHERE hospital_key = :hospitalKey And ymd = :ymd And file_path = :filePath")
//    void deleteByHospitalAndYmdAndPath(String hospitalKey, String ymd, String filePath);

//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    void insertPicture(PictureVO... pictureVOS);
}
