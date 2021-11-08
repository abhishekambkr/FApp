package org.nic.fruits.pojo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="cropweeklydata")
public class ModelCropWeeklyData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerid;
    private String cropregid;
    private String year;
    private String yearcode;
    private String season;
    private String seasoncode;
    private String ownerid;
    private String ownername;
    private String district;
    private String taluk;
    private String village;
    private String ownerarea;
    private String surveynumber;
    private String cropextent;
    private String cropname;
    private String weeknumber;
    private String farming;
    private String presowingtype;
    private String presowingdetails;
    private String pesticidetype;
    private String pesticidedetails;
    private String cropdisease;
    private String cropdiseasetoexperts;
    private String croppick;
    private String croppickyield;
    private String cropimagepath;
    private String diseaseimagepath;
    private String cropsoweddate;
    private String pickingdate;
    private String isuploaded;

    @Ignore
    public ModelCropWeeklyData(int id, String farmerid, String cropregid, String year, String yearcode, String season, String seasoncode, String ownerid, String ownername, String district, String taluk, String village, String ownerarea, String surveynumber, String cropextent, String cropname, String weeknumber, String farming, String presowingtype, String presowingdetails, String pesticidetype, String pesticidedetails, String cropdisease, String cropdiseasetoexperts, String croppick, String croppickyield, String cropimagepath, String diseaseimagepath, String cropsoweddate, String pickingdate, String isuploaded) {
        this.id = id;
        this.farmerid = farmerid;
        this.cropregid = cropregid;
        this.year = year;
        this.yearcode = yearcode;
        this.season = season;
        this.seasoncode = seasoncode;
        this.ownerid = ownerid;
        this.ownername = ownername;
        this.district = district;
        this.taluk = taluk;
        this.village = village;
        this.ownerarea = ownerarea;
        this.surveynumber = surveynumber;
        this.cropextent = cropextent;
        this.cropname = cropname;
        this.weeknumber = weeknumber;
        this.farming = farming;
        this.presowingtype = presowingtype;
        this.presowingdetails = presowingdetails;
        this.pesticidetype = pesticidetype;
        this.pesticidedetails = pesticidedetails;
        this.cropdisease = cropdisease;
        this.cropdiseasetoexperts = cropdiseasetoexperts;
        this.croppick = croppick;
        this.croppickyield = croppickyield;
        this.cropimagepath = cropimagepath;
        this.diseaseimagepath = diseaseimagepath;
        this.cropsoweddate = cropsoweddate;
        this.pickingdate = pickingdate;
        this.isuploaded = isuploaded;
    }

    public ModelCropWeeklyData(String farmerid, String cropregid, String year, String yearcode, String season, String seasoncode, String ownerid, String ownername, String district, String taluk, String village, String ownerarea, String surveynumber, String cropextent, String cropname, String weeknumber, String farming, String presowingtype, String presowingdetails, String pesticidetype, String pesticidedetails, String cropdisease, String cropdiseasetoexperts, String croppick, String croppickyield, String cropimagepath, String diseaseimagepath, String cropsoweddate, String pickingdate, String isuploaded) {
        this.farmerid = farmerid;
        this.cropregid = cropregid;
        this.year = year;
        this.yearcode = yearcode;
        this.season = season;
        this.seasoncode = seasoncode;
        this.ownerid = ownerid;
        this.ownername = ownername;
        this.district = district;
        this.taluk = taluk;
        this.village = village;
        this.ownerarea = ownerarea;
        this.surveynumber = surveynumber;
        this.cropextent = cropextent;
        this.cropname = cropname;
        this.weeknumber = weeknumber;
        this.farming = farming;
        this.presowingtype = presowingtype;
        this.presowingdetails = presowingdetails;
        this.pesticidetype = pesticidetype;
        this.pesticidedetails = pesticidedetails;
        this.cropdisease = cropdisease;
        this.cropdiseasetoexperts = cropdiseasetoexperts;
        this.croppick = croppick;
        this.croppickyield = croppickyield;
        this.cropimagepath = cropimagepath;
        this.diseaseimagepath = diseaseimagepath;
        this.cropsoweddate = cropsoweddate;
        this.pickingdate = pickingdate;
        this.isuploaded = isuploaded;
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

    public String getCropregid() {
        return cropregid;
    }

    public void setCropregid(String cropregid) {
        this.cropregid = cropregid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYearcode() {
        return yearcode;
    }

    public void setYearcode(String yearcode) {
        this.yearcode = yearcode;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSeasoncode() {
        return seasoncode;
    }

    public void setSeasoncode(String seasoncode) {
        this.seasoncode = seasoncode;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
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

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getOwnerarea() {
        return ownerarea;
    }

    public void setOwnerarea(String ownerarea) {
        this.ownerarea = ownerarea;
    }

    public String getSurveynumber() {
        return surveynumber;
    }

    public void setSurveynumber(String surveynumber) {
        this.surveynumber = surveynumber;
    }

    public String getCropextent() {
        return cropextent;
    }

    public void setCropextent(String cropextent) {
        this.cropextent = cropextent;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getWeeknumber() {
        return weeknumber;
    }

    public void setWeeknumber(String weeknumber) {
        this.weeknumber = weeknumber;
    }

    public String getFarming() {
        return farming;
    }

    public void setFarming(String farming) {
        this.farming = farming;
    }

    public String getPresowingtype() {
        return presowingtype;
    }

    public void setPresowingtype(String presowingtype) {
        this.presowingtype = presowingtype;
    }

    public String getPresowingdetails() {
        return presowingdetails;
    }

    public void setPresowingdetails(String presowingdetails) {
        this.presowingdetails = presowingdetails;
    }

    public String getPesticidetype() {
        return pesticidetype;
    }

    public void setPesticidetype(String pesticidetype) {
        this.pesticidetype = pesticidetype;
    }

    public String getPesticidedetails() {
        return pesticidedetails;
    }

    public void setPesticidedetails(String pesticidedetails) {
        this.pesticidedetails = pesticidedetails;
    }

    public String getCropdisease() {
        return cropdisease;
    }

    public void setCropdisease(String cropdisease) {
        this.cropdisease = cropdisease;
    }

    public String getCropdiseasetoexperts() {
        return cropdiseasetoexperts;
    }

    public void setCropdiseasetoexperts(String cropdiseasetoexperts) {
        this.cropdiseasetoexperts = cropdiseasetoexperts;
    }

    public String getCroppick() {
        return croppick;
    }

    public void setCroppick(String croppick) {
        this.croppick = croppick;
    }

    public String getCroppickyield() {
        return croppickyield;
    }

    public void setCroppickyield(String croppickyield) {
        this.croppickyield = croppickyield;
    }

    public String getCropimagepath() {
        return cropimagepath;
    }

    public void setCropimagepath(String cropimagepath) {
        this.cropimagepath = cropimagepath;
    }

    public String getDiseaseimagepath() {
        return diseaseimagepath;
    }

    public void setDiseaseimagepath(String diseaseimagepath) {
        this.diseaseimagepath = diseaseimagepath;
    }

    public String getCropsoweddate() {
        return cropsoweddate;
    }

    public void setCropsoweddate(String cropsoweddate) {
        this.cropsoweddate = cropsoweddate;
    }

    public String getPickingdate() {
        return pickingdate;
    }

    public void setPickingdate(String pickingdate) {
        this.pickingdate = pickingdate;
    }

    public String getIsuploaded() {
        return isuploaded;
    }

    public void setIsuploaded(String isuploaded) {
        this.isuploaded = isuploaded;
    }
}
