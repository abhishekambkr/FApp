package org.nic.fruits.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelIrrigationType;

import java.util.List;

@Dao
public interface IrrigationTypeDao {

    @Query("SELECT * FROM irrigationtype")
    LiveData<List<ModelIrrigationType>> getAllirrigationtype();

    @Query("Select * from irrigationtype where irrigationtype= :irrigationtype")
    LiveData<List<ModelIrrigationType>> getirrigationbasedontype(String irrigationtype);

    @Insert
    void insertirrigationtype(ModelIrrigationType modelIrrigationType);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateirrigationtype(ModelIrrigationType modelIrrigationType);

    @Delete
    void deleteirrigationtype(ModelIrrigationType modelIrrigationType);

    @Query("DELETE FROM irrigationtype")
    void deleteirrigationtype();
}

