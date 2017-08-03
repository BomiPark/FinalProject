package project.boostcamp.final_project.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import project.boostcamp.final_project.Model.Item;
import project.boostcamp.final_project.Model.ItemList;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Retrofit.NaverService;
import project.boostcamp.final_project.Retrofit.ServiceAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class RetrofitActivity extends AppCompatActivity {

    NaverService naverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        naverService = ServiceAdapter.getService();

        getList("스타벅스");

    }

    public void getList(String query) {
        naverService.getSearchList(query, 20).enqueue(new Callback<ItemList>() {
            @Override
            public void onResponse(Call<ItemList> call, Response<ItemList> response) {
                if(response.isSuccessful()) {
                    Log.d("Retrofit ", " 반환갯수 " + response.body().getItem().size()); //todo 이거 0이면 처리해주기!

                    Item item = response.body().getItem().get(0);
                    item.setTitle(stripHtml(item.getTitle()));
                    Log.d("html 처리 ", "  " + item.getTitle());


                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<ItemList> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");

            }
        });
    }

    public String stripHtml(String html){
        return Html.fromHtml(html).toString();
    }
}
