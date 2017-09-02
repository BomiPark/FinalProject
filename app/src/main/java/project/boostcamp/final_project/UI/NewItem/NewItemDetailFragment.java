package project.boostcamp.final_project.UI.NewItem;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import io.realm.RealmResults;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.FolderItem;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.RealmHelper;

public class NewItemDetailFragment extends NewItemBaseFragment {

    private TextView subTitleText;
    private EditText todo;
    private Button folder, toSearch, toMap, on, off;

    private RealmResults<FolderItem> realmResults = null;
    private List<String> folderList = new ArrayList<>();

    private Realm realm;

    private int status;
    private boolean isAlarm = true;

    public NewItemDetailFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item_detail, container, false);

        setToolbar(view, Constant.DETAIL);

        toMap = (Button) view.findViewById(R.id.to_map);
        toSearch = (Button) view.findViewById(R.id.to_search);
        btn_ok = (ImageView)view.findViewById(R.id.detail_ok);
        subTitleText = (TextView)view.findViewById(R.id.sut_title);
        todo = (EditText)view.findViewById(R.id.todo);
        on = (Button)view.findViewById(R.id.on);
        off = (Button)view.findViewById(R.id.off);
        folder = (Button)view.findViewById(R.id.folder);

        toSearch.setOnClickListener(clickListener); // 클릭리스너 연결
        toMap.setOnClickListener(clickListener);
        btn_ok.setOnClickListener(clickListener);
        on.setOnClickListener(clickListener);
        off.setOnClickListener(clickListener);
        folder.setOnClickListener(clickListener);

        realm = RealmHelper.getInstance(getActivity());

        return view;

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
        listener.setStatus(Constant.DETAIL);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener.setStatus(Constant.DETAIL);
    }

    @Override
    public void onStart(){
        super.onStart();

        if(item != null)
            setView(); // 디테일 -> 다른 프래그먼트 다녀온 경우.
        else {
            if(listener.getCurrentItem().getTodo()!= null){ // 이전에 저장한 아이템 수정하는 경우
                this.item = listener.getCurrentItem();
                subTitleText.setText(getResources().getString(R.string.sub_title));
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.label_edit));
                setView();
            }
            else
                item = new TodoItem();
        }

        if(todo.getText().toString().equals("")) // wish를 입력하지 않은 경우 키보드 세팅
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        else
            imm.hideSoftInputFromWindow(todo.getWindowToken(), 0);
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
                case R.id.detail_ok :
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
            }
        }
    };

    boolean isItemEmpty(TodoItem item) {

        if (todo.getText().toString().equals("")) {
            Toasty.info(getContext(), getResources().getString(R.string.input_todo), Toast.LENGTH_SHORT, true).show();
            return true;
        }
        if (item.getAddress() == null) {
            Toasty.info(getContext(),  getResources().getString(R.string.input_where), Toast.LENGTH_SHORT).show();

            imm.hideSoftInputFromWindow(todo.getWindowToken(), 0);
            return true;
        }
        if (folder.getText().toString().equals(getResources().getString(R.string.select_folder))) {
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
        if(!folder.getText().toString().equals(getResources().getString(R.string.select_folder)))
            item.setFolder(folder.getText().toString());
        else
            item.setFolder("기본 폴더");

        realm.commitTransaction();
    }

    void setView(){

        todo.setText(item.getTodo());
        if(item.getFolder() == null)
            folder.setText(getResources().getString(R.string.select_folder));
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
                toMap.setTextColor(getResources().getColor(R.color.click_back)); // 체크된 상태
                toSearch.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.to_search :
                toMap.setTextColor(getResources().getColor(R.color.gray));
                toSearch.setTextColor(getResources().getColor(R.color.click_back));
                break;
            case R.id.on :
                on.setTextColor(getResources().getColor(R.color.click_back));
                off.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.off :
                on.setTextColor(getResources().getColor(R.color.gray));
                off.setTextColor(getResources().getColor(R.color.click_back));

        }
    }

    void getDialog(){
        new MaterialDialog.Builder(getActivity())
                .title(getResources().getString(R.string.select_folder))
                .items(getFolderList())
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
                .positiveText(getResources().getString(R.string.ok))
                .show();
    }

    List<String> getFolderList(){

        realmResults = realm.where(FolderItem.class).findAll();

        folderList = ImmutableList.copyOf(Collections2.transform(realmResults, new Function<FolderItem, String>(){
            @Nullable
            @Override
            public String apply(@Nullable FolderItem input) {
                return input.getFolder();
            }
        }));

        return folderList;
    }


}



