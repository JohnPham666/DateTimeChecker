package com.trieupk.dateTimeCheckerMobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etDay, etMonth, etYear;
    private Button btnCheck;
    private TextView tvResult;
    private DateValidator dateValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateValidator = new DateValidator();

        etDay = findViewById(R.id.etDay);
        etMonth = findViewById(R.id.etMonth);
        etYear = findViewById(R.id.etYear);
        btnCheck = findViewById(R.id.btnCheck);
        tvResult = findViewById(R.id.tvResult);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = etDay.getText().toString();
                String month = etMonth.getText().toString();
                String year = etYear.getText().toString();
                
                String result = dateValidator.getValidationMessage(day, month, year);
                tvResult.setText(result);
            }
        });
    }
}
