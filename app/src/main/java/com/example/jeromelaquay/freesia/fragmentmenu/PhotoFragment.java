package com.example.jeromelaquay.freesia.fragmentmenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jeromelaquay.freesia.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JEROMELaquay on 17/03/2018.
 */

public class PhotoFragment extends Fragment {

    @BindView(R.id.goToMap)
    Button mapBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, null);
        ButterKnife.bind(this, view);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                Uri uri = Uri.parse("geo:0.0?q=universite+catholique+lille");
                intent.setData(uri);
                startActivity(intent);

            }
        });
        return view;
    }
}
