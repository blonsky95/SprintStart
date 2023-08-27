package pabs.trackstarter;

import android.Manifest;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class AudioRecorder extends AppCompatActivity {


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;


    private static String mFileName1;
    private static String mFileName2;


    private Button mRecordButton;
    private Button stopRecordBtn;
    private MediaRecorder mRecorder;

    private Button mPlayButton;
    private Button stopPlayBtn;
    private MediaPlayer mPlayer;

    private String audioName;
    private EditText audioNameET;
    private Button saveAudioBtn;


    boolean mStartPlaying = false;
    boolean mStartRecording = false;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToReadESAccepted = false;
    private boolean permissionToWriteESAccepted = false;

    private boolean recordingValid = false;

    String extDir = Environment.getExternalStorageDirectory().toString();


    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_recorder);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean READ_GRANTED = isReadStoragePermissionGranted();
        boolean WRITE_GRANTED = isWriteStoragePermissionGranted();

        if (READ_GRANTED && WRITE_GRANTED) {
            File directory1 = new File(extDir + "/demo");
            if (!directory1.exists()) {
                directory1.mkdir();

            }
            File directory2 = new File(extDir + "/real");
            if (!directory2.exists()) {
                directory2.mkdir();

            }
            mFileName2 = directory1.toString();
            mFileName1 = directory1.toString() + "/000test.3gp";

        }
        else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        mRecordButton = findViewById(R.id.record_btn);
        stopRecordBtn = findViewById(R.id.record_stop_btn);
        mRecordButton.setOnClickListener(RecordButton);
        stopRecordBtn.setOnClickListener(StopRecord);
        mPlayButton = findViewById(R.id.play_btn);
        stopPlayBtn = findViewById(R.id.play_stop_btn);

        mPlayButton.setOnClickListener(PlayButton);
        stopPlayBtn.setOnClickListener(StopPlay);

        saveAudioBtn = findViewById(R.id.record_save_btn);
        saveAudioBtn.setOnClickListener(saveAudio);

        audioNameET = findViewById(R.id.audio_name);


    }

    View.OnClickListener saveAudio = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            audioName = audioNameET.getText().toString().trim();
            if (!recordingValid) {
                Toast.makeText(getApplicationContext(), getString(R.string.audio_invalid), Toast.LENGTH_SHORT).show();
                return;
            }
            if (audioName.length() > 1 && audioName.length() < 15) {

                File succesfulAudioDemo = new File(mFileName1);
                mFileName2 = extDir + "/real/" + audioName + ".3gp";
                File succesfulAudioReal = new File(mFileName2);
                succesfulAudioDemo.renameTo(succesfulAudioReal);
                Toast.makeText(getApplicationContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();

                return;
            }
            Toast.makeText(getApplicationContext(), getString(R.string.audioname_invalid), Toast.LENGTH_SHORT).show();
        }
    };


    View.OnClickListener RecordButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!mStartRecording) {
                recordingValid = false;
                mRecordButton.setBackground(getResources().getDrawable(R.drawable.btn_pressed));

                startRecording();
                mStartRecording = true;

                return;
            }
            Toast.makeText(getApplicationContext(), getString(R.string.recording_on), Toast.LENGTH_SHORT).show();

        }


    };

    View.OnClickListener StopRecord = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mStartRecording) {
                mRecordButton.setBackground(getResources().getDrawable(R.drawable.stop_box));
                recordingValid = true;
                stopRecording();
                mStartRecording = false;
            }
        }
    };

    View.OnClickListener PlayButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (recordingValid) {
                if (!mStartPlaying) {
                    mPlayButton.setBackground(getResources().getDrawable(R.drawable.btn_pressed));
                    startPlaying();
                    mStartPlaying = true;
                    return;
                }
                Toast.makeText(getApplicationContext(), getString(R.string.player_on), Toast.LENGTH_SHORT).show();

            }
            Toast.makeText(getApplicationContext(), getString(R.string.audio_invalid), Toast.LENGTH_SHORT).show();

        }
    };

    View.OnClickListener StopPlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mStartPlaying) {
                mPlayButton.setBackground(getResources().getDrawable(R.drawable.stop_box));
                stopPlaying();
                mStartPlaying = false;
            }
        }
    };


    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName1);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mStartPlaying = false;
                mPlayButton.setBackground(getResources().getDrawable(R.drawable.stop_box));

            }
        });
    }


    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }


    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mFileName1);
        Log.e("OUTPUT PATH", mFileName1);


        try {
            mRecorder.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.secondary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Dialog dialog = new Dialog(this);

                dialog.setContentView(R.layout.dialogue_layout);
                TextView txt1 = (TextView)dialog.findViewById(R.id.text1);
                txt1.setText(getString(R.string.dialogue1));
                TextView txt2 = (TextView)dialog.findViewById(R.id.text2);
                txt2.setText(getString(R.string.dialogue2));

                dialog.show();

                return true;

            case android.R.id.home:

                // UP NAVIGATION DOESNT TAKE ACTIVITY TO PARENT ACTIVITY - SHORT TERM SOLUTION
                Intent backSoundEditor=new Intent(AudioRecorder.this,SoundEditor.class);
                startActivity(backSoundEditor);
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


}
