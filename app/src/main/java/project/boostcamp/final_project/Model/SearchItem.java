package project.boostcamp.final_project.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by qkrqh on 2017-08-02.
 */

public class SearchItem {

    String title;
    String address;
    String roadAddress;
    String telephone;
    @Expose
    boolean selected = false;
    @Expose //object 중 해당 값이 null일 경우, json으로 만들 필드를 자동 생략
    Double latitude;
    @Expose
    Double longitude;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String zipcode) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString(){
        return title + " " + address;
    }
}
