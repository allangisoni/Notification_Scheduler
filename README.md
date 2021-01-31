# Notification_Scheduler

1. How to implement a JobService.
2. How to construct a JobInfo object with specific constraints.
3. How to schedule a JobService based on the JobInfo object.

# Challenge
1. Implement a JobService that starts an AsyncTask when the given constraints are met.
2. The AsyncTask should sleep for 5 seconds.
3. If the constraints stop being met while the thread is sleeping, reschedule the job and show a Toast message saying that the job failed.
