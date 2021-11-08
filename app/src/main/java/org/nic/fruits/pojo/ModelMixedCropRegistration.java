package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="mixedcropsregistration")
public class ModelMixedCropRegistration {

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
  private String croptype;
  private String mixedcropname;
  private String cropcode;
  private String mixedcropnumber;
  private String mixedcropvariety;
  private String totalcrops;
  private String mixedcropextent;
  private String totalcropextent;
  private String farming;
  private String presowing;
  private String sowingdate;
  private String gpscoordinates;
  private String mixedcropimagepath;
  private String mixedcropimage;
  private String isuploaded;

  @Ignore
  public ModelMixedCropRegistration(int id, String farmerid, String crid, String year, String yearcode, String season, String season_code, String ownerid, String ownername, String ownerarea, String surveynumber, String district, String taluk, String village, String croptype, String mixedcropname, String cropcode, String mixedcropnumber, String mixedcropvariety, String totalcrops, String mixedcropextent, String totalcropextent, String farming, String presowing, String sowingdate, String gpscoordinates, String mixedcropimagepath, String mixedcropimage, String isuploaded) {
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
    this.croptype = croptype;
    this.mixedcropname = mixedcropname;
    this.cropcode = cropcode;
    this.mixedcropnumber = mixedcropnumber;
    this.mixedcropvariety = mixedcropvariety;
    this.totalcrops = totalcrops;
    this.mixedcropextent = mixedcropextent;
    this.totalcropextent = totalcropextent;
    this.farming = farming;
    this.presowing = presowing;
    this.sowingdate = sowingdate;
    this.gpscoordinates = gpscoordinates;
    this.mixedcropimagepath = mixedcropimagepath;
    this.mixedcropimage = mixedcropimage;
    this.isuploaded = isuploaded;
  }

  public ModelMixedCropRegistration(String farmerid, String crid, String year, String yearcode, String season, String season_code, String ownerid, String ownername, String ownerarea, String surveynumber, String district, String taluk, String village, String croptype, String mixedcropname, String cropcode, String mixedcropnumber, String mixedcropvariety, String totalcrops, String mixedcropextent, String totalcropextent, String farming, String presowing, String sowingdate, String gpscoordinates, String mixedcropimagepath, String mixedcropimage, String isuploaded) {
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
    this.croptype = croptype;
    this.mixedcropname = mixedcropname;
    this.cropcode = cropcode;
    this.mixedcropnumber = mixedcropnumber;
    this.mixedcropvariety = mixedcropvariety;
    this.totalcrops = totalcrops;
    this.mixedcropextent = mixedcropextent;
    this.totalcropextent = totalcropextent;
    this.farming = farming;
    this.presowing = presowing;
    this.sowingdate = sowingdate;
    this.gpscoordinates = gpscoordinates;
    this.mixedcropimagepath = mixedcropimagepath;
    this.mixedcropimage = mixedcropimage;
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

  public String getCroptype() {
    return croptype;
  }

  public void setCroptype(String croptype) {
    this.croptype = croptype;
  }

  public String getMixedcropname() {
    return mixedcropname;
  }

  public void setMixedcropname(String mixedcropname) {
    this.mixedcropname = mixedcropname;
  }

  public String getCropcode() {
    return cropcode;
  }

  public void setCropcode(String cropcode) {
    this.cropcode = cropcode;
  }

  public String getMixedcropnumber() {
    return mixedcropnumber;
  }

  public void setMixedcropnumber(String mixedcropnumber) {
    this.mixedcropnumber = mixedcropnumber;
  }

  public String getMixedcropvariety() {
    return mixedcropvariety;
  }

  public void setMixedcropvariety(String mixedcropvariety) {
    this.mixedcropvariety = mixedcropvariety;
  }

  public String getTotalcrops() {
    return totalcrops;
  }

  public void setTotalcrops(String totalcrops) {
    this.totalcrops = totalcrops;
  }

  public String getMixedcropextent() {
    return mixedcropextent;
  }

  public void setMixedcropextent(String mixedcropextent) {
    this.mixedcropextent = mixedcropextent;
  }

  public String getTotalcropextent() {
    return totalcropextent;
  }

  public void setTotalcropextent(String totalcropextent) {
    this.totalcropextent = totalcropextent;
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

  public String getMixedcropimagepath() {
    return mixedcropimagepath;
  }

  public void setMixedcropimagepath(String mixedcropimagepath) {
    this.mixedcropimagepath = mixedcropimagepath;
  }

  public String getMixedcropimage() {
    return mixedcropimage;
  }

  public void setMixedcropimage(String mixedcropimage) {
    this.mixedcropimage = mixedcropimage;
  }

  public String getIsuploaded() {
    return isuploaded;
  }

  public void setIsuploaded(String isuploaded) {
    this.isuploaded = isuploaded;
  }
}
