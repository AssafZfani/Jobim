package zfani.assaf.jobim.models;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class JobType {

    private ArrayList<Integer> color;
    private int id;
    private String jobType;
    private boolean selected;

    JobType() {

    }

    public JobType(ArrayList<Integer> color, int id, String jobType) {
        this.color = color;
        this.id = id;
        this.jobType = jobType;
    }

    public ArrayList<Integer> getColor() {
        return color;
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
