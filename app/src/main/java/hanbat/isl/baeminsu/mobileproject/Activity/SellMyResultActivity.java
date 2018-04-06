package hanbat.isl.baeminsu.mobileproject.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import hanbat.isl.baeminsu.mobileproject.Entity.BuyPostUpLoadEntity;
import hanbat.isl.baeminsu.mobileproject.Entity.SellListEntity;
import hanbat.isl.baeminsu.mobileproject.R;
import hanbat.isl.baeminsu.mobileproject.Set.ApplicationContext;
import hanbat.isl.baeminsu.mobileproject.Set.FileType;
import hanbat.isl.baeminsu.mobileproject.Set.MainListRecyclerViewDecoration;
import hanbat.isl.baeminsu.mobileproject.Set.Network;
import hanbat.isl.baeminsu.mobileproject.Set.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SellMyResultActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_my_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.sell_my_recyclerview);

        adapter = new RecyclerViewAdapter();
        adapter.setContext(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(ApplicationContext.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MainListRecyclerViewDecoration(20));


        new GetAsync().execute(UserInfo.getUserInfo().getId());


    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<CustumViewHolder> {

        ArrayList<SellListEntity> arrayList = new ArrayList();
        Context context;

        public void setArrayList(ArrayList arrayList) {
            this.arrayList = arrayList;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public RecyclerViewAdapter() {
        }

        @Override
        public CustumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_my_sell, parent, false);
            return new CustumViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustumViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            holder.setView((SellListEntity) arrayList.get(position));
            Glide.with(context).load(((SellListEntity) arrayList.get(position)).getImageURL()).into(holder.iv);
            if (arrayList.get(position).getState() == 0) {
                holder.complete.setVisibility(View.INVISIBLE);
                holder.delete.setGravity(Gravity.CENTER);
            }


            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SellMyResultActivity.this, SellDetailActivity.class);
                    intent.putExtra("detail", arrayList.get(position));
                    startActivity(intent);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = arrayList.get(position).getId();
                    new RemoveAsync().execute(id);

                }
            });
            holder.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = arrayList.get(position).getId();
                    new UpdateAsync().execute(id);
                }
            });


        }

        @Override
        public int getItemCount() {

            if (arrayList.size() == 0)
                return 0;
            return arrayList.size();
        }
    }


    class CustumViewHolder extends RecyclerView.ViewHolder {


        View v;
        ImageView iv;
        TextView title;
        TextView price;
        TextView name;
        TextView date;
        Button delete;
        Button complete;
        ConstraintLayout layout;

        public CustumViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            layout = itemView.findViewById(R.id.layout2);
            iv = itemView.findViewById(R.id.sell_iv);
            title = itemView.findViewById(R.id.sell_title);
            price = itemView.findViewById(R.id.sell_price);
            name = itemView.findViewById(R.id.sell_name);
            date = itemView.findViewById(R.id.sell_date);
            delete = itemView.findViewById(R.id.sell_delete);
            complete = itemView.findViewById(R.id.sell_complete);
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

    private class GetAsync extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog progressDialog = new ProgressDialog(SellMyResultActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("리스트 로딩중");
            progressDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            String keword = strings[0];
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Network.getAddress("get_sell_my_list.php?keyword=" + keword))
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
            } catch (JSONException e1) {
                e1.printStackTrace();
                adapter.setArrayList(new ArrayList());
                adapter.notifyDataSetChanged();
            } finally {
                progressDialog.dismiss();
            }
        }


    }

    private class RemoveAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(SellMyResultActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("삭제중");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            JSONObject jsonObject = new JSONObject();

            OkHttpClient okHttpClient = new OkHttpClient();

            try {
                jsonObject.put("id", id);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(FileType.JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Network.getAddress("sell_delete.php"))
                    .post(body)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String strResponse = response.body().string();
                JSONObject responseJson = new JSONObject(strResponse);
                return responseJson.getString("msg");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            new GetAsync().execute(UserInfo.getUserInfo().getId());

        }
    }

    private class UpdateAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(SellMyResultActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("삭제중");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            JSONObject jsonObject = new JSONObject();

            OkHttpClient okHttpClient = new OkHttpClient();

            try {
                jsonObject.put("id", id);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(FileType.JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Network.getAddress("sell_update.php"))
                    .post(body)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String strResponse = response.body().string();
                JSONObject responseJson = new JSONObject(strResponse);
                return responseJson.getString("msg");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            new GetAsync().execute(UserInfo.getUserInfo().getId());

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
