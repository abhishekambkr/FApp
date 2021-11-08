package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelCropMasterType;

import java.util.List;

@Dao
public interface CropMasterType {

    @Query("SELECT * FROM cropmastertype")
    LiveData<List<ModelCropMasterType>> getAllcroptype();

    @Query("Select * from cropmastertype where croptype= :croptype")
    LiveData<List<ModelCropMasterType>> getcropbasedoncroptype(String croptype);

    @Insert
    void insertcrop(ModelCropMasterType modelCropMasterType);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatefcropmaster(ModelCropMasterType modelCropMasterType);

    @Delete
    void deletecropmaster(ModelCropMasterType modelCropMasterType);

    @Query("DELETE FROM cropmastertype")
    void deletecropmaster();
}
