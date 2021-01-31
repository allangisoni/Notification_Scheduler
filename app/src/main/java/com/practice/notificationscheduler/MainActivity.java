package com.practice.notificationscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RadioGroup rgNetworkOptions;
    Button btnScheduleJobs;
    Button btnScheduleAsyncJobs;
    Button btnCancelJobs;

    SwitchCompat switchCompatIdle, switchCompatCharging;

    TextView tvSeekBarLabel, tvSeekBarProgress;

    SeekBar seekBar;

    private JobScheduler jobScheduler;
    private static final int JOB_ID = 0;

    int seekBarInteger = 0;
    boolean seekBarSet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        btnScheduleJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleJob();
            }
        });

        btnScheduleAsyncJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleAsyncJob();
            }
        });

        btnCancelJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelJob();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(i > 0 ){
                    tvSeekBarProgress.setText(i + "s");

                } else{
                    tvSeekBarProgress.setText("Not Set");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    // initialize layout views
    private  void initViews(){
        rgNetworkOptions = findViewById(R.id.rgNetworkOptions);
        btnScheduleJobs = findViewById(R.id.btnScheduleJob);
        btnScheduleAsyncJobs = findViewById(R.id.btnScheduleAsyncJob);
        btnCancelJobs = findViewById(R.id.btnCancelBob);
        switchCompatIdle = findViewById(R.id.switchIdle);
        switchCompatCharging = findViewById(R.id.switchCharhing);
        seekBar = findViewById(R.id.seekBar);
        tvSeekBarProgress = findViewById(R.id.tvProgress);
    }

   // Schedule job method
    private void ScheduleJob(){
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
        int selectedNetworkID = rgNetworkOptions.getCheckedRadioButtonId();
        switch (selectedNetworkID){
            case  R.id.rbNoNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case  R.id.rbAnyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case  R.id.rbWifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;

        }

        boolean constraintSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE )  || switchCompatIdle.isChecked()
                || switchCompatCharging.isChecked()|| seekBarSet;

        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName);
        builder.setRequiredNetworkType(selectedNetworkOption)
                .setRequiresDeviceIdle(switchCompatIdle.isChecked())
                .setRequiresCharging(switchCompatCharging.isChecked());

        seekBarInteger = seekBar.getProgress();
        seekBarSet = seekBarInteger >0;

        if(seekBarSet){
            builder.setOverrideDeadline(seekBarInteger*1000);
        }


        if(constraintSet) {
            JobInfo jobInfo = builder.build();
            jobScheduler.schedule(jobInfo);

            Toast.makeText(this, "Job Scheduled, job will run when " + "the constraints are met.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Please select atleast one network type.", Toast.LENGTH_SHORT).show();
        }
    }


    private void ScheduleAsyncJob(){
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
        int selectedNetworkID = rgNetworkOptions.getCheckedRadioButtonId();
        switch (selectedNetworkID){
            case  R.id.rbNoNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case  R.id.rbAnyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case  R.id.rbWifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;

        }



        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(getPackageName(),
                AsyncNotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName);
        builder.setRequiredNetworkType(selectedNetworkOption)
                .setRequiresDeviceIdle(switchCompatIdle.isChecked())
                .setRequiresCharging(switchCompatCharging.isChecked());

        seekBarInteger = seekBar.getProgress();
        seekBarSet = seekBarInteger >0;

        if(seekBarSet){
            builder.setOverrideDeadline(seekBarInteger*1000);
        }
        boolean constraintSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE )  || switchCompatIdle.isChecked()
                || switchCompatCharging.isChecked()|| seekBarSet;

        if(constraintSet) {
            JobInfo jobInfo = builder.build();
            jobScheduler.schedule(jobInfo);

            Toast.makeText(this, "Job Scheduled, job will run when " + "the constraints are met.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Please select atleast one network type.", Toast.LENGTH_SHORT).show();
        }
    }
    // Cancel all scheduled jobs
    private void cancelJob(){
        if(jobScheduler != null){
            jobScheduler.cancelAll();
            jobScheduler = null;
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}