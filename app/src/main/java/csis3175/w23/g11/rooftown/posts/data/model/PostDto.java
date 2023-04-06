package csis3175.w23.g11.rooftown.posts.data.model;

import java.util.Date;

public class PostDto {
    private String postType;
    private String location;
    private String city;
    private String country;
    private String postalCode;
    private String latLong;
    private String geohash;
    private String numOfRooms;
    private boolean furnished;
    private boolean sharedBathroom;
    private String roomDescription;
    private String roomImage;
    private String initiator;
    private String initiatorName;
    private String initiatorGender;
    private String initiatorAge;
    private String initiatorDescription;
    private String initiatorImage;
    private String postStatus;
    private Date postAt;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() { return postalCode; }

    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getGeohash() { return geohash; }

    public void setGeohash(String geohash) { this.geohash = geohash; }

    public String getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(String numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public boolean isSharedBathroom() {
        return sharedBathroom;
    }

    public void setSharedBathroom(boolean sharedBathroom) {
        this.sharedBathroom = sharedBathroom;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getInitiatorGender() {
        return initiatorGender;
    }

    public void setInitiatorGender(String initiatorGender) {
        this.initiatorGender = initiatorGender;
    }

    public String getInitiatorAge() {
        return initiatorAge;
    }

    public void setInitiatorAge(String initiatorAge) {
        this.initiatorAge = initiatorAge;
    }

    public String getInitiatorDescription() {
        return initiatorDescription;
    }

    public void setInitiatorDescription(String initiatorDescription) {
        this.initiatorDescription = initiatorDescription;
    }

    public String getInitiatorImage() {
        return initiatorImage;
    }

    public void setInitiatorImage(String initiatorImage) {
        this.initiatorImage = initiatorImage;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public Date getPostAt() {
        return postAt;
    }

    public void setPostAt(Date postAt) {
        this.postAt = postAt;
    }
}
