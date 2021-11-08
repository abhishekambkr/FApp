package org.nic.fruits.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName="cropmaster")
public class ModelCropMaster implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String cropcode;
    private String cropname_eng;
    private String cropname_kn;  
    private String croptype;
    private String intercroptype;
    private String cropcategory;
    private String cropgroup;
    private String crop_link_code;

    @Ignore
    public ModelCropMaster(int id, String cropcode, String cropname_eng, String cropname_kn, String croptype, String intercroptype, String cropcategory, String cropgroup, String crop_link_code) {
        this.id = id;
        this.cropcode = cropcode;
        this.cropname_eng = cropname_eng;
        this.cropname_kn = cropname_kn;
        this.croptype = croptype;
        this.intercroptype = intercroptype;
        this.cropcategory = cropcategory;
        this.cropgroup = cropgroup;
        this.crop_link_code = crop_link_code;
    }

    public ModelCropMaster(String cropcode, String cropname_eng, String cropname_kn, String croptype, String intercroptype, String cropcategory, String cropgroup, String crop_link_code) {
        this.cropcode = cropcode;
        this.cropname_eng = cropname_eng;
        this.cropname_kn = cropname_kn;
        this.croptype = croptype;
        this.intercroptype = intercroptype;
        this.cropcategory = cropcategory;
        this.cropgroup = cropgroup;
        this.crop_link_code = crop_link_code;
    }

    public static final Creator<ModelCropMaster> CREATOR = new Creator<ModelCropMaster>() {
        @Override
        public ModelCropMaster createFromParcel(Parcel in) {
            return new ModelCropMaster(in);
        }

        @Override
        public ModelCropMaster[] newArray(int size) {
            return new ModelCropMaster[size];
        }
    };

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

    public String getIntercroptype() {
        return intercroptype;
    }

    public void setIntercroptype(String intercroptype) {
        this.intercroptype = intercroptype;
    }

    public String getCropcategory() {
        return cropcategory;
    }

    public void setCropcategory(String cropcategory) {
        this.cropcategory = cropcategory;
    }

    public String getCropgroup() {
        return cropgroup;
    }

    public void setCropgroup(String cropgroup) {
        this.cropgroup = cropgroup;
    }

    public String getCrop_link_code() {
        return crop_link_code;
    }

    public void setCrop_link_code(String crop_link_code) {
        this.crop_link_code = crop_link_code;
    }

    protected ModelCropMaster(Parcel in){

        id = in.readInt();
        cropcode = in.readString();
        cropname_eng = in.readString();
        cropname_kn = in.readString();
        croptype = in.readString();
        intercroptype = in.readString();
        cropcategory = in.readString();
        cropgroup = in.readString();
        crop_link_code = in.readString();
        
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(cropcode);
        parcel.writeString(cropname_eng);
        parcel.writeString(cropname_kn);
        parcel.writeString(croptype);
        parcel.writeString(intercroptype);
        parcel.writeString(cropcategory);
        parcel.writeString(cropgroup);
        parcel.writeString(crop_link_code);
    }
}
