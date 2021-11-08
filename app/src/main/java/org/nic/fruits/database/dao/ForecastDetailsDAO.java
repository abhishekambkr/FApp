package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelForeCastDetails;

import java.util.List;



//Programming by Harsha  for version 1.0 release
@Dao
public interface ForecastDetailsDAO {

    @Query("SELECT * FROM forecast_details ")
    LiveData<List<ModelForeCastDetails>> loadAllForeCastDetails();

    @Query("Select * from forecast_details where farmerId= :fID")
    LiveData<List<ModelForeCastDetails>> getAllForeCastDetailsBasedOnFid(String fID);

    @Query("Select * from forecast_details where farmerId= :fID")
    List<ModelForeCastDetails> getAllForeCastDetailsBasedOnFidList(String fID);

    @Query("Select * from forecast_details where gp= :villageName")
    LiveData<List<ModelForeCastDetails>> getAllForeCastDetailsBasedOnVillageName(String villageName);


    @Insert
    void insertForeCastDetails(ModelForeCastDetails forecastData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateForeCastDetails(ModelForeCastDetails forecastData);

    @Delete
    void deleteForeCastDetails(ModelForeCastDetails forecastData);

    @Query("DELETE FROM forecast_details")
    void deleteAllForeCast();

    @Query("DELETE FROM forecast_details where farmerId = :fID")
    void deleteAllForecastDetailsBasedOnFarmerID(String fID);
}
