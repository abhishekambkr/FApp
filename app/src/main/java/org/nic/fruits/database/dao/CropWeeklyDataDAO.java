package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelCropWeeklyData;

import java.util.List;

@Dao
public interface CropWeeklyDataDAO {

    @Query("SELECT * FROM cropweeklydata ")
    LiveData<List<ModelCropWeeklyData>> loadAllCropWeeklyData();

    @Query("Select * from cropweeklydata where farmerId= :fID AND cropregid = :crID")
    LiveData<List<ModelCropWeeklyData>> getCropWeeklyDataBasedonfidcrid(String fID, String crID);

    @Query("SELECT * FROM cropweeklydata WHERE farmerId = :fID AND cropregid = :crID AND weeknumber = :weeknumber AND surveynumber = :surveynum AND year = :year AND season = :season AND ownerid = :ownerid")
    int isCropWeeklyDataExist(String fID,String crID,String weeknumber,String surveynum,String year,String season,String ownerid);

    @Query("Select weeknumber from cropweeklydata where farmerid= :fID AND cropregid = :crID AND surveynumber = :surveynum AND yearcode = :yearcode AND seasoncode = :seasoncode AND ownerid = :ownerid AND cropname = :cropname AND weeknumber = :weekvalue")
    int isWeekExist(String fID,String crID,String surveynum,String yearcode,String seasoncode,String ownerid,String cropname,String weekvalue);

    @Query("Select * from cropweeklydata where farmerid= :fID AND cropregid = :crID AND surveynumber = :surveynum AND yearcode = :yearcode AND seasoncode = :seasoncode AND ownerid = :ownerid AND cropname = :cropname")
    LiveData<List<ModelCropWeeklyData>> getWeek(String fID,String crID,String surveynum,String yearcode,String seasoncode,String ownerid,String cropname);

    @Insert
    void insertCropWeeklyData(ModelCropWeeklyData modelCropWeeklyData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCropWeeklyDataDetails(ModelCropWeeklyData modelCropWeeklyData);

    @Delete
    void deleteCropWeeklyDataDetails(ModelCropWeeklyData modelCropWeeklyData);

    @Query("DELETE FROM cropweeklydata")
    void deleteAllCropWeeklyData();
}
