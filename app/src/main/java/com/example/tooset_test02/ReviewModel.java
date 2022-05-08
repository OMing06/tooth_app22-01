package com.example.tooset_test02;

public class ReviewModel {

    private String title;
    private String good_review;
    private String bad_review;
    private String now_date;
    private String imageUrl;
    private String reviewUserName;

    public ReviewModel() {

    }

    public ReviewModel(String title, String good_review, String bad_review,
                       String reviewUserName, String now_date, String imageUrl) {
        this.title = title;
        this.good_review = good_review;
        this.bad_review = bad_review;
        this.reviewUserName = reviewUserName;
        this.now_date = now_date;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



}
