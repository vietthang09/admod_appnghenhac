package com.anns.appnghenhacso.API;

public class APIService {

    private static final String base_url = "https://api.airtable.com/v0/";
    public static Dataservice getService() {
        return APIRetrofitClient.getClient(base_url).create(Dataservice.class);
    }
}
