import UIKit

class WeatherViewCell: UICollectionViewCell {
    
    private let containerView: UIView = {
        let view = UIView()
        view.layer.cornerRadius = 40
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let weatherIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.tintColor = UIColor(
            named: "accent"
        )!
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let locationLabel: UILabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = .systemFont(ofSize: 24, weight: .medium)
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let temperatureLabel: UILabel = {
        let label = UILabel()
        label.textColor = UIColor(
            named: "accent"
        )!
        label.font = .systemFont(ofSize: 24)
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let verticalLineLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = UIColor(
            named: "accent"
        )!
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let weatherDescriptionLabel: UILabel = {
        let label = UILabel()
        label.textColor = UIColor(
            named: "accent"
        )!
        label.font = .systemFont(ofSize: 24)
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let horizontalLineLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = UIColor(
            named: "accent"
        )!
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let cloudinessIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(systemName: "cloud.fill")
        imageView.tintColor = UIColor(
            named: "accent"
        )!
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let cloudinessLabel: UILabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = .systemFont(ofSize: 18)
        label.text = "Cloudiness"
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let cloudinessValueLabel: UILabel = {
        let label = UILabel()
        label.textColor = UIColor(
            named: "accent"
        )!
        label.font = .systemFont(ofSize: 18)
        label.textAlignment = .right
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let humidityIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(systemName: "humidity.fill")
        imageView.tintColor = UIColor(
            named: "accent"
        )!
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let humidityLabel: UILabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = .systemFont(ofSize: 18)
        label.text = "Humidity"
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let humidityValueLabel: UILabel = {
        let label = UILabel()
        label.textColor = UIColor(
            named: "accent"
        )!
        label.font = .systemFont(ofSize: 18)
        label.textAlignment = .right
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let windSpeedIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(systemName: "wind")
        imageView.tintColor = UIColor(
            named: "accent"
        )!
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let windSpeedLabel: UILabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = .systemFont(ofSize: 18)
        label.text = "Wind Speed"
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let windSpeedValueLabel: UILabel = {
        let label = UILabel()
        label.textColor = UIColor(
            named: "accent"
        )!
        label.font = .systemFont(ofSize: 18)
        label.textAlignment = .right
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let windDirectionIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(systemName: "safari")
        imageView.tintColor = UIColor(
            named: "accent"
        )!
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let windDirectionLabel: UILabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = .systemFont(ofSize: 18)
        label.text = "Wind Direction"
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let windDirectionValueLabel: UILabel = {
        let label = UILabel()
        label.textColor = UIColor(
            named: "accent"
        )!
        label.font = .systemFont(ofSize: 18)
        label.textAlignment = .right
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var infoStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(temperatureLabel)
        stackView.addArrangedSubview(verticalLineLabel)
        stackView.addArrangedSubview(weatherDescriptionLabel)
        return stackView
    }()
    
    private lazy var headerStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 8
        stackView.alignment = .center
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(weatherIcon)
        stackView.addArrangedSubview(locationLabel)
        stackView.addArrangedSubview(infoStackView)
        return stackView
    }()
    
    private lazy var cloudinessStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(cloudinessIcon)
        stackView.addArrangedSubview(cloudinessLabel)
        stackView.addArrangedSubview(cloudinessValueLabel)
        return stackView
    }()
    
    private lazy var humidityStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(humidityIcon)
        stackView.addArrangedSubview(humidityLabel)
        stackView.addArrangedSubview(humidityValueLabel)
        return stackView
    }()
    
    private lazy var windSpeedStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(windSpeedIcon)
        stackView.addArrangedSubview(windSpeedLabel)
        stackView.addArrangedSubview(windSpeedValueLabel)
        return stackView
    }()
    
    private lazy var windDirectionStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(windDirectionIcon)
        stackView.addArrangedSubview(windDirectionLabel)
        stackView.addArrangedSubview(windDirectionValueLabel)
        return stackView
    }()
    
    private lazy var statsStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 16
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(cloudinessStackView)
        stackView.addArrangedSubview(humidityStackView)
        stackView.addArrangedSubview(windSpeedStackView)
        stackView.addArrangedSubview(windDirectionStackView)
        return stackView
    }()
    
