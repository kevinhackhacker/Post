package pawprince.com.Profile;

import androidx.appcompat.app.AppCompatActivity;
import pawprince.com.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterSelection extends AppCompatActivity {

    Button user1,user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_selection);

        user1 = findViewById(R.id.bUser1);
        user2 = findViewById(R.id.bUser2);


        user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , UserRegister.class));
            }
        });
        user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , AdminRegister.class));
            }
        });
    }
}
