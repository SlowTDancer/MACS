//
//  ForecastCellModel.swift
//  WeatherApplication
//
//  Created by ikhut21 on 23.02.25.
//

import Foundation

struct ForecastCellModel {
    let time: String
    let weather: String
    let temperature: String
    let iconCode: String
    
    var iconURL: URL? {
        return URL(string: "https://openweathermap.org/img/wn/\(iconCode)@2x.png")
    }
}

