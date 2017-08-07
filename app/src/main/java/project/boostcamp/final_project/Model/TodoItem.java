package project.boostcamp.final_project.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

import static android.R.attr.format;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class TodoItem extends RealmObject{

    @Required
    private String todo;
    private String address;
    private double latitude;
    private double longitude;
    private String date;
    private String folder;
    private boolean alarm;
    private boolean isCompleted;

    public TodoItem(){
        this.date = getCurrentDate();
        isCompleted = false;
    }

    public TodoItem(String todo, String address, double latitude, double longitude, String folder){
        this.date = getCurrentDate();
        isCompleted = false;
        this.todo = todo;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.folder = folder;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String getCurrentDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        String current = format.format(date);
        return current;
    }
}
