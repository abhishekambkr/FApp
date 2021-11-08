package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import org.nic.fruits.pojo.ModelFarmFertilizer;

import java.util.List;

@Dao
public interface FarmFertilizerDAO {

    @Query("SELECT * FROM farmfertilizer ")
    LiveData<List<ModelFarmFertilizer>> loadAllFarmFertilizers();

    @Query("Select * from farmfertilizer where farmerId= :fID AND district = :district AND taluk = :taluk AND hobli = :hobli AND village = :village AND survey = :survey AND cropname = :crop")
    LiveData<List<ModelFarmFertilizer>> getData(String fID,String district,String taluk,String hobli,String village,String survey,String crop);

    @Insert
    void insertCropDetails(ModelFarmFertilizer modelFarmFertilizer);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFarmFertilizer(ModelFarmFertilizer modelFarmFertilizer);

    @Delete
    void deleteFarmFertilizer(ModelFarmFertilizer modelFarmFertilizer);

    @Query("DELETE FROM farmfertilizer")
    void deleteAllFarmFertilizer();

}
