package com.example.jeromelaquay.freesia.fragmentmenu;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.jeromelaquay.freesia.Photo;
import com.example.jeromelaquay.freesia.R;
import com.example.jeromelaquay.freesia.adapter.OnRecyclerItemClickListener;
import com.example.jeromelaquay.freesia.adapter.PhotoAdapter;
import com.example.jeromelaquay.freesia.adapter.TodolistAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class PhotoFragment extends Fragment implements OnRecyclerItemClickListener {

    private List<Photo> photos=new ArrayList<>();
    private PhotoAdapter mAdapter;
    private RecyclerView recyclerView;
    private OnRecyclerItemClickListener recyclerItemClickListener ;
    private FloatingActionButton addPhotoBtn;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    //elements du create dialog
    ImageView imageEdit;
    Button prendrePhoto;
    EditText messageEdit;
    Button createPhotoBtn;

    Bitmap image;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, null);
        ButterKnife.bind(this, view);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        addPhotoBtn = (FloatingActionButton) view.findViewById(R.id.addPhotoBtn);
        mySwipeRefreshLayout =(SwipeRefreshLayout) view.findViewById(R.id.swiperefreshtodolist);
        recyclerItemClickListener = this;
        recupererPhotos();
        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPhoto();
            }
        });

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("TodolistFragment", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        return view;
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }

    @Override
    public void onLongClick(View view, int position, boolean isLongClick) {

    }

    private void createPhoto(){
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_create_photo, null);
        imageEdit = (ImageView) mView.findViewById(R.id.imageEdit);
        prendrePhoto = (Button) mView.findViewById(R.id.prendrePhoto);
        messageEdit = (EditText) mView.findViewById(R.id.textPhoto);
        createPhotoBtn = (Button) mView.findViewById(R.id.createPhotoBtn);
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        prendrePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CapturePhoto();
            }
        });

        createPhotoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Photo photo = new Photo();
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String temp=Base64.encodeToString(b, Base64.DEFAULT);
                photo.setImage(temp);
                photo.setMessage(messageEdit.getText().toString());
                photos.add(photo);
                recupererPhotos();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void CapturePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            imageEdit.setImageBitmap(image);
        }
    }

    private void recupererPhotos(){
        recyclerView.setHasFixedSize(false);
        mAdapter = new PhotoAdapter(photos, recyclerItemClickListener);
        recyclerView.setAdapter(mAdapter);
    }
}
