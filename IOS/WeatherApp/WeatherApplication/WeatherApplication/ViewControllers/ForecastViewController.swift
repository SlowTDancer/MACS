//
//  ForecastViewController.swift
//  WeatherApplication
//
//  Created by ikhut21 on 23.02.25.
//

import UIKit

class ForecastViewController: UIViewController {

    private let titleLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .white
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var backButtonItem: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(
            image: UIImage(systemName: "chevron.left"),
            style: .plain,
            target: self,
            action: #selector(backButtonTapped)
        )
        buttonItem.tintColor = UIColor(
            named: "accent"
        )!
        return buttonItem
    }()
    
    private lazy var tableView: UITableView = UITableView(frame: .zero, style: .grouped)
    
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
    
    private let weatherService = WeatherService()
    private var sections: [ForecastSection] = []
    private let weekdays = [
        "SUNDAY",
        "MONDAY",
        "TUESDAY",
        "WEDNESDAY",
        "THURSDAY",
        "FRIDAY",
        "SATURDAY"
    ]
    
    private var city: String?
    private var countryCode: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if #available(iOS 11.0, *) {
            self.additionalSafeAreaInsets.top = 0
        }
        
        setUpView()
        addSubviews()
        setConstraints()
        loadForecastData()
    }
    
    private func setUpView() {
        setUpGradientBackground()
        setUpNavBar()
        setUpTableView()
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
        navigationItem.leftBarButtonItem = backButtonItem
        
        let appearance = UINavigationBarAppearance()
        appearance.configureWithTransparentBackground()
        appearance.backgroundColor = .clear
        appearance.shadowColor = .clear
        
        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        
        if #available(iOS 15.0, *) {
            navigationController?.navigationBar.compactScrollEdgeAppearance = appearance
        }
    }
    
    private func setUpTableView() {
        tableView.sectionHeaderTopPadding = 0
        
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.backgroundColor = .clear
        tableView.contentInsetAdjustmentBehavior = .never
        tableView.automaticallyAdjustsScrollIndicatorInsets = false
        
        if #available(iOS 15.0, *) {
            tableView.isPrefetchingEnabled = false
            tableView.fillerRowHeight = 0
        }
        
        tableView.backgroundColor = .clear
        tableView.separatorStyle = .none
        tableView.translatesAutoresizingMaskIntoConstraints = false
        
        tableView.register(
            UINib(nibName: "ForecastViewCell", bundle: .main),
            forCellReuseIdentifier: "ForecastViewCell"
        )
        tableView.register(
            UINib(nibName: "ForecastViewHeader", bundle: .main),
            forHeaderFooterViewReuseIdentifier: "ForecastViewHeader"
        )
    }
    
    private func addSubviews() {
        view.addSubview(tableView)
        view.addSubview(loader)
        view.addSubview(errorView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.topAnchor
            ),
            tableView.leadingAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.leadingAnchor
            ),
            tableView.trailingAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.trailingAnchor
            ),
            tableView.bottomAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.bottomAnchor
            ),
            
            loader.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            loader.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            
            errorView.topAnchor.constraint(equalTo: view.topAnchor),
            errorView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            errorView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            errorView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
    
    private func loadForecastData() {
        guard let city = city, let countryCode = countryCode else { return }
        
        startLoading()
        
        weatherService.loadForecast(
            for: city,
            country: countryCode,
            units: .metric
        ) { [weak self] result in
            guard let self = self else { return }
            
            DispatchQueue.main.async {
                switch result {
                case .success(let forecast):
                    self.processForecastData(forecast)
                case .failure(let error):
                    if let serviceError = error as? ServiceError {
                        self.showError(
                            message: serviceError.errorDescription ?? "Unknown error occurred"
                        )
                    } else {
                        self.showError(message: "Failed to load forecast data")
                    }
                }
            }
        }
    }
    
    private func processForecastData(_ forecast: ForecastResponse) {
        var sectionsDictionary: [String: ForecastSection] = [:]
        
        forecast.list.forEach { item in
            let dateComponents = item.dtTxt.split(separator: " ")
            guard dateComponents.count == 2,
                  let date = self.parseDate(String(dateComponents[0])),
                  let weekday = self.getWeekday(from: date)
            else { return }
            
            let time = String(dateComponents[1].prefix(5))
            let cellModel = ForecastCellModel(
                time: time,
                weather: item.weather.first?.description ?? "",
                temperature: "\(Int(round(item.main.temp)))°",
                iconCode: item.weather.first?.icon ?? ""
            )
            
            if var section = sectionsDictionary[weekday] {
                section.times.append(cellModel)
                sectionsDictionary[weekday] = section
            } else {
                sectionsDictionary[weekday] = ForecastSection(
                    header: ForecastHeaderModel(weekDay: weekday),
                    times: [cellModel]
                )
            }
        }
        
        sections = Array(sectionsDictionary.values)
            .sorted { $0.header?.weekDay ?? "" < $1.header?.weekDay ?? "" }
        
        tableView.reloadData()
        stopLoading()
    }
    
    func configure(with location: LocationModel) {
        titleLabel.text = location.city
        self.city = location.city
        self.countryCode = location.countryCode
    }
    
    @objc private func backButtonTapped() {
        navigationController?.popViewController(animated: true)
    }
}

