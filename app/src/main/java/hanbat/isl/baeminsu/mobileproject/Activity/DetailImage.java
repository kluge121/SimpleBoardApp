package hanbat.isl.baeminsu.mobileproject.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import hanbat.isl.baeminsu.mobileproject.R;

public class DetailImage extends AppCompatActivity {

    String imagePath;
    Bitmap bitmap;
    ImageView imageView;
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_image);

        PhotoView photoView = (PhotoView) findViewById(R.id.photoview);


//        imageView = findViewById(R.id.image);
//        photoViewAttacher = new PhotoViewAttacher(imageView);
//        photoViewAttacher.setScaleType(ScaleType.FIT_XY);
        imagePath = getIntent().getStringExtra("url");

        Thread thread = new Thread(new GetIamgeRunnable());

        thread.start();
        try {
            thread.join();
            photoView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    class GetIamgeRunnable implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL(imagePath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
