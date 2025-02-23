//
//  WeatherModel.swift
//  WeatherApplication
//
//  Created by ikhut21 on 22.02.25.
//

final class WeatherModel {
    
    var location: String
    var temperature: Double
    var description: String
    var cloudiness: Int
    var humidity: Int
    var windSpeed: Double
    var windDirection: String = ""
    var iconURL: String

    init(
        location: String,
        temperature: Double,
        description: String,
        cloudiness: Int,
        humidity: Int,
        windSpeed: Double,
        windDegree: Int,
        iconCode: String
    ) {
        self.location = location
        self.temperature = temperature
        self.description = description
        self.cloudiness = cloudiness
        self.humidity = humidity
        self.windSpeed = windSpeed
        self.iconURL = "https://openweathermap.org/img/wn/\(iconCode)@2x.png"
        self.windDirection = getWindDirection(degrees: windDegree)
    }

    static let empty = WeatherModel(
        location: "Unknown",
        temperature: 0.0,
        description: "No Data",
        cloudiness: 0,
        humidity: 0,
        windSpeed: 0.0,
        windDegree: 0,
        iconCode: ""
    )

    private func getWindDirection(degrees: Int) -> String {
        let directions = ["N", "NE", "E", "SE", "S", "SW", "W", "NW"]
        let index = Int(
            (Double(degrees) + 22.5).truncatingRemainder(dividingBy: 360) / 45.0
        )
        return directions[index]
    }
}