    private lazy var mainStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 90
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(headerStackView)
        stackView.addArrangedSubview(horizontalLineLabel)
        stackView.addArrangedSubview(statsStackView)
        return stackView
    }()
    
    private var gradientLayer: CAGradientLayer?
    
    override func layoutSubviews() {
        super.layoutSubviews()
        gradientLayer?.frame = containerView.bounds
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        commonInit()
    }
    
    private func commonInit() {
        addSubviews()
        setConstraints()
    }
    
    private func addSubviews() {
        contentView.addSubview(containerView)
        containerView.addSubview(mainStackView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            containerView.topAnchor.constraint(
                equalTo: contentView.topAnchor,
                constant: 50
            ),
            containerView.leadingAnchor.constraint(
                equalTo: contentView.leadingAnchor
            ),
            containerView.trailingAnchor.constraint(
                equalTo: contentView.trailingAnchor
            ),
            containerView.bottomAnchor.constraint(
                equalTo: contentView.bottomAnchor,
                constant: -50
            ),
            
            mainStackView.topAnchor.constraint(
                equalTo: containerView.topAnchor,
                constant: 50
            ),
            mainStackView.leadingAnchor.constraint(
                equalTo: containerView.leadingAnchor,
                constant: 30
            ),
            mainStackView.trailingAnchor.constraint(
                equalTo: containerView.trailingAnchor,
                constant: -30
            ),
            
            verticalLineLabel.widthAnchor.constraint(
                equalToConstant: 1
            ),
            verticalLineLabel.heightAnchor.constraint(
                equalToConstant: 24
            ),
            
            weatherIcon.heightAnchor.constraint(
                equalToConstant: 100
            ),
            weatherIcon.widthAnchor.constraint(
                equalToConstant: 100
            ),
            
            horizontalLineLabel.heightAnchor.constraint(
                equalToConstant: 1
            ),
            horizontalLineLabel.leadingAnchor.constraint(
                equalTo: containerView.leadingAnchor,
                constant: 40
            ),
            horizontalLineLabel.trailingAnchor.constraint(
                equalTo: containerView.trailingAnchor,
                constant: -40
            ),
            
            cloudinessIcon.widthAnchor.constraint(
                equalToConstant: 24
            ),
            cloudinessIcon.heightAnchor.constraint(
                equalToConstant: 24
            ),
            humidityIcon.widthAnchor.constraint(
                equalToConstant: 24
            ),
            humidityIcon.heightAnchor.constraint(
                equalToConstant: 24
            ),
            windSpeedIcon.widthAnchor.constraint(
                equalToConstant: 24
            ),
            windSpeedIcon.heightAnchor.constraint(
                equalToConstant: 24
            ),
            windDirectionIcon.widthAnchor.constraint(
                equalToConstant: 24
            ),
            windDirectionIcon.heightAnchor.constraint(
                equalToConstant: 24
            )
        ])
    }
    
    private func setupGradient(style: GradientStyle) {
        gradientLayer?.removeFromSuperlayer()
        
        let gradient = CAGradientLayer()
        gradient.frame = containerView.bounds
        let colors = style.colors
        gradient.colors = [colors.start.cgColor, colors.end.cgColor]
        gradient.locations = [0.0, 1.0]
        gradient.cornerRadius = 40
        
        containerView.layer.insertSublayer(gradient, at: 0)
        gradientLayer = gradient
    }
    
    func configure(
        with weather: WeatherModel,
        style: GradientStyle
    ) {
        locationLabel.text = weather.location
        temperatureLabel.text = "\(weather.temperature)°C"
        weatherDescriptionLabel.text = weather.description
        
        cloudinessValueLabel.text = "\(weather.cloudiness)%"
        humidityValueLabel.text = "\(weather.humidity) mm"
        windSpeedValueLabel.text = "\(weather.windSpeed) km/h"
        windDirectionValueLabel.text = weather.windDirection
        
        let weatherIconView = WeatherIconView()
        weatherIconView.setIcon(from: weather.iconURL)
        weatherIcon.image = weatherIconView.image
        
        setupGradient(style: style)
    }
}
