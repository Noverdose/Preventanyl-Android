package noverdose.preventanyl

import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * Created by brayden on 2017-11-28.
 */
class Overdose (val region: String?, val reportedTime: Date, val coordinates: LatLng) {
    //
//  noverdose.preventanyl.Overdose.swift
//  Preventanyl
//
//  Created by Brayden Traas on 2017-11-28.
//  Copyright © 2017 Yudhvir Raj. All rights reserved.
//


    // data set...
    //     $data = ["region" => $region, "date"=>$date,"timestamp"=>$time,"latitude"=>$lat,"longitude"=>$long];

    val id: String


    companion object {
        fun getId(timestamp: Double, latitude: Double, longitude: Double): String {

            return "$timestamp $latitude $longitude".replace("",",")

        }
    }

    init {
        this.id = getId(reportedTime.time.toDouble(), coordinates.latitude, coordinates.longitude)
    }

//    private static func getId(timestamp: Double, latitude: Double, longitude: Double) -> String {
//        return "\(timestamp)_\(latitude)_\(longitude)".replacingOccurrences(of: ".", with: ",")
//    }

//    //used when getting data from firebase
//    init?(From snapshot: DataSnapshot) {
//        guard let dict = snapshot.value as? [String:Any] else { print("dict is nil")
//            return nil }
//
//        let region = dict["region"]  as? String ?? ""
//
//
//        guard let date = dict["date"] as? String else {
//            print("date is nil")
//            return nil }
//
//        /*
//
//         timestamp is nil
//         error! overdose from snapshot not parseable!
//         Snap (1511929609,8266_49,2503090091265_-123,001969296547) {
//         date = "2017-11-28";
//         id = "1511929609,8266_49,2503090091265_-123,001969296547";
//         latitude = "49.25030900912655";
//         longitude = "-123.0019692965467";
//         timestamp = "1511929609.826604";
//         }
//
//            */
//
//        guard let __timestamp = dict["timestamp"] else {
//            print("timestamp is nil")
//            return nil
//        }
//
//        let _timestamp = "\(__timestamp)"
//
//        guard let _latitude = dict["latitude"] else {
//            print("latitude is nil")
//            return nil }
//        guard let _longitude = dict["longitude"] else {
//
//
//            print("longitude is nil")
//
//
//            return nil }
//
//
//        guard let timestamp = Double(_timestamp) else {
//            print("timstamp unparseable!")
//            return nil
//        }
//
//        guard let latitude = Double("\(_latitude)"), let longitude = Double("\(_longitude)")  else {
//            print("lat/long unparseable!")
//            return nil
//        }
//
//
//
//
////        guard let timestamp = Double(_timestamp) else {
////            print("timestamp invalid")
////            return nil
////        }
//
//        self.region  = region
//        self.reportedTime = Date(timeIntervalSince1970: timestamp)
//        self.coordinates = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
//
//
//        self.id = dict["id"] as? String ?? noverdose.preventanyl.Overdose.getId(timestamp: timestamp, latitude: latitude, longitude: longitude)
//
//
////        self.coordinates.latitude = latitude
////        self.coordinates.longitude = longitude
//
//    }



}