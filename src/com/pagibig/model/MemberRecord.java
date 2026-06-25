package com.pagibig.model;

public class MemberRecord {
    // Existing variables
    private String pagibigId;
    private String memName;
    private String memType;
    private String sex;
    private String birthDate;
    private String citizenship;

    // NEW variables added by you
    private String regisNum;
    private String occupationStatus;
    private String firstTime;
    private String memSubtype;
    private String typeWork;
    private String typeCountry;
    private String fatName;
    private String motName;
    private String spouseName;
    private String memCertName;
    private String placeOfBirth;
    private String height;
    private String weight;
    private String maritalStatus;
    private String facialFeatures;
    private String frequencyOfPayment;

    public MemberRecord(String pagibigId, String regisNum, String occupationStatus, String firstTime, 
                        String memType, String memSubtype, String typeWork, String typeCountry, 
                        String memName, String fatName, String motName, String spouseName, 
                        String memCertName, String birthDate, String placeOfBirth, String sex, 
                        String height, String weight, String maritalStatus, String citizenship, 
                        String facialFeatures, String frequencyOfPayment) {
        this.pagibigId = pagibigId;
        this.regisNum = regisNum;
        this.occupationStatus = occupationStatus;
        this.firstTime = firstTime;
        this.memType = memType;
        this.memSubtype = memSubtype;
        this.typeWork = typeWork;
        this.typeCountry = typeCountry;
        this.memName = memName;
        this.fatName = fatName;
        this.motName = motName;
        this.spouseName = spouseName;
        this.memCertName = memCertName;
        this.birthDate = birthDate;
        this.placeOfBirth = placeOfBirth;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.maritalStatus = maritalStatus;
        this.citizenship = citizenship;
        this.facialFeatures = facialFeatures;
        this.frequencyOfPayment = frequencyOfPayment;
    }

    // Getters and Setters
    public String getPagibigId() { return pagibigId; }
    public void setPagibigId(String pagibigId) { this.pagibigId = pagibigId; }

    public String getRegisNum() { return regisNum; }
    public String getOccupationStatus() { return occupationStatus; }
    public String getFirstTime() { return firstTime; }
    public String getMemType() { return memType; }
    public String getMemSubtype() { return memSubtype; }
    public String getTypeWork() { return typeWork; }
    public String getTypeCountry() { return typeCountry; }
    public String getMemName() { return memName; }
    public String getFatName() { return fatName; }
    public String getMotName() { return motName; }
    public String getSpouseName() { return spouseName; }
    public String getMemCertName() { return memCertName; }
    public String getBirthDate() { return birthDate; }
    public String getPlaceOfBirth() { return placeOfBirth; }
    public String getSex() { return sex; }
    public String getHeight() { return height; }
    public String getWeight() { return weight; }
    public String getMaritalStatus() { return maritalStatus; }
    public String getCitizenship() { return citizenship; }
    public String getFacialFeatures() { return facialFeatures; }
    public String getFrequencyOfPayment() { return frequencyOfPayment; }
}