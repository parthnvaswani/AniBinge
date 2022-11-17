package com.example.kuro.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuro.LoginActivity;
import com.example.kuro.R;
import com.example.kuro.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {
    Button logoutButton;
    TextView detailsTextView;
    FirebaseAuth mAuth;
    String Uid = Utils.getUid();
    CollectionReference userRef = FirebaseFirestore.getInstance().collection("User");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutButton = view.findViewById(R.id.logout);
        detailsTextView = view.findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            this.getActivity().finish();
            mAuth.signOut();
        });
        userRef.document(Uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                detailsTextView.setText(documentSnapshot.getString("name"));
            }
        });
    }
}