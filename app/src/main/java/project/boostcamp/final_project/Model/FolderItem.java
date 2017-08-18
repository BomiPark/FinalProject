package project.boostcamp.final_project.Model;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;
import project.boostcamp.final_project.Model.Dto.PojoFolderItem;

public class FolderItem  extends RealmObject {

    private int id;
    private String folder;

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

    public static List<String> getFolderList(RealmResults<FolderItem> list){

        List<String> folderList = new ArrayList<>();

        for(FolderItem item : list){
            folderList.add(item.getFolder());
        }
        return folderList;
    }

    public static PojoFolderItem toPojo(FolderItem item){
        PojoFolderItem pojo = new PojoFolderItem(item.id, item.folder);

        return pojo;
    }
}
