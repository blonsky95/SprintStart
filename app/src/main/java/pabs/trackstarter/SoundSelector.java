package pabs.trackstarter;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SoundSelector extends AppCompatActivity {

    int soundID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_selector);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent rIntent = getIntent();
        soundID = rIntent.getIntExtra("soundIdentifier", 0);

        loadSounds();

    }

    String extDir = Environment.getExternalStorageDirectory().toString();
    String directory_custom_sounds = extDir + "/real";
    String default_sound1 = "On your Marks";
    AudioFile sound1_af = new AudioFile(default_sound1, "1", false);
    String default_sound2 = "Set";
    AudioFile sound2_af = new AudioFile(default_sound2, "2", false);
    String default_sound3 = "Go";
    AudioFile sound3_af = new AudioFile(default_sound3, "3", false);

    ArrayList<AudioFile> audioFileList = new ArrayList<>();

    private void loadSounds() {
        File dirFile = new File(directory_custom_sounds);
        File[] soundFiles = dirFile.listFiles();

        audioFileList.add(sound1_af);
        audioFileList.add(sound2_af);
        audioFileList.add(sound3_af);

        if (soundFiles!=null) {
            for (int i = 0; i < soundFiles.length; i++) {

                String audio3gp = soundFiles[i].getName();
                String audioName = audio3gp.substring(0, audio3gp.length() - 4);
                String audioDir = directory_custom_sounds + "/" + audio3gp;
                AudioFile cuurentFile = new AudioFile(audioName, audioDir, true);
                audioFileList.add(cuurentFile);

            }
        }
        AudioFileAdapter aFilesAdapter = new AudioFileAdapter(SoundSelector.this, audioFileList, soundID);
        ListView listView = findViewById(R.id.sounds_list);
        listView.setAdapter(aFilesAdapter);

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
                TextView txt1 = (TextView) dialog.findViewById(R.id.text1);
                txt1.setText(getString(R.string.dialogue1));
                TextView txt2 = (TextView) dialog.findViewById(R.id.text2);
                txt2.setText(getString(R.string.dialogue2));

                dialog.show();


                return true;

            case android.R.id.home:
       //         NavUtils.navigateUpFromSameTask(this);
        //        Log.e("LISTEN","HOME IS CALLED");

                // UP NAVIGATION DOESNT TAKE ACTIVITY TO PARENT ACTIVITY - SHORT TERM SOLUTION
                Intent backSoundEditor=new Intent(SoundSelector.this,SoundEditor.class);
                startActivity(backSoundEditor);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
