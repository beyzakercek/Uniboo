package com.example.uniboo;


import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.io.Resources;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;


public class MyAccountFragment extends Fragment {
    TextView nameSurname, userName,eMail;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    String userID, name_surname, username, email;
    ViewPager vp;
    DocumentReference reference;
    Button goToUpdate;
    String updateNameSurname, updateUsername;
    public static final String TAG = "Tag";

    public MyAccountFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        vp = getActivity().findViewById(R.id.viewPagerID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        nameSurname = view.findViewById(R.id.accountNameSurnameID);
        userName = view.findViewById(R.id.accountUserNameID);
        eMail = view.findViewById(R.id.accountEmailID);

        goToUpdate = view.findViewById(R.id.editID);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("USERS").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    name_surname = task.getResult().getString("NameSurname");
                    username = task.getResult().getString("Username");
                    email = task.getResult().getString("Email");

                    nameSurname.setText(name_surname);
                    userName.setText(username);
                    eMail.setText(email);

                }
            }
        });

        goToUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        return view;
    }
    private void openDialog(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.alert_dialog, null);
        final EditText subEditText = (EditText)subView.findViewById(R.id.dialogEditText1);
        final EditText subEditText2 = (EditText)subView.findViewById(R.id.dialogEditText2);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getText(R.string.updateInfo));
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                updateNameSurname = subEditText.getText().toString();
                updateUsername =    subEditText2.getText().toString();

                final DocumentReference sfDocRef = db.collection("USERS").document(fAuth.getUid());

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);


                        transaction.update(sfDocRef, "NameSurname", updateNameSurname);
                        transaction.update(sfDocRef, "Username", updateUsername);

                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                nameSurname.setText(subEditText.getText().toString());
                userName.setText(subEditText2.getText().toString());

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    @Override
    public void onStop(){
        super.onStop();
        vp.setVisibility(View.VISIBLE);
    }

}