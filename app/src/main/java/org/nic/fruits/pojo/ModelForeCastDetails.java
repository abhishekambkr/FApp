package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


//Programming by Harsha  for version 1.0 release
@Entity(tableName = "forecast_details")
public class ModelForeCastDetails implements Parcelable {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String farmerId;
  private String district;
  private String taluk;
  private String gp;
  private String lastUpadtedDate;
  private String hours;
  private String rainFall;
  private String cloud;
  private String temperature;
  private String humidity;
  private String windSpeed;

  @Ignore
  public ModelForeCastDetails(String farmerId, String district, String taluk, String gp, String lastUpadtedDate, String hours, String rainFall, String cloud, String temperature, String humidity, String windSpeed) {
    this.farmerId = farmerId;
    this.district = district;
    this.taluk = taluk;
    this.gp = gp;
    this.lastUpadtedDate = lastUpadtedDate;
    this.hours = hours;
    this.rainFall = rainFall;
    this.cloud = cloud;
    this.temperature = temperature;
    this.humidity = humidity;
    this.windSpeed = windSpeed;
  }

  public ModelForeCastDetails(int id, String farmerId, String district, String taluk, String gp, String lastUpadtedDate, String hours, String rainFall, String cloud, String temperature, String humidity, String windSpeed) {
    this.id = id;
    this.farmerId = farmerId;
    this.district = district;
    this.taluk = taluk;
    this.gp = gp;
    this.lastUpadtedDate = lastUpadtedDate;
    this.hours = hours;
    this.rainFall = rainFall;
    this.cloud = cloud;
    this.temperature = temperature;
    this.humidity = humidity;
    this.windSpeed = windSpeed;
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

  public String getGp() {
    return gp;
  }

  public void setGp(String gp) {
    this.gp = gp;
  }

  public String getLastUpadtedDate() {
    return lastUpadtedDate;
  }

  public void setLastUpadtedDate(String lastUpadtedDate) {
    this.lastUpadtedDate = lastUpadtedDate;
  }

  public String getHours() {
    return hours;
  }

  public void setHours(String hours) {
    this.hours = hours;
  }

  public String getRainFall() {
    return rainFall;
  }

  public void setRainFall(String rainFall) {
    this.rainFall = rainFall;
  }

  public String getCloud() {
    return cloud;
  }

  public void setCloud(String cloud) {
    this.cloud = cloud;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getHumidity() {
    return humidity;
  }

  public void setHumidity(String humidity) {
    this.humidity = humidity;
  }

  public String getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(String windSpeed) {
    this.windSpeed = windSpeed;
  }

  public ModelForeCastDetails(Parcel in) {
    id = in.readInt();
    farmerId = in.readString();
    district = in.readString();
    taluk = in.readString();
    gp = in.readString();
    lastUpadtedDate = in.readString();
    hours = in.readString();
    rainFall = in.readString();
    cloud = in.readString();
    temperature = in.readString();
    humidity = in.readString();
    windSpeed = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags) {
    parcel.writeInt(id);
    parcel.writeString(farmerId);
    parcel.writeString(district);
    parcel.writeString(taluk);
    parcel.writeString(gp);
    parcel.writeString(lastUpadtedDate);
    parcel.writeString(hours);
    parcel.writeString(rainFall);
    parcel.writeString(cloud);
    parcel.writeString(temperature);
    parcel.writeString(humidity);
    parcel.writeString(windSpeed);
  }

  public static final Parcelable.Creator<ModelForeCastDetails> CREATOR = new Parcelable.Creator<ModelForeCastDetails>() {
    public ModelForeCastDetails createFromParcel(Parcel in) {
      return new ModelForeCastDetails(in);
    }

    public ModelForeCastDetails[] newArray(int size) {
      return new ModelForeCastDetails[size];
    }
  };
}
