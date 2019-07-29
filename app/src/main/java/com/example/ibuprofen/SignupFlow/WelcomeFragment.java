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
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class WelcomeFragment extends Fragment {

    private EditText etName;
    private Button btnNext;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword1;
    private EditText etPassword2;


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
        etEmail = view.findViewById(R.id.etEmail);
        btnNext = view.findViewById(R.id.btnNext);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword1 = view.findViewById(R.id.etPassword1);
        etPassword2 = view.findViewById(R.id.etPassword2);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gets username and password entered
                String username = etUsername.getText().toString();
                String password_1 = etPassword1.getText().toString();
                String password_2 = etPassword2.getText().toString();
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();

                // booleans to make sure information is filled in
                boolean hasName = (name.length() > 0);
                boolean hasEmail = (email.length() > 0);

                // sends fragment profile picture set-up
                if (hasName && hasEmail) {
                    if (samePassword(password_1, password_2) && checkUniqueUser(username)) {
                        Fragment nextFragment = new SetProfilePicFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        Bundle bundle = new Bundle();
                        bundle.putString("username", username.toLowerCase());
                        bundle.putString("password", password_1);
                        bundle.putString("email", email);
                        bundle.putString("name", name);

                        nextFragment.setArguments(bundle);
                        transaction.replace(R.id.flSignupContainer, nextFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                    }
                } else {
                    Toast.makeText(getContext(), "Check entered information",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkUniqueUser(String user) {
        ParseQuery<ParseUser> p = new ParseQuery<ParseUser>(ParseUser.class);
        try {
            if (p.whereContains("username", user.toLowerCase()).count() > 0) {
                Toast.makeText(getContext(), "Username taken already", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean samePassword(String password1, String password2) {

        if (!password1.equals(password2)) {
            Toast.makeText(getContext(), "The passwords don't match", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password1.length() <= 0) {
            Toast.makeText(getContext(), "The password mustn't be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
