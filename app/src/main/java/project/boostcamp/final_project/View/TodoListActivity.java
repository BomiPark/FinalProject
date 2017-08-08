package project.boostcamp.final_project.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import project.boostcamp.final_project.Adapter.TodoItemAdapter;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;

public class TodoListActivity extends AppCompatActivity {

    RealmResults<TodoItem> itemList;
    RecyclerView recyclerView;
    TodoItemAdapter adapter;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        init();
    }

    void init(){

        setData();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new TodoItemAdapter(this, itemList, R.layout.todo_item);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void setData(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        itemList = realm.where(TodoItem.class).findAll();

    }
}
