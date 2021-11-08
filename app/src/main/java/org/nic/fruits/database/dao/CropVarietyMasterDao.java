package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelCropVarietyMaster;

import java.util.List;

@Dao
public interface CropVarietyMasterDao {

    @Query("SELECT * FROM cropvarietymaster ")
    LiveData<List<ModelCropVarietyMaster>> loadAllCropVarietyMaster();

    @Query("Select * from cropvarietymaster where cropcode= :cID")
    LiveData<List<ModelCropVarietyMaster>> getAllCropVarietyBasedonCropCode(int cID);

    @Insert
    void insertCropVarietyMaster(ModelCropVarietyMaster cvMaster);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCropVarietyMaster(ModelCropVarietyMaster cvMaster);

    @Delete
    void deleteCropVarietyMaster(ModelCropVarietyMaster cvMaster);

    @Query("DELETE FROM cropvarietymaster")
    void deleteAllCropVarietyMaster();

}
