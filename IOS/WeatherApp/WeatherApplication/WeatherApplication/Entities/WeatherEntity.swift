import Foundation

struct WeatherResponse: Codable {
    let weather: [Weather]
    let main: MainTemperatures
    let wind: Wind
    let clouds: Clouds
    let sys: Sys    
}

struct Sys: Codable {
    let country: String
}

struct Weather: Codable {
    let main: String
    let description: String
    let icon: String
}

struct MainTemperatures: Codable {
    let temp: Double
    let humidity: Int
}

struct Wind: Codable {
    let speed: Double
    let deg: Int
}

struct Clouds: Codable {
    let percentage: Double
    
    enum CodingKeys: String, CodingKey {
        case percentage = "all"
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        percentage = try container.decode(Double.self, forKey: .percentage)
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(percentage, forKey: .percentage)
    }
}
