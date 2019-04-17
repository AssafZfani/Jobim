package zfani.assaf.jobim.repositories;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

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
        String query = "";
        List<Object> args = new ArrayList<>();
        boolean containsCondition = false;
        query += "Select * from job_table";
        if (!jobTypeList.isEmpty()) {
            StringBuilder jobTypeStr = new StringBuilder();
            for (String jobType : jobTypeList) {
                jobTypeStr.append("'").append(jobType).append("', ");
            }
            query += " where type in (" + jobTypeStr.substring(0, jobTypeStr.length() - 2) + ")";
            containsCondition = true;
        }
        if (jobLocation != null) {
            query += (containsCondition ? " and" : " where") + " address like '%" + jobLocation + "'";
            containsCondition = true;
        }
        if (jobFirm != null) {
            query += (containsCondition ? " and" : " where") + " firm = ?";
            args.add(jobFirm);
        }
        query += " order by distance asc";
        try {
            return new GetAllJobsTask(jobDao).execute(new SimpleSQLiteQuery(query, args.toArray())).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void insert(Job job) {
        new InsertJobTask(jobDao).execute(job);
    }

    public void delete(int jobId) {
        new DeleteJobTask(jobDao).execute(jobId);
    }

    public int getColorByJobType(String jobType) {
        Job job;
        try {
            job = new GetJobByJobTypeTask(jobDao).execute(jobType).get();
        } catch (Exception e) {
            return 0;
        }
        return Color.rgb(job.getColor1(), job.getColor2(), job.getColor3());
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

    private static class DeleteJobTask extends AsyncTask<Integer, Void, Void> {

        private JobDao jobDao;

        DeleteJobTask(JobDao jobDao) {
            this.jobDao = jobDao;
        }

        @Override
        protected Void doInBackground(Integer... jobIds) {
            jobDao.deleteJob(jobIds[0]);
            return null;
        }
    }

    private static class GetJobByJobTypeTask extends AsyncTask<String, Void, Job> {

        private JobDao jobDao;

        GetJobByJobTypeTask(JobDao jobDao) {
            this.jobDao = jobDao;
        }

        @Override
        protected Job doInBackground(String... jobTypes) {
            return jobDao.getJobByJobType(jobTypes[0]);
        }
    }
}
