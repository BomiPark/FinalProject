package project.boostcamp.final_project.UI.NewItem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;


public abstract class NewItemBaseFragment extends Fragment {

    protected static View view;
    protected FragmentChangeListener listener;
    protected Toolbar toolbar;
    protected ImageView btn_ok;

    protected TodoItem item;
    protected InputMethodManager imm;

    public static Fragment newInstance(int now, int to){

        Fragment fragment = null;

        if(now == Constant.DETAIL && to == Constant.SEARCH) {
            fragment = new NewItemSearchFragment();
        }
        else if(now == Constant.DETAIL && to == Constant.MAP) {
            fragment = new NewItemMapFragment();
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setToolbar(View view, int status){
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.gray));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(status == Constant.MAP){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
