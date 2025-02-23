import Foundation

class LocationService {
    
    private var locationUrlComponents = URLComponents()
    
    init(){
        locationUrlComponents.scheme = "https"
        locationUrlComponents.host = "api.bigdatacloud.net"
        locationUrlComponents.path = "/data/reverse-geocode-client"
    }
    
    func loadLocation(
        for longitude: String,
        latitude: String,
        completion: @escaping (Result<LocationEntity, Error>) -> Void
    ) {
        let parameters = [
            "latitude": latitude,
            "longitude": longitude
        ]
        
        locationUrlComponents.queryItems = parameters.map {
            URLQueryItem(name: $0.key, value: $0.value)
        }
        
        guard let url = locationUrlComponents.url else {
            completion(
                .failure(
                    ServiceError.invalidParameters(
                        details: "Invalid location coordinates"
                    )
                )
            )
            return
        }
        
        let request = URLRequest(url: url)
        
        URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                completion(
                    .failure(
                        ServiceError.sessionErrorOccurred(
                            reason: "Connection error while fetching location")
                    )
                )
                return
            }
            
            guard let data = data else {
                completion(.failure(ServiceError.noData))
                return
            }
            
            do {
                let result = try JSONDecoder().decode(LocationEntity.self, from: data)
                completion(.success(result))
            } catch {
                completion(
                    .failure(
                        ServiceError.decoderError(
                            details: "Unable to parse location data"
                        )
                    )
                )
            }
        }.resume()
    }
}
