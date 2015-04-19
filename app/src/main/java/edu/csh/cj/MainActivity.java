package edu.csh.cj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;

//Viagra Challenge, If you persist for longer than 4 hours, contact your Doctor, Because you just Won!!!


public class MainActivity extends ActionBarActivity implements SensorEventListener {
    Button btnDown;
    TextView txtCount, txtTimer;
    Vibrator v;

    //Accelerometer
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 400;
    // Get instance of Vibrator from current Context

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    SensorEventListener listener;
    //InProgress used to ensure multiple timers don't start when START button is pressed
    boolean inProgress = false;
    int tally = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btnDown=(Button)findViewById(R.id.btnDown);
        txtCount=(TextView)findViewById(R.id.txtCount);
        txtTimer=(TextView)findViewById(R.id.txtTimer);
        listener = this;


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);


        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void start(View view){

        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        tally = 0;
        txtCount.setText(Integer.toString(tally));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Start of Timer block
        int timerValue = prefs.getInt("timer_seekBar",0);
        //Converts mS to Seconds for CountDownTimer function below
        timerValue = timerValue * 1000;

        // is the Check box enabling SeekBar checked?
        boolean checked = prefs.getBoolean("Checked",false);

        if(!checked){
            //Start of Level Block
            String Level = prefs.getString("level_setting", "medium");
            switch (Level){
                case "easy":
                    timerValue = 15000;
                    break;
                case "medium":
                    timerValue = 30000;
                    break;
                case "hard":
                    timerValue = 60000;
                    break;
            }
            //End of Level Block
        }

        //Handles in game timer length
        if(inProgress == false) {
            inProgress = true;
            new CountDownTimer(timerValue, 1000) {

                public void onTick(long millisUntilFinished) {
                    txtTimer.setText(":" + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    txtTimer.setText(tally + " Finished!");
                    // Vibrate for 5 seconds
                    v.vibrate(5000);
                    senSensorManager.unregisterListener(listener);
                    inProgress = false;
                }
            }.start();
        }
        //End of Timer Block
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
                //if speed is greater than Threshold and the start button has been pressed (ie inProgress)
                //This stops pre-tallying
                if (speed > SHAKE_THRESHOLD && inProgress) {
                    tally++;
                    txtCount.setText(Integer.toString(tally));
                }


                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
