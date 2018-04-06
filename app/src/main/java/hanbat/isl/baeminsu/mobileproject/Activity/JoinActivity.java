package hanbat.isl.baeminsu.mobileproject.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import hanbat.isl.baeminsu.mobileproject.Entity.JoinEntity;
import hanbat.isl.baeminsu.mobileproject.R;
import hanbat.isl.baeminsu.mobileproject.Set.FileType;
import hanbat.isl.baeminsu.mobileproject.Set.Network;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    private int LOGIN_BACK_RESULT = 12;

    EditText editId;
    EditText editPass;
    EditText editName;
    EditText editPhone;

    Button btnJoin;
    Button btnCancle;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        mContext = this;
        setWidget();


    }

    void setWidget() {
        editId = findViewById(R.id.join_id_edit);
        editPass = findViewById(R.id.join_pass_edit);
        editName = findViewById(R.id.join_name_edit);
        editPhone = findViewById(R.id.join_phone_edit);
        btnCancle = findViewById(R.id.join_cancle_btn);
        btnJoin = findViewById(R.id.join_join_btn);

        btnJoin.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.join_cancle_btn:
                finish();
                break;
            case R.id.join_join_btn:
                JoinEntity data = new JoinEntity();

                data.setId(editId.getText().toString());
                data.setPassword(editPass.getText().toString());
                data.setName(editName.getText().toString());
                data.setPhone(editPhone.getText().toString());


                new JoinAsyncTask().execute(data);

                break;

        }

    }


    private class JoinAsyncTask extends AsyncTask<JoinEntity, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(JoinActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("회원가입 중");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(JoinEntity... joinEntities) {
            JoinEntity data = joinEntities[0];
            JSONObject jsonObject = new JSONObject();

            OkHttpClient okHttpClient = new OkHttpClient();

            try {
                jsonObject.put("id", data.getId());
                jsonObject.put("pass", data.getPassword());
                jsonObject.put("name", data.getName());
                jsonObject.put("phone", data.getPhone());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(FileType.JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Network.getAddress("join.php"))
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
            if (s.equals("가입 성공")) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("id", editId.getText().toString());
                setResult(LOGIN_BACK_RESULT, intent);
                finish();
            } else {
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
