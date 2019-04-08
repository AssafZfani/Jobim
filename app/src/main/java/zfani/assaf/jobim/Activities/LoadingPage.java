package zfani.assaf.jobim.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import zfani.assaf.jobim.R;

public class LoadingPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.loadingpage);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                startActivity(new Intent(LoadingPage.this, HomePage.class));

                finish();

            }
        }, 500);
    }
}