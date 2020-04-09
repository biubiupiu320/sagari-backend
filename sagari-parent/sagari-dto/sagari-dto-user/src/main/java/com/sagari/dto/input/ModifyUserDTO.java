package com.sagari.dto.input;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @author biubiupiu~
 */
public class ModifyUserDTO {

    @Size(max = 10, message = "用户名不能超过10个字符")
    private String username;
    @Size(max = 1, message = "性别必须为男或女")
    private String gender;
    @Past(message = "生日必须是过去的一个时间")
    private LocalDate birth;
    @Size(max = 20, message = "职位最多20个字")
    private String office;
    @Size(max = 50, message = "公司名称最多50个字")
    private String company;
    @Size(max = 10, message = "学历最多10个字")
    private String education;
    @Size(max = 15, message = "学校最多15个字")
    private String school;
    @Size(max = 15, message = "专业最多15个字")
    private String profession;
    @Size(max = 300, message = "个人简介最多300个字符")
    private String introduction;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModifyUserDTO{");
        sb.append(", username='").append(username).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", birth=").append(birth);
        sb.append(", office='").append(office).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", education='").append(education).append('\'');
        sb.append(", school='").append(school).append('\'');
        sb.append(", profession='").append(profession).append('\'');
        sb.append(", introduction='").append(introduction).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
