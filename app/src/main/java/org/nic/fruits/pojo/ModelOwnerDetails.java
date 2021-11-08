package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="farmer_owner_details")
public class ModelOwnerDetails {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String farmerId;
    private String ownerName;
    private String ownernumber;
    private String mainownernumber;
    private String relativename;
    private String surveynumber;
    private String villagecode;
    private String villagelgcode;
    private String area;

    @Ignore
    public ModelOwnerDetails(int id, String farmerId, String ownerName, String ownernumber, String mainownernumber, String relativename,String surveynumber, String villagecode, String villagelgcode,String area) {
        this.id = id;
        this.farmerId = farmerId;
        this.ownerName = ownerName;
        this.ownernumber = ownernumber;
        this.mainownernumber = mainownernumber;
        this.relativename = relativename;
        this.surveynumber = surveynumber;
        this.villagecode = villagecode;
        this.villagelgcode = villagelgcode;
        this.area = area;
    }

    public ModelOwnerDetails(String farmerId, String ownerName, String ownernumber, String mainownernumber, String relativename,String surveynumber, String villagecode, String villagelgcode,String area) {
        this.farmerId = farmerId;
        this.ownerName = ownerName;
        this.ownernumber = ownernumber;
        this.mainownernumber = mainownernumber;
        this.relativename = relativename;
        this.surveynumber = surveynumber;
        this.villagecode = villagecode;
        this.villagelgcode = villagelgcode;
        this.area = area;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnernumber() {
        return ownernumber;
    }

    public void setOwnernumber(String ownernumber) {
        this.ownernumber = ownernumber;
    }

    public String getMainownernumber() {
        return mainownernumber;
    }

    public void setMainownernumber(String mainownernumber) {
        this.mainownernumber = mainownernumber;
    }

    public String getRelativename() {
        return relativename;
    }

    public void setRelativename(String relativename) {
        this.relativename = relativename;
    }

    public String getSurveynumber() {
        return surveynumber;
    }

    public void setSurveynumber(String surveynumber) {
        this.surveynumber = surveynumber;
    }

    public String getVillagecode() {
        return villagecode;
    }

    public void setVillagecode(String villagecode) {
        this.villagecode = villagecode;
    }

    public String getVillagelgcode() {
        return villagelgcode;
    }

    public void setVillagelgcode(String villagelgcode) {
        this.villagelgcode = villagelgcode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
