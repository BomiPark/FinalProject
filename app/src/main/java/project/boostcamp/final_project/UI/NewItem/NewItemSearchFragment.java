package project.boostcamp.final_project.UI.NewItem;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import project.boostcamp.final_project.Interface.RecyclerItemClickListener;
import project.boostcamp.final_project.Adapter.SearchItemAdapter;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.SearchItem;
import project.boostcamp.final_project.Model.SearchItemList;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Retrofit.NaverService;
import project.boostcamp.final_project.Retrofit.ServiceAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewItemSearchFragment  extends Fragment {

    private static View view;
    private EditText editSearch;
    private ImageView searchIcon, back, ok;
    private ArrayList<SearchItem> searchItemList;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SearchItemAdapter adapter;

    private NaverService naverService;
    private FragmentChangeListener listener;

    private TodoItem item = new TodoItem();
    private int beforeSelected = -1;
    private Geocoder geoCoder;

    public NewItemSearchFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_item_search, container, false);

        editSearch = (EditText)view.findViewById(R.id.edit_search);
        searchIcon = (ImageView)view.findViewById(R.id.search_icon);
        recyclerView = (RecyclerView)view.findViewById(R.id.search_list);
        back = (ImageView)view.findViewById(R.id.back);
        ok = (ImageView)view.findViewById(R.id.ok);
        searchIcon.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);
        editSearch.setOnKeyListener(keyListener);
        geoCoder = new Geocoder(getContext());

        naverService = ServiceAdapter.getService();

        searchItemList = new ArrayList<>();
        adapter = new SearchItemAdapter(getContext(), searchItemList, R.layout.item_search);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if(beforeSelected > -1) { // 이전에 선택한 아이템이 있는 경우 체크 해제
                    searchItemList.get(beforeSelected).setSelected(false);}
                if(position > -1) {
                    searchItemList.get(position).setSelected(true);
                    if (beforeSelected != position)
                        adapter.notifyDataSetChanged();
                    beforeSelected = position;
                    new BackAsyncTask().execute(item.getAddress());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener.setStatus(Constant.DETAIL);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
        listener.setStatus(Constant.SEARCH);
    }

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.search_icon :
                    setSearch();
                    break;
                case R.id.back :
                    listener.changeFragment(Constant.SEARCH, Constant.DETAIL, null);
                    break;
                case R.id.ok :
                    getSelectItem();
                    break;
            }
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

        String query = editSearch.getText().toString();

        if(query != null) {
            getSearchList(query);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);    //hide keyboard
        }
        else
            Toast.makeText(getActivity(), "검색어를 입력해주세요", Toast.LENGTH_LONG).show();
    }

    void getSearchList(String query){

        getList(query);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void getList(String query) {
        naverService.getSearchList(query, 20).enqueue(new Callback<SearchItemList>() {
            @Override
            public void onResponse(Call<SearchItemList> call, Response<SearchItemList> response) {
                if(response.isSuccessful()) {

                    int itemCount = response.body().getItem().size();

                    if(itemCount != 0){
                        searchItemList = (ArrayList<SearchItem>) response.body().getItem();

                        for(SearchItem searchItem : searchItemList){
                            searchItem.setTitle(stripHtml(searchItem.getTitle()));
                        }
                        adapter.setSearchItemList(searchItemList);
                        adapter.notifyDataSetChanged();
                    }

                    else{
                        Toasty.info(getActivity(), "검색 결과가 없습니다." , Toast.LENGTH_LONG).show();
                    }
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<SearchItemList> call, Throwable t) {
                Log.d("NewItemSearch", "error loading from API");

            }
        });
    }

    public String stripHtml(String html){
        return Html.fromHtml(html).toString();
    }

     void getSelectItem(){
        if(beforeSelected < 0)
            Toast.makeText(getActivity(), "지점을 선택해주세요 ", Toast.LENGTH_LONG).show();
        else{
            item.setAddress(searchItemList.get(beforeSelected).getAddress());
            if(item.getLatitude() == 0)
                new BackAsyncTask().execute(item.getAddress());

            listener.changeFragment(Constant.SEARCH, Constant.DETAIL,item);}
    }

    public TodoItem setLatLng(SearchItem searchItem){

        List<Address> list = null;
        try {
            list = geoCoder.getFromLocationName(searchItem.getAddress(), 1);
            item.setLatitude(list.get(0).getLatitude());
            item.setLongitude(list.get(0).getLongitude());
        } catch (Exception e) {

        }
        return item;
    }

    class BackAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            item = setLatLng(searchItemList.get(beforeSelected));
            return null;
        }
    }
}
