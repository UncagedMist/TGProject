package tbc.techbytecare.kk.touristguideproject.Model;

public class BudgetPlaceDetails {

    private String placeName;
    private String tripDuration;
    private String visitTime;
    private String videoLink;
    private String weatherWinter;
    private String weatherSummer;
    private String visitPlace1;
    private String visitPlace2;
    private String visitPlace3;
    private String visitPlace4;
    private String visitPlace5;
    private String packagePlace;
    private String district;
    private String state;

    public BudgetPlaceDetails() {
    }

    public BudgetPlaceDetails(String placeName, String tripDuration, String visitTime, String videoLink, String weatherWinter, String weatherSummer, String visitPlace1, String visitPlace2, String visitPlace3, String visitPlace4, String visitPlace5, String packagePlace, String district, String state) {
        this.placeName = placeName;
        this.tripDuration = tripDuration;
        this.visitTime = visitTime;
        this.videoLink = videoLink;
        this.weatherWinter = weatherWinter;
        this.weatherSummer = weatherSummer;
        this.visitPlace1 = visitPlace1;
        this.visitPlace2 = visitPlace2;
        this.visitPlace3 = visitPlace3;
        this.visitPlace4 = visitPlace4;
        this.visitPlace5 = visitPlace5;
        this.packagePlace = packagePlace;
        this.district = district;
        this.state = state;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getWeatherWinter() {
        return weatherWinter;
    }

    public void setWeatherWinter(String weatherWinter) {
        this.weatherWinter = weatherWinter;
    }

    public String getWeatherSummer() {
        return weatherSummer;
    }

    public void setWeatherSummer(String weatherSummer) {
        this.weatherSummer = weatherSummer;
    }

    public String getVisitPlace1() {
        return visitPlace1;
    }

    public void setVisitPlace1(String visitPlace1) {
        this.visitPlace1 = visitPlace1;
    }

    public String getVisitPlace2() {
        return visitPlace2;
    }

    public void setVisitPlace2(String visitPlace2) {
        this.visitPlace2 = visitPlace2;
    }

    public String getVisitPlace3() {
        return visitPlace3;
    }

    public void setVisitPlace3(String visitPlace3) {
        this.visitPlace3 = visitPlace3;
    }

    public String getVisitPlace4() {
        return visitPlace4;
    }

    public void setVisitPlace4(String visitPlace4) {
        this.visitPlace4 = visitPlace4;
    }

    public String getVisitPlace5() {
        return visitPlace5;
    }

    public void setVisitPlace5(String visitPlace5) {
        this.visitPlace5 = visitPlace5;
    }

    public String getPackagePlace() {
        return packagePlace;
    }

    public void setPackagePlace(String packagePlace) {
        this.packagePlace = packagePlace;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}