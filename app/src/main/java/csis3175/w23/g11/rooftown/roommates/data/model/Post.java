package csis3175.w23.g11.rooftown.roommates.data.model;

import com.google.android.gms.maps.model.LatLng;

public class Post {
    private String title;
    private LatLng coordinates;
    private String PostDescription;
    private int PostPic;

    public Post(String title, LatLng coordinates, String postDescription, int postPic) {
        this.title = title;
        this.coordinates = coordinates;
        PostDescription = postDescription;
        PostPic = postPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getPostDescription() {
        return PostDescription;
    }

    public void setPostDescription(String postDescription) {
        PostDescription = postDescription;
    }

    public int getPostPic() {
        return PostPic;
    }

    public void setPostPic(int postPic) {
        PostPic = postPic;
    }
}
