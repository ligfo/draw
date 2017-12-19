package com.jinglan.lilo.broadcastbestpractice;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lilo on 2017/12/17.
 */

public class BaseActivity extends AppCompatActivity
{

    private ForceOfflineReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.jinglan.lilo.broadcastbestpractice.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(receiver != null)
        {
            unregisterReceiver(receiver);
            receiver=null;
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }




    class ForceOfflineReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("警告");
            builder.setMessage("你被强迫下线");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(context                             ,LoginActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.show();
        }
    }
}
