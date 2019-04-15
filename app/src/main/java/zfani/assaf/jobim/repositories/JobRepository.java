package zfani.assaf.jobim.repositories;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import zfani.assaf.jobim.db.JobDatabase;
import zfani.assaf.jobim.interfaces.JobDao;
import zfani.assaf.jobim.models.Job;

public class JobRepository {

    private JobDao jobDao;

    public JobRepository(Context context) {
        jobDao = JobDatabase.getDatabase(context).getJobDAO();
    }

    public LiveData<List<Job>> getAllJobs() {
        return jobDao.getAllJobs();
    }

    public List<Job> getAllJobs(List<String> jobTypeList, String jobLocation, String jobFirm) {
        String queryString = "";
        List<Object> args = new ArrayList<>();
        boolean containsCondition = false;
        queryString += "Select * from job_table";
        if (!jobTypeList.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (String jobType : jobTypeList) {
                str.append("'").append(jobType).append("', ");
            }
            queryString += " where type in (" + str.substring(0, str.length() - 2) + ")";
            containsCondition = true;
        }
        if (jobLocation != null) {
            queryString += (containsCondition ? " and" : " where") + " address like ?";
            containsCondition = true;
            args.add(jobLocation);
        }
        if (jobFirm != null) {
            queryString += (containsCondition ? " and" : " where") + " firm like ?";
            args.add(jobFirm);
        }
        try {
            return new GetAllJobsTask(jobDao).execute(new SimpleSQLiteQuery(queryString, args.toArray())).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void insert(Job job) {
        new InsertJobTask(jobDao).execute(job);
    }

    public void delete(Job job) {
        new DeleteJobTask(jobDao).execute(job);
    }

    private static class GetAllJobsTask extends AsyncTask<SimpleSQLiteQuery, Void, List<Job>> {

        private JobDao jobDao;

        GetAllJobsTask(JobDao jobDao) {
            this.jobDao = jobDao;
        }

        @Override
        protected List<Job> doInBackground(SimpleSQLiteQuery... simpleSQLiteQueries) {
            return jobDao.getAllJobs(simpleSQLiteQueries[0]);
        }
    }

    private static class InsertJobTask extends AsyncTask<Job, Void, Void> {

        private JobDao jobDao;

        InsertJobTask(JobDao jobDao) {
            this.jobDao = jobDao;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            jobDao.insertJob(jobs[0]);
            return null;
        }
    }

    private static class DeleteJobTask extends AsyncTask<Job, Void, Void> {

        private JobDao jobDao;

        DeleteJobTask(JobDao jobDao) {
            this.jobDao = jobDao;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            jobDao.deleteJob(jobs[0]);
            return null;
        }
    }
}
