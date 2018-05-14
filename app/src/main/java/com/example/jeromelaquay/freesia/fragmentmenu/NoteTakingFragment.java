package com.example.jeromelaquay.freesia.fragmentmenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeromelaquay.freesia.R;

/**
 * Created by JEROMELaquay on 17/03/2018.
 */

public class NoteTakingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_taking, null);
    }
}
