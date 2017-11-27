package project.boostcamp.final_project.model.Dto;

import project.boostcamp.final_project.model.TodoItem;

/**
 * Created by qkrqh on 2017-08-17.
 */

public class PojoTodoItem {

    private int id;
    private String todo;
    private String address;
    private double latitude;
    private double longitude;
    private String date;
    private String folder;
    private boolean alarm;
    private boolean isCompleted;

    public PojoTodoItem(){}

    public PojoTodoItem(int id, String todo, String address, double latitude, double longitude, String date,
                        String folder, boolean alarm, boolean isCompleted){
        this.id = id;
        this.todo = todo;
        this.address = address;
        this.alarm = alarm;
        this.latitude = latitude;
        this.longitude = longitude;
        this.folder = folder;
        this.date = date;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public static TodoItem toRealm(PojoTodoItem item){
        TodoItem todo = new TodoItem(item.id, item.todo, item.address, item.latitude, item.longitude, item.date,
                item.folder, item.alarm,item.isCompleted);

        return todo;
    }
}
