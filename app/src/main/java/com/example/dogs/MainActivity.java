package com.example.dogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://dog.ceo/api/breeds/";

    private ImageView imageViewDog;
    private Button buttonNextImage;
    private ProgressBar progressBar;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.loadDogImage();

        mainViewModel.getDogImage().observe(this, new Observer<DogImage>() {
            @Override
            public void onChanged(DogImage dogImage) {
                Glide.with(MainActivity.this)
                        .load(dogImage.getImage())
                        .into(imageViewDog);
            }
        });

        mainViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        mainViewModel.getIsError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if (isError) {
                    Toast.makeText(
                            MainActivity.this,
                            R.string.error_msg,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });


        buttonNextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.loadDogImage();
            }
        });

    }

    public void initViews() {
        imageViewDog = findViewById(R.id.imageViewDog);
        buttonNextImage = findViewById(R.id.buttonNextImage);
        progressBar = findViewById(R.id.progressBar);
    }


}