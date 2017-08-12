package project.boostcamp.final_project.Model;

import io.realm.RealmModel;

/**
 * Created by qkrqh on 2017-08-10.
 */

public class Folder implements RealmModel {

    int id;
    String folder;

    public Folder(){}

    public Folder(int id, String folder){
        this.folder = folder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
