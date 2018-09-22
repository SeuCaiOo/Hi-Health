package br.com.thecharles.hihealth.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import br.com.thecharles.hihealth.activity.MapsActivity;
import br.com.thecharles.hihealth.R;
import br.com.thecharles.hihealth.activity.WalkMyAndroidActivity;

public class ChildDFragment extends Fragment {

    private Button btnMap, btnGitbook;
    private ImageView address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_child_d, container, false);

//        btnMap = view.findViewById(R.id.btnMapa);
//        btnMap.setOnClickListener(openMap());

//        btnGitbook = view.findViewById(R.id.btnGitbook);
//        btnGitbook.setOnClickListener(openWalkMyAndroid());

        address = view.findViewById(R.id.ivAddress);
        address.setOnClickListener(openMap());

        return view;
    }

    private View.OnClickListener openWalkMyAndroid() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WalkMyAndroidActivity.class);
                startActivity(intent);
            }
        };
    }

        private View.OnClickListener openMap() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), MapsActivity.class);
                startActivity(it);
            }
        };
    }
//
//    public void openMap(View view) {
//        Intent it = new Intent(getActivity(), MapsActivity.class);
//        startActivity(it);
//    }

}
