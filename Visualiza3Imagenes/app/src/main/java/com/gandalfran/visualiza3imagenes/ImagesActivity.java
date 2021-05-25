package com.gandalfran.visualiza3imagenes;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ImagesActivity extends AppCompatActivity {

    public enum ImageIds {
        DOG, CAT, LION
    };

    private ImageView image;
    private ProgressBar progressBar;
    private TextView loadingMessage;
    private TextView finishedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        // retrieve component, set listeners and update user list
        this.image = (ImageView) findViewById(R.id.image);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar);
        this.loadingMessage = (TextView) findViewById(R.id.loadingMessage);
        this.finishedMessage = (TextView) findViewById(R.id.finishMessage);

        // reset progress bar
        this.progressBar.setMax(100);
        this.progressBar.setProgress(0);

        // hide image and finished message and show progress bar and loading message
        this.image.setVisibility(View.GONE);
        this.finishedMessage.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.loadingMessage.setVisibility(View.VISIBLE);

        // load images in background
        this.loadImage();
    }

    private void loadImage() {

        // create thread for not running in main thread and don't block the view
        Thread t = new Thread(()->{
            for (ImageIds imageId : ImageIds.values()){
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // show loading message and bar. also reset progress bar
                        runOnUiThread(()->{
                            progressBar.setProgress(0);
                            image.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            finishedMessage.setVisibility(View.GONE);
                            loadingMessage.setVisibility(View.VISIBLE);
                        });

                        // generate random time
                        int time = new Random().nextInt(1 + Constants.MAX_BOUND - Constants.MIN_BOUND) + Constants.MIN_BOUND;

                        // upload progress periodically
                        for (int current = 0; current <= time; current++) {
                            final int curretTime = current;

                            // sleep one second
                            try {
                                Thread.sleep(1000);
                            } catch (Exception ex) {
                            }

                            // update progress
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(curretTime * 100 / time);
                                }
                            });
                        }

                        // select image and make it visible
                        runOnUiThread(()->{

                            image.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            loadingMessage.setVisibility(View.GONE);

                            switch (imageId){
                                case DOG:
                                    image.setImageResource(R.drawable.perro);
                                    break;
                                case CAT:
                                    image.setImageResource(R.drawable.gato);
                                    break;
                                case LION:
                                    image.setImageResource(R.drawable.leon);
                                    break;
                                default:
                                    image.setImageResource(R.drawable.leon);
                            }
                        });

                        // sleep during visualization time
                        try {
                            Thread.sleep(Constants.IMAGE_TIME);
                        } catch (Exception ex) {
                        }

                        // hide image and set progress bar and loading message visible
                        runOnUiThread(()->{
                            image.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            loadingMessage.setVisibility(View.VISIBLE);
                        });
                    }

                });
                th.start();
                try {
                    th.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // show finished message
            runOnUiThread(()->{
                image.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                loadingMessage.setVisibility(View.GONE);
                finishedMessage.setVisibility(View.VISIBLE);
            });

            // sleep during two seconds and come back main activity
            try {
                Thread.sleep(Constants.FINAL_SLEEP_TIME);
            } catch (Exception ex) {
            }
            runOnUiThread(()->{
                ImagesActivity.this.finish();
            });
        });
        t.start();

    }
}