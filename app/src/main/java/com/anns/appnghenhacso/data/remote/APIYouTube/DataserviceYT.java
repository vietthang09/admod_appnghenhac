package com.anns.appnghenhacso.data.remote.APIYouTube;

import com.anns.appnghenhacso.domain.model.ModelYT.RecordDetailVideoYT;
import com.anns.appnghenhacso.domain.model.ModelYT.RecordYT;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataserviceYT {
    @GET("search?&page=1")
    Call<RecordYT> GetListSearchYT(@Query("q") String search);

    @GET("stream/{idVideo}")
    Call<RecordDetailVideoYT> GetDetailVideo(@Path("idVideo") String idVideo);
}
