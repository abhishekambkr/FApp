package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelMixedCropRegistration;

import java.util.List;

@Dao
public interface MixedCropRegistrationDao {

    @Query("SELECT * FROM mixedcropsregistration ")
    LiveData<List<ModelMixedCropRegistration>> loadAllCropRegistrationDetails();

    @Query("Select * from mixedcropsregistration where farmerId= :fID")
    LiveData<List<ModelMixedCropRegistration>> getMixedCropRegistrationDetailsBasedOnFid(String fID);

    @Query("Select * from mixedcropsregistration where farmerId= :fID AND crid = :crID")
    LiveData<List<ModelMixedCropRegistration>> getMixedCropRegistrationDetailsBasedOnFidCrid(String fID, String crID);


      /*  @Query("Select surveynumber from cropregistration where farmerId= :fID AND surveynumber = :surveynum")
        LiveData<String>getCropRegistrationDetailsBasedOnFidSurveynum(String fID,String surveynum);*/

    @Query("SELECT * FROM mixedcropsregistration WHERE farmerId = :fID AND surveynumber = :surveynum AND year = :year AND season = :season AND ownerid = :ownerid")
    int isDataExist(String fID,String surveynum,String year,String season,String ownerid);

    @Insert
    void insertMixedCropRegistrationDetails(ModelMixedCropRegistration mcropRegistration);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCropRegistrationDetails(ModelMixedCropRegistration mcropRegistration);

    @Delete
    void deleteCropRegistrationDetails(ModelMixedCropRegistration mcropRegistration);

    @Query("DELETE FROM mixedcropsregistration")
    void deleteAllMultipleCropRegistration();
}
