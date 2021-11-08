package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;



//Programming by Harsha  for version 1.0 release
@Entity(tableName="farmer_farmer_details")
public class ModelFarmerDetails implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerId;
    private String farmerNameInKannada;
    private String farmerNameInEnglish;
    private String farmerFatherNameInEnglish;
    private String farmerFatherNameInKannada;
    private String age;
    private String gender;
    private  String district;
    private  String taluku;
    private  String hobli;
    private String village;
    private String address;
    private String caste;
    private String speciallyAbled;
    private String minorities;
    private String farmerType;

    @Ignore
    public ModelFarmerDetails(String farmerId,String farmerNameInKannada, String farmerNameInEnglish, String farmerFatherNameInEnglish, String farmerFatherNameInKannada, String age, String gender, String district, String taluku, String hobli, String village, String address, String caste, String speciallyAbled, String minorities, String farmerType) {
        this.farmerId = farmerId;
        this.farmerNameInKannada = farmerNameInKannada;
        this.farmerNameInEnglish = farmerNameInEnglish;
        this.farmerFatherNameInEnglish = farmerFatherNameInEnglish;
        this.farmerFatherNameInKannada = farmerFatherNameInKannada;
        this.age = age;
        this.gender = gender;
        this.district = district;
        this.taluku = taluku;
        this.hobli = hobli;
        this.village = village;
        this.address = address;
        this.caste = caste;
        this.speciallyAbled = speciallyAbled;
        this.minorities = minorities;
        this.farmerType = farmerType;
    }

    public ModelFarmerDetails(int id,String farmerId,String farmerNameInKannada, String farmerNameInEnglish, String farmerFatherNameInEnglish, String farmerFatherNameInKannada, String age, String gender, String district, String taluku, String hobli, String village, String address, String caste, String speciallyAbled, String minorities, String farmerType) {
        this.id = id;
        this.farmerId = farmerId;
        this.farmerNameInKannada = farmerNameInKannada;
        this.farmerNameInEnglish = farmerNameInEnglish;
        this.farmerFatherNameInEnglish = farmerFatherNameInEnglish;
        this.farmerFatherNameInKannada = farmerFatherNameInKannada;
        this.age = age;
        this.gender = gender;
        this.district = district;
        this.taluku = taluku;
        this.hobli = hobli;
        this.village = village;
        this.address = address;
        this.caste = caste;
        this.speciallyAbled = speciallyAbled;
        this.minorities = minorities;
        this.farmerType = farmerType;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public ModelFarmerDetails(Parcel in) {
        id = in.readInt();
        farmerId = in.readString();
        farmerNameInKannada = in.readString();
        farmerNameInEnglish = in.readString();
        farmerFatherNameInEnglish = in.readString();
        farmerFatherNameInKannada = in.readString();
        age = in.readString();
        gender = in.readString();
        district = in.readString();
        taluku = in.readString();
        hobli = in.readString();
        village = in.readString();
        address = in.readString();
        caste = in.readString();
        speciallyAbled = in.readString();
        minorities = in.readString();
        farmerType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(farmerId);
        parcel.writeString(farmerNameInKannada);
        parcel.writeString(farmerNameInEnglish);
        parcel.writeString(farmerFatherNameInEnglish);
        parcel.writeString(farmerFatherNameInKannada);
        parcel.writeString(age);
        parcel.writeString(gender);
        parcel.writeString(district);
        parcel.writeString(taluku);
        parcel.writeString(hobli);
        parcel.writeString(village);
        parcel.writeString(address);
        parcel.writeString(caste);
        parcel.writeString(speciallyAbled);
        parcel.writeString(minorities);
        parcel.writeString(farmerType);

    }

    public static final Parcelable.Creator<ModelFarmerDetails> CREATOR = new Parcelable.Creator<ModelFarmerDetails>()
    {
        public ModelFarmerDetails createFromParcel(Parcel in)
        {
            return new ModelFarmerDetails(in);
        }
        public ModelFarmerDetails[] newArray(int size)
        {
            return new ModelFarmerDetails[size];
        }
    };


    public String getFarmerNameInKannada() {
        return farmerNameInKannada;
    }

    public void setFarmerNameInKannada(String farmerNameInKannada) {
        this.farmerNameInKannada = farmerNameInKannada;
    }

    public String getFarmerNameInEnglish() {
        return farmerNameInEnglish;
    }

    public void setFarmerNameInEnglish(String farmerNameInEnglish) {
        this.farmerNameInEnglish = farmerNameInEnglish;
    }

    public String getFarmerFatherNameInEnglish() {
        return farmerFatherNameInEnglish;
    }

    public void setFarmerFatherNameInEnglish(String farmerFatherNameInEnglish) {
        this.farmerFatherNameInEnglish = farmerFatherNameInEnglish;
    }

    public String getFarmerFatherNameInKannada() {
        return farmerFatherNameInKannada;
    }

    public void setFarmerFatherNameInKannada(String farmerFatherNameInKannada) {
        this.farmerFatherNameInKannada = farmerFatherNameInKannada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluku() {
        return taluku;
    }

    public void setTaluku(String taluku) {
        this.taluku = taluku;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getSpeciallyAbled() {
        return speciallyAbled;
    }

    public void setSpeciallyAbled(String speciallyAbled) {
        this.speciallyAbled = speciallyAbled;
    }

    public String getMinorities() {
        return minorities;
    }

    public void setMinorities(String minorities) {
        this.minorities = minorities;
    }

    public String getFarmerType() {
        return farmerType;
    }

    public void setFarmerType(String farmerType) {
        this.farmerType = farmerType;
    }
}
