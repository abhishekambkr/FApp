package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;

import java.util.List;

@Dao
public interface CropFertilizerMasterDAO {

    @Query("SELECT * FROM cropfertilizermaster")
    LiveData<List<ModelCropFertilizerMasternpk>> getAllcrops();

    @Query("Select * from cropfertilizermaster where cf_crop_code= :cropcode")
    LiveData<List<ModelCropFertilizerMasternpk>> getAllcropbasedcropcode(String cropcode);

    @Query("Select * from cropfertilizermaster where cf_crop_code= :cropcode AND cf_irrigation_type = :irrigationtype AND cf_plant_age = :plantage")
    LiveData<List<ModelCropFertilizerMasternpk>> getnutrients(String cropcode,String irrigationtype,String plantage);

    @Query("Select * from cropfertilizermaster where cf_crop_code= :cropcode AND cf_irrigation_type = :irrigationtype AND cf_plant_age = :plantage")
    LiveData<List<ModelCropFertilizerMasternpk>> getfertilizers(String cropcode,String irrigationtype,String plantage);

    @Query("Select * from cropfertilizermaster where cf_crop_code= :cropcode AND cf_irrigation_type= 'I' ORDER BY cf_plant_age desc LIMIT 1")
    LiveData<List<ModelCropFertilizerMasternpk>> getCropNPKdetails(String cropcode);

    @Query("Select * from cropfertilizermaster where cf_crop_code= :cropcode")
    List<ModelCropFertilizerMasternpk> getcropforverify(String cropcode);

    @Insert
    void insertfertilizercrop(ModelCropFertilizerMasternpk masternpk);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatecropfertilizermaster(ModelCropFertilizerMasternpk masternpk);

    @Delete
    void deletecropfertilizermaster(ModelCropFertilizerMasternpk masternpk);

    @Query("DELETE FROM cropfertilizermaster")
    void deletecropfertilizermaster();

}
