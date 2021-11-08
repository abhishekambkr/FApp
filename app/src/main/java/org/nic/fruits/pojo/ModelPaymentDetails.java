package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


//Programming by Harsha  for version 1.0 release
@Entity(tableName="farmer_payment_details")
public class ModelPaymentDetails implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerId;
    private String beneficiaryId;
    private String name;
    private String department;
    private String scheme;
    private String financialYear;
    private String K2UTRNo;
    private String paymentDate;
    private String paymentMode;
    private String sanctionedAmount;

    @Ignore
    public ModelPaymentDetails(String farmerId,String beneficiaryId, String name, String department, String scheme, String financialYear, String K2UTRNo, String paymentDate, String paymentMode, String sanctionedAmount) {
        this.farmerId = farmerId;
        this.beneficiaryId = beneficiaryId;
        this.name = name;
        this.department = department;
        this.scheme = scheme;
        this.financialYear = financialYear;
        this.K2UTRNo = K2UTRNo;
        this.paymentDate = paymentDate;
        this.paymentMode = paymentMode;
        this.sanctionedAmount = sanctionedAmount;
    }

    public ModelPaymentDetails(int id,String farmerId,String beneficiaryId, String name, String department, String scheme, String financialYear, String K2UTRNo, String paymentDate, String paymentMode, String sanctionedAmount) {
        this.id = id;
        this.farmerId = farmerId;
        this.beneficiaryId = beneficiaryId;
        this.name = name;
        this.department = department;
        this.scheme = scheme;
        this.financialYear = financialYear;
        this.K2UTRNo = K2UTRNo;
        this.paymentDate = paymentDate;
        this.paymentMode = paymentMode;
        this.sanctionedAmount = sanctionedAmount;
    }

    public ModelPaymentDetails(Parcel in) {
        id = in.readInt();
        farmerId = in.readString();
        beneficiaryId = in.readString();
        name = in.readString();
        department = in.readString();
        scheme = in.readString();
        financialYear = in.readString();
        K2UTRNo = in.readString();
        paymentDate = in.readString();
        paymentMode = in.readString();
        sanctionedAmount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(farmerId);
        parcel.writeString(beneficiaryId);
        parcel.writeString(name);
        parcel.writeString(department);
        parcel.writeString(scheme);
        parcel.writeString(financialYear);
        parcel.writeString(K2UTRNo);
        parcel.writeString(paymentDate);
        parcel.writeString(paymentMode);
        parcel.writeString(sanctionedAmount);

    }

    public static final Parcelable.Creator<ModelPaymentDetails> CREATOR = new Parcelable.Creator<ModelPaymentDetails>()
    {
        public ModelPaymentDetails createFromParcel(Parcel in)
        {
            return new ModelPaymentDetails(in);
        }
        public ModelPaymentDetails[] newArray(int size)
        {
            return new ModelPaymentDetails[size];
        }
    };

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

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getK2UTRNo() {
        return K2UTRNo;
    }

    public void setK2UTRNo(String k2UTRNo) {
        K2UTRNo = k2UTRNo;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(String sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
