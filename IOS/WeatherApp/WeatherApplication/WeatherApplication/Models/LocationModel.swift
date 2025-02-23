//
//  LocationModel.swift
//  WeatherApplication
//
//  Created by ikhut21 on 23.02.25.
//

final class LocationModel {
    
    var city: String
    var countryCode: String
    var location: Location?
    
    init(
        city: String,
        countryCode: String,
        location: Location? = nil
    ) {
        self.city = city
        self.countryCode = countryCode
        self.location = location
    }
    
}
