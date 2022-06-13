package com.example.uniboo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment {
    EditText nameSurname, username, email, password, repeatPassword;
    CheckBox selectingPolicy;
    Button signUpButton;
    TextView havingAccountLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public SignUpFragment() {
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_sign_up, container, false);
        nameSurname = fragmentLayout.findViewById(R.id.registerNameSurnameID);
        username = fragmentLayout.findViewById(R.id.registerUsernameID);
        email = fragmentLayout.findViewById(R.id.registerEmailID);
        password = fragmentLayout.findViewById(R.id.registerPasswordID);
        repeatPassword = fragmentLayout.findViewById(R.id.registerRepeatPasswordID);
        signUpButton = fragmentLayout.findViewById(R.id.signUpButtonID);


        selectingPolicy = fragmentLayout.findViewById(R.id.policyID);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlTextViews();
            }
        });

        return fragmentLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void controlTextViews(){
        if(!TextUtils.isEmpty(nameSurname.getText()) &&  !TextUtils.isEmpty(username.getText()) && !TextUtils.isEmpty(email.getText())
                && !TextUtils.isEmpty(password.getText()) && !TextUtils.isEmpty(repeatPassword.getText()) && selectingPolicy.isChecked()){
            String pass1 = password.getText().toString();
            String pass2 = repeatPassword.getText().toString();
            if(!pass1.equals(pass2)){
                Toast.makeText(getContext(),getResources().getText(R.string.passwords) , Toast.LENGTH_SHORT).show();
            }
            else{

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Map<Object, String> userData= new HashMap<>();
                            userData.put("NameSurname",nameSurname.getText().toString());
                            userData.put("Username",username.getText().toString());
                            userData.put("Email",email.getText().toString());

                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), getResources().getText(R.string.success), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    else{
                                        String errorMessage =task.getException().getMessage();
                                        Toast.makeText(getContext(),errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            String errorMessage =task.getException().getMessage();
                            Toast.makeText(getContext(),errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
        else{
            Toast.makeText(getContext(), getResources().getText(R.string.empty), Toast.LENGTH_SHORT).show();
        }
    }
}