/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.util;

import com.rastrack.rastrackservices.entities.Tbcoordinates;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author usuario
 */
public class GeofenceUtil {

    public static final double PI = 3.14159265;
    public static final double TWOPI = 2 * PI;

    public static boolean coordinate_is_inside_polygon(
            double latitude, double longitude,
            ArrayList<Double> lat_array, ArrayList<Double> long_array) {
        int i;
        double angle = 0;
        double point1_lat;
        double point1_long;
        double point2_lat;
        double point2_long;
        int n = lat_array.size();

        for (i = 0; i < n; i++) {
            point1_lat = lat_array.get(i) - latitude;
            point1_long = long_array.get(i) - longitude;
            point2_lat = lat_array.get((i + 1) % n) - latitude;
            point2_long = long_array.get((i + 1) % n) - longitude;
            angle += Angle2D(point1_lat, point1_long, point2_lat, point2_long);
        }

        return Math.abs(angle) >= PI;
    }

   public static boolean coordinate_is_inside_polygon(
            double latitude, double longitude,
            List<Tbcoordinates> geofenceCoordinates) {
        int i;
        double angle = 0;
        double point1_lat;
        double point1_long;
        double point2_lat;
        double point2_long;
        int n = geofenceCoordinates.size();

        for (i = 0; i < n; i++) {
            Tbcoordinates coord = geofenceCoordinates.get(i);
            Tbcoordinates avgCoord = geofenceCoordinates.get((i + 1) % n);
            point1_lat = coord.getLatitude() - latitude;
            point1_long = coord.getLongitude() - longitude;
            point2_lat = avgCoord.getLatitude() - latitude;
            point2_long = avgCoord.getLongitude() - longitude;
            angle += Angle2D(point1_lat, point1_long, point2_lat, point2_long);
        }

        return Math.abs(angle) >= PI;
    }

    public static double Angle2D(double y1, double x1, double y2, double x2) {
        double dtheta, theta1, theta2;

        theta1 = Math.atan2(y1, x1);
        theta2 = Math.atan2(y2, x2);
        dtheta = theta2 - theta1;
        while (dtheta > PI) {
            dtheta -= TWOPI;
        }
        while (dtheta < -PI) {
            dtheta += TWOPI;
        }

        return (dtheta);
    }

    public static boolean is_valid_gps_coordinate(double latitude,
            double longitude) {
        //This is a bonus function, it's unused, to reject invalid lat/longs.
        
        return latitude > -90 && latitude < 90
                && longitude > -180 && longitude < 180;
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double distanceBetweenTwoPoints(double latitudeForPoint1, double latitudeForPoint2, double longitudeForPoint1,
            double longitudeForPoint2, double elevationForPoint1, double elevationForPoint2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latitudeForPoint2 - latitudeForPoint1);
        double lonDistance = Math.toRadians(longitudeForPoint2 - longitudeForPoint1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitudeForPoint1)) * Math.cos(Math.toRadians(latitudeForPoint2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = elevationForPoint1 - elevationForPoint2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
    
    
}
