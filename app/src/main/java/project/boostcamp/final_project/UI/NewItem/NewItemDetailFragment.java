package project.boostcamp.final_project.UI.NewItem;

import android.content.Context;
import android.graphics.Color;
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

import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;

public class NewItemDetailFragment extends Fragment {

    static View view;
    FragmentChangeListener listener;
    TextView toMap, toSearch, on, off;
    ImageView back, ok;
    EditText todo;
    boolean isAlarm = true;

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
        on = (TextView)view.findViewById(R.id.on);
        off = (TextView)view.findViewById(R.id.off);

        item = new TodoItem();

        toSearch.setOnClickListener(clickListener); // 클릭리스너 연결
        toMap.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);
        on.setOnClickListener(clickListener);
        off.setOnClickListener(clickListener);

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
            setBtnColor(view.getId()); // 위치 설정한 방법은 붉은색으로 표시
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
                        item.setTodo(todo.getText().toString()); // 입력 받은 값 세팅 todo 폴더 추가
                        item.setAlarm(isAlarm);
                        listener.changeFragment(Constant.DETAIL, Constant.SAVE, item);
                    }
                    break;
                case R.id.on :
                    isAlarm = true;
                    break;
                case R.id.off :
                    isAlarm = false;
                    break;
            }
        }
    };

    void setBtnColor(int id){
        Log.e("new", id + "  " ); //todo 텍스트뷰 색 바꾼거 상태 저장 안되고 on off 버튼은 이전에체크한애만 반응한당
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
                on.setTextColor(Color.parseColor("#767676")); // 체크
                off.setTextColor(Color.parseColor("#E35757"));
                break;
            case R.id.off :
                on.setTextColor(Color.parseColor("#E35757"));
                off.setTextColor(Color.parseColor("#767676")); // 체크

        }
    }


}
