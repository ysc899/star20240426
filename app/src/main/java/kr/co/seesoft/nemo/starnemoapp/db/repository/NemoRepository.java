package kr.co.seesoft.nemo.starnemoapp.db.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.db.AppDataBase;
import kr.co.seesoft.nemo.starnemoapp.db.dao.LocationDAO;
import kr.co.seesoft.nemo.starnemoapp.db.dao.PictureDAO;
import kr.co.seesoft.nemo.starnemoapp.db.dao.TakingOverDAO;
import kr.co.seesoft.nemo.starnemoapp.db.vo.LocationVO;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.db.vo.TakingOverVO;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;

/**
 * https://m.blog.naver.com/PostView.nhn?blogId=spring1a&logNo=221251239170&proxyReferer=https:%2F%2Fwww.google.com%2F
 * 참고
 */
public class NemoRepository {

    private PictureDAO pictureDAO;
    private LocationDAO locationDAO;
    private TakingOverDAO takingOverDAO;

    private LiveData<List<PictureVO>> allDatas;

    private LiveData<List<PictureVO>> hospitalYmdDatas;

    private LiveData<List<TakingOverVO>> hospitalYmdTakingOver;

//    private LiveData<List<LocationVO>> locAllData;
    private LiveData<LocationVO> locLastOneData;

    public NemoRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        pictureDAO = db.pictureDAO();
        locationDAO = db.locationDAO();
        takingOverDAO = db.takingOverDAO();

//        locAllData = locationDAO.findAll();
        locLastOneData = locationDAO.findLastOne();
    }

    public NemoRepository(Context context, boolean getAllFlag){
        AppDataBase db = AppDataBase.getInstance(context);
        pictureDAO = db.pictureDAO();
        allDatas = pictureDAO.findAll();
    }

    public NemoRepository(Context context, String hospitalCd, String ymd){
        AppDataBase db = AppDataBase.getInstance(context);
        pictureDAO = db.pictureDAO();
        takingOverDAO = db.takingOverDAO();

        hospitalYmdDatas = pictureDAO.findByHospitalAndYmd(hospitalCd, ymd);

        hospitalYmdTakingOver = takingOverDAO.findByHospitalAndYmd(hospitalCd, ymd);

    }

    public NemoRepository(Context context, String ymd){
        AppDataBase db = AppDataBase.getInstance(context);
        pictureDAO = db.pictureDAO();

        hospitalYmdDatas = pictureDAO.findByYmd(ymd);

    }

    public void insert(@NotNull PictureVO p){
//        AndroidUtil.log("Insert : " + p);
        AppDataBase.databaseWriteExecutor.execute(()->{
            pictureDAO.insert(p);
        });
    }

    public void insert(@NotNull LocationVO l){
        AppDataBase.databaseWriteExecutor.execute(()->{
            locationDAO.insert(l);
        });
    }

    public void insert(@NotNull TakingOverVO t){
        AppDataBase.databaseWriteExecutor.execute(()->{
            takingOverDAO.insert(t);
        });
    }

    public void update(@NotNull PictureVO p){
        AppDataBase.databaseWriteExecutor.execute(()->{
            pictureDAO.update(p);
        });
    }

    public void update(@NotNull TakingOverVO t){
        AppDataBase.databaseWriteExecutor.execute(()->{
            takingOverDAO.update(t);
        });
    }

    public void deleteByHospitalAndYmdAndPath(@NotNull String hospitalCd, @NotNull String ymd, @NotNull String filePath){
        AppDataBase.databaseWriteExecutor.execute(()->{
            pictureDAO.deleteByHospitalAndYmdAndPath(hospitalCd, ymd, filePath);
        });
    }
    public void deleteByYmd(@NotNull String ymd){
        AppDataBase.databaseWriteExecutor.execute(()->{
            int delCount = pictureDAO.deleteByYmd( ymd);
            AndroidUtil.log("delCount : " + delCount);
        });
    }

    public void deleteLocationsByBeforeDate(@NotNull Date date){
        AppDataBase.databaseWriteExecutor.execute(()->{
            int delCount = locationDAO.deleteByBeforeDate( date);
            AndroidUtil.log("deleteLocationsCount : " + delCount);
        });
    }


    public LiveData<List<PictureVO>> findAll(){
        return this.allDatas;
    }


    public LiveData<List<PictureVO>> getHospitalYmdDatas() {
        return this.hospitalYmdDatas;
    }

    public LiveData<List<TakingOverVO>> getHospitalYmdTakingOver() {
        return this.hospitalYmdTakingOver;
    }

//    public LiveData<List<LocationVO>> findLocAll(){return this.locAllData;}


    public LiveData<LocationVO> getLocLastOneData() {
        return locLastOneData;
    }
}
