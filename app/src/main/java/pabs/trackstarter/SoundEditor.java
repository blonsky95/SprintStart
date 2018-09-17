package pabs.trackstarter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class SoundEditor extends AppCompatActivity {



    public SoundEditor() {
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_editor);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String sound1name = prefs.getString("sound1", "1");
        String sound2name = prefs.getString("sound2", "2");
        String sound3name = prefs.getString("sound3", "3");

        TextView sound1TV = findViewById(R.id.sound1tv);
        String file1Name=prefToName(sound1name);
        sound1TV.setText(file1Name);
        TextView sound2TV = findViewById(R.id.sound2tv);
        String file2Name=prefToName(sound2name);
        sound2TV.setText(file2Name);
        TextView sound3TV = findViewById(R.id.sound3tv);
        String file3Name=prefToName(sound3name);
        sound3TV.setText(file3Name);

        TextView homeBtn=findViewById(R.id.home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent= new Intent(SoundEditor.this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(homeIntent);
                finish();
            }
        });

        TextView sound1btn = findViewById(R.id.sound1_btn);
        sound1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), SoundSelector.class);
                int sound1ID = 1;
                intent1.putExtra("soundIdentifier", sound1ID);
                startActivity(intent1);
                finish();
            }
        });
        TextView sound2btn = findViewById(R.id.sound2_btn);
        sound2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), SoundSelector.class);
                int sound2ID = 2;

                intent2.putExtra("soundIdentifier", sound2ID);
                startActivity(intent2);
                finish();

            }
        });
        TextView sound3btn = findViewById(R.id.sound3_btn);
        sound3btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(SoundEditor.this);

                builder.setMessage(getApplicationContext().getResources().getString(R.string.sound3_change));
                builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.delete_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent3 = new Intent(getApplicationContext(), SoundSelector.class);
                        int sound3ID = 3;

                        intent3.putExtra("soundIdentifier", sound3ID);
                        startActivity(intent3);
                        finish();

                    }
                });

                builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.delete_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                builder.show();




            }
        });

        TextView soundAdd = findViewById(R.id.sound_add);
        soundAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AudioRecorder.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private String prefToName(String soundName) {
        String fileName;
        if (soundName.matches("1")) {
            fileName = "On your marks";
            return fileName;

        }
        if (soundName.matches("2")) {
            fileName = "Set";
            return fileName;

        }
        if (soundName.matches("3")) {
            fileName = "GO";
            return fileName;

        }
        else {
            fileName=soundName.substring(soundName.indexOf("/real")+6,soundName.length()-4);
        }
        return fileName;
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


        }
        return super.onOptionsItemSelected(item);
    }

}

