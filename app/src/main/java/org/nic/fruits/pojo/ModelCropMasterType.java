package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="cropmastertype")
public class ModelCropMasterType {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String croptype;
    private String croptype_eng;
    private String croptype_kn;

    @Ignore
    public ModelCropMasterType(int id, String croptype, String croptype_eng, String croptype_kn) {
        this.id = id;
        this.croptype = croptype;
        this.croptype_eng = croptype_eng;
        this.croptype_kn = croptype_kn;
    }

    public ModelCropMasterType(String croptype, String croptype_eng, String croptype_kn) {
        this.croptype = croptype;
        this.croptype_eng = croptype_eng;
        this.croptype_kn = croptype_kn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCroptype() {
        return croptype;
    }

    public void setCroptype(String croptype) {
        this.croptype = croptype;
    }

    public String getCroptype_eng() {
        return croptype_eng;
    }

    public void setCroptype_eng(String croptype_eng) {
        this.croptype_eng = croptype_eng;
    }

    public String getCroptype_kn() {
        return croptype_kn;
    }

    public void setCroptype_kn(String croptype_kn) {
        this.croptype_kn = croptype_kn;
    }
}
