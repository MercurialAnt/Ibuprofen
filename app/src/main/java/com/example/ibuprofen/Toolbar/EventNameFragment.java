package com.example.ibuprofen.Toolbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ibuprofen.R;

public class EventNameFragment extends Fragment {

    private Button btnNext;
    private EditText etEventName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_name_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnNext = view.findViewById(R.id.btnNext);
        etEventName = view.findViewById(R.id.etEventName);

        final String[] name = {""};
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = etEventName.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("eventName", name[0]);
                bundle.putString("fragment", "normal");

                FragmentManager fragmentManager = getFragmentManager();

                Fragment nextFragment = new EventFragment();
                nextFragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.flSignupContainer, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

    }
}
