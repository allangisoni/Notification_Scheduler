package com.practice.notificationscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncNotificationJobService  extends JobService {
    JobParameters mjobParameters;
    Boolean isCancelled = false;

    AsyncTask jobAsyncTask;
    @Override

    public boolean onStartJob(JobParameters jobParameters) {

        mjobParameters = jobParameters;
        jobAsyncTask = new JobAsyncTask().execute(mjobParameters);
        Toast.makeText(getBaseContext(), "Job execution started", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        jobAsyncTask.cancel(true);
        Toast.makeText(getBaseContext(), "AsyncTask was cancelled", Toast.LENGTH_SHORT).show();
        return true;
    }


    public class JobAsyncTask extends AsyncTask<JobParameters, Void, Boolean>
    {

        private JobParameters parameters;
        protected Boolean doInBackground(JobParameters... params)
        {

             parameters = params[0];
            try
            {
                Thread.sleep( 5 * 1000 );
               // jobFinished(params[0], false);
                Log.d("Async", " Thread sleeping");
                return true;
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
                return false;
                //jobFinished(params[0], true);

            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {

          //  while(parameters)
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result)
        {

            if(result){
                Toast.makeText(getBaseContext(), "Task executed successfully", Toast.LENGTH_SHORT).show();

            }

            jobFinished(parameters, !result);



        }

    }

}
