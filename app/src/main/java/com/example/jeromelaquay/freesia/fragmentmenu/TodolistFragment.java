package com.example.jeromelaquay.freesia.fragmentmenu;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.jeromelaquay.freesia.AllTodoListQuery;
import com.example.jeromelaquay.freesia.CreatetodolistMutation;
import com.example.jeromelaquay.freesia.EditTodoListNameMutation;
import com.example.jeromelaquay.freesia.R;
import com.example.jeromelaquay.freesia.TodoitemActivity;
import com.example.jeromelaquay.freesia.TodolistbyidQuery;
import com.example.jeromelaquay.freesia.adapter.OnRecyclerItemClickListener;
import com.example.jeromelaquay.freesia.adapter.TodoitemAdapter;
import com.example.jeromelaquay.freesia.adapter.TodolistAdapter;
import com.example.jeromelaquay.freesia.apollo.MyApolloClient;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JEROMELaquay on 17/03/2018.
 */

public class TodolistFragment extends Fragment implements OnRecyclerItemClickListener {

    private SharedPreferences sharedPref;
    private String token;
    //elements nécessaires pour afficher la liste
    private TodolistAdapter mAdapter;
    private RecyclerView recyclerView;
    private OnRecyclerItemClickListener recyclerItemClickListener ;
    private FloatingActionButton addTodolistBtn;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    //elements du create dialog
    EditText nameTxt;
    Button createBtn;

    //elements du modify dialog
    EditText nomEdit;
    Button modifyBtn;
    Button cancelBtn;
    Button deleteBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        addTodolistBtn = (FloatingActionButton) view.findViewById(R.id.addTodolistBtn);
        mySwipeRefreshLayout =(SwipeRefreshLayout) view.findViewById(R.id.swiperefreshtodolist);
        sharedPref = this.getActivity().getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        token=sharedPref.getString("token","default");
        recyclerItemClickListener = this;
        recupererSesTodolists();

        addTodolistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creation de l'alert dialog pour donner un nom a la nouvelle todolist
                createDialogTodolist();
            }
        });

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("TodolistFragment", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        recupererSesTodolists();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        return view;
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        sharedPref = this.getActivity().getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("idTodolist", ""+mAdapter.getTodolist(position));
        editor.commit();
        Intent intent = new Intent(getActivity(), TodoitemActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position, boolean isLongClick) {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_modify_todolist, null);
        nomEdit = (EditText) mView.findViewById(R.id.nomEdit);
        modifyBtn = (Button) mView.findViewById(R.id.modifyBtn);
        cancelBtn = (Button) mView.findViewById(R.id.CancelBtn);
        deleteBtn = (Button) mView.findViewById(R.id.deleteBtn);

        recupererTodolistChoisi(position);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        modifyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                modifierTodolistNom(position);
                dialog.cancel();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void createDialogTodolist(){
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_create_todolist, null);
        nameTxt = (EditText) mView.findViewById(R.id.nameEdit);
        createBtn = (Button) mView.findViewById(R.id.createBtn);
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        createBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                creerTodolist();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void recupererSesTodolists(){
        MyApolloClient.getMyApolloClient(token).query(AllTodoListQuery.builder()
                .build()).enqueue(new ApolloCall.Callback<AllTodoListQuery.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<AllTodoListQuery.Data> response){
                Log.d("TodolistFragment", "entree dans la query");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.data() != null) {
                            Log.d("TodolistFragment", "entree dans le run");
                            recyclerView.setHasFixedSize(false);
                            mAdapter = new TodolistAdapter(response.data().allTodoList(), recyclerItemClickListener);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TodolistFragment", e.toString());
                    }
                });
            }
        });
    }

    private void recupererTodolistChoisi(int idTodolist){
        MyApolloClient.getMyApolloClient(token).query(TodolistbyidQuery.builder().id(mAdapter.getTodolist(idTodolist))
                .build()).enqueue(new ApolloCall.Callback<TodolistbyidQuery.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<TodolistbyidQuery.Data> response){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.data() != null) {
                            nomEdit.setText(response.data().todoListById().title());
                        }
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TodoitemFragment", e.toString());
                    }
                });
            }
        });
    }

    private void creerTodolist(){
        MyApolloClient.getMyApolloClient(token).mutate(CreatetodolistMutation.builder().name(nameTxt.getText().toString())
                .build()).enqueue(new ApolloCall.Callback<CreatetodolistMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<CreatetodolistMutation.Data> response){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "creation effectue", Toast.LENGTH_SHORT).show();
                        recupererSesTodolists();
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "erreur de creation", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void modifierTodolistNom(int position){
        MyApolloClient.getMyApolloClient(token).mutate(EditTodoListNameMutation.builder().text(nomEdit.getText().toString()).itemId(mAdapter.getTodolist(position))
                .build()).enqueue(new ApolloCall.Callback<EditTodoListNameMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<EditTodoListNameMutation.Data> response){
                Log.d("TodolistFragment", "entree dans la query");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recupererSesTodolists();
                        Toast.makeText(getActivity(), "modification effectue", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "erreur de modification", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
