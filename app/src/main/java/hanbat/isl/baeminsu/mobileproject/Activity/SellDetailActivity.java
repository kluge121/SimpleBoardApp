package hanbat.isl.baeminsu.mobileproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import hanbat.isl.baeminsu.mobileproject.Entity.SellListEntity;
import hanbat.isl.baeminsu.mobileproject.R;

public class SellDetailActivity extends AppCompatActivity {


    TextView tvTitle;
    TextView tvNamePriceDate;
    TextView price;
    ImageView imageView;
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
        setContentView(R.layout.activity_sell_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setWidget();

        final SellListEntity data = (SellListEntity) getIntent().getSerializableExtra("detail");
        inputData(data);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailImage.class);
                intent.putExtra("url", data.getImageURL());
                startActivity(intent);

            }
        });


    }

    void setWidget() {
        tvTitle = findViewById(R.id.sell_detail_title);
        tvNamePriceDate = findViewById(R.id.sell_detail_name_price_date);
        imageView = findViewById(R.id.sell_detail_image);
        tvContent = findViewById(R.id.sell_detail_name_content);
        tvPhone = findViewById(R.id.sell_detail_phone);
        tvName = findViewById(R.id.sell_detail_name);
        price = findViewById(R.id.sell_detail_price);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhone.getText().toString()));
                startActivity(intent);
            }
        });


    }

    void inputData(SellListEntity data) {
        tvTitle.setText(data.getTitle());
        Glide.with(this).load((data.getImageURL())).into(imageView);
        tvContent.setText(data.getContent());
        tvPhone.setText(data.getPhone());
        tvName.setText(data.getName());
        tvNamePriceDate.setText(data.getDate());

        String number = data.getPrice();
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###원");
        String formatted = formatter.format(amount);
        price.setText(formatted);

    }


}
