package project.boostcamp.final_project.View;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.boostcamp.final_project.Adapter.SearchItemAdapter;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Item;
import project.boostcamp.final_project.Model.ItemList;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Retrofit.NaverService;
import project.boostcamp.final_project.Retrofit.ServiceAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewItemSearchFragment  extends Fragment {

    static View view;
    EditText editSearch;
    ImageView searchIcon;
    ArrayList<Item> itemList;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SearchItemAdapter adapter;

    NaverService naverService;
    FragmentChangeListener listener;

    public NewItemSearchFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item_search, container, false);

        editSearch = (EditText)view.findViewById(R.id.edit_search);
        searchIcon = (ImageView)view.findViewById(R.id.search_icon);
        recyclerView = (RecyclerView)view.findViewById(R.id.search_list);
        searchIcon.setOnClickListener(clickListener);
        editSearch.setOnKeyListener(keyListener);

        naverService = ServiceAdapter.getService();

        itemList = new ArrayList<>();
        adapter = new SearchItemAdapter(getContext(),itemList, R.layout.search_item);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
    }

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            setSearch();
        }
    };

    View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                setSearch();
                return true;
            }
            return false;
        }
    };

    void setSearch(){
        getSearchList(editSearch.getText().toString());

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow( editSearch.getWindowToken(), 0);    //hide keyboard
    }

    void getSearchList(String query){

        getList(query);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public void getList(String query) {
        naverService.getSearchList(query, 20).enqueue(new Callback<ItemList>() {
            @Override
            public void onResponse(Call<ItemList> call, Response<ItemList> response) {
                if(response.isSuccessful()) {

                    int itemCount = response.body().getItem().size();

                    if(itemCount != 0){
                        itemList = (ArrayList<Item>) response.body().getItem();

                        for(Item item : itemList){
                            item.setTitle(stripHtml(item.getTitle()));
                        }
                        adapter.setItemList(itemList);
                        adapter.notifyDataSetChanged();
                    }

                    else{
                        Toast.makeText(getActivity(), "검색 결과가 업습니다." , Toast.LENGTH_LONG).show();
                    }

                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<ItemList> call, Throwable t) {
                Log.d("NewItemSearch", "error loading from API");

            }
        });
    }

    public String stripHtml(String html){
        return Html.fromHtml(html).toString();
    }
}
