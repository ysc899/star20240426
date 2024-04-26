package kr.co.seesoft.nemo.starnemoapp.db.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDAO<T> {

    @Insert
    void insert(T... t);

    @Update
    void update(T... t);

    @Delete
    void delete(T... t);
}
