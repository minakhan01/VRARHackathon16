package com.example.sneha.trails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SendDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        //Sneha, To get the selected track:
        //SavedPaths.getInstance().getSelectedPath();
    }

    public void toggleSendData(View view)
    {
        //Sneha Do some cool stuff here ;)

    }

}
