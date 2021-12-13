package id.ac.umn.covitorwatchembedded;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity {
    Button tosearchbluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome_screen);
        tosearchbluetooth = findViewById(R.id.tosearchbluetooth);
        tosearchbluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivity = new Intent(WelcomeScreen.this, MainActivity.class);
                startActivity(MainActivity);
            }
        });
    }
}