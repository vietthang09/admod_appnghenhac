package com.anns.appnghenhacso.data.remote.APIYouTube;

import com.anns.appnghenhacso.data.remote.API.APIRetrofitClient;
import com.anns.appnghenhacso.common.Constants;

public class APIServiceSearchYT {
    public static DataserviceYT getService() {
        return APIRetrofitClient.getClient(Constants.YT_API_URL).create(DataserviceYT.class);
    }
}
