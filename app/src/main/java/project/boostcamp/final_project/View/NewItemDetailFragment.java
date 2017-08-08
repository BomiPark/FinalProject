package project.boostcamp.final_project.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;

public class NewItemDetailFragment extends Fragment {

    static View view;
    FragmentChangeListener listener;
    TextView toMap, toSearch;
    ImageView back, ok;
    EditText todo;

    TodoItem item;

    public NewItemDetailFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item_detail, container, false);

        toMap = (TextView)view.findViewById(R.id.to_map);
        toSearch = (TextView)view.findViewById(R.id.to_search);
        back = (ImageView)view.findViewById(R.id.back);
        ok = (ImageView)view.findViewById(R.id.ok);
        todo = (EditText)view.findViewById(R.id.todo);

        item = new TodoItem();

        toSearch.setOnClickListener(clickListener);
        toMap.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);

        return view;
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
        listener.setStatus(Constant.DETAIL);
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.to_map :
                    listener.changeFragment(Constant.DETAIL, Constant.MAP, null);
                    break;
                case R.id.to_search :
                    listener.changeFragment(Constant.DETAIL, Constant.SEARCH, null);
                    break;
                case R.id.back :
                    listener.changeFragment(Constant.DETAIL, Constant.END, null);
                    break;
                case R.id.ok :
                    item = listener.getCurrentItem();
                    if(item.getAddress() == null)
                        Toast.makeText(getContext(), "알람이 울릴 지점을 선택해주세요", Toast.LENGTH_LONG).show();
                    else{
                        item.setTodo(todo.getText().toString());
                        listener.changeFragment(Constant.DETAIL, Constant.SAVE, item);
                    }
                    break;
            }
        }
    };



}
