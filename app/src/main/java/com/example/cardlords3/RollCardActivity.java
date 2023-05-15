package com.example.cardlords3;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class RollCardActivity extends AppCompatActivity implements ShakeDetector.OnShakeListener{

    private ShakeDetector mShakeDetector;
    int count = 0;
    int exist_card = 0;
    TextView TotalCardTextView;
    TextView AddCardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_card);

        if(getIntent().hasExtra("card_count")){
            exist_card = getIntent().getIntExtra("card_count", 0);
        }

        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(this);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mShakeDetector, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);

        TotalCardTextView = this.findViewById(R.id.card_number_text);
        TotalCardTextView.setText(Integer.toString(exist_card + count));
        AddCardTextView = this.findViewById(R.id.addCardNum);
    }
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();

        SensorManager sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sensorManager.unregisterListener(shakeDetector);
    }*/

    @Override
    public void onShake() {
        // do something in response to the shake event
        if((exist_card + count) < 50){
            addCard();
        }else{
            Toast t = Toast.makeText(this, "CardDeck is full", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void addCard(){
        count++;
        Toast t = Toast.makeText(this, "Shake added " + count + " cards!", Toast.LENGTH_SHORT);
        t.show();
        TotalCardTextView.setText(Integer.toString(exist_card + count));
        String input = "+"+ Integer.toString(count);
        AddCardTextView.setText(input);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(mShakeDetector);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mShakeDetector, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }
}