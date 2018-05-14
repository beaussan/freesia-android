package com.example.jeromelaquay.freesia.fragmentmenu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.jeromelaquay.freesia.R;
import com.example.jeromelaquay.freesia.UpdateemailMutation;
import com.example.jeromelaquay.freesia.UpdatepasswordMutation;
import com.example.jeromelaquay.freesia.UpdateuserinfoMutation;
import com.example.jeromelaquay.freesia.apollo.MyApolloClient;

import javax.annotation.Nonnull;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JEROMELaquay on 17/03/2018.
 */

public class ProfilFragment extends Fragment {

    private EditText oldPassword;
    private EditText newPassword;
    private Button validerPassword;

    private EditText passwordActuel;
    private EditText newMail;
    private Button validerMail;

    private EditText firstname;
    private EditText lastname;
    private Button validerInfo;

    private SharedPreferences sharedPref;
    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, null);
        oldPassword = view.findViewById(R.id.oldpasswordTxt);
        newPassword = view.findViewById(R.id.newpasswordTxt);
        validerPassword = view.findViewById(R.id.validerNewPasswordBtn);

        passwordActuel = view.findViewById(R.id.passwordModifMail);
        newMail = view.findViewById(R.id.mailModifMail);
        validerMail = view.findViewById(R.id.validerNewEmailBtn);

        firstname = view.findViewById(R.id.firstnameTxt);
        lastname = view.findViewById(R.id.lastnameTxt);
        validerInfo = view.findViewById(R.id.validerInfoBtn);

        sharedPref = this.getActivity().getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        token=sharedPref.getString("token","default");

        validerPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        validerMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });

        validerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserInfo();
            }
        });

        return view;
    }

    void changePassword(){
        MyApolloClient.getMyApolloClient(token).mutate(UpdatepasswordMutation.builder().oldPassword(oldPassword.getText().toString()).newPassword(newPassword.getText().toString())
                .build()).enqueue(new ApolloCall.Callback<UpdatepasswordMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<UpdatepasswordMutation.Data> response){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "mot de passe modifié", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "erreur lors du changement", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void changeEmail(){
        MyApolloClient.getMyApolloClient(token).mutate(UpdateemailMutation.builder().password(passwordActuel.getText().toString()).email(newMail.getText().toString())
                .build()).enqueue(new ApolloCall.Callback<UpdateemailMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<UpdateemailMutation.Data> response){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "email modifié", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "erreur lors du changement", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void changeUserInfo(){
        MyApolloClient.getMyApolloClient(token).mutate(UpdateuserinfoMutation.builder().firstname(firstname.getText().toString()).lastname(lastname.getText().toString())
                .build()).enqueue(new ApolloCall.Callback<UpdateuserinfoMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<UpdateuserinfoMutation.Data> response){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "infos modifiés", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "erreur lors du changement", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
