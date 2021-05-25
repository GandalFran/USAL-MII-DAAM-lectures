package com.example.datospersonalistaalmacen.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class UnaPersona implements Serializable, Parcelable {

    public enum EnglishLevel {
        LOW, MEDIUM, HIGH
    }

    private String name;
    private String surname;
    private String age;
    private Date date;
    private String phone;
    private EnglishLevel englishLevel;
    private boolean drivingLicense;

    public UnaPersona(String name, String surname, String age, Date date, String phone, EnglishLevel englishLevel, boolean drivingLicense) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.date = date;
        this.phone = phone;
        this.englishLevel = englishLevel;
        this.drivingLicense = drivingLicense;
    }

    protected UnaPersona(Parcel in) {
        this.name = in.readString();
        this.surname = in.readString();
        this.age = in.readString();
        this.phone = in.readString();
        this.drivingLicense = in.readByte() != 0;
        this.englishLevel = EnglishLevel.valueOf(in.readString());
    }

    public static final Creator<UnaPersona> UNA_PERSONA_CREATOR = new Creator<UnaPersona>() {
        @Override
        public UnaPersona createFromParcel(Parcel in) {
            return new UnaPersona(in);
        }

        @Override
        public UnaPersona[] newArray(int size) {
            return new UnaPersona[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeString(this.age);
        dest.writeString(this.phone);
        dest.writeByte((byte) (this.drivingLicense ? 1 : 0));
        dest.writeString(this.englishLevel.name());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append(" ").append(surname)
                .append("\n").append("Age: ").append(age)
                .append(" ").append("PC: ").append(this.drivingLicense ? "YES" : "NO")
                .append(" ").append("English: ").append(englishLevel)
                .append(" ").append("Phone: ").append(phone)
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EnglishLevel getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(EnglishLevel englishLevel) {
        this.englishLevel = englishLevel;
    }

    public boolean isDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(boolean drivingLicense) {
        this.drivingLicense = drivingLicense;
    }
}
