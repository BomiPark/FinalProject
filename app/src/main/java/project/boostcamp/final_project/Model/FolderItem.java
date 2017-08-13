package project.boostcamp.final_project.Model;

import io.realm.RealmObject;


public class FolderItem  extends RealmObject {

    int id;
    String folder;

    public FolderItem(){}

    public FolderItem(int id, String folder){
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
