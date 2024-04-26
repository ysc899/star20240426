package kr.co.seesoft.nemo.starnemoapp.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.seesoft.nemo.starnemoapp.db.dao.LocationDAO;
import kr.co.seesoft.nemo.starnemoapp.db.dao.PictureDAO;
import kr.co.seesoft.nemo.starnemoapp.db.dao.TakingOverDAO;
import kr.co.seesoft.nemo.starnemoapp.db.vo.LocationVO;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.db.vo.TakingOverVO;

/**
 * https://themach.tistory.com/71 참고
 *
 */
@Database(entities = {PictureVO.class, LocationVO.class, TakingOverVO.class}, version = 4)
@TypeConverters(DataConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    //singleton
    private static volatile AppDataBase INSTANCE;

    /**
     * @return 사진 데이터 베이스 dao
     */
    public abstract PictureDAO pictureDAO();

    /**
     * @return 인수인계 dao
     */
    public abstract TakingOverDAO takingOverDAO();

    /**
     * @return gps 위치 저장 dao
     */
    public abstract LocationDAO locationDAO();


    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static AppDataBase getInstance(Context context){
//        synchronized (sLock){
            if(INSTANCE == null){
                synchronized (AppDataBase.class) {
                    if(INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDataBase.class, "nemo.db")
                                .addMigrations(MIGRATION_3_4)
//                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
            }
            return INSTANCE;
//        }
    }

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE pictures "
                            + "ADD COLUMN save_file_name TEXT");
        }
    };
}
