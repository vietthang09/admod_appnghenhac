package com.anns.appnghenhacso.API;
import com.anns.appnghenhacso.Model.Records;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Dataservice {

    @GET("app0hEtQ8jNQVUOdq/1?")
    Call<Records> GetList(@Query("api_key") String api_key);

    @GET("{id_category}/1?")
    Call<Records> GetMusicByCategory(@Path("id_category") String id_category, @Query("api_key") String api_key);
}
