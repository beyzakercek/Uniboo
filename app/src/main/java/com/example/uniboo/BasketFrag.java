package com.example.uniboo;

import static android.content.Context.NOTIFICATION_SERVICE;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.uniboo.BoundService.BoundBinder;


public class BasketFrag extends Fragment {
    RecyclerView rView;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    List<Books> basketViewHolderList;
    basketAdapter basketAdapter;
    ViewPager vp;
    TextView basket_price;
    Button buyButton;
    String message, message2;
    static final String channelID = "channel";
    public static final String TAG = "Tag";
    float basketPrice = 0;
    private NotificationManagerCompat notificationManager;
    BoundService boundService = new BoundService();
    boolean isConnected = false;

    public BasketFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket2, container, false);
        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        rView = view.findViewById(R.id.basketRecyclerView);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));
        basketViewHolderList = new ArrayList<>();
        basketAdapter = new basketAdapter(getActivity(), basketViewHolderList);
        basket_price = view.findViewById(R.id.myBasketPriceID);
        rView.setAdapter(basketAdapter);

        notificationManager = NotificationManagerCompat.from(getActivity());

        vp = getActivity().findViewById(R.id.viewPagerID);
        vp.setVisibility(View.GONE);
        buyButton = view.findViewById(R.id.buyButtonID);

        message = (String)getResources().getText(R.string.message);
        message2 = (String)getResources().getText(R.string.messageTwo);

        firestore.collection("BASKET").document(fAuth.getCurrentUser().getUid()).collection("CURRENT_USER_BASKET")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        Books books = documentSnapshot.toObject(Books.class);
                        basketViewHolderList.add(books);
                        basketAdapter.notifyDataSetChanged();

                    }
                    calculatePrice(basketViewHolderList);
                }
            }
        });
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification();
                Toast.makeText(getActivity(),getResources().getText(R.string.time)+boundService.getOrderTime() , Toast.LENGTH_LONG).show();

            }
        });

        Intent i = new Intent(getActivity(), BoundService.class);
        getActivity().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        return view;
    }


    private void showNotification() {
        createNotificaton();
        NotificationCompat.Builder build = new NotificationCompat.Builder(getActivity(),channelID);
        build.setSmallIcon(R.drawable.ic_baseline_shopping_cart_24);
        build.setContentTitle(message);
        build.setContentText(message2);
        build.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat compat = NotificationManagerCompat.from(getActivity());
        compat.notify(1, build.build());
    }

    private void createNotificaton() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelID, "Notification", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    public class basketAdapter extends RecyclerView.Adapter<basketAdapter.ViewHolder> {
        List<Books> basketViewHolderList;
        Context context;

        public basketAdapter(Context context,List<Books> basketViewHolderList) {
            this.context = context;
            this.basketViewHolderList = basketViewHolderList;
        }

        public int getItemCount() {
            return basketViewHolderList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView book_image;
            TextView book_name, book_author, book_price;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                book_image= itemView.findViewById(R.id.basketImageID);;
                book_name = itemView.findViewById(R.id.basketBookNameID);
                book_author =  itemView.findViewById(R.id.basketAuthorID);
                book_price =  itemView.findViewById(R.id.basketPriceID);
            }
        }


        @NonNull
        @Override
        public basketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_basket, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull basketAdapter.ViewHolder holder, int position) {
            holder.book_name.setText(basketViewHolderList.get(position).getBookName());
            holder.book_author.setText(basketViewHolderList.get(position).getBookAuthor());
            holder.book_price.setText(String.valueOf(basketViewHolderList.get(position).getBookPrice()));
            Glide.with(holder.book_image.getContext()).load(basketViewHolderList.get(position).getBookImage()).into(holder.book_image);
        }
    }

    private void calculatePrice(List <Books> basketViewHolderList){
        float totalPrice = 0;

        for(Books books : basketViewHolderList){
            totalPrice += books.getBookPrice();
        }
        basket_price.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onStop(){
        super.onStop();
        vp.setVisibility(View.VISIBLE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundBinder boundBinder = (BoundBinder) iBinder;
            boundService = boundBinder.getBoundService();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnected = false;
        }
    };

}