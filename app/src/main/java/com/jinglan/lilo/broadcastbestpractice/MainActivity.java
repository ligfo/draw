package com.jinglan.lilo.broadcastbestpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity {

    private ImageView u_image;
    private TextView u_name;
    private String figure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        u_image= (ImageView) findViewById(R.id.user_image);
        u_name = (TextView) findViewById(R.id.user_name);
        Intent intent = getIntent();
        u_name.setText(intent.getStringExtra("user_name"));
        figure= intent.getStringExtra("user_image");
        Picasso.with(getApplicationContext()).load(figure).into(u_image);


        Button forceOffline = (Button) findViewById(R.id.force_offline);
        forceOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.jinglan.lilo.broadcastbestpractice.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });
    }
}
