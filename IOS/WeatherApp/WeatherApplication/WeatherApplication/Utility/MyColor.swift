//
//  Colors.swift
//  WeatherApplication
//
//  Created by ikhut21 on 23.02.25.
//

import UIKit

extension UIColor {
    
    static let customBlueStart = UIColor(named: "blue-gradient-start")!
    static let customGreenStart = UIColor(named: "green-gradient-start")!
    static let customOchreStart = UIColor(named: "ochre-gradient-start")!
    
    static let customBlueEnd = UIColor(named: "blue-gradient-end")!
    static let customGreenEnd = UIColor(named: "green-gradient-end")!
    static let customOchreEnd = UIColor(named: "ochre-gradient-end")!
    
}

enum GradientStyle {
    case blue
    case green
    case ochre
    
    var colors: (start: UIColor, end: UIColor) {
        switch self {
        case .blue:
            return (
                start: .customBlueStart,
                end: .customBlueEnd
            )
        case .green:
            return (
                start: .customGreenStart,
                end: .customGreenEnd
            )
        case .ochre:
            return (
                start: .customOchreStart,
                end: .customOchreEnd
            )
        }
    }
}

