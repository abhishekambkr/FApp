package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


//Programming by Harsha  for version 1.0 release
@Entity(tableName="farmer_land_details")
public class ModelFarmerLandDeatails {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerId;
    private String farmerName;
    private String districtname;
    private String talukName;
    private String hobliName;
    private String villageName;
    private String surveyNumber;
    private String area;


    @Ignore
    public ModelFarmerLandDeatails(String farmerId,String farmerName, String districtname, String talukName, String hobliName, String villageName, String surveyNumber, String area) {
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.districtname = districtname;
        this.talukName = talukName;
        this.hobliName = hobliName;
        this.villageName = villageName;
        this.surveyNumber = surveyNumber;
        this.area = area;

    }

    public ModelFarmerLandDeatails(int id,String farmerId,String farmerName, String districtname, String talukName, String hobliName, String villageName, String surveyNumber, String area) {
        this.id = id;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.districtname = districtname;
        this.talukName = talukName;
        this.hobliName = hobliName;
        this.villageName = villageName;
        this.surveyNumber = surveyNumber;
        this.area = area;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
