package com.soft.sergey.numeriseur;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class DActivity extends AppCompatActivity {


    final static int maxNumber = 123;
    final static int timeToGenerate = 3000;
    private long mLastClickTime = 0;


    String [] numberText = {
            "zero", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf", "dix",
            "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix-sept", "dix-huit", "dix-neuf", "vingt",
            "Vingt-et-un", "Vingt-deux", "vingt-trois", "vingt-quatre", "vingt-cinq", "vingt-six", "vingt-sept", "vingt-huit", "Vingt-neuf", "trente",
            "trente et un", "trente-deux", "trente-trois", "trente-quatre", "trente-cinq", "trente-six", "trente-sept", "trente-huit", "trente-neuf", "quarante",
            "quarante et un", "quarante-deux", "quarante-trois", "quarante-quatre", "quarante-cinq", "quarante-six", "quarante-sept", "quarante-huit", "quarante-neuf", "cinquante",
            "cinquante et un", "cinquante-deux", "cinquante-trois", "cinquante-quatre", "cinquante-cinq", "cinquante-six", "cinquante-sept", "cinquante-huit", "cinquante-neuf", "soixante",
            "soixante et un", "soixante-deux", "soixante-trois", "soixante-quatre", "soixante-cinq", "soixante-six", "soixante-sept", "soixante-huit", "soixante-neuf", "soixante-dix",
            "soixante et onze", "soixante-douze", "soixante-treize", "soixante-quatorze", "soixante-quinze", "soixante-seize", "soixante-dix-sept", "soixante-dix-huit", "soixante-dix-neuf", "quatre-vingt",
            "quatre-vingt un", "quatre-vingt deux", "quatre-vingt trois", "quatre-vingt quatre", "quatre-vingt cinq", "quatre-vingt six", "quatre-vingt sept", "quatre-vingt huit", "quatre-vingt neuf", "quatre-vingt dix",
            "quatre-vingt onze", "quatre-vingt-douze", "quatre-vingt-treize", "quatre-vingt-quatorze", "quatre-vingt-quinze", "quatre-vingt-seize", "quatre-vingt-dix-sept", "quatre-vingt-dix-huit", "quatre-vingt-dix-neuf", "cent",
            "cent et un", "cent deux", "cent trois", "cent quatre", "cent cinq", "cent six", "cent sept", "cent huit", "cent neuf", "cent dix",
            "cent onze", "cent douze", "cent treize", "cent quatorze", "cent quinze", "cent seize", "cent dix-sept", "cent dix-huit", "cent dix-neuf", "cent vingt",
            "cent Vingt-et-un", "cent Vingt-deux", "cent vingt-trois", "cent vingt-quatre"
    };

    protected static final int RESULT_SPEECH = 1;

    private String sText;
    private int lastNmber = maxNumber;
    TextView tNumber;

    private Handler mHandler = new Handler();
    Random randomNumber = new Random();
    Boolean isHelped = false;
    int count = 0;
    int countYellow = 0;
    int countGreen = 0;


    private class MyAsyncTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            final ImageButton clickButton = (ImageButton) findViewById(R.id.buttonGen);

            long endTime = System.currentTimeMillis() + timeToGenerate;
            while (System.currentTimeMillis() < endTime) {


                String numb = String.valueOf(lastNmber = randomNumber.nextInt(maxNumber));
                publishProgress(numb);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {

            tNumber.setText(values[0]);
            TextView tShow = (TextView)findViewById(R.id.textShow);
            tShow.setText("");

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
        tNumber = (TextView)findViewById(R.id.textView);


        final ImageButton clickButton = (ImageButton) findViewById(R.id.buttonGen);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (lastNmber != maxNumber) {
                    AddMessage(String.valueOf(lastNmber), Color.RED);
                }
                isHelped = false;

                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });


        ImageButton clickButtonShow = (ImageButton) findViewById(R.id.buttonShow);
        clickButtonShow.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView tShow = (TextView)findViewById(R.id.textShow);
                tShow.setText(numberText[lastNmber]);
                isHelped = true;
            }
        });


        ImageButton clickButtonParler = (ImageButton) findViewById(R.id.buttonParler);
        clickButtonParler.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView tShow = (TextView)findViewById(R.id.textShow);

                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parlez le numero, svp!");
                    String languagePref = "fr";
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languagePref);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languagePref);
                    intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, languagePref);
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch(ActivityNotFoundException e) {
                    String appPackageName = "com.google.android.googlequicksearchbox";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }

        });



    }


    void AddMessage(String message, int color) {
        View linearLayout =  findViewById(R.id.info);

        //too much numbers
        if (count > 13)    {
            ((LinearLayout) linearLayout).removeAllViews();
            //counting good rate + yellow rate/2
            int rate = (int)(100.0 * (countGreen + countYellow / 2.0) / 13.0);
            Toast.makeText(getApplicationContext(),
                    "Taux: "+ String.valueOf(rate)+" %",
                    Toast.LENGTH_SHORT).show();
            count = 0;
            countYellow = 0;
            countGreen = 0;
        }


        TextView msg = new TextView(DActivity.this);
        msg.setText(message);
        msg.setTextColor(color);
        msg.setPadding(5, 5, 5, 5);
        ((LinearLayout) linearLayout).addView(msg);
        if (color == Color.GREEN) countGreen++;
        if (color == Color.YELLOW) countYellow++;
        count++ ;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {


                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    TextView tShow = (TextView)findViewById(R.id.textShow);


                    String found = text.get(0);

                    if (found.equals("un") || found.equals("une")) found = "1";
                    if (found.equals("zéro") || found.equals("zero")) found = "0";


                    tShow.setText(found);

                    if (found.trim().equals(String.valueOf(lastNmber))) {

                        Toast.makeText(getApplicationContext(),
                                "Magnifique!",
                                Toast.LENGTH_SHORT).show();

                       if (!isHelped)
                           AddMessage(found, Color.GREEN);
                        else
                           AddMessage(found, Color.YELLOW);

                        ImageButton clickButton = (ImageButton) findViewById(R.id.buttonGen);
                        lastNmber = maxNumber;
                        clickButton.callOnClick();
                    }



                }
                break;
            }

        }
    }



}
