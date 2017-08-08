package com.example.administrator.thumbnaildemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv_click;
    private TextView tv_click1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_click= (TextView) findViewById(R.id.tv_click);
        tv_click1=(TextView)findViewById(R.id.tv_click1);
        tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ListThumbnailActivity.class);

                startActivity(intent);
            }
        });
        tv_click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TransfereeLocal_Activity.class);
                startActivity(intent);
            }
        });
    }
}
