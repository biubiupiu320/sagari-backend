package com.sagari.service.entity;

import java.time.LocalDate;

/**
 * @author biubiupiu~
 */
public class User {

    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private Integer articleCount;
    private Integer followCount;
    private Integer fansCount;
    private String gender;
    private LocalDate birth;
    private String office;
    private String company;
    private String education;
    private String school;
    private String profession;
    private String introduction;
    private String qqId;
    private String baiduId;
    private String githubId;
    private Long createTime;
    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public Integer getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getBaiduId() {
        return baiduId;
    }

    public void setBaiduId(String baiduId) {
        this.baiduId = baiduId;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", avatar='").append(avatar).append('\'');
        sb.append(", articleCount=").append(articleCount);
        sb.append(", followCount=").append(followCount);
        sb.append(", fansCount=").append(fansCount);
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", birth=").append(birth);
        sb.append(", office='").append(office).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", education='").append(education).append('\'');
        sb.append(", school='").append(school).append('\'');
        sb.append(", profession='").append(profession).append('\'');
        sb.append(", introduction='").append(introduction).append('\'');
        sb.append(", qqId='").append(qqId).append('\'');
        sb.append(", baiduId='").append(baiduId).append('\'');
        sb.append(", githubId='").append(githubId).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
