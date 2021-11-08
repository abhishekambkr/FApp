package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.nic.fruits.pojo.ModelPaymentDetails;

import java.util.List;



//Programming by Harsha  for version 1.0 release
@Dao
public interface PaymentDetailsDAO {

    @Query("SELECT * FROM farmer_payment_details ")
    LiveData<List<ModelPaymentDetails>> loadAllPaymentDetails();

    @Query("Select * from farmer_payment_details where farmerId= :fID")
    LiveData<List<ModelPaymentDetails>> getAllPaymentDetailsBasedOnFid(String fID);

    @Insert
    void insertPayDetails(ModelPaymentDetails payment);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePaymentDetails(ModelPaymentDetails payment);

    @Delete
    void deletePaymentDetails(ModelPaymentDetails payment);

    @Query("DELETE FROM farmer_payment_details")
    void deleteAllPayment();
}
