package zfani.assaf.jobim.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import zfani.assaf.jobim.adapters.JobsAdapter;
import zfani.assaf.jobim.utils.FilteredAdapter;

@SuppressWarnings("unused")
public class Job implements Parcelable, Comparable<Job> {

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

    protected Job(Parcel in) {
        address = in.readString();
        firm = in.readString();
        id = in.readString();
        title = in.readString();
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
        return Integer.compare(businessNumber1, businessNumber2);
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
        dest.writeInt(businessNumber);
        dest.writeInt(distance);
        dest.writeByte((byte) (applied ? 1 : 0));
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeByte((byte) (posted ? 1 : 0));
    }
}