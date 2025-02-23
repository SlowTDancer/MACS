import Foundation

class WeatherService {
    private let apiKey = "310e7f87bfa11a7fbef3a542246abde6"
    private var components = URLComponents()
    
    init() {
        components.scheme = "https"
        components.host = "api.openweathermap.org"
    }
    
    func loadWeather(
        for city: String,
        country: String,
        units: Units,
        completion: @escaping (Result<WeatherResponse, Error>) -> Void
    ) {
        components.path = "/data/2.5/weather"
        
        let parameters = [
            "q": "\(city),\(country)",
            "appid": apiKey,
            "units": units.rawValue
        ]
        
        performRequest(
            with: parameters,
            responseType: WeatherResponse.self,
            completion: completion
        )
    }
    
    func loadForecast(
        for city: String,
        country: String,
        units: Units,
        completion: @escaping (Result<ForecastResponse, Error>) -> Void
    ) {
        components.path = "/data/2.5/forecast"
        
        let parameters = [
            "q": "\(city),\(country)",
            "appid": apiKey,
            "units": units.rawValue
        ]
        
        performRequest(
            with: parameters,
            responseType: ForecastResponse.self,
            completion: completion
        )
    }
    
    private func performRequest<T: Decodable>(
        with parameters: [String: String],
        responseType: T.Type,
        completion: @escaping (Result<T, Error>) -> Void
    ) {
        components.queryItems = parameters.map {
            URLQueryItem(name: $0.key, value: $0.value)
        }
        
        guard let url = components.url else {
            completion(
                .failure(
                    ServiceError.invalidParameters(
                        details: "Invalid weather request parameters"
                    )
                )
            )
            return
        }
        
        let request = URLRequest(url: url)
        let task = URLSession.shared.dataTask(
            with: request
        ) { data, response, error in
            if let error = error {
                completion(
                    .failure(
                        ServiceError.sessionErrorOccurred(
                            reason: "Weather service connection error"
                        )
                    )
                )
                return
            }
            
            guard let data = data else {
                completion(.failure(ServiceError.noData))
                return
            }
            
            do {
                let result = try JSONDecoder().decode(T.self, from: data)
                completion(.success(result))
            } catch {
                completion(
                    .failure(
                        ServiceError.decoderError(
                            details: "Unable to parse weather data"
                        )
                    )
                )
            }
        }
        
        task.resume()
    }
}
