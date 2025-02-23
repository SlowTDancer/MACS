//
//  ForecastSection.swift
//  WeatherApplication
//
//  Created by ikhut21 on 23.02.25.
//

struct ForecastSection {
    var header: ForecastHeaderModel?
    var times: [ForecastCellModel]
    
    var numberOfRows: Int {
        return times.count
    }
}
