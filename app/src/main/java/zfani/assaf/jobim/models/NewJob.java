package zfani.assaf.jobim.models;

import android.app.Activity;

import zfani.assaf.jobim.utils.GPSTracker;

public class NewJob extends Job {

    private String branchName, description, email, phoneNumber, question, smsNumber;
    private boolean answer, fitForTeens;

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public String getBranchName() {

        return branchName;
    }

    public void setBranchName(String branchName) {

        this.branchName = branchName;
    }

    public void setBusinessNumber(int businessNumber) {
        this.businessNumber = businessNumber;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDistance(Activity activity) {

        this.distance = GPSTracker.getDistanceFromAddress(activity.getApplication(), this.address);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirm(String firm) {

        this.firm = firm;
    }

    boolean isFitForTeens() {
        return fitForTeens;
    }

    public void setFitForTeens(boolean fitForTeens) {
        this.fitForTeens = fitForTeens;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public void setTitle(String title) {

        this.title = title;
    }
}
