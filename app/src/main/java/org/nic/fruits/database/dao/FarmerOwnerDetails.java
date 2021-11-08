package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelOwnerDetails;

import java.util.List;

@Dao
public interface FarmerOwnerDetails {

    @Query("SELECT * FROM farmer_owner_details")
    LiveData<List<ModelOwnerDetails>> loadAllOwnerDetails();

    @Query("Select * from farmer_owner_details where farmerId= :fID")
    LiveData<List<ModelOwnerDetails>> getAllOwnerBasedOnFid(String fID);


    @Query("Select * from farmer_owner_details where farmerId= :fID AND surveynumber= :surveySpinnerValue")
    LiveData<List<ModelOwnerDetails>> getAllOwnerBasedOnFidsurvey(String fID, String surveySpinnerValue);

    @Query("Select * from farmer_owner_details where farmerId= :fID")
    List<ModelOwnerDetails> getAllOwnerBasedOnFidNoLive(String fID);

    @Query("Select * from farmer_owner_details where farmerId= :fID")
    List<ModelOwnerDetails> getAlldataSurveyNos(String fID);
/*
    @Query("Select surveynumber from farmer_owner_details where farmerId= :fID")
    List<ModelOwnerDetails> getAllSurveyNos(String fID);

    @Query("Select ownernumber from farmer_owner_details where farmerId= :fID")
    List<ModelOwnerDetails> getAllOwnerNos(String fID);

    @Query("Select mainownernumber from farmer_owner_details where farmerId= :fID")
    List<ModelOwnerDetails> getAllMainOwnerNos(String fID);

    @Query("Select relativename from farmer_owner_details where farmerId= :fID")
    List<ModelOwnerDetails> getownerrelativenames(String fID);*/


    @Insert
    void insertOwnerDetails(ModelOwnerDetails ownerDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatefarmerownerDetails(ModelOwnerDetails ownerDetails);

    @Delete
    void deletefarmerownerDetails(ModelOwnerDetails ownerDetails);

    @Query("DELETE FROM farmer_owner_details")
    void deleteAllOwner();

}
