// ForecastViewCell.swift
import UIKit

class ForecastViewCell: UITableViewCell {
    
    private let timeLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "Inter-Regular", size: 16)
        label.textColor = .white
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let weatherLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "Inter-Regular", size: 14)
        label.textColor = .white.withAlphaComponent(0.8)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var dataStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 0
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(timeLabel)
        stackView.addArrangedSubview(weatherLabel)
        stackView.alignment = .leading
        return stackView
    }()
    
    private let temperatureLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "Inter-Bold", size: 16)
        label.textColor = UIColor(
            named: "accent"
        )!
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let weatherIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.tintColor = .white
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let separatorView: UIView = {
        let view = UIView()
        view.backgroundColor = .black.withAlphaComponent(0.2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        commonInit()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        commonInit()
    }
    
    private func commonInit() {
        setUpView()
        addSubviews()
        setConstraints()
    }
    
    private func setUpView() {
        backgroundColor = .clear
        selectionStyle = .none
    }
    
    private func addSubviews() {
        contentView.addSubview(weatherIcon)
        contentView.addSubview(dataStackView)
        contentView.addSubview(temperatureLabel)
        contentView.addSubview(separatorView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            
            weatherIcon.leadingAnchor.constraint(
                equalTo: contentView.leadingAnchor,
                constant: 16
            ),
            weatherIcon.centerYAnchor.constraint(
                equalTo: contentView.centerYAnchor
            ),
            weatherIcon.widthAnchor.constraint(
                equalToConstant: 24
            ),
            weatherIcon.heightAnchor.constraint(
                equalToConstant: 24
            ),
            
            dataStackView.leadingAnchor.constraint(
                equalTo: weatherIcon.trailingAnchor,
                constant: 20
            ),
            dataStackView.centerYAnchor.constraint(
                equalTo: contentView.centerYAnchor
            ),
            
            temperatureLabel.trailingAnchor.constraint(
                equalTo: contentView.trailingAnchor,
                constant: -16
            ),
            temperatureLabel.centerYAnchor.constraint(
                equalTo: contentView.centerYAnchor
            ),
            separatorView.leadingAnchor.constraint(
                equalTo: contentView.leadingAnchor,
                constant: 16
            ),
            separatorView.trailingAnchor.constraint(
                equalTo: contentView.trailingAnchor,
                constant: -16
            ),
            separatorView.bottomAnchor.constraint(
                equalTo: contentView.bottomAnchor
            ),
            separatorView.heightAnchor.constraint(
                equalToConstant: 0.5
            )
        ])
    }
    
    func configure(with model: ForecastCellModel, isLastInSection: Bool = false) {
        timeLabel.text = model.time
        weatherLabel.text = model.weather.capitalized
        temperatureLabel.text = "\(model.temperature)C"
        
        let weatherIconView = WeatherIconView()
        weatherIconView.setIcon(from: model.iconURL!.absoluteString)
        weatherIcon.image = weatherIconView.image
        
        separatorView.isHidden = isLastInSection
    }
}
