package hanbat.isl.baeminsu.mobileproject.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import hanbat.isl.baeminsu.mobileproject.Activity.MainActivity;
import hanbat.isl.baeminsu.mobileproject.Activity.SellDetailActivity;
import hanbat.isl.baeminsu.mobileproject.Activity.WriteSellActivity;
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

public class MainTab1 extends Fragment {

    public static MainTab1 newInstance() {
        return new MainTab1();
    }


    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    Tab1RecyclerViewAdapter adapter = new Tab1RecyclerViewAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_maintab1, container, false);

        adapter = new Tab1RecyclerViewAdapter();
        adapter.setContext(getContext());

        recyclerView = v.findViewById(R.id.tab1_recyclerview);
        swipe = v.findViewById(R.id.tab1_swipe);

        recyclerView.setLayoutManager(new LinearLayoutManager(ApplicationContext.getContext()));
        recyclerView.setAdapter(adapter);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetAsync().execute();
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

    // 팔아요 탭
    class Tab1RecyclerViewAdapter extends RecyclerView.Adapter<MainTab1.Tab1ViewHolder> {

        ArrayList<SellListEntity> arrayList = new ArrayList();
        Context context;

        public void setArrayList(ArrayList arrayList) {
            this.arrayList = arrayList;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public Tab1RecyclerViewAdapter() {
        }

        @Override
        public Tab1ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_sell, parent, false);
            return new Tab1ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(Tab1ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            Drawable alpha = holder.out.getBackground();

            holder.setView((SellListEntity) arrayList.get(position));
            Glide.with(context).load(((SellListEntity) arrayList.get(position)).getImageURL()).into(holder.iv);

            if (arrayList.get(position).getState() == 0) {
                holder.out.setVisibility(View.VISIBLE);
            }

            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList.get(position).getState() == 1) {
                        Intent intent = new Intent(getContext(), SellDetailActivity.class);
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


    class Tab1ViewHolder extends RecyclerView.ViewHolder {


        View v;
        ImageView iv;
        TextView title;
        TextView price;
        TextView name;
        TextView date;
        TextView out;

        public Tab1ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            iv = itemView.findViewById(R.id.sell_iv);
            title = itemView.findViewById(R.id.sell_title);
            price = itemView.findViewById(R.id.sell_price);
            name = itemView.findViewById(R.id.sell_name);
            date = itemView.findViewById(R.id.sell_date);
            out = itemView.findViewById(R.id.item_sell_out);
        }

        void setView(SellListEntity data) {



            title.setText(data.getTitle());
            name.setText(data.getName());
            date.setText(data.getDate());


            String number = data.getPrice();
            double amount = Double.parseDouble(number);
            DecimalFormat formatter = new DecimalFormat("#,###원");
            String formatted = formatter.format(amount);
            price.setText(formatted);

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
                    .url(Network.getAddress("get_sell_list.php"))
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
        ArrayList<SellListEntity> arrayList = new ArrayList<SellListEntity>();
        JSONObject tmpJson;
        SellListEntity tmpEntity;

        for (int i = 0; i < jsonArray.length(); i++) {
            tmpJson = jsonArray.getJSONObject(i);
            tmpEntity = new SellListEntity();

            tmpEntity.setId(tmpJson.getString("id"));
            tmpEntity.setWriter(tmpJson.getString("writer"));
            tmpEntity.setTitle(tmpJson.getString("title"));
            tmpEntity.setName(tmpJson.getString("name"));
            tmpEntity.setPrice(tmpJson.getString("price"));
            tmpEntity.setDate(tmpJson.getString("date"));
            tmpEntity.setImageURL(Network.getAddress2(tmpJson.getString("image")));
            tmpEntity.setContent(tmpJson.getString("content"));
            tmpEntity.setState(Integer.parseInt(tmpJson.getString("state")));
            tmpEntity.setPhone(tmpJson.getString("phone"));

            arrayList.add(tmpEntity);
            tmpEntity = null;

        }
        return arrayList;
    }
}
