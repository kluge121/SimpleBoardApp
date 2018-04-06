package hanbat.isl.baeminsu.mobileproject.Activity;

import android.app.ProgressDialog;
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

import hanbat.isl.baeminsu.mobileproject.Entity.LoginEntity;
import hanbat.isl.baeminsu.mobileproject.R;
import hanbat.isl.baeminsu.mobileproject.Set.FileType;
import hanbat.isl.baeminsu.mobileproject.Set.Network;
import hanbat.isl.baeminsu.mobileproject.Set.PropertyManager;
import hanbat.isl.baeminsu.mobileproject.Set.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private int JOIN_BACK_REQUEST = 11;
    private int JOIN_BACK_RESULT = 12;

    EditText editId;
    EditText editPass;
    Button btnLogin;
    Button btnJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setWidget();

    }


    void setWidget() {

        editId = findViewById(R.id.login_id_edit);
        editPass = findViewById(R.id.login_pass_edit);
        btnLogin = findViewById(R.id.login_login_btn);
        btnJoin = findViewById(R.id.login_join_btn);

        btnJoin.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_join_btn:
                Intent intent = new Intent(this, JoinActivity.class);
                startActivityForResult(intent, JOIN_BACK_REQUEST);

                break;

            case R.id.login_login_btn:
                String Id = editId.getText().toString();
                String Pass = editPass.getText().toString();

                LoginEntity data = new LoginEntity();
                data.setId(Id);
                data.setPass(Pass);

                new LoginAsync().execute(data);

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == JOIN_BACK_REQUEST && resultCode == JOIN_BACK_RESULT) {
            String tmpId = data.getStringExtra("id");
            editId.setText(tmpId);
        }
    }


    private class LoginAsync extends AsyncTask<LoginEntity, Void, String> {
        LoginEntity data;
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        JSONObject responseJson;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로그인중");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(LoginEntity... loginEntities) {
            data = loginEntities[0];
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("id", data.getId());
                jsonObject.put("pass", data.getPass());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            OkHttpClient okHttpClient = new OkHttpClient();


            RequestBody body = RequestBody.create(FileType.JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Network.getAddress("login.php"))
                    .post(body)
                    .build();


            try {
                Response response = okHttpClient.newCall(request).execute();
                String strResponse = response.body().string();
                responseJson = new JSONObject(strResponse);
                return responseJson.getString("msg");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            if (s.equals("로그인 성공")) {
                PropertyManager propertyManager = PropertyManager.getInstance();
                propertyManager.setAutoLogin(data.getId(), data.getPass(), true);
                UserInfo userInfo = UserInfo.getUserInfo();
                userInfo.setId(data.getId());
                try {
                    userInfo.setPhone(responseJson.getString("phone"));
                    userInfo.setName(responseJson.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }
}
