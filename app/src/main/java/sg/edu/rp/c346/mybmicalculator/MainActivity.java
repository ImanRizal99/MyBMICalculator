package sg.edu.rp.c346.mybmicalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText etHeight, etWeight;
    Button calculateBtn, resetBtn;
    TextView tvDate, tvBMI, tvResult;

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String datetime = prefs.getString("datetime", null);
        float bmi = prefs.getFloat("bmi", 0.0f);
        String bmiResults = bmiResults(bmi);
        tvResult.setText(bmiResults);
        tvBMI.setText(String.format("Last Calculated BMI: %s", Float.toString(bmi)));
        if (datetime == null) {
            tvDate.setText("Last Calculated Date:");
        }
        else {
            tvDate.setText(String.format("Last Calculated Date: %s", datetime));
        }
    }

    private String bmiResults(double bmi) {
        String results = "";
        if (bmi >= 30) {
            results = "You are obese";
        }
        else if (bmi >= 25 && bmi <= 29.9) {
            results = "You are overweight";
        }
        else if (bmi >= 18.5 && bmi <=24.9) {
            results = "Your BMI is normal";
        }
        else if (bmi < 18.5){
            results = "You are underweight";
        }
        else if (bmi == 0.0){
            results = "";
        }
        return results;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etWeight.requestFocus();
        calculateBtn = findViewById(R.id.calculateBtn);
        resetBtn = findViewById(R.id.calculateReset);

        tvDate = findViewById(R.id.tvDate);
        tvBMI = findViewById(R.id.tvBMI);
        tvResult = findViewById(R.id.tvResult);

        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double height = Double.parseDouble(etHeight.getText().toString());
                Double weight = Double.parseDouble(etWeight.getText().toString());
                Double bmi = (weight / (height * height));
                String results = bmiResults(bmi);
                Calendar now = Calendar.getInstance();  //Create a Calendar object with current date and time
                String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                        (now.get(Calendar.MONTH)+1) + "/" +
                        now.get(Calendar.YEAR) + " " +
                        now.get(Calendar.HOUR_OF_DAY) + ":" +
                        now.get(Calendar.MINUTE);

                tvDate.setText("Last Calculated Date: " + datetime);
                tvBMI.setText(String.format("Last Calculated BMI: %.3f",bmi));
                tvResult.setText(results);

                etWeight.setText("");
                etHeight.setText("");

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor prefEdit = prefs.edit();
                prefEdit.putString("datetime",datetime);
                prefEdit.putFloat("bmi",Float.parseFloat(bmi.toString()));
                prefEdit.commit();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBMI.setText("Last Calculated BMI:0.0");
                tvDate.setText("Last Calculated Date:");
                tvResult.setText("");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor prefEdit = prefs.edit();
                prefEdit.clear();
                prefEdit.commit();
            }
        });
    }
}
