package project.boostcamp.final_project.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.R;

public class NewItemDetailFragment extends Fragment {

    static View view;
    FragmentChangeListener listener;
    TextView toMap, toSearch;

    public NewItemDetailFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item_detail, container, false);

        toMap = (TextView)view.findViewById(R.id.to_map);
        toSearch = (TextView)view.findViewById(R.id.to_search);

        toSearch.setOnClickListener(clickListener);

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.to_map :

                    break;
                case R.id.to_search :
                    listener.changeFragment(Constant.DETAIL, Constant.SEARCH);
            }
        }
    };

}
