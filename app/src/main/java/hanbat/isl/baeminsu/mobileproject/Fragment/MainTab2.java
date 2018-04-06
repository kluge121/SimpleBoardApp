package hanbat.isl.baeminsu.mobileproject.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import hanbat.isl.baeminsu.mobileproject.Activity.BuyDetailActivity;
import hanbat.isl.baeminsu.mobileproject.Activity.MainActivity;
import hanbat.isl.baeminsu.mobileproject.Activity.SellDetailActivity;
import hanbat.isl.baeminsu.mobileproject.Entity.BuyListEntity;
import hanbat.isl.baeminsu.mobileproject.Entity.SellListEntity;
import hanbat.isl.baeminsu.mobileproject.R;
import hanbat.isl.baeminsu.mobileproject.Set.ApplicationContext;
import hanbat.isl.baeminsu.mobileproject.Set.MainListRecyclerViewDecoration;
import hanbat.isl.baeminsu.mobileproject.Set.Network;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by baeminsu on 2017. 12. 7..
 */

public class MainTab2 extends Fragment {

    public static MainTab2 newInstance() {
        return new MainTab2();
    }

    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    Tab2RecyclerViewAdapter adapter = new Tab2RecyclerViewAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_maintab2, container, false);

        adapter = new MainTab2.Tab2RecyclerViewAdapter();
        adapter.setContext(getContext());

        recyclerView = v.findViewById(R.id.tab2_recyclerview);
        swipe = v.findViewById(R.id.tab2_swipe);


        recyclerView.setLayoutManager(new LinearLayoutManager(ApplicationContext.getContext()));
        recyclerView.setAdapter(adapter);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new MainTab2.GetAsync().execute();
            }
        });

        recyclerView.addItemDecoration(new MainListRecyclerViewDecoration(20));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && MainActivity.fab.isShown())
                    MainActivity.fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    MainActivity.fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        new GetAsync().execute();
        return v;


    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAsync().execute();
    }

    class Tab2RecyclerViewAdapter extends RecyclerView.Adapter<MainTab2.Tab2ViewHolder> {

        ArrayList<BuyListEntity> arrayList = new ArrayList();
        Context context;


        public void setArrayList(ArrayList arrayList) {
            this.arrayList = arrayList;
        }

        public void setContext(Context context) {
            this.context = context;
        }


        @Override
        public MainTab2.Tab2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_buy, parent, false);
            return new MainTab2.Tab2ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MainTab2.Tab2ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            holder.setView((BuyListEntity) arrayList.get(position));
            if (arrayList.get(position).getState() == 0) {
                holder.out.setVisibility(View.VISIBLE);
            }


            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (arrayList.get(position).getState() == 1) {
                        Intent intent = new Intent(getContext(), BuyDetailActivity.class);
                        intent.putExtra("detail", arrayList.get(position));
                        startActivity(intent);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


    class Tab2ViewHolder extends RecyclerView.ViewHolder {

        View v;
        TextView title;
        TextView name;
        TextView date;
        TextView out;

        public Tab2ViewHolder(View itemView) {

            super(itemView);
            v = itemView;
            title = itemView.findViewById(R.id.buy_title);
            name = itemView.findViewById(R.id.buy_name);
            date = itemView.findViewById(R.id.buy_date);

        }

        void setView(BuyListEntity data) {
            title.setText(data.getTitle());
            name.setText(data.getName());
            date.setText(data.getDate());
            out = itemView.findViewById(R.id.item_buy_out);


        }
    }


    private class GetAsync extends AsyncTask<Void, Void, JSONObject> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!swipe.isRefreshing()) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("리스트 로딩중");
                progressDialog.show();
            }

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Network.getAddress("get_buy_list.php"))
                    .get()
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String strResponse = response.body().string();
                JSONObject jsonObject = new JSONObject(strResponse);

                return jsonObject;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            JSONArray jsonArray = null;
            try {
                jsonArray = jsonObject.getJSONArray("json");
                adapter.setArrayList(jsonHandling(jsonArray));
                adapter.notifyDataSetChanged();


                if (swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                } else {
                    progressDialog.dismiss();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }


    }


    private ArrayList jsonHandling(JSONArray jsonArray) throws JSONException {
        ArrayList<BuyListEntity> arrayList = new ArrayList<BuyListEntity>();
        JSONObject tmpJson;
        BuyListEntity tmpEntity;

        for (int i = 0; i < jsonArray.length(); i++) {
            tmpJson = jsonArray.getJSONObject(i);
            tmpEntity = new BuyListEntity();
            tmpEntity.setId(tmpJson.getString("id"));
            tmpEntity.setWriter(tmpJson.getString("writer"));
            tmpEntity.setTitle(tmpJson.getString("title"));
            tmpEntity.setName(tmpJson.getString("name"));
            tmpEntity.setDate(tmpJson.getString("date"));
            tmpEntity.setContent(tmpJson.getString("content"));
            tmpEntity.setState(Integer.parseInt(tmpJson.getString("state")));
            tmpEntity.setPhone(tmpJson.getString("phone"));


            arrayList.add(tmpEntity);
            tmpEntity = null;

        }
        return arrayList;
    }
}
