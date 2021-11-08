package org.nic.fruits.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelFarmerDetails;

import java.util.List;



//Programming by Harsha  for version 1.0 release
@Dao
public interface FarmerDetailsDAO {

    @Query("SELECT * FROM farmer_farmer_details ")
    LiveData<List<ModelFarmerDetails>> loadAllFarmerData();

    @Query("Select * from farmer_farmer_details where farmerId= :fID")
    LiveData<List<ModelFarmerDetails>> getAllFarmerDetailsBasedOnFid(String fID);

    @Query("SELECT DISTINCT farmerNameInKannada from farmer_farmer_details where farmerId= :fID")
    public String getFarmerName(String fID);

    @Insert
    void insertFarmerDetails(ModelFarmerDetails farmerData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFarmerDetails(ModelFarmerDetails farmerData);

    @Delete
    void deleteFarmerDetails(ModelFarmerDetails farmerData);

    @Query("DELETE FROM farmer_farmer_details")
    void deleteAllFarmer();
}
