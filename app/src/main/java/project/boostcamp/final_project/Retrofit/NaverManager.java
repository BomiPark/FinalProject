package project.boostcamp.final_project.Retrofit;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import project.boostcamp.final_project.Model.ItemList;
import project.boostcamp.final_project.Model.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NaverManager { // 현재 사용 x

    public ArrayList<Item> getList(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com/v1/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NaverService naverService = retrofit.create(NaverService.class);

        ArrayList<Item> list = new ArrayList<>();

        Call<ItemList> call = naverService.getSearchList(query, 20);

        call.enqueue(new Callback<ItemList>() {
            @Override
            public void onResponse(Call<ItemList> call, Response<ItemList> response) {
                if(response.isSuccessful()) {
                    List<Item> list = new ArrayList<>();
                    list = response.body().getItem();
                }
            else
                Log.e("NaverManager", "response fail");}

            @Override
            public void onFailure(Call<ItemList> call, Throwable t) {

                Log.e("fail", " ");
            }
        });

        return list;
    }

}
