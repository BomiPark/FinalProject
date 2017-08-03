package project.boostcamp.final_project.Retrofit;

import project.boostcamp.final_project.Model.ItemList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NaverService {

    @Headers({
            "X-Naver-Client-Id:ApiXTaPn_pn5I75LiYv0",
            "X-Naver-Client-Secret:pqPTYVFL17"
    })
    @GET("local.json")
    Call<ItemList> getSearchList(@Query("query") String query, @Query("display") int display);
}
