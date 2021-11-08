package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import org.nic.fruits.pojo.ModelCropMaster;
import java.util.List;

@Dao
public interface CropMasterDao {

    @Query("SELECT * FROM cropmaster")
    LiveData<List<ModelCropMaster>> getAllcrops();

    @Query("Select * from cropmaster where croptype= :Seasonalcrop")
    LiveData<List<ModelCropMaster>> getMixedcrops(String Seasonalcrop);

    @Query("Select * from cropmaster where cropcode= :cropcode")
    LiveData<List<ModelCropMaster>> getcropcode(String cropcode);

    @Query("Select * from cropmaster where cropcode= :cropcode AND croptype= :croptype")
    LiveData<List<ModelCropMaster>> getcropbasedoncropcodecroptype(String cropcode, String croptype);

    @Query("Select * from cropmaster where cropcode= :cropcode AND croptype= :croptype AND intercroptype = :intercroptype")
    LiveData<List<ModelCropMaster>> getcropbasedoncropcodecroptypeintercroptype(String cropcode, String croptype,String intercroptype);

    @Insert
    void insertcrop(ModelCropMaster cropmaster);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatefcropmaster(ModelCropMaster cropmaster);

    @Delete
    void deletecropmaster(ModelCropMaster cropmaster);

    @Query("DELETE FROM cropmaster")
    void deletecropmaster();

}
