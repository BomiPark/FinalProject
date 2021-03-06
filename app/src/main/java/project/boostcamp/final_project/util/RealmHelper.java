package project.boostcamp.final_project.util;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import project.boostcamp.final_project.model.FolderItem;
import project.boostcamp.final_project.model.TodoItem;

public class RealmHelper {

    private static Realm realm;

    public static Realm getInstance(Context context){

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        return realm;
    }

    public static int getNextTodoId(){

        int nextID =0;

        if(realm.where(TodoItem.class).findAll().size() > 0)
            nextID = realm.where(TodoItem.class).findAllSorted("id").last().getId() + 1;

        return nextID;
    }

    public static int getNextFolderId(Realm realm){

        int nextID =0;

        if(realm.where(TodoItem.class).findAll().size() > 0)
            nextID = realm.where(FolderItem.class).findAllSorted("id").last().getId() + 1;

        return nextID;
    }

    public static void close(){
        realm.close();
    }

}
