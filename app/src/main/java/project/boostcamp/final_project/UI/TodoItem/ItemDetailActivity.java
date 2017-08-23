package project.boostcamp.final_project.UI.TodoItem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.renderscript.Byte2;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.NewItem.NewItemActivity;
import project.boostcamp.final_project.Util.BindingService;
import project.boostcamp.final_project.Util.RealmHelper;

import static project.boostcamp.final_project.R.id.btn_completed;

public class ItemDetailActivity extends AppCompatActivity {

    private Intent intent;
    private TodoItem item = new TodoItem();
    private Realm realm;
    private AlertDialog.Builder dialog;

    private TextView todo, address, folder;
    private ImageView back, ok, update, ic_delete;
    private Button on, off, btnCompleted;

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
        btnCompleted = (Button)findViewById(btn_completed);
        update = (ImageView)findViewById(R.id.to_update);
        on = (Button) findViewById(R.id.on);
        off = (Button)findViewById(R.id.off);
        ic_delete = (ImageView)findViewById(R.id.ic_delete);

        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);
        btnCompleted.setOnClickListener(clickListener);
        ic_delete.setOnClickListener(clickListener);

        intent = getIntent();
        dialog = new AlertDialog.Builder(ItemDetailActivity.this);
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
                case R.id.ic_delete :
                    removeItemDialogBox();
                    break;
                case btn_completed :
                    if(!item.isCompleted()){
                        realm.beginTransaction(); // case -> 수행완료
                        item.setCompleted(true);
                        realm.commitTransaction();

                        btnCompleted.setText(R.string.completed_ok);
                    }
                    else{
                        realm.beginTransaction(); // case -> 수행이전
                        item.setCompleted(false);
                        realm.commitTransaction();

                        btnCompleted.setText(R.string.completed_no);
                    }
                    BindingService.getInstance(getApplicationContext()).upDateService();
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(getParentActivityIntent() == null){
                    intent = new Intent(ItemDetailActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
                break;
        }
        return true;
    }

    void setLayout(){
        update.setVisibility(View.VISIBLE);
        ic_delete.setVisibility(View.VISIBLE);
        ok.setVisibility(View.GONE);
        todo.setText(item.getTodo());
        address.setText(item.getAddress());
        folder.setText(item.getFolder());
        if(item.isCompleted())
            btnCompleted.setText(R.string.completed_ok);
        else
            btnCompleted.setText(R.string.completed_no);
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

    @Override
    protected void onStop() {
        super.onStop();
        update.setVisibility(View.INVISIBLE);
    }

    void removeItemDialogBox(){
        dialog = new AlertDialog.Builder(ItemDetailActivity.this);
        dialog.setTitle(R.string.dialog_title).setMessage( " \n'" +item.getTodo() + "' 를 삭제하시겠습니까")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                realm.where(TodoItem.class).equalTo("id", item.getId()).findAll().deleteAllFromRealm();

                                BindingService.getInstance(getApplicationContext()).upDateService();
                                finish();
                            }
                        });
                    }
                })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // negative button logic
                            }
                        });
        AlertDialog dialogCreate = dialog.create();
        dialogCreate.show();
    }

}