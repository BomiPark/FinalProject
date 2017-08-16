package project.boostcamp.final_project.UI.TodoItem;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import io.realm.Realm;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.NewItem.NewItemActivity;
import project.boostcamp.final_project.Util.GeofencingService;
import project.boostcamp.final_project.Util.GeofencingService.GeoBinder;
import project.boostcamp.final_project.Util.RealmHelper;

public class ItemDetailActivity extends AppCompatActivity {

    private Intent intent;
    private TodoItem item = new TodoItem();
    private Realm realm;

    private TextView todo, address, folder;
    private ImageView back, ok;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        init();

    }

    void init(){

        realm = RealmHelper.getInstance(this);

        todo = (TextView)findViewById(R.id.todo);
        address = (TextView)findViewById(R.id.address);
        folder = (TextView)findViewById(R.id.folder);
        back = (ImageView)findViewById(R.id.back);
        ok = (ImageView)findViewById(R.id.ok);
        ok.setVisibility(View.INVISIBLE);

        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);

        intent = getIntent();
        position = intent.getExtras().getInt("id"); // 아이템 받아와서 세팅하면 될 듯!!

        item = realm.where(TodoItem.class).equalTo("id", position).findFirst();
        setLayout();

    }

    public void onResume(){
        super.onResume();
        setLayout();
    }

    View.OnClickListener clickListener = new ImageView.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back :
                    if(getParentActivityIntent() == null)
                        startActivity(new Intent(ItemDetailActivity.this, MainActivity.class));
                    finish();
                    break;
                case R.id.ok :
                    break;
            }
        }
    };

    void setLayout(){

        todo.setText(item.getTodo());
        address.setText(item.getAddress());
        folder.setText(item.getFolder());
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.to_update :
                Intent intent = new Intent(this, NewItemActivity.class);
                intent.putExtra("id", position);
                startActivity(intent);
        }
    }

}
