package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import org.nic.fruits.pojo.ModelCropDetailFertilizer;
import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;

import java.util.List;

@Dao
public interface CropDetailFertilizerDAO {

    @Query("SELECT * FROM cropdetailfertilizer")
    LiveData<List<ModelCropDetailFertilizer>> getCropDetailsfertilizers();

    @Query("Select * from cropdetailfertilizer where fertilizertype= :fertilizertype")
    LiveData<List<ModelCropDetailFertilizer>> getCropDetailFertilizers(String fertilizertype);

    @Query("Select * from cropdetailfertilizer where feid= :fid ORDER BY fertilizernutrient DESC")
    LiveData<List<ModelCropDetailFertilizer>> getAllFertilizerBasedonid(String fid);

    @Query("Select * from cropdetailfertilizer where feid= :fertilizerid")
    LiveData<List<ModelCropDetailFertilizer>> getFertilizerNPK(String fertilizerid);//String fertilizernutrient,

    @Query("Select * from cropdetailfertilizer where feid= :fid")
    List<ModelCropDetailFertilizer> getfertilizerforverify(String fid);

    @Insert
    void insertFertilizerNames(ModelCropDetailFertilizer modelFertilizerNameMaster);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFertilizerNames(ModelCropDetailFertilizer modelFertilizerNameMaster);

    @Delete
    void deleteFertilizerNames(ModelCropDetailFertilizer modelFertilizerNameMaster);

    @Query("DELETE FROM cropdetailfertilizer")
    void deleteFertilizerNames();

    }
