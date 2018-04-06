package hanbat.isl.baeminsu.mobileproject.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import hanbat.isl.baeminsu.mobileproject.Entity.BuyPostUpLoadEntity;
import hanbat.isl.baeminsu.mobileproject.R;
import hanbat.isl.baeminsu.mobileproject.Set.FileType;
import hanbat.isl.baeminsu.mobileproject.Set.Network;
import hanbat.isl.baeminsu.mobileproject.Set.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WriteBuyActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTtile;
    EditText editContent;
    Button btnOk;



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_buy);
        setWidget();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    void setWidget() {
        editTtile = findViewById(R.id.write_buy_title);
        editContent = findViewById(R.id.write_buy_content);
        btnOk = findViewById(R.id.write_buy_ok);
        btnOk.setOnClickListener(this);


    }

    boolean validCheck() {

        if (editTtile.getText().toString().length() == 0 || editContent.getText().toString().length() == 0
                ) {
            Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.write_buy_ok:
                BuyPostUpLoadEntity data = new BuyPostUpLoadEntity();
                data.setTitle(editTtile.getText().toString());
                data.setContent(editContent.getText().toString());

                if (validCheck()) {
                    new WriteAsyncTask().execute(data);

                }

        }
    }

    private class WriteAsyncTask extends AsyncTask<BuyPostUpLoadEntity, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(WriteBuyActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("구매글 등록중");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(BuyPostUpLoadEntity... buyPostUpLoadEntities) {
            BuyPostUpLoadEntity data = buyPostUpLoadEntities[0];
            JSONObject jsonObject = new JSONObject();

            OkHttpClient okHttpClient = new OkHttpClient();
            UserInfo userInfo = UserInfo.getUserInfo();

            try {
                jsonObject.put("writer", userInfo.getId());
                jsonObject.put("title", data.getTitle());
                jsonObject.put("name", userInfo.getName());
                jsonObject.put("content", data.getContent());
                jsonObject.put("state", "1");
                jsonObject.put("phone", userInfo.getPhone());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(FileType.JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Network.getAddress("write_buy_post.php"))
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
            if (s.equals("구매글 등록 성공")) {
                finish();
            }

        }
    }
}
