package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelCropSurveyDetails;

import java.util.List;

//Programming by Harsha  for version 1.0 release
@Dao
public interface CropDetailsDAO {
    @Query("SELECT * FROM farmer_crop_survey_details ")
    LiveData<List<ModelCropSurveyDetails>> loadAllCropDetails();

    @Query("Select * from farmer_crop_survey_details where farmerId= :fID")
    LiveData<List<ModelCropSurveyDetails>> getAllFarmerCropBasedOnFid(String fID);

    @Query("Select * from farmer_crop_survey_details where farmerId= :fID AND year = :year")
    LiveData<List<ModelCropSurveyDetails>> getSeason(String fID,String year);

    @Query("Select * from farmer_crop_survey_details where farmerId= :fID AND year = :year AND season = :season")
    LiveData<List<ModelCropSurveyDetails>> getCropsDetails(String fID,String year,String season);


    @Insert
    void insertCropDetails(ModelCropSurveyDetails cropData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCropDetails(ModelCropSurveyDetails cropData);

    @Delete
    void deleteCropDetails(ModelCropSurveyDetails cropData);

    @Query("DELETE FROM farmer_crop_survey_details")
    void deleteAllCropSurvey();
}
