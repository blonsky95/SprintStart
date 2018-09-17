package pabs.trackstarter;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    ArrayList<Float> timeArray = new ArrayList<>();
    ArrayList<Float> accelArray = new ArrayList<>();


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToReadESAccepted = false;
    private boolean permissionToWriteESAccepted = false;

    private long time1;

    private long time2;

    private long time31,time32;
    private long time_GO;

    private long sensorDelay = 1000;

    private static int file0;
    private static int file1;
    private static int file2;

    private static boolean process_currently_on = false;
    private static boolean audio1_in_queue = false;
    private static boolean audio2_in_queue = false;
    private static boolean audio3_in_queue = false;
    boolean reac_btn_pressable = false;


    MediaPlayer mp1 = new MediaPlayer();
    MediaPlayer mp2 = new MediaPlayer();
    MediaPlayer mp3 = new MediaPlayer();

    String LOG_TAG = "LOG TAG";

    public static String newTimesMain;

    String fileSound1;
    String fileSound2;
    String fileSound3;
    final Handler handler = new Handler();

    Button fab;
    Button reac_btn;

    NotificationChannel channel;
    String CHANNEL_ID = "1";
    int NOTIFICATION_ID = 1;
    NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (String.valueOf(getIntent()).indexOf("1040") > 0) {

//store booleans in preferences

        }
        String intent = getIntent().toString();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        createNotificationChannel();



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        fab = findViewById(R.id.start_btn);
        fab.setOnClickListener(startProgram);

        reac_btn = findViewById(R.id.reaction_btn);
        reac_btn.setBackgroundColor(getResources().getColor(R.color.light_grey));
        reac_btn.setTextColor(getResources().getColor(R.color.grey_black));
        reac_btn.setOnClickListener(reactionGraph);

        TextView stop_TV = findViewById(R.id.stop_btn);
        stop_TV.setOnClickListener(stopMP);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        }


    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private String stringToFile(String soundStr) {
        String fileStr;
        if (soundStr.equals("1")) {
            fileStr = "1";
            return fileStr;

        }
        if (soundStr.equals("2")) {
            fileStr = "2";
            return fileStr;

        }
        if (soundStr.equals("3")) {
            fileStr = "3";
            return fileStr;

        }

        fileStr = soundStr;

        return fileStr;
    }


    public void playAudio1() {

        if (fileSound1.length() < 5) {
            if (fileSound1.equals("1")) {
                file0 = R.raw.onyourmarks;
            }
            if (fileSound1.equals("2")) {
                file0 = R.raw.set;
            }
            if (fileSound1.equals("3")) {
                file0 = R.raw.go;
            }
            mp1 = MediaPlayer.create(this, file0);

        } else {
            try {
                mp1.setDataSource(fileSound1);
                mp1.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mp1.start();
        fab.setText(getResources().getString(R.string.on_your_marks));

        audio1_in_queue = false;
        audio2_in_queue = true;

        mp1.setLooping(false);

        mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        playAudio2();
                    }
                }, time2);

            }
        });

    }

    public void playAudio2() {

        first_sensor_change = true;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);


        if (fileSound2.length() < 5) {
            if (fileSound2.equals("1")) {
                file1 = R.raw.onyourmarks;
            }
            if (fileSound2.equals("2")) {
                file1 = R.raw.set;
            }
            if (fileSound2.equals("3")) {
                file1 = R.raw.go;
            }
            mp2 = MediaPlayer.create(this, file1);

        } else {
            try {
                mp2.setDataSource(fileSound2);
                mp2.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mp2.start();
        fab.setText(getResources().getString(R.string.set));

        audio2_in_queue = false;
        audio3_in_queue = true;

        mp2.setLooping(false);
        mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playAudio3();
                    }
                }, time3_mod(time31,time32));

            }
        });

    }

    public long time3_mod(long x, long y) {
        double threshold = y-x;
        int threshold_int = (int) threshold;
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(threshold_int);
        double time3_dou = x+randomInt;
        return (long) time3_dou;

    }

    public void playAudio3() {
        if (fileSound3.length() < 5) {
            if (fileSound3.equals("1")) {
                file2 = R.raw.onyourmarks;
            }
            if (fileSound3.equals("2")) {
                file2 = R.raw.set;
            }
            if (fileSound3.equals("3")) {
                file2 = R.raw.go;
            }
            mp3 = MediaPlayer.create(this, file2);

        } else {
            try {
                mp3.setDataSource(fileSound3);
                mp3.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mp3.start();
        time_GO = System.currentTimeMillis() - t_0;
        fab.setText(getResources().getString(R.string.go));
        audio3_in_queue = false;
        mp3.setLooping(false);
        mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp1.reset();
                mp2.reset();
                mp3.reset();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopAccSensor();
                        fab.setBackground(getResources().getDrawable(R.drawable.fab));
                        fab.setText(getResources().getString(R.string.frag1));
                        reac_btn.setBackground(getResources().getDrawable(R.drawable.stop_box));
                        reac_btn.setTextColor(getResources().getColor(R.color.white));
                        reac_btn_pressable = true;
                        notificationManager.cancelAll();

                        // Log.e("ACCA", String.valueOf(accelArray.size())+String.valueOf(accelArray));
                    }
                }, sensorDelay);


                process_currently_on = false;

            }
        });
    }

    private void stopAccSensor() {
        mSensorManager.unregisterListener(this, mAccelerometer);

    }

    private View.OnClickListener reactionGraph = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (reac_btn_pressable) {
                Intent intent = new Intent(MainActivity.this, ReactionTime.class);
                float[] arrayTime = new float[timeArray.size()];

                float[] arrayAccel = new float[accelArray.size()];
                for (int i = 0; i < timeArray.size(); i++) {
                    arrayTime[i] = timeArray.get(i);
                }
                for (int i = 0; i < accelArray.size(); i++) {
                    arrayAccel[i] = accelArray.get(i);
                }
                Bundle bund = new Bundle();
                bund.putLong("time_GO", time_GO);
                bund.putFloatArray("timeArray", arrayTime);
                bund.putFloatArray("accelArray", arrayAccel);
                intent.putExtras(bund);
                startActivity(intent); //add arrays
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.reac_btn_unpressable), Toast.LENGTH_SHORT).show();

            }
        }

    };


    private View.OnClickListener startProgram = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            timeArray = new ArrayList<>(); //set arrays to 0 in case reaction isnt called
            accelArray = new ArrayList<>();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            time1 = prefs.getLong("time1", 2000);
            time2 = prefs.getLong("time2", 5000);
            time31 = prefs.getLong("time31", 1000);
            time32 = prefs.getLong("time32", 2000);

            String sound1name = prefs.getString("sound1", "1");
            fileSound1 = stringToFile(sound1name);
            String sound2name = prefs.getString("sound2", "2");
            fileSound2 = stringToFile(sound2name);
            String sound3name = prefs.getString("sound3", "3");
            fileSound3 = stringToFile(sound3name);

            if (!process_currently_on) {

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.setAction(Intent.ACTION_MAIN);
                resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
//                stackBuilder.addParentStack(MainActivity.class);
//                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.play_on)
                        .setContentTitle("TrackStarter running")
                        .setContentText("Tap to go back to app")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(false);

                notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                fab.setBackground(getResources().getDrawable(R.drawable.fab_pressed));
                process_currently_on = true;
                audio1_in_queue = true;
                reac_btn.setBackgroundColor(getResources().getColor(R.color.light_grey));
                reac_btn.setTextColor(getResources().getColor(R.color.grey_black));
                reac_btn_pressable = false;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playAudio1();

                    }
                }, time1);
            } else {

                Toast.makeText(getApplicationContext(), getString(R.string.start_fail), Toast.LENGTH_SHORT).show();

            }
        }

    };

    private View.OnClickListener stopMP = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (process_currently_on) {
                if (audio1_in_queue) {
                    handler.removeCallbacksAndMessages(null);
                    process_currently_on = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.start_stopped), Toast.LENGTH_SHORT).show();
                    fab.setBackground(getResources().getDrawable(R.drawable.fab));
                    fab.setText(getResources().getString(R.string.frag1));
                    notificationManager.cancelAll();

                    return;
                }

                if (mp1.isPlaying()) {
                    mp1.stop();
                    mp1.reset();
                    process_currently_on = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.start_stopped), Toast.LENGTH_SHORT).show();
                    fab.setBackground(getResources().getDrawable(R.drawable.fab));
                    fab.setText(getResources().getString(R.string.frag1));
                    notificationManager.cancelAll();

                    return;
                }

                if (audio2_in_queue && !mp1.isPlaying()) {
                    handler.removeCallbacksAndMessages(null);
                    mp1.reset();
                    process_currently_on = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.start_stopped), Toast.LENGTH_SHORT).show();
                    fab.setBackground(getResources().getDrawable(R.drawable.fab));
                    fab.setText(getResources().getString(R.string.frag1));
                    notificationManager.cancelAll();

                    return;
                }


                if (mp2.isPlaying()) {
                    mp2.stop();
                    mp2.reset();
                    process_currently_on = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.start_stopped), Toast.LENGTH_SHORT).show();
                    fab.setBackground(getResources().getDrawable(R.drawable.fab));
                    fab.setText(getResources().getString(R.string.frag1));
                    notificationManager.cancelAll();

                    return;
                }

                if (!mp2.isPlaying() && audio3_in_queue) {
                    mp1.reset();
                    mp2.reset();
                    handler.removeCallbacksAndMessages(null);
                    process_currently_on = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.start_stopped), Toast.LENGTH_SHORT).show();
                    fab.setBackground(getResources().getDrawable(R.drawable.fab));
                    fab.setText(getResources().getString(R.string.frag1));
                    if (mAccelerometer != null) {
                        mSensorManager.unregisterListener(MainActivity.this, mAccelerometer);
                    }
                    notificationManager.cancelAll();

                    return;
                }

                if (mp3.isPlaying()) {
                    mSensorManager.unregisterListener(MainActivity.this, mAccelerometer);

                    mp3.stop();
                    mp3.reset();
                    process_currently_on = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.start_stopped), Toast.LENGTH_SHORT).show();
                    fab.setBackground(getResources().getDrawable(R.drawable.fab));
                    fab.setText(getResources().getString(R.string.frag1));
                    notificationManager.cancelAll();


                } else {
                    mSensorManager.unregisterListener(MainActivity.this, mAccelerometer);
                    fab.setBackground(getResources().getDrawable(R.drawable.fab));
                    fab.setText(getResources().getString(R.string.frag1));
                    Toast.makeText(getApplicationContext(), getString(R.string.start_stopped), Toast.LENGTH_SHORT).show();
                    process_currently_on = false;
                    notificationManager.cancelAll();

                }

            } else {
                Toast.makeText(getApplicationContext(), "Start is not currently running", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToReadESAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteESAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Dialog dialog = new Dialog(this);

                dialog.setContentView(R.layout.dialogue_layout);
                TextView txt1 = (TextView) dialog.findViewById(R.id.text1);
                txt1.setText(getString(R.string.dialogue1));
                TextView txt2 = (TextView) dialog.findViewById(R.id.text2);
                txt2.setText(getString(R.string.dialogue2));

                dialog.show();

                return true;
            case R.id.timing:
                Intent timingIntent = new Intent(this, TimeEditor.class);
                startActivity(timingIntent);

                return true;

            case R.id.sounds:
                Intent soundsIntent = new Intent(this, SoundEditor.class);
                startActivity(soundsIntent);

                return true;

            case R.id.accelerometer:
                Intent accelIntent = new Intent(this, Accelerometer.class);
                startActivity(accelIntent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static boolean first_sensor_change = false; //if true, timestamp is edited to start from t=0 to suit the graph
    long t_current;
    long t_current_mod;

    long t_0;
    boolean go_sound_started;

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (first_sensor_change) {
            t_0 = System.currentTimeMillis();

            first_sensor_change = false;
        }

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        t_current = System.currentTimeMillis();
        t_current_mod = (t_current - t_0);

        getMag(x, y, z, t_current_mod);

    }

    public void getMag(float x, float y, float z, long t_current_mod) {

        double mag = Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)) + (Math.pow(z, 2)));
        accelArray.add((float) mag);
        timeArray.add((float) t_current_mod);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onBackPressed() {
        if (audio1_in_queue||audio2_in_queue||audio3_in_queue) {
            handler.removeCallbacksAndMessages(null);
        }
        if (mp1.isPlaying()) {
            mp1.stop();
        }
        if (mp2.isPlaying()) {
            mp2.stop();
        }
        if (mp3.isPlaying()) {
            mp3.stop();
        }
        mp1.reset();
        mp2.reset();
        mp3.reset();
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        process_currently_on = false;
        audio1_in_queue = false;
        audio2_in_queue = false;
        audio3_in_queue = false;
        reac_btn_pressable = false;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
