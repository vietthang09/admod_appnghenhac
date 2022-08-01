package com.anns.appnghenhacso.data.remote.API;

import com.anns.appnghenhacso.common.Constants;
import com.anns.appnghenhacso.data.remote.MainApi;

public class APIService {

    public static MainApi getService() {
        return APIRetrofitClient.getClient(Constants.BASE_URL).create(MainApi.class);
    }
}
