package com.jinglan.lilo.broadcastbestpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity
{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;


    private TextView qq_login;
    private Tencent mTencent;
    private static final String APPID="1106542895";
    private QQLoginListener mListener;
    private UserInfo mInfo;

    private  String nickName,figureurl;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        login = (Button) findViewById(R.id.login);
        qq_login = (TextView) findViewById(R.id.qq_login);
        mTencent = Tencent.createInstance(APPID,this);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember)
        {
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(account.equals("admin") && password.equals("123456"))
                {
                   editor=pref.edit();
                   if(rememberPass.isChecked())
                   {
                       editor.putBoolean("remember_password",true);
                       editor.putString("account",account);
                       editor.putString("password",password);
                   }
                   else
                   {
                       editor.clear();
                   }
                   editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast toast=Toast.makeText(LoginActivity.this,"account or password is invalid",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,300);
                    toast.show();
                }
            }
        });

        qq_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                QQLoigin();

            }
        });
    }

    private void QQLoigin()
    {
        if(!mTencent.isSessionValid())
        {
            mTencent.login(this,"all",mListener);
        }
    }

    public class QQLoginListener implements IUiListener
    {
        @Override
        public void onComplete(Object object)
        {
            initOpenIdAndToken(object);
            getUserInfo();
        }

        @Override
        public void onError(UiError e)
        {
            Toast.makeText(LoginActivity.this,"onError:"+", code"+ e.errorCode+", msg"+e.errorMessage
                    +", detail:"+e.errorDetail,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel()
        {
            Log.d("LoginActivity","取消");
        }

        private void initOpenIdAndToken(Object object)
        {
            JSONObject jb = (JSONObject) object;
            try
            {
                String openID = jb.getString("openid");
                String access_token = jb.getString("access_token");
                String expires = jb.getString("expires_in");

                mTencent.setOpenId(openID);
                mTencent.setAccessToken(access_token,expires);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        private void getUserInfo()
        {
            QQToken token = mTencent.getQQToken();
            mInfo = new UserInfo(getApplicationContext(),token);
            mInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject jb = (JSONObject) o;
                    try
                    {
                        nickName=jb.getString("nickname");
                        figureurl=jb.getString("figureurl_qq_2");
                        //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                       // intent.putExtra("user_name",nickName);
                        //intent.putExtra("user_image",figureurl);
                        //startActivity(intent);
                        accountEdit.setText(nickName);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(UiError uiError) {
                    Log.d("LoginActivity","登录失败"+uiError.toString());

                }

                @Override
                public void onCancel() {
                    Log.d("LoginActivity","登录取消");

                }
            });



        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mTencent.onActivityResultData(requestCode, resultCode, data,mListener);
        super.onActivityResult(requestCode,resultCode,data);
    }
}