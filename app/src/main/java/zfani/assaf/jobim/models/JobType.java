package zfani.assaf.jobim.models;

@SuppressWarnings("unused")
public class JobType {

    private int id;
    private String jobType;
    private boolean selected;

    JobType() {

    }

    public JobType(int id, String jobType) {
        this.id = id;
        this.jobType = jobType;
    }

    public int getId() {
        return id;
    }

    public String getJobType() {
        return jobType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Integer && ((Integer) obj) == id;
    }
}
