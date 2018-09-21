package pabs.trackstarter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class TimeEditor extends AppCompatActivity {

    private Long time1;
    private Long time2;
    private Long time31,time32;

    private EditText time1et;

    private EditText time2et;

    private EditText time31et,time32et;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.times_editor);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        time1 = prefs.getLong("time1", 2000);
        double time1d=(double) time1;
        time2 = prefs.getLong("time2", 5000);
        double time2d=(double) time2;
        time31 = prefs.getLong("time31", 1000);
        double time31d=(double) time31;
        time32 = prefs.getLong("time32", 2000);
        double time32d=(double) time32;


        time1et = findViewById(R.id.time1_et);
        time1et.setText(Double.toString(time1d / 1000));


        time2et = findViewById(R.id.time2_et);
        time2et.setText(Double.toString(time2d / 1000));


        time31et = findViewById(R.id.time31_et);
        time31et.setText(Double.toString(time31d / 1000));
        time32et = findViewById(R.id.time32_et);
        time32et.setText(Double.toString(time32d / 1000));

        TextView save_btn = findViewById(R.id.save_time_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time1_str = time1et.getText().toString();
                Double time1_d = Double.parseDouble(time1_str) * 1000;
                Long time1_long = Math.round(time1_d);

                String time2_str = time2et.getText().toString();
                Double time2_d = Double.parseDouble(time2_str) * 1000;
                Long time2_long = Math.round(time2_d);

                String time31_str = time31et.getText().toString();
                Double time31_d = Double.parseDouble(time31_str) * 1000;
                Long time31_long = Math.round(time31_d);
                String time32_str = time32et.getText().toString();
                Double time32_d = Double.parseDouble(time32_str) * 1000;
                Long time32_long = Math.round(time32_d);

                if (time1_long < 40000 && time1_long >= 0
                        && time2_long < 40000 && time2_long > 0
                        && time32_long > 0 && time31_long > 0) {
                    saveData(time1_long, time2_long, time31_long, time32_long);
                    Toast.makeText(getApplicationContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.save_fail), Toast.LENGTH_SHORT).show();

                }
            }

        });

        TextView reset_btn = findViewById(R.id.reset_time_btn);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetData();
            }

        });


        ImageView timing1Info = findViewById(R.id.timings_info_1);
        timing1Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder_help = new AlertDialog.Builder(TimeEditor.this);

                builder_help.setMessage(getString(R.string.timings1_info));
                builder_help.setPositiveButton(getString(R.string.go_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }

                });

                builder_help.show();
            }
        });

        ImageView timing2Info = findViewById(R.id.timings_info_2);
        timing2Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder_help = new AlertDialog.Builder(TimeEditor.this);

                builder_help.setMessage(getString(R.string.timings2_info));
                builder_help.setPositiveButton(getString(R.string.go_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }

                });
                builder_help.show();

            }
        });

        ImageView timing3Info = findViewById(R.id.timings_info_3);
        timing3Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder_help = new AlertDialog.Builder(TimeEditor.this);

                builder_help.setMessage(getString(R.string.timings3_info));
                builder_help.setPositiveButton(getString(R.string.go_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }

                });
                builder_help.show();
            }
        });
    }


    private void saveData(Long time1, Long time2, Long time31, Long time32) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("time1", time1);
        editor.putLong("time2", time2);
        editor.putLong("time31", time31);
        editor.putLong("time32", time32);

        editor.apply();
    }

    private void resetData() {
        time1 = 2000L;
        time2 = 5000L;
        time31 = 1000L;
        time32 = 2000L;

        time1et.setText(Long.toString(time1 / 1000));
        time2et.setText(Long.toString(time2 / 1000));
        time31et.setText(Long.toString(time31 / 1000));
        time32et.setText(Long.toString(time32 / 1000));

        Toast.makeText(this, getString(R.string.reset_success), Toast.LENGTH_SHORT).show();

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

        }
        return super.onOptionsItemSelected(item);
    }

}
