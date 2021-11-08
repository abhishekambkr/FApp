package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelFarmerLandDeatails;

import java.util.List;


//Programming by Harsha  for version 1.0 release
@Dao
public interface LandDetailsDao {

    @Query("SELECT * FROM farmer_land_details ")
    LiveData<List<ModelFarmerLandDeatails>> loadAllLandDetails();

    @Query("Select * from farmer_land_details where farmerId= :fID")
    LiveData<List<ModelFarmerLandDeatails>> getAllFarmerLandBasedOnFid(String fID);


    @Query("Select * from farmer_land_details where farmerId= :fID")
    List<ModelFarmerLandDeatails> getAllFarmerLandBasedOnFidNoLive(String fID);


    @Insert
    void insertLandDetails(ModelFarmerLandDeatails landData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateLandDetails(ModelFarmerLandDeatails landData);

    @Delete
    void deleteLandDetails(ModelFarmerLandDeatails landData);

    @Query("DELETE FROM farmer_land_details")
    void deleteAllLand();
}
