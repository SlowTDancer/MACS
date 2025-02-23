//
//  WeatherIconView.swift
//  WeatherApplication
//
//  Created by ikhut21 on 23.02.25.
//

import UIKit
import Kingfisher

class WeatherIconView: UIImageView {
    
    func setIcon(from urlString: String) {
        guard let url = URL(string: urlString) else {
            self.image = UIImage(systemName: "cloud")
            return
        }
        
        self.kf.setImage(
            with: url,
            placeholder: UIImage(systemName: "cloud"),
            options: [
                .transition(.fade(0.3)),
                .cacheOriginalImage
            ]
        )
    }
}
