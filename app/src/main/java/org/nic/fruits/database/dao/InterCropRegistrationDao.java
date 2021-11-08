package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import org.nic.fruits.pojo.ModelInterCropRegistration;

import java.util.List;

@Dao
public interface InterCropRegistrationDao {

    @Query("SELECT * FROM intercropsregistration ")
    LiveData<List<ModelInterCropRegistration>> loadAllCropRegistrationDetails();

    @Query("Select * from intercropsregistration where farmerId= :fID")
    LiveData<List<ModelInterCropRegistration>> getInterCropRegistrationDetailsBasedOnFid(String fID);

    @Query("Select * from intercropsregistration where farmerId= :fID AND crid = :crID")
    LiveData<List<ModelInterCropRegistration>> getInterCropRegistrationDetailsBasedOnFidCrid(String fID, String crID);


      /*  @Query("Select surveynumber from cropregistration where farmerId= :fID AND surveynumber = :surveynum")
        LiveData<String>getCropRegistrationDetailsBasedOnFidSurveynum(String fID,String surveynum);*/

    @Query("SELECT * FROM intercropsregistration WHERE farmerId = :fID AND surveynumber = :surveynum AND year = :year AND season = :season AND ownerid = :ownerid")
    int isDataExist(String fID,String surveynum,String year,String season,String ownerid);

    @Insert
    void insertinterCropRegistrationDetails(ModelInterCropRegistration icropRegistration);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCropRegistrationDetails(ModelInterCropRegistration icropRegistration);

    @Delete
    void deleteCropRegistrationDetails(ModelInterCropRegistration icropRegistration);

    @Query("DELETE FROM intercropsregistration")
    void deleteAllInterCropRegistration();


}
