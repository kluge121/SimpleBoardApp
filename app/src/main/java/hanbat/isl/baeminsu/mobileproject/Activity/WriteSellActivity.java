package hanbat.isl.baeminsu.mobileproject.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import hanbat.isl.baeminsu.mobileproject.Entity.SellPostUpLoadEntity;
import hanbat.isl.baeminsu.mobileproject.R;
import hanbat.isl.baeminsu.mobileproject.Set.FileType;
import hanbat.isl.baeminsu.mobileproject.Set.Network;
import hanbat.isl.baeminsu.mobileproject.Set.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WriteSellActivity extends AppCompatActivity implements View.OnClickListener {


    private final int PERMISSION_REQUEST_CODE = 100;
    private final int PICK_FROM_ALBUM = 90;

    EditText editTitle;
    EditText editContent;
    EditText editPrice;
    ImageView imagePlus;

    Button btnOk;

    String imgPath;
    String absolutePath;

    static String imgUpLoadPath = "";


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sell);
        setWidget();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkPermission();
    }


    void setWidget() {
        editTitle = findViewById(R.id.write_sell_title);
        editContent = findViewById(R.id.write_sell_content);
        editPrice = findViewById(R.id.write_sell_price);
        imagePlus = findViewById(R.id.write_sell_image);
        btnOk = findViewById(R.id.write_sell_ok);

        imagePlus.setOnClickListener(this);
        btnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.write_sell_image:
                doTakeAlbumAction();
                break;
            case R.id.write_sell_ok:
                SellPostUpLoadEntity data = new SellPostUpLoadEntity();
                data.setTitle(editTitle.getText().toString());
                data.setContent(editContent.getText().toString());
                data.setImagePath(imgUpLoadPath);
                data.setPrice(editPrice.getText().toString());

                if (validCheck()) {
                    new WriteAsyncTask().execute(data);
                }

                //TODO 정보 받아서 팔아요에 서버 POST


        }


    }

    boolean validCheck() {


        if (editTitle.getText().toString().length() == 0 || editContent.getText().toString().length() == 0
                || editPrice.getText().toString().length() == 0 || imgUpLoadPath.length() == 0) {
            Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    public static void saveBitmaptoJpeg(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/" + folder + "/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name;
        imgUpLoadPath = string_path + file_name;


        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_FROM_ALBUM:
                try {

                    //이미지 경로, 이름
                    if (data == null) break;
                    String imaName = getImageNameToUri(data.getData());
                    Uri selectImageUri = data.getData();

                    Cursor c = getContentResolver().query(Uri.parse(selectImageUri.toString()), null, null, null, null);
                    c.moveToNext();
                    absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //이미지 리사이징
                    int height = image_bitmap.getHeight();
                    int width = image_bitmap.getWidth();

                    Log.e("체크", absolutePath);
                    Bitmap src = BitmapFactory.decodeFile(absolutePath);
                    Bitmap resized = Bitmap.createScaledBitmap(src, width / 2, height / 2, true);

                    saveBitmaptoJpeg(resized, "resizeTmp", imaName);

                    imagePlus.setImageBitmap(resized);


                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "저장공간 권한 허용", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            } else {
                // 사용자 언제나 허락시
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // 퍼미션 모두 허용일 시
                } else {
                    Toast.makeText(this, "권한 설정을 하지 않을시 이용 불가", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    private class WriteAsyncTask extends AsyncTask<SellPostUpLoadEntity, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(WriteSellActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("판매글 등록중");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(SellPostUpLoadEntity... sellPostUpLoadEntities) {
            SellPostUpLoadEntity data = sellPostUpLoadEntities[0];
            OkHttpClient okHttpClient = new OkHttpClient.Builder().
                    connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            UserInfo userInfo = UserInfo.getUserInfo();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("writer", userInfo.getId());
            builder.addFormDataPart("title", data.getTitle());
            builder.addFormDataPart("name", userInfo.getName());
            builder.addFormDataPart("price", data.getPrice());
//            builder.addFormDatCouldn't load memtrack moduleaPart("date", );  날짜는 서버에서 처리
            builder.addFormDataPart("content", data.getContent());
            builder.addFormDataPart("state", "1");
            builder.addFormDataPart("phone", userInfo.getPhone());

            File tmpFile = new File(data.getImagePath());

            builder.addFormDataPart("image", tmpFile.getName(), RequestBody.create(FileType.IMAGE_MINE_TYPE, tmpFile));

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(Network.getAddress("write_sell_post.php"))
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String strResponse = response.body().string();
                return new JSONObject(strResponse).getString("msg");

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
            if (s.equals("판매글등록 성공")) {
                finish();
            }
        }
    }


}
