package csis3175.w23.g11.rooftown.posts.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "POSTS")
public class Post {
    @PrimaryKey
    @NonNull
    private UUID postId;
    @ColumnInfo(name = "post_type")
    private PostType postType;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "lat_long")
    private LatLng latLong;
    @ColumnInfo(name = "num_of_rooms")
    private String numOfRooms;
    @ColumnInfo(name = "furnished")
    private boolean furnished;
    @ColumnInfo(name = "shared_bathroom")
    private boolean sharedBathroom;
    @ColumnInfo(name = "room_description")
    private String roomDescription;
    @ColumnInfo(name = "room_image")
    private String roomImage;
    @ColumnInfo(name = "initiator")
    private String initiator;
    @ColumnInfo(name = "initiator_name")
    private String initiatorName;
    @ColumnInfo(name = "initiator_gender")
    private String initiatorGender;
    @ColumnInfo(name = "initiator_age")
    private String initiatorAge;
    @ColumnInfo(name = "initiator_description")
    private String initiatorDescription;
    @ColumnInfo(name = "initiator_image")
    private String initiatorImage;
    @ColumnInfo(name = "post_status")
    private PostStatus postStatus;
    @ColumnInfo(name = "post_at")
    private Date postAt;

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
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

    public LatLng getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLng latLong) {
        this.latLong = latLong;
    }

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

    public PostStatus getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    public Date getPostAt() {
        return postAt;
    }

    public void setPostAt(Date postAt) {
        this.postAt = postAt;
    }

    public String getDisplayTitle() {
        return this.postType == PostType.ROOM ? this.location : this.getInitiatorName();
    }

    public String getDisplayImage() {
        return this.postType == PostType.ROOM ? this.roomImage : this.initiatorImage;
    }
}
