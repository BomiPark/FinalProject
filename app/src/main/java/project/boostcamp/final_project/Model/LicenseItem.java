package project.boostcamp.final_project.Model;

/**
 * Created by qkrqh on 2017-08-23.
 */

public class LicenseItem {

    private String title;
    private String address;
    private String copyright;

    public LicenseItem(){}

    public LicenseItem(String title, String address, String copyright){
        this.title = title;
        this.address= address;
        this.copyright = copyright;
    }

    public String getTitle() {
        return title;
    }

    public void setValue(String title, String address, String copyright){
        this.title = title;
        this.address= address;
        this.copyright = copyright;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
