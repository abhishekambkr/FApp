package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="cropregistration")
public class ModelCropRegistration {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerid;
    private String crid;
    private String year;
    private String yearcode;
    private String season;
    private String season_code;
    private String ownerid;
    private String ownername;
    private String ownerarea;
    private String surveynumber;
    private String district;
    private String taluk;
    private String village;
    private String cropname;
    private String croptype;
    private String totalcrops;
    private String cropcode;
    private String cropvariety;
    private String cropextent;
    private String irrigation_type_id;
    private String irrigation_type;
    private String irrigation_source_id;
    private String irrigation_source;
    private String farming;
    private String presowing;
    private String sowingdate;
    private String gpscoordinates;
    private String cropimagepath;
    private String cropimage;
    private String isuploaded;

    @Ignore
    public ModelCropRegistration(int id, String farmerid, String crid, String year, String yearcode, String season, String season_code, String ownerid, String ownername, String ownerarea, String surveynumber, String district, String taluk, String village, String cropname, String croptype, String totalcrops, String cropcode, String cropvariety, String cropextent, String irrigation_type_id, String irrigation_type, String irrigation_source_id, String irrigation_source, String farming, String presowing, String sowingdate, String gpscoordinates, String cropimagepath, String cropimage, String isuploaded) {
        this.id = id;
        this.farmerid = farmerid;
        this.crid = crid;
        this.year = year;
        this.yearcode = yearcode;
        this.season = season;
        this.season_code = season_code;
        this.ownerid = ownerid;
        this.ownername = ownername;
        this.ownerarea = ownerarea;
        this.surveynumber = surveynumber;
        this.district = district;
        this.taluk = taluk;
        this.village = village;
        this.cropname = cropname;
        this.croptype = croptype;
        this.totalcrops = totalcrops;
        this.cropcode = cropcode;
        this.cropvariety = cropvariety;
        this.cropextent = cropextent;
        this.irrigation_type_id = irrigation_type_id;
        this.irrigation_type = irrigation_type;
        this.irrigation_source_id = irrigation_source_id;
        this.irrigation_source = irrigation_source;
        this.farming = farming;
        this.presowing = presowing;
        this.sowingdate = sowingdate;
        this.gpscoordinates = gpscoordinates;
        this.cropimagepath = cropimagepath;
        this.cropimage = cropimage;
        this.isuploaded = isuploaded;
    }

    public ModelCropRegistration(String farmerid, String crid, String year, String yearcode, String season, String season_code, String ownerid, String ownername, String ownerarea, String surveynumber, String district, String taluk, String village, String cropname, String croptype, String totalcrops, String cropcode, String cropvariety, String cropextent, String irrigation_type_id, String irrigation_type, String irrigation_source_id, String irrigation_source, String farming, String presowing, String sowingdate, String gpscoordinates, String cropimagepath, String cropimage, String isuploaded) {
        this.farmerid = farmerid;
        this.crid = crid;
        this.year = year;
        this.yearcode = yearcode;
        this.season = season;
        this.season_code = season_code;
        this.ownerid = ownerid;
        this.ownername = ownername;
        this.ownerarea = ownerarea;
        this.surveynumber = surveynumber;
        this.district = district;
        this.taluk = taluk;
        this.village = village;
        this.cropname = cropname;
        this.croptype = croptype;
        this.totalcrops = totalcrops;
        this.cropcode = cropcode;
        this.cropvariety = cropvariety;
        this.cropextent = cropextent;
        this.irrigation_type_id = irrigation_type_id;
        this.irrigation_type = irrigation_type;
        this.irrigation_source_id = irrigation_source_id;
        this.irrigation_source = irrigation_source;
        this.farming = farming;
        this.presowing = presowing;
        this.sowingdate = sowingdate;
        this.gpscoordinates = gpscoordinates;
        this.cropimagepath = cropimagepath;
        this.cropimage = cropimage;
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

    public String getCrid() {
        return crid;
    }

    public void setCrid(String crid) {
        this.crid = crid;
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

    public String getSeason_code() {
        return season_code;
    }

    public void setSeason_code(String season_code) {
        this.season_code = season_code;
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

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getCroptype() {
        return croptype;
    }

    public void setCroptype(String croptype) {
        this.croptype = croptype;
    }

    public String getTotalcrops() {
        return totalcrops;
    }

    public void setTotalcrops(String totalcrops) {
        this.totalcrops = totalcrops;
    }

    public String getCropcode() {
        return cropcode;
    }

    public void setCropcode(String cropcode) {
        this.cropcode = cropcode;
    }

    public String getCropvariety() {
        return cropvariety;
    }

    public void setCropvariety(String cropvariety) {
        this.cropvariety = cropvariety;
    }

    public String getCropextent() {
        return cropextent;
    }

    public void setCropextent(String cropextent) {
        this.cropextent = cropextent;
    }

    public String getIrrigation_type_id() {
        return irrigation_type_id;
    }

    public void setIrrigation_type_id(String irrigation_type_id) {
        this.irrigation_type_id = irrigation_type_id;
    }

    public String getIrrigation_type() {
        return irrigation_type;
    }

    public void setIrrigation_type(String irrigation_type) {
        this.irrigation_type = irrigation_type;
    }

    public String getIrrigation_source_id() {
        return irrigation_source_id;
    }

    public void setIrrigation_source_id(String irrigation_source_id) {
        this.irrigation_source_id = irrigation_source_id;
    }

    public String getIrrigation_source() {
        return irrigation_source;
    }

    public void setIrrigation_source(String irrigation_source) {
        this.irrigation_source = irrigation_source;
    }

    public String getFarming() {
        return farming;
    }

    public void setFarming(String farming) {
        this.farming = farming;
    }

    public String getPresowing() {
        return presowing;
    }

    public void setPresowing(String presowing) {
        this.presowing = presowing;
    }

    public String getSowingdate() {
        return sowingdate;
    }

    public void setSowingdate(String sowingdate) {
        this.sowingdate = sowingdate;
    }

    public String getGpscoordinates() {
        return gpscoordinates;
    }

    public void setGpscoordinates(String gpscoordinates) {
        this.gpscoordinates = gpscoordinates;
    }

    public String getCropimagepath() {
        return cropimagepath;
    }

    public void setCropimagepath(String cropimagepath) {
        this.cropimagepath = cropimagepath;
    }

    public String getCropimage() {
        return cropimage;
    }

    public void setCropimage(String cropimage) {
        this.cropimage = cropimage;
    }

    public String getIsuploaded() {
        return isuploaded;
    }

    public void setIsuploaded(String isuploaded) {
        this.isuploaded = isuploaded;
    }
}
