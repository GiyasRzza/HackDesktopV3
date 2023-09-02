package api.entity;


import java.time.LocalDateTime;

public class Company {
    private static String name;
    private static String password;
    private static String baseBoardNumber;
    private static String responsibleCode;
    private static long responsibleCodeId;


    public static long getResponsibleCodeId() {
        return responsibleCodeId;
    }

    public static void setResponsibleCodeId(long responsibleCodeId) {
        Company.responsibleCodeId = responsibleCodeId;
    }

    public  String getResponsibleCode() {
        return responsibleCode;
    }

    public void setResponsibleCode(String responsibleCode) {
        this.responsibleCode = responsibleCode;
    }

    private static final LocalDateTime createdDate = LocalDateTime.now();

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", baseBoardNumber='" + baseBoardNumber + '\'' +
                ", firstDate=" + createdDate +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Company.password = password;
    }

    public String getBaseBoardNumber() {
        return baseBoardNumber;
    }

    public void setBaseBoardNumber(String baseBoardNumber) {
        this.baseBoardNumber = baseBoardNumber;
    }

    public LocalDateTime getFirstDate() {
        return createdDate;
    }
}

