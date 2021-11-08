package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="farmfertilizer")
public class ModelFarmFertilizer {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerid;
    private String year;
    private String season;
    private String cropname;
    private String cropcode;
    private String cropextent;
    private String croptype;
    private String district;
    private String taluk;
    private String hobli;
    private String village;
    private String survey;
    private String recommendedNPK;
    private String cropextentNPK;
    private String totalNPK;
    private String fertilizerNames;
    private String fertilizerKGs;
    private String fertilizerBags;

    @Ignore
    public ModelFarmFertilizer(int id, String farmerid, String year, String season, String cropname, String cropcode, String cropextent, String croptype, String district, String taluk, String hobli, String village, String survey, String recommendedNPK, String cropextentNPK, String totalNPK, String fertilizerNames, String fertilizerKGs, String fertilizerBags) {
        this.id = id;
        this.farmerid = farmerid;
        this.year = year;
        this.season = season;
        this.cropname = cropname;
        this.cropcode = cropcode;
        this.cropextent = cropextent;
        this.croptype = croptype;
        this.district = district;
        this.taluk = taluk;
        this.hobli = hobli;
        this.village = village;
        this.survey = survey;
        this.recommendedNPK = recommendedNPK;
        this.cropextentNPK = cropextentNPK;
        this.totalNPK = totalNPK;
        this.fertilizerNames = fertilizerNames;
        this.fertilizerKGs = fertilizerKGs;
        this.fertilizerBags = fertilizerBags;
    }

    public ModelFarmFertilizer(String farmerid, String year, String season, String cropname, String cropcode, String cropextent, String croptype, String district, String taluk, String hobli, String village, String survey, String recommendedNPK, String cropextentNPK, String totalNPK, String fertilizerNames, String fertilizerKGs, String fertilizerBags) {
        this.farmerid = farmerid;
        this.year = year;
        this.season = season;
        this.cropname = cropname;
        this.cropcode = cropcode;
        this.cropextent = cropextent;
        this.croptype = croptype;
        this.district = district;
        this.taluk = taluk;
        this.hobli = hobli;
        this.village = village;
        this.survey = survey;
        this.recommendedNPK = recommendedNPK;
        this.cropextentNPK = cropextentNPK;
        this.totalNPK = totalNPK;
        this.fertilizerNames = fertilizerNames;
        this.fertilizerKGs = fertilizerKGs;
        this.fertilizerBags = fertilizerBags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmerid() {
        return farmerid;
    }

    public void setFarmerid(String farmerid) {
        this.farmerid = farmerid;
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

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getCropcode() {
        return cropcode;
    }

    public void setCropcode(String cropcode) {
        this.cropcode = cropcode;
    }

    public String getCropextent() {
        return cropextent;
    }

    public void setCropextent(String cropextent) {
        this.cropextent = cropextent;
    }

    public String getCroptype() {
        return croptype;
    }

    public void setCroptype(String croptype) {
        this.croptype = croptype;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getHobli() {
        return hobli;
    }

    public void setHobli(String hobli) {
        this.hobli = hobli;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getRecommendedNPK() {
        return recommendedNPK;
    }

    public void setRecommendedNPK(String recommendedNPK) {
        this.recommendedNPK = recommendedNPK;
    }

    public String getCropextentNPK() {
        return cropextentNPK;
    }

    public void setCropextentNPK(String cropextentNPK) {
        this.cropextentNPK = cropextentNPK;
    }

    public String getTotalNPK() {
        return totalNPK;
    }

    public void setTotalNPK(String totalNPK) {
        this.totalNPK = totalNPK;
    }

    public String getFertilizerNames() {
        return fertilizerNames;
    }

    public void setFertilizerNames(String fertilizerNames) {
        this.fertilizerNames = fertilizerNames;
    }

    public String getFertilizerKGs() {
        return fertilizerKGs;
    }

    public void setFertilizerKGs(String fertilizerKGs) {
        this.fertilizerKGs = fertilizerKGs;
    }

    public String getFertilizerBags() {
        return fertilizerBags;
    }

    public void setFertilizerBags(String fertilizerBags) {
        this.fertilizerBags = fertilizerBags;
    }
}
