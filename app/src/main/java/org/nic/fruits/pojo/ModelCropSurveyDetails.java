package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


//Programming by Harsha  for version 1.0 release
@Entity(tableName="farmer_crop_survey_details")
public class ModelCropSurveyDetails implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerId;
    private String farmerName;
    private String districtname;
    private String talukName;
    private String hobliName;
    private String villageName;
    private String surveyNumber;
    private String cropArea;
    private String year;
    private String season;
    private String cropName;

    @Ignore
    public ModelCropSurveyDetails(String farmerId,String farmerName, String districtname, String talukName, String hobliName, String villageName, String surveyNumber, String cropArea, String year, String season, String cropName) {
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.districtname = districtname;
        this.talukName = talukName;
        this.hobliName = hobliName;
        this.villageName = villageName;
        this.surveyNumber = surveyNumber;
        this.cropArea = cropArea;
        this.year = year;
        this.season = season;
        this.cropName = cropName;
    }

    public ModelCropSurveyDetails(int id,String farmerId,String farmerName, String districtname, String talukName, String hobliName, String villageName, String surveyNumber, String cropArea, String year, String season, String cropName) {
        this.id = id;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.districtname = districtname;
        this.talukName = talukName;
        this.hobliName = hobliName;
        this.villageName = villageName;
        this.surveyNumber = surveyNumber;
        this.cropArea = cropArea;
        this.year = year;
        this.season = season;
        this.cropName = cropName;
    }

    public ModelCropSurveyDetails(Parcel in) {
        id = in.readInt();
        farmerId = in.readString();
        farmerName = in.readString();
        districtname = in.readString();
        talukName = in.readString();
        hobliName = in.readString();
        villageName = in.readString();
        surveyNumber = in.readString();
        cropArea = in.readString();
        year = in.readString();
        season = in.readString();
        cropName = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public String getTalukName() {
        return talukName;
    }

    public void setTalukName(String talukName) {
        this.talukName = talukName;
    }

    public String getHobliName() {
        return hobliName;
    }

    public void setHobliName(String hobliName) {
        this.hobliName = hobliName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getSurveyNumber() {
        return surveyNumber;
    }

    public void setSurveyNumber(String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    public String getCropArea() {
        return cropArea;
    }

    public void setCropArea(String cropArea) {
        this.cropArea = cropArea;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(farmerId);
        parcel.writeString(farmerName);
        parcel.writeString(districtname);
        parcel.writeString(talukName);
        parcel.writeString(hobliName);
        parcel.writeString(villageName);
        parcel.writeString(surveyNumber);
        parcel.writeString(cropArea);
        parcel.writeString(year);
        parcel.writeString(season);
        parcel.writeString(cropName);
    }

    public static final Parcelable.Creator<ModelCropSurveyDetails> CREATOR = new Parcelable.Creator<ModelCropSurveyDetails>() {
        public ModelCropSurveyDetails createFromParcel(Parcel in) {
            return new ModelCropSurveyDetails(in);
        }

        public ModelCropSurveyDetails[] newArray(int size) {
            return new ModelCropSurveyDetails[size];
        }
    };
}
