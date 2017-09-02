package project.boostcamp.final_project.UI.TodoItem;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.NewItem.NewItemActivity;
import project.boostcamp.final_project.UI.Setting.BaseActivity;
import project.boostcamp.final_project.Util.BindingService;
import project.boostcamp.final_project.Util.RealmHelper;

import static project.boostcamp.final_project.R.id.btn_completed;

public class ItemDetailActivity extends BaseActivity {

    private Intent intent;
    private TodoItem item = new TodoItem();
    private Realm realm;
    private AlertDialog.Builder dialog;

    private TextView todo, address, folder;
    private ImageView update, ic_delete;
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
        btnCompleted = (Button)findViewById(btn_completed);
        update = (ImageView)findViewById(R.id.to_update);
        on = (Button) findViewById(R.id.on);
        off = (Button)findViewById(R.id.off);
        ic_delete = (ImageView)findViewById(R.id.ic_delete);

        btnCompleted.setOnClickListener(clickListener);
        ic_delete.setOnClickListener(clickListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.gray));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        dialog = new AlertDialog.Builder(ItemDetailActivity.this);
        position = intent.getExtras().getInt("id");
        item = realm.where(TodoItem.class).equalTo("id", position).findFirst();
        setLayout();

        BindingService.getInstance(getApplicationContext()).stopService();
    }

    public void onResume(){
        super.onResume();
        setLayout();
    }

    public void onDestroy(){
        super.onDestroy();
        BindingService.getInstance(getApplicationContext()).upDateService();

    }

    View.OnClickListener clickListener = new ImageView.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
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
        todo.setText(item.getTodo());
        address.setText(item.getAddress());
        folder.setText(item.getFolder());
        if(item.isCompleted())
            btnCompleted.setText(R.string.completed_ok);
        else
            btnCompleted.setText(R.string.completed_no);
        if(item.isAlarm()){
            on.setTextColor(getResources().getColor(R.color.click_back));
            off.setTextColor(getResources().getColor(R.color.gray));
        }
        else{
            on.setTextColor(getResources().getColor(R.color.gray));
            off.setTextColor(getResources().getColor(R.color.click_back));
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
        dialog.setMessage( " \n'" +item.getTodo() + "' 를 삭제하시겠습니까")
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        if(getParentActivityIntent() == null){
            intent = new Intent(ItemDetailActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();

        return super.onOptionsItemSelected(menu);
    }

}