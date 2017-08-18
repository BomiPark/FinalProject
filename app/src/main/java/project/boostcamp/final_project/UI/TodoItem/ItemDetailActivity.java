package project.boostcamp.final_project.UI.TodoItem;

import android.content.Intent;
import android.graphics.Color;
import android.renderscript.Byte2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.NewItem.NewItemActivity;
import project.boostcamp.final_project.Util.RealmHelper;

import static project.boostcamp.final_project.R.id.toSearch;

public class ItemDetailActivity extends AppCompatActivity {

    private Intent intent;
    private TodoItem item = new TodoItem();
    private Realm realm;

    private TextView todo, address, folder;
    private ImageView back, ok;
    private Button on, off, completed, not_completed;

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
        on = (Button) findViewById(R.id.on);
        off = (Button)findViewById(R.id.off);
        completed = (Button)findViewById(R.id.completed);
        not_completed = (Button)findViewById(R.id.not_completed);


        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);
        completed.setOnClickListener(clickListener);
        not_completed.setOnClickListener(clickListener);

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
                    if(getParentActivityIntent() == null){
                        intent = new Intent(ItemDetailActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    finish();
                    break;
                case R.id.on :

                    break;
                case R.id.off :

                    break;
                case R.id.completed :

                    break;
                case R.id.not_completed :

                    break;
            }
        }
    };

    void setLayout(){

        todo.setText(item.getTodo());
        address.setText(item.getAddress());
        folder.setText(item.getFolder());
        if(item.isCompleted()) {
            completed.setTextColor(Color.parseColor("#E35757"));
            not_completed.setTextColor(Color.parseColor("#767676"));
        }
        else{
            completed.setTextColor(Color.parseColor("#E35757"));
            not_completed.setTextColor(Color.parseColor("#767676"));
        }
        if(item.isAlarm()){
            on.setTextColor(Color.parseColor("#E35757"));
            off.setTextColor(Color.parseColor("#767676"));
        }
        else{
            on.setTextColor(Color.parseColor("#767676"));
            off.setTextColor(Color.parseColor("#E35757"));
        }
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
