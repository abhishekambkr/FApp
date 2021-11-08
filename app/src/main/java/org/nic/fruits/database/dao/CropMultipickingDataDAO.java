package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import org.nic.fruits.pojo.ModelCropMultipickingData;

import java.util.List;

@Dao
public interface CropMultipickingDataDAO {

    @Query("SELECT * FROM cropmultipickdata ")
    LiveData<List<ModelCropMultipickingData>> loadAllCropMultipickWeeklyData();

    @Query("Select * from cropmultipickdata where farmerId= :fID AND cropregid = :crID")
    LiveData<List<ModelCropMultipickingData>> getMultipickCropWeeklyDataBasedonfidcrid(String fID, String crID);

    @Query("SELECT * FROM cropmultipickdata WHERE farmerId = :fID AND cropregid = :crID AND week = :weeknumber AND surveynumber = :surveynum AND year = :year AND season = :season AND ownerid = :ownerid")
    int isCropWeeklyMultipickDataExist(String fID,String crID,String weeknumber,String surveynum,String year,String season,String ownerid);


    @Query("Select * from cropmultipickdata where farmerid= :fID AND cropregid = :crID AND surveynumber = :surveynum AND yearcode = :yearcode AND seasoncode = :seasoncode AND ownerid = :ownerid AND cropname = :cropname")
    LiveData<List<ModelCropMultipickingData>> getmultipick(String fID,String crID,String surveynum,String yearcode,String seasoncode,String ownerid,String cropname);

    @Insert
    void insertCropweeklypickingData(ModelCropMultipickingData multipickingData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCropWeeklyDataDetails(ModelCropMultipickingData multipickingData);

    @Delete
    void deleteCropWeeklyDataDetails(ModelCropMultipickingData multipickingData);

    @Query("DELETE FROM cropweeklydata")
    void deleteAllCropWeeklyData();

}
