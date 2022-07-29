package com.anns.appnghenhacso.APIYouTube;

import com.anns.appnghenhacso.Model.Records;
import com.anns.appnghenhacso.ModelYT.RecordDetailVideoYT;
import com.anns.appnghenhacso.ModelYT.RecordYT;

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
