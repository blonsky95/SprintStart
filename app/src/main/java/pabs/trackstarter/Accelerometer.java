package pabs.trackstarter;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Accelerometer extends AppCompatActivity {

    double accSens, accSens2, accSens3;
    String accSensStr, accSensStr2, accSensStr3;
    EditText accSensET, accSensET2, accSensET3;
    DecimalFormat twodp;

    ImageView hint1, hint2, hint3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        twodp = new DecimalFormat("##.##");


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Accelerometer.this);
        accSensStr = prefs.getString("acc_sensitivity", "1");
        accSensStr2 = prefs.getString("acc_sensitivity2", "5");
        accSensStr3 = prefs.getString("acc_sensitivity3", "0");

        accSens = Double.parseDouble(accSensStr);
        accSens2 = Double.parseDouble(accSensStr2);
        accSens3 = Double.parseDouble(accSensStr3);

        accSensStr = twodp.format(accSens);
        accSensStr2 = twodp.format(accSens2);
        accSensStr3 = twodp.format(accSens3);


        accSensET = findViewById(R.id.acc_et);
        accSensET.setText(accSensStr);

        accSensET2 = findViewById(R.id.acc_et2);
        accSensET2.setText(accSensStr2);

        accSensET3 = findViewById(R.id.acc_et3);
        accSensET3.setText(accSensStr3);

        Button plusBTN = findViewById(R.id.plus_btn);
        plusBTN.setOnClickListener(new addQ(1));
        Button minusBTN = findViewById(R.id.minus_btn);
        minusBTN.setOnClickListener(new minusQ(1));

        Button plusBTN2 = findViewById(R.id.plus_btn2);
        plusBTN2.setOnClickListener(new addQ(2));
        Button minusBTN2 = findViewById(R.id.minus_btn2);
        minusBTN2.setOnClickListener(new minusQ(2));

        Button plusBTN3 = findViewById(R.id.plus_btn3);
        plusBTN3.setOnClickListener(new addQ(3));
        Button minusBTN3 = findViewById(R.id.minus_btn3);
        minusBTN3.setOnClickListener(new minusQ(3));


        TextView saveBTN = findViewById(R.id.save_btn);
        saveBTN.setOnClickListener(saveSettings);
        TextView resetBTN = findViewById(R.id.reset_btn);
        resetBTN.setOnClickListener(resetSettings);

        hint1 = findViewById(R.id.hint1);
        hint1.setOnClickListener(showHint1);
        hint2 = findViewById(R.id.hint2);
        hint2.setOnClickListener(showHint2);
        hint3 = findViewById(R.id.hint3);
        hint3.setOnClickListener(showHint3);


    }

    private View.OnClickListener showHint1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Dialog dialog = new Dialog(Accelerometer.this);

            dialog.setContentView(R.layout.dialogue_layout2);
            TextView txt1 = (TextView) dialog.findViewById(R.id.text1);
            txt1.setText(getString(R.string.hint1exp));


            dialog.show();

        }

    };
    private View.OnClickListener showHint2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Dialog dialog = new Dialog(Accelerometer.this);

            dialog.setContentView(R.layout.dialogue_layout2);
            TextView txt1 = (TextView) dialog.findViewById(R.id.text1);
            txt1.setText(getString(R.string.hint2exp));


            dialog.show();

        }

    };

    private View.OnClickListener showHint3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Dialog dialog = new Dialog(Accelerometer.this);

            dialog.setContentView(R.layout.dialogue_layout2);
            TextView txt1 = (TextView) dialog.findViewById(R.id.text1);
            txt1.setText(getString(R.string.hint3exp));


            dialog.show();

        }

    };

    public class addQ implements View.OnClickListener {

        int parameter_ID;

        public addQ(int parameter_ID) {
            this.parameter_ID = parameter_ID;
        }

        @Override
        public void onClick(View v) {
            if (parameter_ID == 1) {
                accSens += 0.1;
                accSensStr = Double.toString(accSens);
                accSensStr = twodp.format(accSens);
                accSensET.setText(accSensStr);
            }
            if (parameter_ID == 2) {
                accSens2 += 0.5;
                accSensStr2 = Double.toString(accSens2);
                accSensStr2 = twodp.format(accSens2);
                accSensET2.setText(accSensStr2);
            }
            if (parameter_ID == 3) {
                accSens3 += 5;
                accSensStr3 = Double.toString(accSens3);
                accSensStr3 = twodp.format(accSens3);
                accSensET3.setText(accSensStr3);
            }

        }


    }


    public class minusQ implements View.OnClickListener {

        int parameter_ID;

        public minusQ(int parameter_ID) {
            this.parameter_ID = parameter_ID;
        }

        @Override
        public void onClick(View v) {

            if (parameter_ID == 1) {
                if (accSens > 0.1) {
                    accSens -= 0.1;
                    accSensStr = Double.toString(accSens);
                    accSensStr = twodp.format(accSens);

                    accSensET.setText(accSensStr);
                    return;
                }
                Toast.makeText(getApplicationContext(), getString(R.string.accel_low), Toast.LENGTH_SHORT).show();
            }
            if (parameter_ID == 2) {
                if (accSens2 > 0.5) {
                    accSens2 -= 0.5;
                    accSensStr2 = Double.toString(accSens2);
                    accSensStr2 = twodp.format(accSens2);
                    accSensET2.setText(accSensStr2);
                    return;
                }
                Toast.makeText(getApplicationContext(), getString(R.string.accel_low), Toast.LENGTH_SHORT).show();

            }
            if (parameter_ID == 3) {

                    accSens3 -= 5;
                    accSensStr3 = Double.toString(accSens3);
                    accSensStr3 = twodp.format(accSens3);
                    accSensET3.setText(accSensStr3);

            }
        }

    }


    private View.OnClickListener saveSettings = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            accSensStr = accSensET.getText().toString().trim();
            accSensStr2 = accSensET2.getText().toString().trim();
            accSensStr3 = accSensET3.getText().toString().trim();
            try {
                accSens = Double.parseDouble(accSensStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.settings_NaN), Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                accSens2 = Double.parseDouble(accSensStr2);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.settings_NaN), Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                accSens3 = Double.parseDouble(accSensStr3);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.settings_NaN), Toast.LENGTH_SHORT).show();
                return;
            }


            if (accSens <= 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.accel_neg), Toast.LENGTH_SHORT).show();
                return;
            }
            if (accSens2 <= 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.accel_neg), Toast.LENGTH_SHORT).show();
                return;
            }

            editor.putString("acc_sensitivity", accSensStr);
            editor.putString("acc_sensitivity2", accSensStr2);
            editor.putString("acc_sensitivity3", accSensStr3);
            editor.apply();
            Toast.makeText(getApplicationContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();

        }

    };

    private View.OnClickListener resetSettings = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            accSens = 1.0;
            accSensStr = Double.toString(accSens);
            accSensET.setText(accSensStr);

            accSens2 = 5.0;
            accSensStr2 = Double.toString(accSens2);
            accSensET2.setText(accSensStr2);

            accSens3 = 0;
            accSensStr3 = Double.toString(accSens3);
            accSensET3.setText(accSensStr3);
        }

    };

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
