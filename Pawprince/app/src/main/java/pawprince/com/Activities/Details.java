package pawprince.com.Activities;


import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import pawprince.com.R;

public class Details extends AppCompatActivity {

    @Override
    public void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.imagedetail);

        Integer IMAGE = getIntent().getIntExtra("image", 1/*Default Value of Int*/);

        ImageView img = (ImageView)findViewById(R.id.images);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageResource(IMAGE);
    }
}
