package com.example.ibuprofen.SignupFlow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ibuprofen.R;

public class WelcomeFragment extends Fragment {

    private EditText name_et;
    private Button next_btn;

    // inflate the layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_fragment, container, false);
    }

    // instantiate views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        name_et = view.findViewById(R.id.name_et);
        next_btn = view.findViewById(R.id.next_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_et.getText().toString().length() > 0) {
                    Fragment nextFragment = new SetNameFragment();
                    FragmentTransaction  transaction = getFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("name", name_et.getText().toString());
                    nextFragment.setArguments(bundle);
                    transaction.replace(R.id.flSignupContainer, nextFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "Enter your name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
