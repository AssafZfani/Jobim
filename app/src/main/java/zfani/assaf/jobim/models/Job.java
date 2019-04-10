package zfani.assaf.jobim.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class Job implements Parcelable, Comparable<Job> {

    private String id, type;
    String address, firm, title;
    int businessNumber, distance;
    private int color1, color2, color3;
    private boolean applied, favorite, posted;

    Job() {

    }

    public Job(String address, boolean applied, int businessNumber, int color1, int color2, int color3, int distance,
               boolean favorite, String firm, String id, boolean posted, String title, String type) {
        this.address = address;
        this.applied = applied;
        this.businessNumber = businessNumber;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.distance = distance;
        this.favorite = favorite;
        this.firm = firm;
        this.id = id;
        this.posted = posted;
        this.title = title;
        this.type = type;
    }

    protected Job(Parcel in) {
        id = in.readString();
        type = in.readString();
        address = in.readString();
        firm = in.readString();
        title = in.readString();
        businessNumber = in.readInt();
        distance = in.readInt();
        color1 = in.readInt();
        color2 = in.readInt();
        color3 = in.readInt();
        applied = in.readByte() != 0;
        favorite = in.readByte() != 0;
        posted = in.readByte() != 0;
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

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

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }

    public int getColor3() {
        return color3;
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

    public String getType() {
        return type;
    }

    @Override
    public int compareTo(@NonNull Job job) {
        return Integer.compare(getBusinessNumber(), job.getBusinessNumber());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeString(firm);
        dest.writeString(title);
        dest.writeInt(businessNumber);
        dest.writeInt(distance);
        dest.writeInt(color1);
        dest.writeInt(color2);
        dest.writeInt(color3);
        dest.writeByte((byte) (applied ? 1 : 0));
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeByte((byte) (posted ? 1 : 0));
    }
}