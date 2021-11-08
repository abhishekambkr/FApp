package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelweatherDetails;

import java.util.List;



//Programming by Harsha  for version 1.0 release
@Dao
public interface WeatherDetailsDAO {

    @Query("SELECT * FROM weather_details ")
    LiveData<List<ModelweatherDetails>> loadAllWeatherDetails();

    @Query("Select * from weather_details where farmerId= :fID")
    LiveData<List<ModelweatherDetails>> getAllWeatherDetailsBasedOnFid(String fID);


    @Query("Select * from weather_details where farmerId= :fID")
    List<ModelweatherDetails> getAllWeatherDetailsBasedOnFidList(String fID);

    @Query("Select * from weather_details where panchayathName= :villageName")
    LiveData<List<ModelweatherDetails>> getAllModelweatherDetailsBasedOnVillageName(String villageName);


    @Insert
    void insertWeatherDetails(ModelweatherDetails weatherData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeatherDetails(ModelweatherDetails weatherData);

    @Delete
    void deleteWeatherDetails(ModelweatherDetails weatherData);

    @Query("DELETE FROM weather_details")
    void deleteAllWeather();

    @Query("DELETE FROM weather_details where farmerId = :fID")
    void deleteAllWeatherDetailsBasedOnFarmerID(String fID);
}
