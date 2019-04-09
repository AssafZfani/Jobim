package zfani.assaf.jobim.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class Job implements Parcelable, Comparable<Job> {

     ArrayList<Integer> color;
     String address, firm, id, title, type;
     int businessNumber, distance;
     boolean applied, favorite, posted;

    Job() {

    }

    public Job(String address, boolean applied, int businessNumber, ArrayList<Integer> color, int distance,
               boolean favorite, String firm, String id, boolean posted, String title, String type) {
        this.address = address;
        this.applied = applied;
        this.businessNumber = businessNumber;
        this.color = color;
        this.distance = distance;
        this.favorite = favorite;
        this.firm = firm;
        this.id = id;
        this.posted = posted;
        this.title = title;
        this.type = type;
    }

    protected Job(Parcel in) {
        address = in.readString();
        firm = in.readString();
        id = in.readString();
        title = in.readString();
        type = in.readString();
        businessNumber = in.readInt();
        distance = in.readInt();
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

    public ArrayList<Integer> getColor() {
        return color;
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
        dest.writeString(address);
        dest.writeString(firm);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeInt(businessNumber);
        dest.writeInt(distance);
        dest.writeByte((byte) (applied ? 1 : 0));
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeByte((byte) (posted ? 1 : 0));
    }
}