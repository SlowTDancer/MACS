//
//  ForecastEntity.swift
//  WeatherApplication
//
//  Created by ikhut21 on 22.02.25.
//

import Foundation


struct ForecastResponse: Codable {
    let list: [DailyWeather]
}

struct DailyWeather: Codable {
    let weather: [Weather]
    let main: MainTemperatures
    let dtTxt: String
    
    enum CodingKeys: String, CodingKey {
        case weather
        case main
        case dtTxt = "dt_txt"
    }
}
