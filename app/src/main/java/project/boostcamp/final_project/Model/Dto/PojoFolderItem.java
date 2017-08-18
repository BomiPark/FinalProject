package project.boostcamp.final_project.Model.Dto;

import project.boostcamp.final_project.Model.FolderItem;

public class PojoFolderItem {

    private int id;
    private String folder;

    public PojoFolderItem(){}

    public PojoFolderItem(int id, String folder){
        this.id  = id;
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

    public static FolderItem toRealm(PojoFolderItem item){
        FolderItem realm = new FolderItem(item.id, item.folder);
        return realm;
    }
}
