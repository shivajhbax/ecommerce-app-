package com.example.myshowroom;

public class ProductSpecificationModel {
    ////specification body
    private String  featureName;
    private String featureValue;

    public ProductSpecificationModel(String featureName, String featureValue, int type) {
        this.featureName = featureName;
        this.featureValue = featureValue;
        this.type = type;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }
    ////specification body

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    ////specification title
    private String title;

    public ProductSpecificationModel(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    ////specification title

    public static final int SPECIFICATION_TITLE=0;
    public static final int SPECIFICATION_BODY=1;
/*
    public ProductSpecificationModel(String featureName, String featureValue) {
        this.featureName = featureName;
        this.featureValue = featureValue;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

 */
}
