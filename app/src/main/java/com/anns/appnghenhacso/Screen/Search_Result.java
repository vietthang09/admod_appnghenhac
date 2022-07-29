package com.anns.appnghenhacso.Screen;

import android.content.Intent;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.anns.appnghenhacso.API.APIService;
import com.anns.appnghenhacso.API.Dataservice;
import com.anns.appnghenhacso.APIYouTube.APIServiceSearchYT;
import com.anns.appnghenhacso.APIYouTube.DataserviceYT;
import com.anns.appnghenhacso.Adapter.AdapterListYoutube;
import com.anns.appnghenhacso.Adapter.ListMusicCategoryAdapter;
import com.anns.appnghenhacso.Model.Item;
import com.anns.appnghenhacso.Model.Records;
import com.anns.appnghenhacso.ModelYT.ItemYT;
import com.anns.appnghenhacso.ModelYT.RecordYT;
import com.anns.appnghenhacso.R;
import com.google.gson.Gson;

import org.w3c.dom.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Search_Result extends AppCompatActivity {
    RecyclerView rcl_result_search;
    TextView text_value_search;
    SearchView searchView_result;
    private String textSearch = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__result);

        rcl_result_search = (RecyclerView) findViewById(R.id.rcl_result_search);
        text_value_search = (TextView) findViewById(R.id.text_value_search);
        searchView_result = (SearchView) findViewById(R.id.searchView_result);

        Intent intent = getIntent();
        if(intent.hasExtra("VALUE_SEARCH")){
            textSearch = intent.getStringExtra("VALUE_SEARCH");
        }
        GetDataSearch();
        getDataSearchYT();
        Search();
    }
    private void getDataSearchYT(){
        DataserviceYT dataserviceYT = APIServiceSearchYT.getService();
        Call<RecordYT> callback = dataserviceYT.GetListSearchYT(textSearch);
        callback.enqueue(new Callback<RecordYT>() {
            @Override
            public void onResponse(Call<RecordYT> call, Response<RecordYT> response) {
                RecordYT record_search = response.body();
                ArrayList<ItemYT> item = record_search.getItems();
                System.out.println(item.toArray().length + "Đã lấy được");
//                Gson gson = new Gson();
//                String json = gson.toJson(record_search.getItems());
                AdapterListYoutube adapterListYoutube = new AdapterListYoutube(item,getApplicationContext());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                rcl_result_search.setLayoutManager(linearLayoutManager);
                rcl_result_search.setAdapter(adapterListYoutube);
                text_value_search.setText(" \" "+textSearch+ " \" ");
            }

            @Override
            public void onFailure(Call<RecordYT> call, Throwable t) {

            }
        });

    }
    private void GetDataSearch(){
        Dataservice dataservice = APIService.getService();
        Call<Records> callback = dataservice.GetList("keyjd4mHxykM7LHvW");
        callback.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Call<Records> call, Response<Records> response) {
                Records record_search = response.body();
                ArrayList<Item> item = record_search.getItems();
                Collections.sort(item, new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            return formatter.parse(o2.getFields().getMusic_Size()).compareTo(formatter.parse(o1.getFields().getMusic_Size()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        };
                        return 0;
                    }
                });
//                ListMusicCategoryAdapter listMusicCategoryAdapter = new ListMusicCategoryAdapter(item,getApplicationContext());
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                rcl_result_search.setLayoutManager(linearLayoutManager);
//                rcl_result_search.setAdapter(listMusicCategoryAdapter);
//                text_value_search.setText(" \" "+textSearch+ " \" ");
//                listMusicCategoryAdapter.filter(textSearch);
            }

            @Override
            public void onFailure(Call<Records> call, Throwable t) {

            }
        });
    }

    private void  Search(){
        searchView_result.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch = query;
                getDataSearchYT();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}