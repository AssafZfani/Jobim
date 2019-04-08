package zfani.assaf.jobim.views.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.R;

import static android.util.Patterns.EMAIL_ADDRESS;

public class MakingContact extends FragmentActivity {

    View[] layouts, images;
    CheckBox[] checkBoxes;
    TextView contactText;
    View phoneLayout, emailLayout;
    EditText email, phoneNumber, smsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.making_contact);

        MainActivity.setupToolBar(this);

        layouts = new View[3];

        layouts[0] = findViewById(R.id.layout1);
        layouts[1] = findViewById(R.id.layout2);
        layouts[2] = findViewById(R.id.layout3);

        checkBoxes = new CheckBox[3];

        checkBoxes[0] = findViewById(R.id.sendMessageCheckBox);
        checkBoxes[1] = findViewById(R.id.callCheckBox);
        checkBoxes[2] = findViewById(R.id.sendEmailCheckBox);

        images = new View[3];

        images[0] = findViewById(R.id.image1);
        images[1] = findViewById(R.id.image2);
        images[2] = findViewById(R.id.image3);

        contactText = findViewById(R.id.contactText);

        phoneLayout = findViewById(R.id.phoneLayout);
        emailLayout = findViewById(R.id.emailLayout);

        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        smsNumber = findViewById(R.id.smsNumber);
    }

    @Override
    protected void onStart() {

        super.onStart();

        checkBoxes[0].setText("הודעת" + " " + "SMS");
        checkBoxes[1].setText("טלפון");
        checkBoxes[2].setText("מייל");

        for (int i = 0; i < 3; i++) {

            checkBoxes[i].setTag(i);

            checkBoxes[i].setOnCheckedChangeListener((compoundButton, checked) -> {

                compoundButton.setTextColor(ContextCompat.getColor(MakingContact.this, checked ? R.color.orange : android.R.color.darker_gray));

                layouts[(int) compoundButton.getTag()].setBackgroundColor(checked ? Color.parseColor("#f1f1f1") : Color.WHITE);

                switch ((int) compoundButton.getTag()) {

                    case 0:
                        images[0].setBackgroundResource(checked ? R.drawable.send_message1 : R.drawable.send_message2);
                        break;
                    case 1:
                        images[1].setBackgroundResource(checked ? R.drawable.call1 : R.drawable.call2);
                        break;
                    default:
                        images[2].setBackgroundResource(checked ? R.drawable.send_email1 : R.drawable.send_email2);
                        break;
                }
            });

            checkBoxes[i].setChecked(true);

            layouts[i].setTag(i);

            layouts[i].setOnClickListener(view -> {

                boolean showPhoneNumber = false, showEmailLayout = false;

                switch ((int) view.getTag()) {

                    case 0:
                        contactText.setText("ההודעה תשלח למספר");
                        showPhoneNumber = false;
                        break;
                    case 1:
                        contactText.setText("השיחה תופנה למספר");
                        showPhoneNumber = true;
                        break;
                    default:
                        contactText.setText("המייל ישלח לכתובת");
                        showEmailLayout = true;
                        break;
                }

                phoneLayout.setVisibility(showEmailLayout ? View.GONE : View.VISIBLE);

                if (phoneLayout.getVisibility() == View.VISIBLE) {

                    phoneNumber.setVisibility(showPhoneNumber ? View.VISIBLE : View.GONE);

                    smsNumber.setVisibility(showPhoneNumber ? View.GONE : View.VISIBLE);
                }

                emailLayout.setVisibility(showEmailLayout ? View.VISIBLE : View.GONE);
            });

            layouts[0].performClick();
        }

        email.setText(Application.sharedPreferences.getString("Email", ""));

        String phone = Application.sharedPreferences.getString("PhoneNumber", "");

        phoneNumber.setText(phone);
        smsNumber.setText(phone);

        findViewById(R.id.backButton).setOnClickListener(view -> onBackPressed());
    }

    public void post(View v) {

        String emailText = email.getText().toString();
        String phoneNumberText = phoneNumber.getText().toString();
        String smsNumberText = smsNumber.getText().toString();

        if (emailText.isEmpty() || !EMAIL_ADDRESS.matcher(emailText).matches())
            Toast.makeText(this, "כתובת המייל אינה תקינה", Toast.LENGTH_SHORT).show();
        else if (phoneNumberText.isEmpty())
            Toast.makeText(this, "מספר הטלפון לשיחה אינו תקין", Toast.LENGTH_SHORT).show();
        else if (smsNumberText.isEmpty())
            Toast.makeText(this, "מספר הטלפון להודעת SMS אינו תקין", Toast.LENGTH_SHORT).show();
        else {

            AddNewJob.newJob.setEmail(emailText);
            AddNewJob.newJob.setPhoneNumber(phoneNumberText);
            AddNewJob.newJob.setSmsNumber(smsNumberText);

            setResult(1);

            MainActivity.displayDialog(this, R.layout.post_job_dialog, null);
        }
    }
}
