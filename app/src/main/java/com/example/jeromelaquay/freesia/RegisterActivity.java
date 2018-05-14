package com.example.jeromelaquay.freesia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.jeromelaquay.freesia.apollo.MyApolloClient;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editFirstname)
     EditText editFirstname;

    @BindView(R.id.editLastname)
     EditText editLastname;

    @BindView(R.id.editEmail)
     EditText editEmail;

    @BindView(R.id.editPassword)
     EditText editPassword;

    @BindView(R.id.inscBtn)
     Button inscBtn;

    @BindView(R.id.connectBtn)
     Button connectbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);


        inscBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createUser();
            }
        });

        connectbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createUser(){

        MyApolloClient.getMyApolloClient().mutate(RegisterMutation.builder().firstname(editFirstname.getText().toString()).lastname(editLastname.getText().toString()).email(editEmail.getText().toString()).password(editPassword.getText().toString())
                .build()).enqueue(new ApolloCall.Callback<RegisterMutation.Data>(){
            @Override
            public void onResponse(@Nonnull Response<RegisterMutation.Data> response){
                Log.d("GrapQL-requete","onRepsonse "+ response.data().register().id);
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Votre compte a été ajouté", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                Log.d("GrapQL-requete","erreur lors de l'enregistrement ");
                Toast.makeText(RegisterActivity.this, "Une erreur est survenue pendant l'enregistrement", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
