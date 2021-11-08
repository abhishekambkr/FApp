package org.nic.fruits.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="irrigationtype")
public class ModelIrrigationType {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String irrigationtype;
    private String irrigationtypeeng;
    private String irrigationtypekn;

    @Ignore
    public ModelIrrigationType(int id, String irrigationtype, String irrigationtypeeng, String irrigationtypekn) {
        this.id = id;
        this.irrigationtype = irrigationtype;
        this.irrigationtypeeng = irrigationtypeeng;
        this.irrigationtypekn = irrigationtypekn;
    }

    public ModelIrrigationType(String irrigationtype, String irrigationtypeeng, String irrigationtypekn) {
        this.irrigationtype = irrigationtype;
        this.irrigationtypeeng = irrigationtypeeng;
        this.irrigationtypekn = irrigationtypekn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIrrigationtype() {
        return irrigationtype;
    }

    public void setIrrigationtype(String irrigationtype) {
        this.irrigationtype = irrigationtype;
    }

    public String getIrrigationtypeeng() {
        return irrigationtypeeng;
    }

    public void setIrrigationtypeeng(String irrigationtypeeng) {
        this.irrigationtypeeng = irrigationtypeeng;
    }

    public String getIrrigationtypekn() {
        return irrigationtypekn;
    }

    public void setIrrigationtypekn(String irrigationtypekn) {
        this.irrigationtypekn = irrigationtypekn;
    }
}