extension ForecastViewController: UITableViewDelegate, UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return sections.count
    }
    
    func tableView(
        _ tableView: UITableView,
        numberOfRowsInSection section: Int
    ) -> Int {
        return sections[section].numberOfRows
    }
    
    func tableView(
        _ tableView: UITableView,
        cellForRowAt indexPath: IndexPath
    ) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(
            withIdentifier: "ForecastViewCell",
            for: indexPath
        )
                
        if let forecastCell = cell as? ForecastViewCell {
            let isLastInSection =
            indexPath.row == sections[indexPath.section].times.count - 1
            
            forecastCell.configure(
                with: getForecastCell(at: indexPath),
                isLastInSection: isLastInSection
            )
        }
        
        return cell
    }
    
    func tableView(
        _ tableView: UITableView,
        viewForHeaderInSection section: Int
    ) -> UIView? {
        let header = tableView.dequeueReusableHeaderFooterView(
            withIdentifier: "ForecastViewHeader"
        )
        
        if let forecastHeader = header as? ForecastViewHeader {
            let isFirstHeader = section == 0
            forecastHeader.configure(
                with: getForecastHeader(at: section),
                isFirstHeader: isFirstHeader
            )
        }
        
        return header
    }
    
    func tableView(
        _ tableView: UITableView,
        heightForHeaderInSection section: Int
    ) -> CGFloat {
        return 44
    }
    
    func tableView(
        _ tableView: UITableView,
        heightForRowAt indexPath: IndexPath
    ) -> CGFloat {
        return 60
    }
}

extension ForecastViewController {
    
    private func parseDate(_ dateString: String) -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter.date(from: dateString)
    }
    
    private func getWeekday(from date: Date) -> String? {
        let weekdayNumber = Calendar.current.component(.weekday, from: date)
        return weekdays[weekdayNumber - 1]
    }
    
    private func startLoading() {
        tableView.isHidden = true
        loader.startAnimating()
    }
    
    private func stopLoading() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) { [weak self] in
            self?.loader.stopAnimating()
            self?.tableView.isHidden = false
        }
    }
    
    private func showError(
        message: String,
        retryAction: (() -> Void)? = nil
    ) {
        if !loader.isHidden {
            loader.stopAnimating()
        }
        
        tableView.isHidden = true

        errorView.configure(
            message: message,
            retryAction: { [weak self] in
                self?.errorView.isHidden = true
                retryAction?() ?? self?.loadForecastData()
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

extension ForecastViewController {
    
    private func getForecastCell(at indexPath: IndexPath) -> ForecastCellModel {
        return sections[indexPath.section].times[indexPath.row]
    }
    
    private func getForecastHeader(at section: Int) -> ForecastHeaderModel {
        return sections[section].header!
    }
    
}
