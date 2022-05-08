package com.example.tooset_test02;

public class ReviewModel {

    String title;
    String good_review;
    String bad_review;
    String now_date;

    String reviewUserName;

    ReviewModel() {

    }

    public ReviewModel(String title, String good_review, String bad_review, String reviewUserName, String now_date) {
        this.title = title;
        this.good_review = good_review;
        this.bad_review = bad_review;
        this.reviewUserName = reviewUserName;
        this.now_date = now_date;
        //this.reviewUrl = reviewUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGood_review() {
        return good_review;
    }

    public void setGood_review(String good_review) {
        this.good_review = good_review;
    }

    public String getBad_review() {
        return bad_review;
    }

    public void setBad_review(String bad_review) {
        this.bad_review = bad_review;
    }

    public String getReviewUserName() {
        return reviewUserName;
    }

    public void setReviewUserName(String reviewUserName) {
        this.reviewUserName = reviewUserName;
    }

    public String getNow_date() {
        return now_date;
    }

    public void setNow_date(String now_date) {
        this.now_date = now_date;
    }



}
