package com.example.glamfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;

public class Payment extends AppCompatActivity {

    private RadioGroup radioGroupPayment;
    private RadioButton radioCash, radioOnlinePayment;
    private ImageView imgReceipt;
    private Button btnUploadReceipt, btnBookMyOrder;
    private TextView tvTotalPrice;
    private Uri receiptUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initializeViews();
        setupListeners();
        displayTotalPrice(); // Fetch and display total price on activity start
    }

    private void initializeViews() {
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        radioCash = findViewById(R.id.radioCash);
        radioOnlinePayment = findViewById(R.id.radioOnlinePayment);
        imgReceipt = findViewById(R.id.imgReceipt);
        btnUploadReceipt = findViewById(R.id.btnUploadReceipt);
        btnBookMyOrder = findViewById(R.id.btnBookMyOrder);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
    }

    private void setupListeners() {
        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            boolean isOnline = checkedId == R.id.radioOnlinePayment;
            imgReceipt.setVisibility(isOnline ? View.VISIBLE : View.GONE);
            btnUploadReceipt.setVisibility(isOnline ? View.VISIBLE : View.GONE);
        });

        ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    imgReceipt.setImageURI(uri);
                    receiptUri = uri;
                });

        btnUploadReceipt.setOnClickListener(v -> getContent.launch("image/*"));

        btnBookMyOrder.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String userPhoneNumber = prefs.getString("number", "");
            bookOrder(userPhoneNumber);
        });
    }

    private void displayTotalPrice() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userPhoneNumber = prefs.getString("number", "");
        calculateTotalPrice(userPhoneNumber); // Separate method to fetch and display the total price
    }

    private void calculateTotalPrice(String phoneNumber) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(phoneNumber);
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalPrice = 0.0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null) {
                        try {
                            totalPrice += Double.parseDouble(item.getPrice());
                        } catch (NumberFormatException e) {
                            // Log or handle incorrect price format
                        }
                    }
                }
                tvTotalPrice.setText(String.format(Locale.US, "Total Price: $%.2f", totalPrice));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void bookOrder(String phoneNumber) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(phoneNumber);
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalPrice = 0.0;
                StringBuilder servicesBuilder = new StringBuilder();
                StringBuilder priceBuilder = new StringBuilder();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null) {
                        if (servicesBuilder.length() > 0) servicesBuilder.append(", ");
                        servicesBuilder.append(item.getName());

                        if (priceBuilder.length() > 0) priceBuilder.append(", ");
                        priceBuilder.append(item.getPrice());

                        try {
                            totalPrice += Double.parseDouble(item.getPrice());
                        } catch (NumberFormatException e) {
                            // Handle incorrect price format
                        }
                    }
                }

                if (totalPrice == 0.0) {
                    Toast.makeText(Payment.this, "Select some services First!!!", Toast.LENGTH_SHORT).show();
                    Intent toServicesActivityIntent = new Intent(Payment.this, ServicesActivity.class);
                    startActivity(toServicesActivityIntent);
                    finish();
                }
                else
                {
                    if(!(radioCash.isChecked()) && !(radioOnlinePayment.isChecked()))
                    {
                        Toast.makeText(Payment.this, "Select the payment method first!!!", Toast.LENGTH_SHORT).show();
                        return; // Exit the method
                    }
                    else if (receiptUri == null && radioOnlinePayment.isChecked())
                    {
                        Toast.makeText(Payment.this, "Add The recipt or select other payment methods", Toast.LENGTH_SHORT).show();
                        return; // Exit the method
                    }
                    else {
                        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Bookings").child(phoneNumber).push();
                        bookingRef.child("services").setValue(servicesBuilder.toString());
                        bookingRef.child("prices").setValue(priceBuilder.toString());
                        bookingRef.child("totalPrice").setValue(totalPrice);
                        bookingRef.child("date").setValue(getIntent().getStringExtra("date"));
                        bookingRef.child("time").setValue(getIntent().getStringExtra("time"));
                        bookingRef.child("PhoneNumber").setValue(getIntent().getStringExtra("userPhoneNumber"));

                        if (receiptUri != null && radioOnlinePayment.isChecked()) {
                            uploadReceipt(bookingRef, receiptUri, phoneNumber);
                        } else {
                            Toast.makeText(Payment.this, "Booking successful without receipt upload!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Cart").child(phoneNumber).removeValue(); // Clear the cart

                            Intent toReviewIntent = new Intent(Payment.this, ViewBookingsActivity.class);
                            startActivity(toReviewIntent);
                            finish();


                            finish(); // Exit the activity
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }


    private void uploadReceipt(DatabaseReference bookingRef, Uri receiptUri, String phoneNumber) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("receipts").child(receiptUri.getLastPathSegment());
        storageRef.putFile(receiptUri).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            bookingRef.child("receiptUrl").setValue(uri.toString());
            Toast.makeText(Payment.this, "Receipt uploaded successfully!", Toast.LENGTH_LONG).show();
            FirebaseDatabase.getInstance().getReference("Cart").child(phoneNumber).removeValue(); // Clear the cart
            Intent toReviewIntent = new Intent(Payment.this, ViewBookingsActivity.class);
            startActivity(toReviewIntent);
            finish();
        })).addOnFailureListener(e -> {
            Toast.makeText(Payment.this, "Failed to upload receipt!", Toast.LENGTH_SHORT).show();
        });
    }

}
