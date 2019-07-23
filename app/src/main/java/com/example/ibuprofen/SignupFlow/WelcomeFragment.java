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

    private EditText etName;
    private Button btnNext;

    // inflate the layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_fragment, container, false);
    }

    // instantiate views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        etName = view.findViewById(R.id.etName);
        btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().length() > 0) {
                    Fragment nextFragment = new SetNameFragment();
                    FragmentTransaction  transaction = getFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("name", etName.getText().toString());
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
