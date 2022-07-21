package com.example.rabbitandturtle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarSound ;
    private SeekBar seekBarBrightness;

    private CheckBox checkboxRabbit;
    private CheckBox checkboxTurtle;

    private Button buttonSave, buttonShow;
    private String CHANNEL_ID = "Setting notification";

    private TextView tvScore;
    private TextView tvResult;

    private int score = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.seekBarSound = (SeekBar)this.findViewById(R.id.seekBar_sound);
        this.seekBarBrightness = (SeekBar)this.findViewById(R.id.seekBar_brightness);

        checkboxRabbit = (CheckBox)findViewById(R.id.checkBox_rbit);
        checkboxTurtle = (CheckBox)findViewById(R.id.checkBox_tt);

        seekBarBrightness.setMax(100);
        seekBarSound.setMax(100);
        checkboxTurtle.setChecked(false);
        checkboxRabbit.setChecked(false);

        tvScore = (TextView) findViewById(R.id.textViewDiem);

        //radioGroupDiffLevel = (RadioGroup) findViewById(R.id.radioGroup_diffLevel);

        //radioButtonEasy = (RadioButton) findViewById(R.id.radioButton_easy);
        //radioButtonMedium = (RadioButton) findViewById(R.id.radioButton_medium);
        //radioButtonHard = (RadioButton) findViewById(R.id.radioButton_hard);

        buttonSave = (Button) findViewById(R.id.button_save);

        tvScore.setText("Score: "+score);

        final CountDownTimer countDownTimer = new CountDownTimer(60000, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                int number = 5;
                Random random = new Random();
                if(seekBarSound.getProgress() >= seekBarSound.getMax()){
                    this.cancel();
                    buttonShow.setVisibility(View.VISIBLE);
                    buttonSave.setVisibility(View.VISIBLE);
                    if(checkboxRabbit.isChecked()){
                        score += 10;
                        Toast.makeText(MainActivity.this, "You win", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        score -=10;
                        Toast.makeText(MainActivity.this, "You lose", Toast.LENGTH_SHORT).show();
                    }
                    tvScore.setText("Score: "+score);
                    EnableCheck();
                    DisableCheck();
                }
                if(seekBarBrightness.getProgress() >= seekBarBrightness.getMax()){
                    this.cancel();
                    buttonShow.setVisibility(View.VISIBLE);
                    buttonSave.setVisibility(View.VISIBLE);
                    if(checkboxTurtle.isChecked()){
                        score += 10;
                        Toast.makeText(MainActivity.this, "You win", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        score -=10;
                        Toast.makeText(MainActivity.this, "Bạn đoán sai", Toast.LENGTH_SHORT).show();
                    }
                    tvScore.setText("Score: "+score);
                    EnableCheck();
                    DisableCheck();
                    //Toast.makeText(MainActivity.this, "Turtle Win", Toast.LENGTH_SHORT).show();
                }
                seekBarSound.setProgress(seekBarSound.getProgress() + random.nextInt(number));
                seekBarBrightness.setProgress(seekBarBrightness.getProgress() + random.nextInt(number));
            }

            @Override
            public void onFinish() {

            }
        };

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxRabbit.isChecked() || checkboxTurtle.isChecked()){
                    seekBarSound.setProgress(0);
                    seekBarBrightness.setProgress(0);
                    buttonShow.setVisibility(View.INVISIBLE);
                    buttonSave.setVisibility(View.INVISIBLE);
                    countDownTimer.start();
                    DisableCheck();
                }else {
                    Toast.makeText(MainActivity.this, "Vui lòng đặt cược trước khi chơi", Toast.LENGTH_SHORT).show();
                }

                //MainActivity.this.saveSetting(v);
            }
        });



//        this.loadSetting();

        buttonShow = (Button) findViewById(R.id.button_show);

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.createNotificationChannel();
                MainActivity.this.showNotification();
            }
        });

        checkboxTurtle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkboxRabbit.setChecked(false);
                }
            }
        });

        checkboxRabbit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkboxTurtle.setChecked(false);
                }
            }
        });
    }
    private void EnableCheck(){
        checkboxRabbit.setEnabled(true);
        checkboxTurtle.setEnabled(true);
    }

    private void DisableCheck(){
        checkboxTurtle.setChecked(false);
        checkboxRabbit.setChecked(false);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Phần trăm quảng đường đi được")
                .setContentText("Rabbit: "+this.seekBarSound.getProgress() + "% | " + "Turtle: "+this.seekBarBrightness.getProgress()+"% | " + "Score: "+this.score)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Rabbit: "+this.seekBarSound.getProgress() + "% | " + "Turtle: "+this.seekBarBrightness.getProgress()+"% | "+ "Score: "+this.score))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(001, builder.build());
    }
}