package com.example.jeromelaquay.freesia;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.jeromelaquay.freesia.adapter.OnRecyclerItemClickListener;
import com.example.jeromelaquay.freesia.adapter.TodoitemAdapter;
import com.example.jeromelaquay.freesia.adapter.TodolistAdapter;
import com.example.jeromelaquay.freesia.apollo.MyApolloClient;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import butterknife.ButterKnife;

public class TodoitemActivity extends AppCompatActivity implements OnRecyclerItemClickListener{

    private SharedPreferences sharedPreferences;
    private String token;
    List<TodolistbyidQuery.TodoItem> Todoitems=new ArrayList<>();
    private TodoitemAdapter mAdapter;
    RecyclerView recyclerItem;
    private OnRecyclerItemClickListener recyclerItemClickListener ;
    private FloatingActionButton addTodoitemBtn;
    private int idTodolist;

    private EditText messageTxt;
    private Button createItemBtn;
    private TextView titleTodolist;

    //elements du modify dialog
    private EditText messageEdit2;
    private Button modifyBtn;
    private Button cancelBtn;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoitem);
        recyclerItem = (RecyclerView) findViewById(R.id.recycler_item);
        addTodoitemBtn = (FloatingActionButton) findViewById(R.id.addTodoitemBtn);
        titleTodolist = (TextView) findViewById(R.id.titleTodolistItem);
        sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        token=sharedPreferences.getString("token","default");
        idTodolist= Integer.parseInt(sharedPreferences.getString("idTodolist","default"));
        recyclerItemClickListener = this;

        recupererLesitems();

        addTodoitemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(TodoitemActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_create_todoitem, null);
                messageTxt = (EditText) mView.findViewById(R.id.messageEdit);
                createItemBtn = (Button) mView.findViewById(R.id.createItemBtn);
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();

                createItemBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        creerTodoitem();
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_modify_todoitem, null);
        messageEdit2 = (EditText) mView.findViewById(R.id.messageEdit2);
        modifyBtn = (Button) mView.findViewById(R.id.modifyBtnItem);
        cancelBtn = (Button) mView.findViewById(R.id.CancelBtnItem);
        deleteBtn = (Button) mView.findViewById(R.id.deleteBtnItem);

        TodolistbyidQuery.TodoItem itemChoisi = mAdapter.getTodoitem(position);
        Log.d("ItemChoisi", itemChoisi.id+"  "+itemChoisi.text);
        messageEdit2.setText(itemChoisi.text);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        modifyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                modifierTodoitemNom(itemChoisi);
                dialog.cancel();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.cancel();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                supprimerTodoitem(itemChoisi);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onLongClick(View view, int position, boolean isLongClick) {
        onClick(view, position, isLongClick);
    }

    private void recupererLesitems(){
        MyApolloClient.getMyApolloClient(token).query(TodolistbyidQuery.builder().id(idTodolist)
                .build()).enqueue(new ApolloCall.Callback<TodolistbyidQuery.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<TodolistbyidQuery.Data> response){
                Log.d("TodoitemFragment", "entree dans la query");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.data() != null) {
                            titleTodolist.setText(response.data().todoListById().title);
                            recyclerItem.setHasFixedSize(false);
                            mAdapter = new TodoitemAdapter(response.data().todoListById().todoItems(), recyclerItemClickListener);
                            recyclerItem.setAdapter(mAdapter);
                        }
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("TodoitemFragment", e.toString());
                    }
                });
            }
        });
    }

    private void creerTodoitem(){
        MyApolloClient.getMyApolloClient(token).mutate(AddtodoitemMutation.builder().listId(idTodolist).message(messageTxt.getText().toString())
                .build()).enqueue(new ApolloCall.Callback<AddtodoitemMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<AddtodoitemMutation.Data> response){
                Log.d("TodoitemFragment", "entree dans la query");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TodoitemActivity.this, "creation effectue", Toast.LENGTH_SHORT).show();
                        recupererLesitems();
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TodoitemActivity.this, "erreur de creation", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void modifierTodoitemNom(TodolistbyidQuery.TodoItem item){
        MyApolloClient.getMyApolloClient(token).mutate(EditTodoitemTextMutation.builder().text(messageEdit2.getText().toString()).itemId(item.id)
                .build()).enqueue(new ApolloCall.Callback<EditTodoitemTextMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<EditTodoitemTextMutation.Data> response){
                Log.d("TodoitemFragment", "entree dans la query");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recupererLesitems();
                        Toast.makeText(TodoitemActivity.this, "modification effectue", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TodoitemActivity.this, "erreur de modification", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void supprimerTodoitem(TodolistbyidQuery.TodoItem item){
        MyApolloClient.getMyApolloClient(token).mutate(ArchiveTodoMutation.builder().itemId(item.id)
                .build()).enqueue(new ApolloCall.Callback<ArchiveTodoMutation.Data>(){
            @Override
            public void onResponse(@Nonnull final Response<ArchiveTodoMutation.Data> response){
                Log.d("TodoitemFragment", "entree dans la query");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recupererLesitems();
                        Toast.makeText(TodoitemActivity.this, "suppression effectue", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(@Nonnull ApolloException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TodoitemActivity.this, "erreur de suppression", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}
