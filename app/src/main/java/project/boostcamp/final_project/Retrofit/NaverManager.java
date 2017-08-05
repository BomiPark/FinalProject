package project.boostcamp.final_project.Retrofit;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import project.boostcamp.final_project.Model.SearchItemList;
import project.boostcamp.final_project.Model.SearchItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NaverManager { // 현재 사용 x

    public ArrayList<SearchItem> getList(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com/v1/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NaverService naverService = retrofit.create(NaverService.class);

        ArrayList<SearchItem> list = new ArrayList<>();

        Call<SearchItemList> call = naverService.getSearchList(query, 20);

        call.enqueue(new Callback<SearchItemList>() {
            @Override
            public void onResponse(Call<SearchItemList> call, Response<SearchItemList> response) {
                if(response.isSuccessful()) {
                    List<SearchItem> list = new ArrayList<>();
                    list = response.body().getItem();
                }
            else
                Log.e("NaverManager", "response fail");}

            @Override
            public void onFailure(Call<SearchItemList> call, Throwable t) {

                Log.e("fail", " ");
            }
        });

        return list;
    }

}
