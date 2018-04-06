package hanbat.isl.baeminsu.mobileproject.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

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

public class SplashActivity extends AppCompatActivity {

    Context context;
    SplashHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        handler = new SplashHandler(this);
        handler.sendEmptyMessageDelayed(0, 2000);
    }


    private class LoginAsync extends AsyncTask<LoginEntity, Void, String> {
        LoginEntity data;
        ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);
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
            if (s != null && s.equals("로그인 성공")) {
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
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }

    private class SplashHandler extends Handler {
        private final WeakReference<SplashActivity> ref;

        SplashHandler(SplashActivity act) {
            this.ref = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final PropertyManager propertyManager = PropertyManager.getInstance();
            final boolean login = propertyManager.getAutoLogin();

            SplashActivity act = ref.get();

            if (act != null) {
                if (login) {
                    LoginEntity data = new LoginEntity();
                    data.setId(propertyManager.getId());
                    data.setPass(propertyManager.getPass());
                    new LoginAsync().execute(data);
                } else {
                    Intent intent = new Intent(act, LoginActivity.class);
                    act.startActivity(intent);
                    act.finish();
                }
            }
        }
    }

}
