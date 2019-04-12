package zfani.assaf.jobim.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShowByBottomSheetViewModel extends ViewModel {

    private MutableLiveData<String> jobTypeQuery, jobLocationQuery,jobLocationText, jobFirmQuery;

    public ShowByBottomSheetViewModel() {
        this.jobTypeQuery = new MutableLiveData<>();
        this.jobLocationQuery = new MutableLiveData<>();
        this.jobLocationText = new MutableLiveData<>();
        this.jobFirmQuery = new MutableLiveData<>();
    }

    public MutableLiveData<String> getJobTypeQuery() {
        return jobTypeQuery;
    }

    public void setJobTypeQuery(String jobTypeQuery) {
        this.jobTypeQuery.setValue(jobTypeQuery);
    }

    public MutableLiveData<String> getJobLocationQuery() {
        return jobLocationQuery;
    }

    public void setJobLocationQuery(String jobLocationQuery) {
        this.jobLocationQuery.setValue(jobLocationQuery);
    }

    public MutableLiveData<String> getJobLocationText() {
        return jobLocationText;
    }

    public void setJobLocationText(String jobLocationText) {
        this.jobLocationText.setValue(jobLocationText);
    }

    public MutableLiveData<String> getJobFirmQuery() {
        return jobFirmQuery;
    }

    public void setJobFirmQuery(String jobFirmQuery) {
        this.jobFirmQuery.setValue(jobFirmQuery);
    }
}
