package com.opdl.api.stats.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "city")
    private String city;

    @Column(name = "state_code")
    private String stateCode = "FL";

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country_code")
    private String countryCode = "US";

    public Address() {
    }

    public Address(String addressLine, String city, String stateCode, String zipCode, String countryCode) {
        this.addressLine = addressLine;
        this.city = city;
        this.stateCode = stateCode;
        this.zipCode = zipCode;
        this.countryCode = countryCode;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(addressLine, address.addressLine) && Objects.equals(city, address.city) && Objects.equals(stateCode, address.stateCode) && Objects.equals(zipCode, address.zipCode) && Objects.equals(countryCode, address.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressLine, city, stateCode, zipCode, countryCode);
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressLine='" + addressLine + '\'' +
                ", city='" + city + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
