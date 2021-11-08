package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import java.util.List;

@Dao
public interface FertilizerCropMasterDAO {

    @Query("SELECT * FROM fertilizercropmaster")
    LiveData<List<ModelFertilizerCropMaster>> getAllcrops();

    @Query("Select * from fertilizercropmaster where croptype= :croptype")
    LiveData<List<ModelFertilizerCropMaster>> getCropType(String croptype);

    @Query("Select * from fertilizercropmaster where cropcode= :cropcode")
    LiveData<List<ModelFertilizerCropMaster>> getcropcode(String cropcode);

    @Query("Select * from fertilizercropmaster where cropcode= :cropcode AND croptype= :croptype")
    LiveData<List<ModelFertilizerCropMaster>> getcropcodetype(String cropcode, String croptype);

    @Query("Select * from fertilizercropmaster where cropname_eng= :cropname")
    LiveData<List<ModelFertilizerCropMaster>> getCropDetails(String cropname);

    @Insert
    void insertcrop(ModelFertilizerCropMaster fcropmaster);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatefcropmaster(ModelFertilizerCropMaster fcropmaster);

    @Delete
    void deletecropmaster(ModelFertilizerCropMaster fcropmaster);

    @Query("DELETE FROM fertilizercropmaster")
    void deletecropmaster();

}
