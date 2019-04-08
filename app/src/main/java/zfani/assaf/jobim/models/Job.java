package zfani.assaf.jobim.models;

import androidx.annotation.NonNull;

import zfani.assaf.jobim.utils.Adapter;
import zfani.assaf.jobim.utils.FilteredAdapter;

@SuppressWarnings("unused")
public class Job implements Comparable<Job> {

    protected String address, firm, id, title;
    int businessNumber, distance;
    private boolean applied, favorite, posted;

    Job() {

    }

    public Job(String address, boolean applied, int businessNumber, int distance,
               boolean favorite, String firm, String id, boolean posted, String title) {

        this.address = address;
        this.applied = applied;
        this.businessNumber = businessNumber;
        this.distance = distance;
        this.favorite = favorite;
        this.firm = firm;
        this.id = id;
        this.posted = posted;
        this.title = title;
    }

    public static Job findJobById(String jobId) {

        for (Job job : FilteredAdapter.filteredList == null ? Adapter.jobsList : FilteredAdapter.filteredList)
            if (job.getId().equalsIgnoreCase(jobId))
                return job;

        return null;
    }

    public String getAddress() {
        return address;
    }

    boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public int getBusinessNumber() {
        return businessNumber;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getFirm() {
        return firm;
    }

    public String getId() {
        return id;
    }

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(@NonNull Job job) {

        int businessNumber1 = this.getBusinessNumber();

        int businessNumber2 = job.getBusinessNumber();

        if (businessNumber1 > businessNumber2)
            return 1;

        if (businessNumber2 > businessNumber1)
            return -1;

        return 0;
    }
}