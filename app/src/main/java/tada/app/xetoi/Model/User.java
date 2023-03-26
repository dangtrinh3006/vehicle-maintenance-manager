package tada.app.xetoi.Model;
import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String fullName;
    private String phone;
    private boolean gender;
    private String birthDay;
    private String country;
    //private List<Vehicle> listVehicle;

    public User(String id, String fullName, String phone, boolean gender, String birthDay, String country/*, List<Vehicle> listVehicle*/) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.birthDay = birthDay;
        this.country = country;
        //this.listVehicle = listVehicle;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /*public List<Vehicle> getListVehicle() {
        return listVehicle;
    }

    public void setListVehicle(List<Vehicle> listVehicle) {
        this.listVehicle = listVehicle;
    }

    public void addVehicle(Vehicle vehicle) {
        listVehicle.add(vehicle);
    }*/
}
