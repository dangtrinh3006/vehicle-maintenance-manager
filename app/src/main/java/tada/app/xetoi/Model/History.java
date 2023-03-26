package tada.app.xetoi.Model;

import java.io.Serializable;
import java.util.Date;

public class History implements Serializable {
    private String id;
    private String type;
    private String date;
    private String location;
    private int km;
    private String detail;


    public History() {
    }

    public History(String id, String type, String date, String location, int KM, String detail) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.location = location;
        this.km = KM;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
