package com.gandalfran.datospersonalist.bean;

import java.io.Serializable;
import java.util.Objects;

public class UnaPersona implements Serializable {

    private String name;
    private String surname;
    private String age;
    private String date;
    private String phone;
    private String englishLevel;
    private boolean drivingLicense;


    public UnaPersona(String name, String surname, String age, String date, String phone, String englishLevel, boolean drivingLicense) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.date = date;
        this.phone = phone;
        this.englishLevel = englishLevel;
        this.drivingLicense = drivingLicense;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(name).append(" ").append(surname)
                .append("\n").append("Edad: ").append(age)
                .append(" ").append("PC: ").append(this.drivingLicense ? "SI" : "NO")
                .append(" ").append("Inglés: ").append(englishLevel)
                .append(" ").append("Teléfono: ").append(phone)
                .append(" ").append("INGRESO: ").append(date);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaPersona that = (UnaPersona) o;
        return drivingLicense == that.drivingLicense &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(age, that.age) &&
                Objects.equals(date, that.date) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(englishLevel, that.englishLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age, date, phone, englishLevel, drivingLicense);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }

    public boolean isDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(boolean drivingLicense) {
        this.drivingLicense = drivingLicense;
    }
}
