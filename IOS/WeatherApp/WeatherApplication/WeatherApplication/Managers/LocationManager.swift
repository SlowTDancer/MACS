import CoreLocation

class LocationManager: NSObject {
    
    private let locationManager = CLLocationManager()
    var onLocationUpdate: ((CLLocation) -> Void)?
    var onError: ((Error) -> Void)?
    var onAuthorizationStatusChange: ((CLAuthorizationStatus) -> Void)?
    
    override init() {
        super.init()
        setupLocationManager()
    }
    
    private func setupLocationManager() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.distanceFilter = 10
    }
    
    func requestLocation() {
        guard CLLocationManager.locationServicesEnabled() else {
            let error = NSError(
                domain: "LocationManagerError",
                code: 1,
                userInfo: [NSLocalizedDescriptionKey: "Location services are disabled"]
            )
            onError?(error)
            return
        }
        
        let authStatus = locationManager.authorizationStatus
        
        switch authStatus {
        case .notDetermined:
            locationManager.requestWhenInUseAuthorization()
        case .authorizedWhenInUse, .authorizedAlways:
            locationManager.requestLocation()
        case .restricted, .denied:
            let error = NSError(
                domain: "LocationManagerError",
                code: 2,
                userInfo: [NSLocalizedDescriptionKey: "Location access denied"]
            )
            onError?(error)
        @unknown default:
            let error = NSError(
                domain: "LocationManagerError",
                code: 3,
                userInfo: [NSLocalizedDescriptionKey: "Unknown authorization status"]
            )
            onError?(error)
        }
    }
    
    func requestAuthorization() {
        locationManager.requestWhenInUseAuthorization()
    }
      
}

extension LocationManager: CLLocationManagerDelegate {

    func locationManager(
        _ manager: CLLocationManager,
        didUpdateLocations locations: [CLLocation]
    ) {
        guard let location = locations.last else { return }
        onLocationUpdate?(location)
    }
    
    func locationManager(
        _ manager: CLLocationManager,
        didFailWithError error: Error
    ) {
        onError?(error)
    }
    
    func locationManager(
        _ manager: CLLocationManager,
        didChangeAuthorization status: CLAuthorizationStatus
    ) {
        onAuthorizationStatusChange?(status)
        
        switch status {
        case .authorizedWhenInUse, .authorizedAlways:
            locationManager.requestLocation()
        default:
            break
        }
    }

}
