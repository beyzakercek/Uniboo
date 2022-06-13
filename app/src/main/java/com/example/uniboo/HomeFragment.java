package com.example.uniboo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    public static final String TAG = "Tag";

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth fAuth;
    CardView cView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("probabilityStatistics.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "url:"+ uri.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        cView = view.findViewById(R.id.cardView);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        Query query = db.collection("BOOKS");
        FirestoreRecyclerOptions<Books> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Books>().setQuery(query,Books.class).build();

        adapter = new FirestoreRecyclerAdapter<Books, BooksViewHolder>(firestoreRecyclerOptions) {
            @NonNull
            @Override
            public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

                return new BooksViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull BooksViewHolder holder, int position, @NonNull Books model) {
                Glide.with(holder.imageV.getContext()).load(model.getBookImage()).into(holder.imageV);
                holder.name.setText(model.bookName);
                holder.author.setText(model.bookAuthor);
                holder.price.setText(model.bookPrice+"TL");

                holder.imageV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.drawerLayoutID, new BookDetail(model.bookName,model.bookAuthor,model.usedInUniversity,model.usingStatus,model.bookImage,model.bookPrice))
                                .addToBackStack(null).commit();

                    }
                });
                holder.basketImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final HashMap<String, Object> basket = new HashMap<>();
                        basket.put("bookName", model.bookName);
                        basket.put("bookAuthor", model.bookAuthor);
                        basket.put("bookPrice", model.bookPrice);
                        basket.put("bookImage", model.bookImage);

                        db.collection("BASKET").document(fAuth.getCurrentUser().getUid()).collection("CURRENT_USER_BASKET")
                        .add(basket).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(getActivity(),getResources().getText(R.string.addedBasket),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);

        return view;
    }

    private class BooksViewHolder extends RecyclerView.ViewHolder{
        TextView name, author, price;
        ImageView imageV, basketImage;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameID);
            author = itemView.findViewById(R.id.authorID);
            price = itemView.findViewById(R.id.priceID);
            imageV = itemView.findViewById(R.id.image);
            basketImage = itemView.findViewById(R.id.addToBasketID);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}