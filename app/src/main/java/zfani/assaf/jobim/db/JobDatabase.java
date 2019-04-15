package zfani.assaf.jobim.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import zfani.assaf.jobim.interfaces.JobDao;
import zfani.assaf.jobim.models.Job;

@Database(entities = {Job.class}, version = 1, exportSchema = false)
public abstract class JobDatabase extends RoomDatabase {

    private static JobDatabase INSTANCE;

    public static JobDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, JobDatabase.class, "jobs.db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public abstract JobDao getJobDAO();
}
