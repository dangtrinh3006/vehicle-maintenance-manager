package tada.app.xetoi.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Vehicle implements Serializable {
    private String id;
    private String name;
    private String number;
    private String type;
    private String brand;
    private String baoduong;
    private String dangkiem;
    private String baohiem;
    //private List<History> lstHistory;

    public Vehicle() { }

    public Vehicle(String id, String name, String number, String type, String brand, String baoduong, String dangkiem, String baohiem/*, List<History> lstHistory*/) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.type = type;
        this.brand = brand;
        this.baoduong = baoduong;
        this.dangkiem = dangkiem;
        this.baohiem = baohiem;
        //this.lstHistory = lstHistory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBaoduong() {
        return baoduong;
    }

    public void setBaoduong(String baoduong) {
        this.baoduong = baoduong;
    }

    public String getDangkiem() {
        return dangkiem;
    }

    public void setDangkiem(String dangkiem) {
        this.dangkiem = dangkiem;
    }

    public String getBaohiem() {
        return baohiem;
    }

    public void setBaohiem(String baohiem) {
        this.baohiem = baohiem;
    }

    /*public List<History> getLstHistory() {
        return lstHistory;
    }

    public void setLstHistory(List<History> lstHistory) {
        this.lstHistory = lstHistory;
    }*/
}
