package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//FertilizerMaster.xml
@Entity(tableName="fertilizernamemaster")
public class ModelFertilizerNameMaster {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String feid;
  private String fertilizername;
  private String fertilizekname;
  private String fertilizertype;
  private String fertilizernitrogen;
  private String fertilizerphosphorous;
  private String fertilizerpotash;
  private String fertilizernutrient;

  @Ignore
  public ModelFertilizerNameMaster(int id, String feid, String fertilizername,String fertilizekname, String fertilizertype, String fertilizernitrogen, String fertilizerphosphorous, String fertilizerpotash, String fertilizernutrient) {
    this.id = id;
    this.feid = feid;
    this.fertilizername = fertilizername;
    this.fertilizekname = fertilizekname;
    this.fertilizertype = fertilizertype;
    this.fertilizernitrogen = fertilizernitrogen;
    this.fertilizerphosphorous = fertilizerphosphorous;
    this.fertilizerpotash = fertilizerpotash;
    this.fertilizernutrient = fertilizernutrient;
  }

  public ModelFertilizerNameMaster(String feid, String fertilizername,String fertilizekname, String fertilizertype, String fertilizernitrogen, String fertilizerphosphorous, String fertilizerpotash, String fertilizernutrient) {
    this.feid = feid;
    this.fertilizername = fertilizername;
    this.fertilizekname = fertilizekname;
    this.fertilizertype = fertilizertype;
    this.fertilizernitrogen = fertilizernitrogen;
    this.fertilizerphosphorous = fertilizerphosphorous;
    this.fertilizerpotash = fertilizerpotash;
    this.fertilizernutrient = fertilizernutrient;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFeid() {
    return feid;
  }

  public void setFeid(String feid) {
    this.feid = feid;
  }

  public String getFertilizername() {
    return fertilizername;
  }

  public void setFertilizername(String fertilizername) {
    this.fertilizername = fertilizername;
  }

  public String getFertilizekname() {
    return fertilizekname;
  }

  public void setFertilizekname(String fertilizekname) {
    this.fertilizekname = fertilizekname;
  }

  public String getFertilizertype() {
    return fertilizertype;
  }

  public void setFertilizertype(String fertilizertype) {
    this.fertilizertype = fertilizertype;
  }

  public String getFertilizernitrogen() {
    return fertilizernitrogen;
  }

  public void setFertilizernitrogen(String fertilizernitrogen) {
    this.fertilizernitrogen = fertilizernitrogen;
  }

  public String getFertilizerphosphorous() {
    return fertilizerphosphorous;
  }

  public void setFertilizerphosphorous(String fertilizerphosphorous) {
    this.fertilizerphosphorous = fertilizerphosphorous;
  }

  public String getFertilizerpotash() {
    return fertilizerpotash;
  }

  public void setFertilizerpotash(String fertilizerpotash) {
    this.fertilizerpotash = fertilizerpotash;
  }

  public String getFertilizernutrient() {
    return fertilizernutrient;
  }

  public void setFertilizernutrient(String fertilizernutrient) {
    this.fertilizernutrient = fertilizernutrient;
  }
}
