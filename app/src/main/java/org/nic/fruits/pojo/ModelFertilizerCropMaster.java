package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//CropMaster_F.xml
@Entity(tableName="fertilizercropmaster")
public class ModelFertilizerCropMaster {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String cropcode;
    private String cropname_eng;
    private String cropname_kn;
    private String croptype;

    @Ignore
    public ModelFertilizerCropMaster(int id, String cropcode, String cropname_eng, String cropname_kn, String croptype) {
        this.id = id;
        this.cropcode = cropcode;
        this.cropname_eng = cropname_eng;
        this.cropname_kn = cropname_kn;
        this.croptype = croptype;
    }

    public ModelFertilizerCropMaster(String cropcode, String cropname_eng, String cropname_kn, String croptype) {
        this.cropcode = cropcode;
        this.cropname_eng = cropname_eng;
        this.cropname_kn = cropname_kn;
        this.croptype = croptype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCropcode() {
        return cropcode;
    }

    public void setCropcode(String cropcode) {
        this.cropcode = cropcode;
    }

    public String getCropname_eng() {
        return cropname_eng;
    }

    public void setCropname_eng(String cropname_eng) {
        this.cropname_eng = cropname_eng;
    }

    public String getCropname_kn() {
        return cropname_kn;
    }

    public void setCropname_kn(String cropname_kn) {
        this.cropname_kn = cropname_kn;
    }

    public String getCroptype() {
        return croptype;
    }

    public void setCroptype(String croptype) {
        this.croptype = croptype;
    }
}

