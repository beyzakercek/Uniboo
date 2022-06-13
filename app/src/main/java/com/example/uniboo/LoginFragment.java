package com.example.uniboo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment implements View.OnClickListener {
    TextView goToRegister;
    Button loginButton;
    EditText email, password;
    private FirebaseAuth firebaseAuth;
    TextView enText, trText;
    boolean lang_selected = true;

    public LoginFragment() {
    }


    public static LoginFragment newInstance(String param1, String param2) {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_login, container, false);

        email = fragmentLayout.findViewById(R.id.emailID);
        password = fragmentLayout.findViewById(R.id.passwordID);

        goToRegister = fragmentLayout.findViewById(R.id.goToRegisterID);
        goToRegister.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        goToRegister.setOnClickListener(this);
        trText = fragmentLayout.findViewById(R.id.langSelectTR);
        enText = fragmentLayout.findViewById(R.id.langSelectEN);


        loginButton = fragmentLayout.findViewById(R.id.loginButtonID);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlEmailPassword();
            }
        });
        LanguageSelection languageSelection = new LanguageSelection(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        enText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageSelection.updateResource("en");
                getActivity().recreate();

            }
        });
        trText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageSelection.updateResource("tr");
                getActivity().recreate();

            }
        });


        return fragmentLayout;
    }
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.goToRegisterID){
            Fragment childFragment = new SignUpFragment();
            Membership.fragmentManager.beginTransaction().replace(R.id.parentFragmentID, childFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void controlEmailPassword(){
        if(!TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(password.getText())) {
            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(getActivity(),getResources().getText(R.string.loginError), Toast.LENGTH_SHORT).show();
        }
    }
}