package com.sr.masharef.masharef.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sr.masharef.masharef.R;

/**
 * Created by systemrapid on 13/01/17.
 */

public class SettingFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View dashboard = inflater.inflate(R.layout.fragment_setting, container, false);

        return dashboard;
    }
}
