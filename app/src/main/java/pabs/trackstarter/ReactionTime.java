package pabs.trackstarter;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReactionTime extends AppCompatActivity {
    float[] arrayTime;
    float[] arrayAccel;
    long time1;
    long time2;

    int offset;

    double accSens, accSens2, accSens3,accSens3fixed;
    String accSensStr, accSensStr2, accSensStr3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reaction_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ReactionTime.this);
        accSensStr = prefs.getString("acc_sensitivity", "1");
        accSens = Double.parseDouble(accSensStr);
        accSensStr2 = prefs.getString("acc_sensitivity2", "5");
        accSens2 = Double.parseDouble(accSensStr2);
        accSensStr3 = prefs.getString("acc_sensitivity3", "0");
        accSens3 = Double.parseDouble(accSensStr3);
        accSens3fixed=320;

        LineChart chart1 = findViewById(R.id.chart1);
        Intent graphData = getIntent();
        Bundle bund = graphData.getExtras();
        assert bund != null;
        arrayTime = bund.getFloatArray("timeArray");
        arrayAccel = bund.getFloatArray("accelArray");
        time1 = bund.getLong("time_GO");
        offset= (int) ((int) accSens3fixed-accSens3); //140 viene de el silencio del sonido, 40 viene de trial and error de acelerometro detectar
        time1 += offset; //Calibration

        float[] arrayDeltaA = new float[arrayTime.length - 1];
        int arrayCut1=0;
        int arrayCut2=0;

        for (int i = 0; i < arrayAccel.length - 1; i++) {
            arrayDeltaA[i] = java.lang.Math.abs(arrayAccel[i + 1] - arrayAccel[i]);
            if (arrayTime[i]<time1) {
               arrayCut1=i;
               arrayCut2=arrayTime.length-arrayCut1;
            }
        }

        float[] arrayDeltaPart1=new float [arrayCut1+1];
        float[] arrayTimePart1=new float [arrayCut1+1];

        float[] arrayDeltaPart2=new float [arrayCut2-1];
        float[] arrayTimePart2=new float [arrayCut2-1];

        System.arraycopy(arrayDeltaA, 0, arrayDeltaPart1, 0, arrayCut1);
        System.arraycopy(arrayDeltaA,  arrayCut1, arrayDeltaPart2, 0, arrayDeltaPart2.length);
        System.arraycopy(arrayTime, 0, arrayTimePart1, 0, arrayCut1);
        System.arraycopy(arrayTime,  arrayCut1, arrayTimePart2, 0, arrayDeltaPart2.length);

       // Log.e("part 2 arrays","time "+ Arrays.toString(arrayTimePart2)+"accel "+ Arrays.toString(arrayDeltaPart2));
        List<Entry> entries = new ArrayList<>();

        boolean first_change = true;
        float delta_min = 0;
        float delta_max = 0;
        float time_mat=arrayTimePart2[0];

        float false_start_delta= (float) accSens2;
        boolean false_start=false;

        if (arrayTime != null && arrayAccel != null) {
            for (int i = 0; i < arrayDeltaPart1.length; i++) {

                if (arrayDeltaPart1[i] > false_start_delta) {
                    false_start=true;
                    time2=-1;
                }

            }
        }
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();


        if (arrayTime != null && arrayAccel != null&&!false_start) {
            for (int i = 0; i < arrayDeltaPart2.length; i++) {
                arrayTimePart2[i]=arrayTimePart2[i]-time_mat;

                entries.add(new Entry(arrayTimePart2[i], arrayDeltaPart2[i]));
                if (arrayDeltaPart2[i] > delta_max) {
                    delta_max = arrayDeltaPart2[i];
                }
                if (arrayDeltaPart2[i] < delta_min) {
                    delta_min = arrayDeltaPart2[i];

                }
                if (arrayDeltaPart2[i] > accSens && first_change) {
                    time2 = (long) (arrayTimePart2[i]);

                    first_change = false;
                }
            }
            entries2.add(new Entry(arrayTimePart2[0], (float) accSens));
            entries2.add(new Entry(arrayTimePart2[arrayTimePart2.length - 1], (float) accSens));
        }
        float false_start_time=0;
        float max_a=0;
        float min_a=0;

        if (arrayTime != null && arrayAccel != null&&false_start) {
            for (int i = 0; i < arrayDeltaA.length; i++) {

                entries.add(new Entry(arrayTime[i], arrayDeltaA[i]));
                if (arrayDeltaA[i]>false_start_delta) {
                    false_start_time=arrayTime[i];
                    max_a=30;
                    min_a=-30;
                }

            }
            entries2.add(new Entry(arrayTime[0], false_start_delta));
            entries2.add(new Entry(arrayTime[arrayTime.length - 1], false_start_delta));

            entries3.add(new Entry(false_start_time, max_a));
            entries3.add(new Entry(false_start_time, min_a));
        }

        String reactionTime = reactionDisplay(time2);

        TextView reactionTV = findViewById(R.id.reaction_time);
        reactionTV.setText(reactionTime);

        LineDataSet dataSet = new LineDataSet(entries, getResources().getString(R.string.label1));
        dataSet.setColor(R.color.blue);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(1);

        LineDataSet dataSet2;
        if (false_start) {
            dataSet2 = new LineDataSet(entries2, getResources().getString(R.string.label2_2));
        }
        else {
            dataSet2 = new LineDataSet(entries2, getResources().getString(R.string.label2_1));

        }
        dataSet2.setDrawValues(false);
        dataSet2.setColor(R.color.redlighter);
        dataSet2.setLineWidth(2);
        dataSet2.setDrawCircles(false);
        dataSet2.enableDashedLine(13,7,0);


        LineDataSet dataSet3 = new LineDataSet(entries3, getResources().getString(R.string.label3));
        if (false_start) {
            dataSet3.setDrawValues(false);
            dataSet3.setColor(R.color.red);
            dataSet3.setLineWidth(3);
            dataSet3.setDrawCircles(false);
            dataSet3.enableDashedLine(13, 7, 0);
        }


        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        dataSets.add(dataSet2);
        if (false_start) {
            dataSets.add(dataSet3);
        }
        LineData lineData = new LineData(dataSets);

        chart1.setData(lineData);

        YAxis left = chart1.getAxisLeft();
        YAxis right = chart1.getAxisRight();
        XAxis top = chart1.getXAxis();
        left.setValueFormatter(new MyYAxisValueFormatter());
        right.setValueFormatter(new MyYAxisValueFormatter());
        top.setValueFormatter(new MyXAxisValueFormatter());
        chart1.setExtraOffsets(5, 5, 5, 5);
        chart1.highlightValue(time1, 0);
        chart1.setMaxVisibleValueCount(0);
        chart1.getDescription().setEnabled(false);
        chart1.invalidate();

    }

    private String reactionDisplay(long time2) {

        DecimalFormat precision = new DecimalFormat("0.000");

        if (time2 < 0) {
            double ftime2 = (double) time2 / 1000;
            String str = getResources().getString(R.string.false_start);
            return str;
        }
        if (time2 == 0) {
            double ftime2 = (double) time2 / 1000;
            String str = "No reaction";
            return str;
        }
        if (time2 < 100) {
            double ftime2 = (double) time2 / 1000;
            String str = "False Start: (" + precision.format(ftime2) + ")";
            return str;
        } else {
            double ftime2 = (double) time2 / 1000;
            String str = precision.format(ftime2);
            return str;
        }
    }

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {

            // format values to 1 decimal digit
            mFormat = new DecimalFormat("#######0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mFormat.format(value);
        }

        private int getDecimalDigits() {
            return 1;
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyXAxisValueFormatter() {

            // format values to 1 decimal digit
            mFormat = new DecimalFormat("#######0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mFormat.format(value);
        }

        private int getDecimalDigits() {
            return 1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.reaction_menu, menu);
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

            case R.id.help:
                Dialog dialog2 = new Dialog(ReactionTime.this);

                dialog2.setContentView(R.layout.dialogue_layout2);
                TextView txt3 = (TextView) dialog2.findViewById(R.id.text1);
                txt3.setText(getString(R.string.reac_hint));

                dialog2.show();

                return true;

            case R.id.back_home:
                backDialogue();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backDialogue();

    }

    public void backDialogue() {
        backHome();

    }

    public void backHome() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.finish();
        startActivity(i);
    }

}
