package com.example.harsh183.eatwell;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


//best project ever


import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    Button browseFilesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browseFilesButton = findViewById(R.id.browse_button);
        browseFilesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivity(intent);
            }
        });

    }
    public void successMessage (String successStatus) {
        Toast toast = Toast.makeText(getApplicationContext(), successStatus, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.START, 400, 1000);
        toast.show();
    }

}