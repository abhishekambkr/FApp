package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelFertilizerNameMaster;

import java.util.List;

@Dao
public interface FertilizerNameMasterDAO {

    @Query("SELECT * FROM fertilizernamemaster")
    LiveData<List<ModelFertilizerNameMaster>> getAllFertilizerNames();

    @Query("Select * from fertilizernamemaster where fertilizertype= :fertilizertype")
    LiveData<List<ModelFertilizerNameMaster>> getAllFertilizerNamesBasedontype(String fertilizertype);

    @Query("Select * from fertilizernamemaster where feid= :fid ORDER BY fertilizernutrient DESC")
    LiveData<List<ModelFertilizerNameMaster>> getAllFertilizerBasedonid(String fid);

    @Query("Select * from fertilizernamemaster where feid= :fertilizerid")
    LiveData<List<ModelFertilizerNameMaster>> getFertilizerNPK(String fertilizerid);//String fertilizernutrient,

    @Insert
    void insertFertilizerNames(ModelFertilizerNameMaster modelFertilizerNameMaster);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFertilizerNames(ModelFertilizerNameMaster modelFertilizerNameMaster);

    @Delete
    void deleteFertilizerNames(ModelFertilizerNameMaster modelFertilizerNameMaster);

    @Query("DELETE FROM fertilizernamemaster")
    void deleteFertilizerNames();

}
