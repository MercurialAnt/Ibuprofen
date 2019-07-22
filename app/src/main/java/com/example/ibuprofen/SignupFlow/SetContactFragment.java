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

public class SetContactFragment extends Fragment {

    private Button btnNext;
    private EditText etEmail;


    // inflate the layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setcontact_fragment, container, false);
    }

    // instantiate views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        etEmail = view.findViewById(R.id.etEmail);
        btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etEmail.getText().toString().length() > 0) {
                    Fragment nextFragment = new SetProfilePicFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    Bundle bundle = getArguments();
                    bundle.putString("email", etEmail.getText().toString());

                    nextFragment.setArguments(bundle);
                    transaction.replace(R.id.flSignupContainer, nextFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "Enter your email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
