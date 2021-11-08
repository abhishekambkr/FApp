package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName="cropvarietymaster")
public class ModelCropVarietyMaster {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int cropcode;
    private String cropvariety;
    private String cropname;
    private String cropsowingduration;
    private String periodicitypicking; //once /alternate days/ once in 3 days/weekly/fortnightly
    private String totalpicks;
    private String numberofdayfirstpicking;
    private String expyieldinfirstpick;


    @Ignore
    public ModelCropVarietyMaster(int cropcode, String cropvariety, String cropname, String cropsowingduration, String periodicitypicking, String totalpicks, String numberofdayfirstpicking, String expyieldinfirstpick) {
        this.cropcode = cropcode;
        this.cropvariety = cropvariety;
        this.cropname = cropname;
        this.cropsowingduration = cropsowingduration;
        this.periodicitypicking = periodicitypicking;
        this.totalpicks = totalpicks;
        this.numberofdayfirstpicking = numberofdayfirstpicking;
        this.expyieldinfirstpick = expyieldinfirstpick;
    }


    public ModelCropVarietyMaster(int id, int cropcode, String cropvariety, String cropname ,String cropsowingduration, String periodicitypicking, String totalpicks, String numberofdayfirstpicking, String expyieldinfirstpick) {
        this.id = id;
        this.cropcode = cropcode;
        this.cropname = cropname;
        this.cropvariety = cropvariety;
        this.cropsowingduration = cropsowingduration;
        this.periodicitypicking = periodicitypicking;
        this.totalpicks = totalpicks;
        this.numberofdayfirstpicking = numberofdayfirstpicking;
        this.expyieldinfirstpick = expyieldinfirstpick;
    }

    public ModelCropVarietyMaster(Parcel parcel){
        id = parcel.readInt();
        cropcode = parcel.readInt();
        cropvariety = parcel.readString();
        cropname = parcel.readString();
        cropsowingduration = parcel.readString();
        periodicitypicking = parcel.readString();
        totalpicks = parcel.readString();
        numberofdayfirstpicking = parcel.readString();
        expyieldinfirstpick = parcel.readString();
         
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCropcode() {
        return cropcode;
    }

    public void setCropcode(int cropcode) {
        this.cropcode = cropcode;
    }

    public String getCropvariety() {
        return cropvariety;
    }

    public void setCropvariety(String cropvariety) {
        this.cropvariety = cropvariety;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getCropsowingduration() {
        return cropsowingduration;
    }

    public void setCropsowingduration(String cropsowingduration) {
        this.cropsowingduration = cropsowingduration;
    }

    public String getPeriodicitypicking() {
        return periodicitypicking;
    }

    public void setPeriodicitypicking(String periodicitypicking) {
        this.periodicitypicking = periodicitypicking;
    }

    public String getTotalpicks() {
        return totalpicks;
    }

    public void setTotalpicks(String totalpicks) {
        this.totalpicks = totalpicks;
    }

    public String getNumberofdayfirstpicking() {
        return numberofdayfirstpicking;
    }

    public void setNumberofdayfirstpicking(String numberofdayfirstpicking) {
        this.numberofdayfirstpicking = numberofdayfirstpicking;
    }

    public String getExpyieldinfirstpick() {
        return expyieldinfirstpick;
    }

    public void setExpyieldinfirstpick(String expyieldinfirstpick) {
        this.expyieldinfirstpick = expyieldinfirstpick;
    }
    
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeInt(id);
        parcel.writeInt(cropcode);
        parcel.writeString(cropvariety);
        parcel.writeString(cropsowingduration);
        parcel.writeString(periodicitypicking);
        parcel.writeString(totalpicks);
        parcel.writeString(numberofdayfirstpicking);
        parcel.writeString(expyieldinfirstpick);
        
    }
    
    public static final Parcelable.Creator<ModelCropVarietyMaster> VAREITY_MASTER_CREATOR = new Parcelable.Creator<ModelCropVarietyMaster>() {
        @Override
        public ModelCropVarietyMaster createFromParcel(Parcel source) {
            return new ModelCropVarietyMaster(source);
        }

        @Override
        public ModelCropVarietyMaster[] newArray(int size) {
            return new ModelCropVarietyMaster[size];
        }
    };
}
