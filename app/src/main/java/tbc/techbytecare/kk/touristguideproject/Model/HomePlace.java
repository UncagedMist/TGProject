package tbc.techbytecare.kk.touristguideproject.Model;

public class HomePlace {

    private String placeName;
    private String placeImage;
    private String placeDescription;

    public HomePlace() {
    }

    public HomePlace(String placeName, String placeImage, String placeDescription) {
        this.placeName = placeName;
        this.placeImage = placeImage;
        this.placeDescription = placeDescription;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }
}
