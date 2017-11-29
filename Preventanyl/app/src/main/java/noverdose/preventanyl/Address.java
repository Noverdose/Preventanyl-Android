package noverdose.preventanyl;

/**
 * Created by yudhvirraj on 2017-11-28.
 */

public class Address {
    private String city;
    private String country;
    private String postalCode;
    private String provinceState;
    private String streetAddress;

    public Address (String city, String country, String postalCode, String provinceState, String streetAddress) {
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.provinceState = provinceState;
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

}
