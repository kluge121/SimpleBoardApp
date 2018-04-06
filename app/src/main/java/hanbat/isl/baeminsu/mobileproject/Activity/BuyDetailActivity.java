package hanbat.isl.baeminsu.mobileproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import hanbat.isl.baeminsu.mobileproject.Entity.BuyListEntity;
import hanbat.isl.baeminsu.mobileproject.Entity.SellListEntity;
import hanbat.isl.baeminsu.mobileproject.R;

public class BuyDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvNameDate;
    TextView tvPhone;
    TextView tvContent;
    TextView tvName;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setWidget();


        BuyListEntity data = (BuyListEntity) getIntent().getSerializableExtra("detail");
        inputData(data);
    }

    void setWidget() {

        tvTitle = findViewById(R.id.buy_detail_title);
        tvNameDate = findViewById(R.id.buy_detail_name_date);
        tvPhone = findViewById(R.id.buy_detail_phone);
        tvContent = findViewById(R.id.buy_detail_content);
        tvName = findViewById(R.id.buy_detail_name);

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhone.getText().toString()));
                startActivity(intent);
            }
        });
    }


    void inputData(BuyListEntity data) {
        tvTitle.setText(data.getTitle());
        tvPhone.setText(data.getPhone());
        tvContent.setText(data.getContent());

        tvName.setText(data.getName());
        tvNameDate.setText(data.getDate());

    }
}
