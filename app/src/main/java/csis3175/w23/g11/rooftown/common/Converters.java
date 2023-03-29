package csis3175.w23.g11.rooftown.common;

import androidx.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.UUID;

public class Converters {
    @TypeConverter
    public static Date toDate(Long dateLong) {
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static UUID toUUID(String uuidString) {
        return uuidString == null ? null : UUID.fromString(uuidString);
    }

    @TypeConverter
    public static String fromUUID(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }


    @TypeConverter
    public static String toLatLngString(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }

    @TypeConverter
    public static LatLng fromLatLngString(String latLng) {
        if (latLng == null) return null;
        try {
            String[] splitLatLng = latLng.split(",");
            if (splitLatLng.length != 2) return null;
            return new LatLng(Double.parseDouble(splitLatLng[0]), Double.parseDouble(splitLatLng[1]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
