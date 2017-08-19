package project.boostcamp.final_project.UI.NewItem;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.FolderItem;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.RealmHelper;

import static project.boostcamp.final_project.R.id.toSearch;
import static project.boostcamp.final_project.UI.NewItem.NewItemMapFragment.view;

public class NewItemDetailFragment extends Fragment {

    static View view;
    FragmentChangeListener listener;
    private TextView subTitleText;
    private ImageView back, ok, img_plus;
    private EditText todo;
    private Button folder, toSearch, toMap, on, off;

    private RealmResults<FolderItem> realmResults = null;
    private List<String> folderList = new ArrayList<>();

    private Realm realm;
    private TodoItem item;

    private int status;
    private boolean isAlarm = true;

    public NewItemDetailFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item_detail, container, false);

        toMap = (Button) view.findViewById(R.id.to_map);
        toSearch = (Button) view.findViewById(R.id.to_search);
        back = (ImageView)view.findViewById(R.id.back);
        ok = (ImageView)view.findViewById(R.id.ok);
        subTitleText = (TextView)view.findViewById(R.id.sut_title);
        img_plus = (ImageView)view.findViewById(R.id.img_plus);
        todo = (EditText)view.findViewById(R.id.todo);
        on = (Button)view.findViewById(R.id.on);
        off = (Button)view.findViewById(R.id.off);
        folder = (Button)view.findViewById(R.id.folder);

        initData();

        img_plus.setOnClickListener(clickListener);
        toSearch.setOnClickListener(clickListener); // 클릭리스너 연결
        toMap.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);
        on.setOnClickListener(clickListener);
        off.setOnClickListener(clickListener);
        folder.setOnClickListener(clickListener);

        return view;

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
    }

    @Override
    public void onStart(){
        super.onStart();

        if(item != null)
            setView(); // 디테일 -> 다른 프래그먼트 다녀온 경우.
        else {
            item = new TodoItem();
            if(listener.getCurrentItem().getTodo()!= null){ // 이전에 저장한 아이템 수정하는 경우
                this.item = listener.getCurrentItem();
                subTitleText.setText(getResources().getString(R.string.sub_title));
                setView();
            }
        }

        initData();

    }

    @Override
    public void onPause(){
        super.onPause();

        saveData();

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setBtnColor(view.getId()); // 위치 설정한 방법은 붉은색으로 표시
            switch (view.getId()){
                case R.id.to_map :
                    status = R.id.to_map;
                    listener.changeFragment(Constant.DETAIL, Constant.MAP, null);
                    break;
                case R.id.to_search :
                    status = R.id.to_search;
                    listener.changeFragment(Constant.DETAIL, Constant.SEARCH, null);
                    break;
                case R.id.back :
                    listener.changeFragment(Constant.DETAIL, Constant.END, null);
                    break;
                case R.id.ok :
                    item = listener.getCurrentItem();
                    if(!isItemEmpty(item)){
                        saveData();
                        listener.changeFragment(Constant.DETAIL, Constant.SAVE, item);
                    }
                    break;
                case R.id.on :
                    isAlarm = true;
                    break;
                case R.id.off :
                    isAlarm = false;
                    break;
                case R.id.folder :
                    getDialog();
                    break;
                case R.id.img_plus :
                    item = listener.getCurrentItem();
                    if(!isItemEmpty(item)) {
                        listener.changeFragment(Constant.DETAIL, Constant.SAVE, item);
                        saveData();
                    }
                    break;
            }
        }
    };

    boolean isItemEmpty(TodoItem item) {

        if (todo.getText().toString().equals("")) {
            Toasty.info(getContext(), getResources().getString(R.string.input_todo), Toast.LENGTH_LONG, true).show();
            return true;
        }
        if (item.getAddress() == null) {
            Toasty.info(getContext(),  getResources().getString(R.string.input_where), Toast.LENGTH_LONG).show();
            return true;
        }
        if (folder.getText().toString().equals("폴더선택")) {
            realm.beginTransaction();
            item.setFolder(getResources().getString(R.string.folder_default0));
            realm.commitTransaction();
            return false;
        }
        return false;
    }

    void saveData(){

        realm.beginTransaction();

        item.setTodo(todo.getText().toString());
        item.setAlarm(isAlarm);
        if(!folder.getText().toString().equals("폴더선택"))
            item.setFolder(folder.getText().toString());
        else
            item.setFolder("기본 폴더");

        realm.commitTransaction();
    }

    void setView(){

        todo.setText(item.getTodo());
        if(item.getFolder() == null)
            folder.setText("폴더 선택");
        else
            folder.setText(item.getFolder());
        setBtnColor(status);

        if(item.isAlarm())
            setBtnColor(R.id.on);
        else
            setBtnColor(R.id.off);
    }


    void setBtnColor(int id){
        switch(id){
            case R.id.to_map :
                toMap.setTextColor(Color.parseColor("#E35757")); // 체크된 상태
                toSearch.setTextColor(Color.parseColor("#767676"));
                break;
            case R.id.to_search :
                toMap.setTextColor(Color.parseColor("#767676")); // 체크
                toSearch.setTextColor(Color.parseColor("#E35757"));
                break;
            case R.id.on :
                on.setTextColor(Color.parseColor("#E35757"));
                off.setTextColor(Color.parseColor("#767676"));
                break;
            case R.id.off :
                on.setTextColor(Color.parseColor("#767676"));
                off.setTextColor(Color.parseColor("#E35757"));

        }
    }

    void getDialog(){
        new MaterialDialog.Builder(getActivity())
                .title("폴더선택")
                .items(folderList)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        folder.setText(folderList.get(which));
                        realm.beginTransaction();
                        item.setFolder(folderList.get(which));
                        realm.commitTransaction();
                        return true;
                    }
                })
                .positiveText("확인")
                .show();
    }

    void initData(){

        realm = RealmHelper.getInstance(getActivity());

        realmResults = realm.where(FolderItem.class).findAll();

        folderList = ImmutableList.copyOf(Collections2.transform(realmResults, new Function<FolderItem, String>(){
            @Nullable
            @Override
            public String apply(@Nullable FolderItem input) {
                return input.getFolder();
            }
        }));
    }


}



