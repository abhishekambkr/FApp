package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="cropfertilizermaster")
public class ModelCropFertilizerMasternpk {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String cf_id;
  private String cf_crop_code;
  private String cf_irrigation_type;
  private String cf_plant_age;
  private String cf_nitrogen;
  private String cf_phosphorous;
  private String cf_potash;

  @Ignore
  public ModelCropFertilizerMasternpk(int id, String cf_id, String cf_crop_code, String cf_irrigation_type, String cf_plant_age, String cf_nitrogen, String cf_phosphorous, String cf_potash) {
    this.id = id;
    this.cf_id = cf_id;
    this.cf_crop_code = cf_crop_code;
    this.cf_irrigation_type = cf_irrigation_type;
    this.cf_plant_age = cf_plant_age;
    this.cf_nitrogen = cf_nitrogen;
    this.cf_phosphorous = cf_phosphorous;
    this.cf_potash = cf_potash;
  }

  public ModelCropFertilizerMasternpk(String cf_id, String cf_crop_code, String cf_irrigation_type, String cf_plant_age, String cf_nitrogen, String cf_phosphorous, String cf_potash) {
    this.cf_id = cf_id;
    this.cf_crop_code = cf_crop_code;
    this.cf_irrigation_type = cf_irrigation_type;
    this.cf_plant_age = cf_plant_age;
    this.cf_nitrogen = cf_nitrogen;
    this.cf_phosphorous = cf_phosphorous;
    this.cf_potash = cf_potash;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCf_id() {
    return cf_id;
  }

  public void setCf_id(String cf_id) {
    this.cf_id = cf_id;
  }

  public String getCf_crop_code() {
    return cf_crop_code;
  }

  public void setCf_crop_code(String cf_crop_code) {
    this.cf_crop_code = cf_crop_code;
  }

  public String getCf_irrigation_type() {
    return cf_irrigation_type;
  }

  public void setCf_irrigation_type(String cf_irrigation_type) {
    this.cf_irrigation_type = cf_irrigation_type;
  }

  public String getCf_plant_age() {
    return cf_plant_age;
  }

  public void setCf_plant_age(String cf_plant_age) {
    this.cf_plant_age = cf_plant_age;
  }

  public String getCf_nitrogen() {
    return cf_nitrogen;
  }

  public void setCf_nitrogen(String cf_nitrogen) {
    this.cf_nitrogen = cf_nitrogen;
  }

  public String getCf_phosphorous() {
    return cf_phosphorous;
  }

  public void setCf_phosphorous(String cf_phosphorous) {
    this.cf_phosphorous = cf_phosphorous;
  }

  public String getCf_potash() {
    return cf_potash;
  }

  public void setCf_potash(String cf_potash) {
    this.cf_potash = cf_potash;
  }
}

