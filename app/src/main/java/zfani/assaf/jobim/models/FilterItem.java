package zfani.assaf.jobim.models;

public class FilterItem {

    private int color;
    private String title;

    public FilterItem(int color, String title) {
        this.color = color;
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }
}
