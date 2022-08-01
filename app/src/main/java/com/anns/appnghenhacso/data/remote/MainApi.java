package com.anns.appnghenhacso.data.remote;
import com.anns.appnghenhacso.domain.model.Model.Records;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MainApi {

    @GET("app0hEtQ8jNQVUOdq/1?")
    Call<Records> GetList(@Query("api_key") String api_key);

    @GET("{id_category}/1?")
    Call<Records> GetMusicByCategory(@Path("id_category") String id_category, @Query("api_key") String api_key);
}
