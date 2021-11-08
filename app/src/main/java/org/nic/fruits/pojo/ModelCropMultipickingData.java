package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="cropmultipickdata")
public class ModelCropMultipickingData {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String farmerid;
  private String cropregid;
  private String year;
  private String yearcode;
  private String season;
  private String seasoncode;
  private String ownerid;
  private String owner;
  private String area;
  private String surveynumber;
  private String cropname;
  private String cropextent;
  private String sowingdate;
  private String pickingdate;
  private String week;
  private String croppick;
  private String yield;
  private String totalpicks;

  @Ignore
  public ModelCropMultipickingData(int id, String farmerid, String cropregid, String year, String yearcode, String season, String seasoncode, String owner, String ownerid, String area, String surveynumber, String cropname, String cropextent, String sowingdate, String week, String pickingdate, String totalpicks, String croppick, String yield) {
    this.id = id;
    this.farmerid = farmerid;
    this.cropregid = cropregid;
    this.year = year;
    this.yearcode = yearcode;
    this.season = season;
    this.seasoncode = seasoncode;
    this.owner = owner;
    this.ownerid = ownerid;
    this.area = area;
    this.surveynumber = surveynumber;
    this.cropname = cropname;
    this.cropextent = cropextent;
    this.sowingdate = sowingdate;
    this.week = week;
    this.pickingdate = pickingdate;
    this.totalpicks = totalpicks;
    this.croppick = croppick;
    this.yield = yield;
  }

  public ModelCropMultipickingData(String farmerid, String cropregid, String year, String yearcode, String season, String seasoncode, String owner, String ownerid, String area, String surveynumber, String cropname, String cropextent, String sowingdate, String week, String pickingdate, String totalpicks, String croppick, String yield) {
    this.farmerid = farmerid;
    this.cropregid = cropregid;
    this.year = year;
    this.yearcode = yearcode;
    this.season = season;
    this.seasoncode = seasoncode;
    this.owner = owner;
    this.ownerid = ownerid;
    this.area = area;
    this.surveynumber = surveynumber;
    this.cropname = cropname;
    this.cropextent = cropextent;
    this.sowingdate = sowingdate;
    this.week = week;
    this.pickingdate = pickingdate;
    this.totalpicks = totalpicks;
    this.croppick = croppick;
    this.yield = yield;
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

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getOwnerid() {
    return ownerid;
  }

  public void setOwnerid(String ownerid) {
    this.ownerid = ownerid;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getSurveynumber() {
    return surveynumber;
  }

  public void setSurveynumber(String surveynumber) {
    this.surveynumber = surveynumber;
  }

  public String getCropname() {
    return cropname;
  }

  public void setCropname(String cropname) {
    this.cropname = cropname;
  }

  public String getCropextent() {
    return cropextent;
  }

  public void setCropextent(String cropextent) {
    this.cropextent = cropextent;
  }

  public String getSowingdate() {
    return sowingdate;
  }

  public void setSowingdate(String sowingdate) {
    this.sowingdate = sowingdate;
  }

  public String getWeek() {
    return week;
  }

  public void setWeek(String week) {
    this.week = week;
  }

  public String getPickingdate() {
    return pickingdate;
  }

  public void setPickingdate(String pickingdate) {
    this.pickingdate = pickingdate;
  }

  public String getTotalpicks() {
    return totalpicks;
  }

  public void setTotalpicks(String totalpicks) {
    this.totalpicks = totalpicks;
  }

  public String getCroppick() {
    return croppick;
  }

  public void setCroppick(String croppick) {
    this.croppick = croppick;
  }

  public String getYield() {
    return yield;
  }

  public void setYield(String yield) {
    this.yield = yield;
  }
}
