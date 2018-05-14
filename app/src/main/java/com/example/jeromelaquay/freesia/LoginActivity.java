package com.example.jeromelaquay.freesia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.jeromelaquay.freesia.apollo.MyApolloClient;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.emailTxt) EditText editMail;
    @BindView(R.id.passwordTxt) EditText editPassword;
    @BindView(R.id.connexionBtn) Button connexionBtn;
    @BindView(R.id.inscriptionBtn) Button inscriptionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        connexionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        inscriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void connect(){
        MyApolloClient.getMyApolloClient().mutate(ConnectMutation.builder().email(editMail.getText().toString()).password(editPassword.getText().toString())
                .build()).enqueue(new ApolloCall.Callback<ConnectMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<ConnectMutation.Data> response){
                if(response.data().getToken() != null){
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPref = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token", response.data().getToken().access_token.toString());
                            editor.commit();

                            Toast.makeText(LoginActivity.this, "Connexion autorisé", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, BottomMenuActivity.class);
                            startActivity(intent);
                        }
                    });
                }else{
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                Log.d("GrapQL-requete","erreur de connexion ");
                Toast.makeText(LoginActivity.this, "erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
