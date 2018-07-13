package com.example.abbinizar.gcmnetworkmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSetSchedule, btnCancelSchedule;
    private ScheduleTask mScheduleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSetSchedule = (Button)findViewById(R.id.btn_set_schedule);
        btnCancelSchedule = (Button)findViewById(R.id.btn_cancel_schedule);
        btnSetSchedule.setOnClickListener((View.OnClickListener) this);
        btnCancelSchedule.setOnClickListener(this);
        mScheduleTask = new ScheduleTask(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_set_schedule){
            mScheduleTask.createPeriodicTask();
            Toast.makeText(this, "Periodic Task Created", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.btn_cancel_schedule){
            mScheduleTask.cancelPeriodicTask();
            Toast.makeText(this, "Periodic Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }
}
