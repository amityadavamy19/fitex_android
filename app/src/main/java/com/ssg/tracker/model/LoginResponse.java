package com.ssg.tracker.model;

public class LoginResponse {


    private String userId;
    private String app_id;
    private String email;
    private String password;
    private String name;
    private String mobile;
    private String roleId;
    private String isDeleted;
    private String createdBy;
    private String createdDtm;
    private String updatedBy = null;
    private String updatedDtm;
    private String age;
    private String gender;
    private String weight;
    private String height;
    private String otp;
    private String pic;
    private String channel;
    private String provider;
    private String username;
    private String status;
    private String location;
    private String email_verified;
    private String mobile_verified;


    // Getter Methods

    public String getUserId() {
        return userId;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDtm() {
        return createdDtm;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getUpdatedDtm() {
        return updatedDtm;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getOtp() {
        return otp;
    }

    public String getPic() {
        return pic;
    }

    public String getChannel() {
        return channel;
    }

    public String getProvider() {
        return provider;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public String getMobile_verified() {
        return mobile_verified;
    }

    // Setter Methods

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDtm(String createdDtm) {
        this.createdDtm = createdDtm;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDtm(String updatedDtm) {
        this.updatedDtm = updatedDtm;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public void setMobile_verified(String mobile_verified) {
        this.mobile_verified = mobile_verified;
    }

    @Override
    public String toString() {
        return "LoginResponse [updatedDtm = " + updatedDtm + ", gender = " + gender + ", roleId = " + roleId + ", mobile = " + mobile + ", channel = " + channel + ", weight = " + weight + ", otp = " + otp + ", pic = " + pic + ", userId = " + userId + ", password = " + password + ", isDeleted = " + isDeleted + ", createdBy = " + createdBy + ", createdDtm = " + createdDtm + ", name = " + name + ", location = " + location + ", email = " + email + ", age = " + age + ", height = " + height + ", username = " + username + ", status = " + status + "]";
    }
}
