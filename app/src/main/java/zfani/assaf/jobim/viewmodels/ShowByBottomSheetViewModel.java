package zfani.assaf.jobim.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShowByBottomSheetViewModel extends ViewModel {

    private MutableLiveData<String> queryTextLive;

    public ShowByBottomSheetViewModel() {
        this.queryTextLive = new MutableLiveData<>();
    }

    public MutableLiveData<String> getQueryTextLive() {
        return queryTextLive;
    }

    public void setQueryTextLive(String queryTextLive) {
        this.queryTextLive.setValue(queryTextLive);
    }
}
