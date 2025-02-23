//
//  CurrentWeatherViewController.swift
//  WeatherApp
//
//  Created by ikhut21 on 19.02.25.
//

import UIKit
import CenteredCollectionView

class CurrentWeatherViewController: UIViewController {
    
    private let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Today"
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .white
        label.textAlignment = .center
        return label
    }()
    
    private lazy var plusButton: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(
            image: UIImage(systemName: "plus"),
            style: .plain,
            target: self,
            action: #selector(handlePlusTap)
        )
        buttonItem.tintColor = UIColor(
            named: "accent"
        )!
        return buttonItem
    }()
    
    private lazy var refreshButton: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(
            image: UIImage(systemName: "arrow.clockwise"),
            style: .plain,
            target: self,
            action: #selector(handleRefreshTap)
        )
        buttonItem.tintColor = UIColor(
            named: "accent"
        )!
        return buttonItem
    }()
    
    private let loader: UIActivityIndicatorView = {
        let loader = UIActivityIndicatorView(style: .large)
        loader.hidesWhenStopped = true
        loader.color = UIColor(named: "accent")
        loader.translatesAutoresizingMaskIntoConstraints = false
        return loader
    }()
    
    private let errorView: ErrorPageView = {
        let view = ErrorPageView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.isHidden = true
        return view
    }()
    
    private var centeredCollectionViewFlowLayout: CenteredCollectionViewFlowLayout!
    
    private lazy var collectionView: UICollectionView = {
        centeredCollectionViewFlowLayout = CenteredCollectionViewFlowLayout()
        let collection = UICollectionView(
            frame: .zero,
            collectionViewLayout: centeredCollectionViewFlowLayout
        )
        return collection
    }()
    
    private lazy var pageControl: UIPageControl = {
        let control = UIPageControl()
        control.currentPageIndicatorTintColor = .yellow
        control.pageIndicatorTintColor = .white.withAlphaComponent(0.5)
        control.translatesAutoresizingMaskIntoConstraints = false
        return control
    }()
    
    private let locationManager = LocationManager()
    private let locationService = LocationService()
    private let weatherService = WeatherService()
    private let dbHelper = DBHelper()
    
    private var locations: [LocationModel] = []
    private var weatherData: [WeatherModel] = [] {
        didSet {
            DispatchQueue.main.async {
                self.pageControl.numberOfPages = self.weatherData.count
                self.collectionView.reloadData()
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        addSampleData()
        setupLocationManager()
        getData()
        setUpView()
        addSubviews()
        setConstraints()
    }
    
    private func setUpView() {
        setUpGradientBackground()
        setUpNavBar()
        setUpCollectionView()
    }
    
    private func setUpGradientBackground() {
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = view.bounds
        
        let startColor = UIColor(
            named: "bg-gradient-start"
        )!.cgColor
        let endColor = UIColor(
            named: "bg-gradient-end"
        )!.cgColor
        
        gradientLayer.colors = [startColor, endColor]
        gradientLayer.locations = [0.0, 1.0]
        view.layer.insertSublayer(gradientLayer, at: 0)
    }
    
    private func setUpNavBar() {
        navigationItem.titleView = titleLabel
        navigationItem.rightBarButtonItem = plusButton
        navigationItem.leftBarButtonItem = refreshButton
    }
    
    private func setUpCollectionView() {
        collectionView.backgroundColor = .clear
        collectionView.delegate = self
        collectionView.dataSource = self
        
        collectionView.scrollsToTop = false
        
        centeredCollectionViewFlowLayout.itemSize = CGSize(
            width: view.bounds.width * cellPercentWidth,
            height: view.bounds.height * cellPercentHeight
        )
        
        collectionView.decelerationRate = .fast
        collectionView.contentInsetAdjustmentBehavior = .never
        collectionView.showsVerticalScrollIndicator = false
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        
        collectionView.register(
            UINib(nibName: "WeatherViewCell", bundle: .main),
            forCellWithReuseIdentifier: "WeatherCell"
        )
        
        collectionView.addGestureRecognizer(
            UILongPressGestureRecognizer(
                target: self,
                action: #selector(handleLongPress)
            )
        )
    }
    
    private func addSubviews() {
        view.addSubview(pageControl)
        view.addSubview(collectionView)
        view.addSubview(loader)
        view.addSubview(errorView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            pageControl.topAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.topAnchor,
                constant: 20
            ),
            pageControl.centerXAnchor.constraint(
                equalTo: view.centerXAnchor
            ),
            
            collectionView.topAnchor.constraint(
                equalTo: pageControl.bottomAnchor,
                constant: 30
            ),
            collectionView.leadingAnchor.constraint(
                equalTo: view.leadingAnchor
            ),
            collectionView.trailingAnchor.constraint(
                equalTo: view.trailingAnchor
            ),
            collectionView.heightAnchor.constraint(
                equalTo: view.heightAnchor,
                multiplier: 0.7
            ),
            
            loader.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            loader.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            
            errorView.topAnchor.constraint(equalTo: view.topAnchor),
            errorView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            errorView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            errorView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
    
    @objc private func handlePlusTap() {
        let popUpVC = AddPopUpWindow()
        popUpVC.delegate = self
        popUpVC.modalPresentationStyle = .overFullScreen
        popUpVC.modalTransitionStyle = .crossDissolve
        present(popUpVC, animated: true)
    }
    
    @objc private func handleRefreshTap() {
        if loader.isHidden {
            getData()
        }
    }
    
    @objc private func handleLongPress(_ gesture: UILongPressGestureRecognizer) {
        let location = gesture.location(in: collectionView)
        if let indexPath = collectionView.indexPathForItem(at: location) {
            deleteItem(at: indexPath)
        }
    }
}

extension CurrentWeatherViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        return weatherData.count
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: "WeatherCell",
            for: indexPath
        )
        
        var gradientStyle: GradientStyle
        switch indexPath.row % 3 {
        case 0:
            gradientStyle = .blue
        case 1:
            gradientStyle = .green
        default:
            gradientStyle = .ochre
        }
        
        if let weatherCell = cell as? WeatherViewCell {
            weatherCell.configure(
                with: getWeather(at: indexPath),
                style: gradientStyle
            )
        }

        return cell
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        didSelectItemAt indexPath: IndexPath
    ) {
        let currentCenteredPage = centeredCollectionViewFlowLayout.currentCenteredPage
        if currentCenteredPage != indexPath.row {
            centeredCollectionViewFlowLayout.scrollToPage(
                index: indexPath.row,
                animated: true
            )
        }
        let forecastViewController = ForecastViewController()
        forecastViewController.configure(with: getLocation(at: indexPath))
        
        navigationController?.pushViewController(
            forecastViewController,
            animated: true
        )
    }
}

