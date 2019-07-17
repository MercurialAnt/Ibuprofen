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

public class SetNameFragment extends Fragment {


    private Button next_btn;
    private EditText username_et;
    private EditText password_et_1;
    private EditText password_et_2;


    // inflate the layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setname_fragment, container, false);
    }

    // instantiate views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        username_et = view.findViewById(R.id.username_et);
        password_et_1 = view.findViewById(R.id.password_et_1);
        password_et_2 = view.findViewById(R.id.password_et_2);
        next_btn = view.findViewById(R.id.next_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = username_et.getText().toString();
                String password_1 = password_et_1.getText().toString();
                String password_2 = password_et_2.getText().toString();

                if (samePassword(password_1, password_2) && checkUniqueUser(username)) {
                    Fragment nextFragment = new SetContactFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    Bundle bundle = getArguments();
                    bundle.putString("username", username.toLowerCase());
                    bundle.putString("password", password_1);

                    nextFragment.setArguments(bundle);
                    transaction.replace(R.id.flSignupContainer, nextFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();
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
