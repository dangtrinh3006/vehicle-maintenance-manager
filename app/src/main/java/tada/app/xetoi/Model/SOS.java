package tada.app.xetoi.Model;

public class SOS {

    public String SOSname;
    public String SOSNumber;
    public SOS() {
    }
    public SOS(String SOSname, String SOSNumber) {
        this.SOSname = SOSname;
        this.SOSNumber = SOSNumber;
    }
    public String getSOSname() {
        return SOSname;
    }

    public String getSOSNumber() {
        return SOSNumber;
    }

    public void setSOSname(String SOSname) {
        this.SOSname = SOSname;
    }

    public void setSOSNumber(String SOSNumber) {
        this.SOSNumber = SOSNumber;
    }


}
