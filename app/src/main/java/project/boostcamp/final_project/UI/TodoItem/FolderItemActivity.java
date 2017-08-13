package project.boostcamp.final_project.UI.TodoItem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import project.boostcamp.final_project.Adapter.FolderItemAdapter;
import project.boostcamp.final_project.Adapter.SimpleTodoItemAdapter;
import project.boostcamp.final_project.Model.FolderItem;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;

public class FolderItemActivity extends AppCompatActivity {

    RealmResults<TodoItem> list;
    RecyclerView recyclerView;
    SimpleTodoItemAdapter todoItemAdapter;

    TextView folder_title;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_item);

        recyclerView = (RecyclerView)findViewById(R.id.simple_todo_list);
        folder_title = (TextView)findViewById(R.id.folder_title);

        initData(getIntent().getExtras().getString("folder"));
    }


    void initData(String folderName){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        folder_title.setText(folderName);

        realm = Realm.getDefaultInstance();

        list = realm.where(TodoItem.class).equalTo("folder", folderName).findAll();

        todoItemAdapter = new SimpleTodoItemAdapter(this, list, R.layout.item_simple_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(todoItemAdapter);

    }
}
