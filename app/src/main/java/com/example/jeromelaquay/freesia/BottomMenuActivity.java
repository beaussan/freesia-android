package com.example.jeromelaquay.freesia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.jeromelaquay.freesia.fragmentmenu.PhotoFragment;
import com.example.jeromelaquay.freesia.fragmentmenu.ProfilFragment;
import com.example.jeromelaquay.freesia.fragmentmenu.NoteTakingFragment;
import com.example.jeromelaquay.freesia.fragmentmenu.TodolistFragment;

public class BottomMenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_menu);

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new ProfilFragment());
    }

    public boolean loadFragment(Fragment fragment){
        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Fragment fragActual = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        switch (item.getItemId()){
            case R.id.navigation_profil:
                if(!(fragActual instanceof ProfilFragment)) {
                    fragment = new ProfilFragment();
                }
                break;
            case R.id.navigation_todolist:
                if(!(fragActual instanceof TodolistFragment)){
                    fragment = new TodolistFragment();
                }
                break;
            case R.id.navigation_note_taking:
                if(!(fragActual instanceof NoteTakingFragment)) {
                    fragment = new NoteTakingFragment();
                }
                break;
            case R.id.navigation_photo:
                if(!(fragActual instanceof PhotoFragment)) {
                    fragment = new PhotoFragment();
                }
                break;
            case R.id.disconnect_menu:
                Intent intent = new Intent(BottomMenuActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.disconnect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.disconnect_menu:
                Intent intent = new Intent(BottomMenuActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
