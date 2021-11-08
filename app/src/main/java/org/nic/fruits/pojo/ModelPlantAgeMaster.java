package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//PlantAgeMaster.xml
@Entity(tableName="plantagemaster")
public class ModelPlantAgeMaster {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String plantid;
    private String plantname;

    @Ignore
    public ModelPlantAgeMaster(int id, String plantid, String plantname) {
        this.id = id;
        this.plantid = plantid;
        this.plantname = plantname;
    }

    public ModelPlantAgeMaster(String plantid, String plantname) {
        this.plantid = plantid;
        this.plantname = plantname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlantid() {
        return plantid;
    }

    public void setPlantid(String plantid) {
        this.plantid = plantid;
    }

    public String getPlantname() {
        return plantname;
    }

    public void setPlantname(String plantname) {
        this.plantname = plantname;
    }
}
