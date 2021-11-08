package org.nic.fruits.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import org.nic.fruits.pojo.ModelPlantAgeMaster;

import java.util.List;

@Dao
public interface PlantAgeMasterDAO {

    @Query("SELECT * FROM plantagemaster")
    LiveData<List<ModelPlantAgeMaster>> getAllPlants();

    @Query("Select * from plantagemaster where plantid= :plantid")
    LiveData<List<ModelPlantAgeMaster>> getplantagebasedonpid(String plantid);

    @Insert
    void insertplantage(ModelPlantAgeMaster modelPlantAgeMaster);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatefplantagemaster(ModelPlantAgeMaster modelPlantAgeMaster);

    @Delete
    void deleteplantagemaster(ModelPlantAgeMaster modelPlantAgeMaster);

    @Query("DELETE FROM plantagemaster")
    void deleteplantagemaster();

}
