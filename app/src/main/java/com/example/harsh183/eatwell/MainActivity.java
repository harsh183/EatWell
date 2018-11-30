package com.example.harsh183.eatwell;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//best project ever


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * to do: test getCalendar
     * @param name
     * @return
     */
    public String getCalendar(String name) {
        //when calling get calendar ask for file name
        try {
            return openFileInput(name).toString();
        } catch(Exception e) {
            System.out.println("File not found. Please retry.");
            return "";
        }
    }
}


