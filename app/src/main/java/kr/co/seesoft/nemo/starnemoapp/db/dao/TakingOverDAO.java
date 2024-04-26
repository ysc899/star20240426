package kr.co.seesoft.nemo.starnemoapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.db.vo.TakingOverVO;

/**
 * https://medium.com/harrythegreat/%EB%B2%88%EC%97%AD-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-room-7%EA%B0%80%EC%A7%80-%EC%9C%A0%EC%9A%A9%ED%95%9C-%ED%8C%81-18252a941e27
 * 참고
 */
@Dao
public interface TakingOverDAO extends BaseDAO<TakingOverVO> {
    @Query("SELECT * FROM takingover ORDER BY seq DESC")
    LiveData<List<TakingOverVO>> findAll();

    @Query("SELECT * FROM takingover WHERE hospital_key = :hospitalKey And ymd = :ymd ORDER BY seq DESC")
    LiveData<List<TakingOverVO>> findByHospitalAndYmd(String hospitalKey, String ymd);


//    @Query("DELETE FROM pictures WHERE ymd = :ymd ")
//    int deleteByYmd(String ymd);
//
//    @Query("DELETE FROM pictures WHERE hospital_key = :hospitalKey And ymd = :ymd And file_path = :filePath")
//    void deleteByHospitalAndYmdAndPath(String hospitalKey, String ymd, String filePath);

//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    void insertPicture(PictureVO... pictureVOS);
}
