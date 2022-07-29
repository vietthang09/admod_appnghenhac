package com.anns.appnghenhacso.APIYouTube;

import com.anns.appnghenhacso.API.APIRetrofitClient;
import com.anns.appnghenhacso.API.Dataservice;

public class APIServiceSearchYT {
    private static final String base_url = "https://offre333.com/api/v1/yt/";
    public static DataserviceYT getService() {
        return APIRetrofitClient.getClient(base_url).create(DataserviceYT.class);
    }
}