extension CurrentWeatherViewController: UIScrollViewDelegate {
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if let page = centeredCollectionViewFlowLayout.currentCenteredPage {
            pageControl.currentPage = page
        }
    }
}

extension CurrentWeatherViewController {
    private func setupLocationManager() {
        locationManager.onLocationUpdate = { [weak self] location in
            guard let self = self else { return }
            self.callLocationService(
                longitude: location.coordinate.longitude.description,
                latitude: location.coordinate.latitude.description
            )
        }
        
        locationManager.onError = { [weak self] error in
            guard let self = self else { return }
            self.showError(
                message: "Unable to retrieve location. Please check your device settings."
            )
        }

        locationManager.onAuthorizationStatusChange = { [weak self] status in
            guard let self = self else { return }
            switch status {
            case .denied, .restricted:
                self.showError(
                    message: "Location access is restricted. Please enable location permissions in Settings.",
                    retryAction: {
                        self.locationManager.requestAuthorization()
                    }
                )
            case .authorizedWhenInUse, .authorizedAlways:
                self.getCurrentLocation()
            case .notDetermined:
                self.locationManager.requestAuthorization()
            @unknown default:
                break
            }
        }
    }
    
    private func getCurrentLocation() {
        locationManager.requestLocation()
    }
}

extension CurrentWeatherViewController {
    private func callLocationService(longitude: String, latitude: String) {
        locationService.loadLocation(
            for: longitude,
            latitude: latitude,
            completion: { [weak self] result in
                guard let self = self else { return }
                switch result {
                case .success(let locationResponse):
                    let currentLocation = LocationModel(
                        city: locationResponse.city,
                        countryCode: locationResponse.countryCode
                    )
            
                    self.locations.append(currentLocation)
                    self.locations += self.dbHelper.fetchLocationModels()
                    self.weatherData = Array(
                        repeating: WeatherModel.empty,
                        count: self.locations.count
                    )
                    self.loadWeatherForAllLocations()
                case .failure(let error):
                    if let serviceError = error as? ServiceError {
                        self.showError(
                            message: serviceError.errorDescription ?? "Unknown error occurred"
                        )
                    } else {
                        self.showError(message: "Failed to load weather data")
                    }
                }
            }
        )
    }
    
