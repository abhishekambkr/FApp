package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelCropRegistration;

import java.util.List;

@Dao
public interface CropRegistrationDao {

        @Query("SELECT * FROM cropregistration ")
        LiveData<List<ModelCropRegistration>> loadAllCropRegistrationDetails();

        @Query("Select * from cropregistration where farmerId= :fID")
        LiveData<List<ModelCropRegistration>> getCropRegistrationDetailsBasedOnFid(String fID);

        @Query("Select * from cropregistration where farmerId= :fID AND crid = :crID")
        LiveData<List<ModelCropRegistration>> getCropRegistrationDetailsBasedOnFidCrid(String fID,String crID);


      /*  @Query("Select surveynumber from cropregistration where farmerId= :fID AND surveynumber = :surveynum")
        LiveData<String>getCropRegistrationDetailsBasedOnFidSurveynum(String fID,String surveynum);*/

        @Query("SELECT * FROM cropregistration WHERE farmerId = :fID AND surveynumber = :surveynum AND year = :year AND season = :season AND ownerid = :ownerid")
        int isDataExist(String fID,String surveynum,String year,String season,String ownerid);

        @Insert
        void insertCropRegistrationDetails(ModelCropRegistration cropRegistration);

        @Update(onConflict = OnConflictStrategy.REPLACE)
        void updateCropRegistrationDetails(ModelCropRegistration cropRegistration);

        @Delete
        void deleteCropRegistrationDetails(ModelCropRegistration cropRegistration);

        @Query("DELETE FROM cropregistration")
        void deleteAllCropRegistration();

}
