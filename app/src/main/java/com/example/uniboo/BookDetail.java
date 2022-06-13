package com.example.uniboo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class BookDetail extends Fragment {
    String bookName, bookAuthor, usedInUniversity, usingStatus, bookImage;
    float bookPrice;
    String message;
    FloatingActionButton shareButton;

    public BookDetail(String bookName, String bookAuthor, String usedInUniversity, String usingStatus, String bookImage, float bookPrice) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.usedInUniversity = usedInUniversity;
        this.usingStatus = usingStatus;
        this.bookImage = bookImage;
        this.bookPrice = bookPrice;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        super.onCreate(savedInstanceState);
        TextView nameHolder,authorHolder,universityHolder,usabilityHolder,priceHolder;
        shareButton = view.findViewById(R.id.shareButtonID);
        Toolbar toolbar =view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ImageView imgHolder = view.findViewById(R.id.bookDetailImageID);
        nameHolder = view.findViewById(R.id.nameOfBookDataID);
        authorHolder = view.findViewById(R.id.authorOfBookDataID);
        universityHolder = view.findViewById(R.id.universityDataID);
        usabilityHolder = view.findViewById(R.id.bookUsabilityDataID);
        priceHolder = view.findViewById(R.id.priceOfBookDataID);

        nameHolder.setText(bookName);
        authorHolder.setText(bookAuthor);
        universityHolder.setText(usedInUniversity);
        usabilityHolder.setText(usingStatus);
        priceHolder.setText(String.valueOf(bookPrice)+"TL");
        Glide.with(getContext()).load(bookImage).into(imgHolder);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = getResources().getText(R.string.bookName)+ nameHolder.getText().toString()+"\n"+getResources().getText(R.string.bookAuthor)+ authorHolder.getText().toString() +"\n"+getResources().getText(R.string.bookUniversity) +universityHolder.getText().toString()+"\n"+getResources().getText(R.string.bookUsability) +usabilityHolder.getText().toString()+"\n"+getResources().getText(R.string.bookPrice) +priceHolder.getText().toString();

                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, message);
                i.setType("text/plain");
                startActivity(i);
            }
        });
        return view;
    }
}