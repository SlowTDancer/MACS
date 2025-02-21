//
//  ColorHelper.swift
//  Notes
//
//  Created by ikhut21 on 20.02.25.
//

import UIKit

class ColorHelper {
    
    public static func getRandomColor() -> UIColor {
        let colors: [UIColor] = [
            UIColor(red: 1.0, green: 0.71, blue: 0.76, alpha: 1.0),
            UIColor(red: 0.68, green: 0.85, blue: 0.90, alpha: 1.0),
            UIColor(red: 1.0, green: 0.94, blue: 0.59, alpha: 1.0),
            UIColor(red: 0.56, green: 0.93, blue: 0.56, alpha: 1.0) 
        ]
        
        return colors.randomElement() ?? UIColor.white
    }
    
    public static func colorToData(_ color: UIColor) -> Data? {
        return try? NSKeyedArchiver.archivedData(
            withRootObject: color,
            requiringSecureCoding: false
        )
    }

    public static func dataToColor(_ data: Data?) -> UIColor {
        guard let data = data else { return .black }
        return (try? NSKeyedUnarchiver.unarchivedObject(
            ofClass: UIColor.self,
            from: data)
        ) ?? .black
    }
    
}
