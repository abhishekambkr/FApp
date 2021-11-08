package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


//Programming by Harsha  for version 1.0 release
@Entity(tableName="weather_details")
public class ModelweatherDetails implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerId;
    private String district;
    private String taluk;
    private String panchayathName;
    private String rainFall;
    private String minMaxTempetarure;
    private String minMaxRh;
    private String minMaxWindSpeed;
    private String weatherStationDate;

    @Ignore
    public ModelweatherDetails(String farmerId,String district, String taluk, String panchayathName, String rainFall, String minMaxTempetarure, String minMaxRh, String minMaxWindSpeed, String weatherStationDate) {
        this.farmerId = farmerId;
        this.district = district;
        this.taluk = taluk;
        this.panchayathName = panchayathName;
        this.rainFall = rainFall;
        this.minMaxTempetarure = minMaxTempetarure;
        this.minMaxRh = minMaxRh;
        this.minMaxWindSpeed = minMaxWindSpeed;
        this.weatherStationDate = weatherStationDate;
    }


    public ModelweatherDetails(int id,String farmerId,String district, String taluk, String panchayathName, String rainFall, String minMaxTempetarure, String minMaxRh, String minMaxWindSpeed, String weatherStationDate) {
        this.id = id;
        this.farmerId = farmerId;
        this.district = district;
        this.taluk = taluk;
        this.panchayathName = panchayathName;
        this.rainFall = rainFall;
        this.minMaxTempetarure = minMaxTempetarure;
        this.minMaxRh = minMaxRh;
        this.minMaxWindSpeed = minMaxWindSpeed;
        this.weatherStationDate = weatherStationDate;
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

    public String getPanchayathName() {
        return panchayathName;
    }

    public void setPanchayathName(String panchayathName) {
        this.panchayathName = panchayathName;
    }

    public String getRainFall() {
        return rainFall;
    }

    public void setRainFall(String rainFall) {
        this.rainFall = rainFall;
    }

    public String getMinMaxTempetarure() {
        return minMaxTempetarure;
    }

    public void setMinMaxTempetarure(String minMaxTempetarure) {
        this.minMaxTempetarure = minMaxTempetarure;
    }

    public String getMinMaxRh() {
        return minMaxRh;
    }

    public void setMinMaxRh(String minMaxRh) {
        this.minMaxRh = minMaxRh;
    }

    public String getMinMaxWindSpeed() {
        return minMaxWindSpeed;
    }

    public void setMinMaxWindSpeed(String minMaxWindSpeed) {
        this.minMaxWindSpeed = minMaxWindSpeed;
    }

    public String getWeatherStationDate() {
        return weatherStationDate;
    }

    public void setWeatherStationDate(String weatherStationDate) {
        this.weatherStationDate = weatherStationDate;
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

    public ModelweatherDetails(Parcel in) {
        id = in.readInt();
        farmerId = in.readString();
        district = in.readString();
        taluk = in.readString();
        panchayathName = in.readString();
        rainFall = in.readString();
        minMaxTempetarure = in.readString();
        minMaxRh = in.readString();
        minMaxWindSpeed = in.readString();
        weatherStationDate = in.readString();
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
        parcel.writeString(panchayathName);
        parcel.writeString(rainFall);
        parcel.writeString(minMaxTempetarure);
        parcel.writeString(minMaxRh);
        parcel.writeString(minMaxWindSpeed);
        parcel.writeString(weatherStationDate);

    }

    public static final Parcelable.Creator<ModelweatherDetails> CREATOR = new Parcelable.Creator<ModelweatherDetails>()
    {
        public ModelweatherDetails createFromParcel(Parcel in)
        {
            return new ModelweatherDetails(in);
        }
        public ModelweatherDetails[] newArray(int size)
        {
            return new ModelweatherDetails[size];
        }
    };
}