    private func callWeatherService(
        city: String,
        countryCode: String,
        index: Int,
        completion: (() -> Void)? = nil
    ) {
        weatherService.loadWeather(
            for: city,
            country: countryCode,
            units: .metric
        ) { [weak self] result in
            guard let self = self else { return }

            DispatchQueue.main.async {
                switch result {
                case .success(let weatherResponse):
                    let iconCode = weatherResponse.weather.first?.icon ?? ""

                    let weatherModel = WeatherModel(
                        location: "\(city), \(countryCode)",
                        temperature: weatherResponse.main.temp,
                        description: weatherResponse.weather.first?.main ?? "",
                        cloudiness: Int(weatherResponse.clouds.percentage),
                        humidity: weatherResponse.main.humidity,
                        windSpeed: weatherResponse.wind.speed,
                        windDegree: weatherResponse.wind.deg,
                        iconCode: iconCode
                    )

                    self.weatherData[index] = weatherModel

                case .failure(let error):
                    if let serviceError = error as? ServiceError {
                        self.showError(
                            message: serviceError.errorDescription ?? "Unknown error occurred"
                        )
                    } else {
                        self.showError(message: "Failed to load weather data")
                    }
                }
                completion?()
            }
        }
    }

}

extension CurrentWeatherViewController {
    
    private var cellPercentWidth: CGFloat { 0.80 }
    private var cellPercentHeight: CGFloat { 0.80 }
    
}

extension CurrentWeatherViewController {
    
    private func getLocation(at indexPath: IndexPath) -> LocationModel {
        return locations[indexPath.row]
    }
    
    private func getWeather(at indexPath: IndexPath) -> WeatherModel {
        return weatherData[indexPath.row]
    }
    
    private func getData() {
        self.startLoading()
        locations.removeAll()
        weatherData.removeAll()
        
        getCurrentLocation()
    }
    
    private func loadWeatherForAllLocations() {
        let dispatchGroup = DispatchGroup()
        
        for (index, location) in locations.enumerated() {
            dispatchGroup.enter()
            callWeatherService(
                city: location.city,
                countryCode: location.countryCode,
                index: index
            ) {
                dispatchGroup.leave()
            }
        }
        
        dispatchGroup.notify(queue: .main) {
            self.collectionView.reloadData()
            self.pageControl.numberOfPages = self.weatherData.count
            self.stopLoading()
        }
    }

    private func deleteItem(at indexPath: IndexPath) {
        if indexPath.row == 0 {
            showError(
                message: "Can not delete current location!"
            )
            return
        }
        let alert = UIAlertController(
            title: "Delete?",
            message: "Are you sure you want to delete this location?",
            preferredStyle: .actionSheet
        )

        alert.addAction(
            UIAlertAction(
                title: "Delete",
                style: .destructive,
                handler: { [unowned self] _ in
                    removeLocation(at: indexPath)
                }
            )
        )
        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
        present(alert, animated: true)
    }
    
    private func removeLocation(at indexPath: IndexPath) {
        dbHelper.deleteLocation(locaion: getLocation(at: indexPath).location!)
        getData()
    }
    
}

extension CurrentWeatherViewController {
    private func startLoading() {
        pageControl.isHidden = true
        collectionView.isHidden = true
        loader.startAnimating()
    }

    private func stopLoading() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) { [weak self] in
            self?.loader.stopAnimating()
            self?.collectionView.isHidden = false
            self?.pageControl.isHidden = false
        }
    }
    
    private func showError(
        message: String,
        retryAction: (() -> Void)? = nil
    ) {
        if !loader.isHidden {
            loader.stopAnimating()
        }
        
        pageControl.isHidden = true
        collectionView.isHidden = true

        errorView.configure(
            message: message,
            retryAction: { [weak self] in
                self?.errorView.isHidden = true
                retryAction?() ?? self?.getData()
            }
        )
        errorView.isHidden = false
        
        errorView.transform = CGAffineTransform(scaleX: 0.5, y: 0.5)
        errorView.alpha = 0
        
        UIView.animate(
            withDuration: 0.5,
            delay: 0,
            usingSpringWithDamping: 0.6,
            initialSpringVelocity: 0.5,
            options: .curveEaseInOut,
            animations: {
                self.errorView.transform = .identity
                self.errorView.alpha = 1
            },
            completion: nil
        )
    }
}

extension CurrentWeatherViewController: AddPopUpWindowDelegate {
    
    func didAddCity(city: String, countryCode: String) {
        dbHelper.addLocation(countryCode: countryCode, city: city)
        getData()
    }
    
}

extension CurrentWeatherViewController {
    
    private func addSampleData() {
        dbHelper.addLocation(countryCode: "US", city: "New York")
        dbHelper.addLocation(countryCode: "US", city: "Los Angeles")
        dbHelper.addLocation(countryCode: "JP", city: "Tokyo")
    }
    
}
