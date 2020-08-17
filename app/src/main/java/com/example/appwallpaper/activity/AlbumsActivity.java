package com.example.appwallpaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.appwallpaper.R;
import com.example.appwallpaper.adapter.AlbumAdapter;
import com.example.appwallpaper.model.Album;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AlbumsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvList;
    private ArrayList<Album> mArrayList = new ArrayList<>();

    AlbumAdapter mAdapter;
    private SwipeRefreshLayout mSrlLayout;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    SweetAlertDialog mProgressDialog;
    private EditText edtSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        mSrlLayout = findViewById(R.id.srlLayout_favorites);

        mSrlLayout.setOnRefreshListener(this);

        edtSearch = (EditText) findViewById(R.id.edtSearch);


        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mAdapter = new AlbumAdapter(mArrayList, AlbumsActivity.this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(staggeredGridLayoutManager);
        rvList.setAdapter(mAdapter);
        rvList.setItemAnimator(null);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvList.getRecycledViewPool().clear();
        mArrayList.clear();

        loadData();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(AlbumsActivity.this, "onTextChanged", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Toast.makeText(AlbumsActivity.this, "afterTextChanged", Toast.LENGTH_SHORT).show();
                mAdapter.SearchId(edtSearch.getText().toString().trim());
            }
        });
    }

    private void loadData() {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mProgressDialog.setTitleText("Loading");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                getData_F();
                return null;
            }
        };
        asyncTask.execute();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mArrayList.clear();
                mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemRangeRemoved(0, mArrayList.size());
                getData_F();
                mSrlLayout.setRefreshing(false);
            }
        }, 2500);
    }

    private void getData_F() {

        AndroidNetworking.get("https://jsonplaceholder.typicode.com/albums").build().getAsJSONArray(new JSONArrayRequestListener() {

            @Override
            public void onResponse(JSONArray response) {
                Log.e("response: ", response.toString() + "");
                mArrayList.addAll(new Gson().fromJson(response.toString(), new TypeToken<ArrayList<Album>>() {
                }.getType()));
                Log.e("myAlbum: ", mArrayList.size() + "");

                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                mSrlLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemRangeRemoved(0, mArrayList.size());
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }


}