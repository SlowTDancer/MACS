//
//  AddPopUpViewController.swift
//  WeatherApplication
//
//  Created by ikhut21 on 23.02.25.
//

import UIKit

protocol AddPopUpWindowDelegate: AnyObject {
    func didAddCity(
        city: String,
        countryCode: String
    )
}

class AddPopUpWindow: UIViewController {
    
    private let weatherService = WeatherService()
    
    private let blurView: UIVisualEffectView = {
        let blurEffect = UIBlurEffect(style: .dark)
        let view = UIVisualEffectView(effect: blurEffect)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let popUpView: UIView = {
        let view = UIView()
        view.layer.cornerRadius = 20
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let gradientLayer: CAGradientLayer = {
        let gradient = CAGradientLayer()
        gradient.colors = [
            UIColor.customGreenStart.cgColor,
            UIColor.customGreenEnd.cgColor
        ]
        gradient.locations = [0.0, 1.0]
        gradient.cornerRadius = 20
        return gradient
    }()
    
    private let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Add City"
        label.textAlignment = .center
        label.font = UIFont(name: "Inter-Bold", size: 20)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.textColor = .white
        return label
    }()
    
    private let subtitleLabel: UILabel = {
        let label = UILabel()
        label.text = "Enter City name you wish to add"
        label.textAlignment = .center
        label.font = UIFont(name: "Inter-Regular", size: 16)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.textColor = .white
        return label
    }()
    
    private let textField: UITextField = {
        let textField = UITextField()
        textField.borderStyle = .roundedRect
        textField.font = UIFont(name: "Inter-Regular", size: 16)
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    private lazy var popUpStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 20
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(titleLabel)
        stackView.addArrangedSubview(subtitleLabel)
        stackView.addArrangedSubview(textField)
        stackView.alignment = .center
        return stackView
    }()
    
    private let errorView: UIView = {
        let view = UIView()
        view.backgroundColor = .systemRed
        view.layer.cornerRadius = 10
        view.isHidden = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let errorLabel: UILabel = {
        let label = UILabel()
        label.text = "Error Occured"
        label.textColor = .white
        label.textAlignment = .left
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let errorDescriptionLabel: UILabel = {
        let label = UILabel()
        label.text = "City with that name was not found!"
        label.textColor = .white
        label.textAlignment = .left
        label.font = UIFont(name: "Inter-Regular", size: 14)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var errorStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 0
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(errorLabel)
        stackView.addArrangedSubview(errorDescriptionLabel)
        return stackView
    }()
    
    private lazy var addButton: UIButton = {
        let button = UIButton(type: .system)
        let configuration = UIImage.SymbolConfiguration(
            pointSize: 24,
            weight: .semibold
        )
        let plusImage = UIImage(
            systemName: "plus",
            withConfiguration: configuration
        )
        button.setImage(plusImage, for: .normal)
        
        button.backgroundColor = .white
        button.tintColor = UIColor.greenGradientEnd
        button.layer.cornerRadius = 30
        button.addTarget(self, action: #selector(addCity), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()

    private let loaderContainer: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.layer.cornerRadius = 30
        view.isHidden = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let loader: UIActivityIndicatorView = {
        let loader = UIActivityIndicatorView(style: .medium)
        loader.hidesWhenStopped = true
        loader.color = UIColor.greenGradientEnd
        loader.translatesAutoresizingMaskIntoConstraints = false
        return loader
    }()

    weak var delegate: AddPopUpWindowDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpView()
        addSubviews()
        setUpConstraints()
        setUpTapGesture()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        gradientLayer.frame = popUpView.bounds
    }
    
    private func setUpView() {
        modalPresentationStyle = .overFullScreen
        modalTransitionStyle = .crossDissolve
        
        view.backgroundColor = .clear
        
        popUpView.layer.insertSublayer(gradientLayer, at: 0)
    }
    
    private func addSubviews() {
        view.addSubview(blurView)
        view.addSubview(errorView)
        view.addSubview(popUpView)
        
        popUpView.addSubview(popUpStackView)
        popUpView.addSubview(addButton)
        popUpView.addSubview(loaderContainer)
        loaderContainer.addSubview(loader)
        
        errorView.addSubview(errorStackView)
    }
    
    private func setUpConstraints() {
        NSLayoutConstraint.activate([

            blurView.topAnchor.constraint(equalTo: view.topAnchor),
            blurView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            blurView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            blurView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            errorView.topAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.topAnchor,
                constant: 40
            ),
            errorView.centerXAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.centerXAnchor
            ),
            errorView.widthAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.widthAnchor,
                multiplier: 0.8
            ),
            errorView.heightAnchor.constraint(equalToConstant: 60),
            
            errorStackView.leadingAnchor.constraint(
                equalTo: errorView.leadingAnchor,
                constant: 10
            ),
            errorStackView.centerYAnchor.constraint(
                equalTo: errorView.centerYAnchor
            ),
            
            popUpView.centerXAnchor.constraint(
                equalTo: view.centerXAnchor),
            popUpView.centerYAnchor.constraint(
                equalTo: view.centerYAnchor),
            popUpView.widthAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.widthAnchor,
                multiplier: 0.8
            ),
            popUpView.heightAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.heightAnchor,
                multiplier: 0.35
            ),
            
            popUpStackView.topAnchor.constraint(
                equalTo: popUpView.topAnchor,
                constant: 30
            ),
            popUpStackView.leadingAnchor.constraint(
                equalTo: popUpView.leadingAnchor
            ),
            popUpStackView.trailingAnchor.constraint(
                equalTo: popUpView.trailingAnchor
            ),
            
            textField.heightAnchor.constraint(
                equalToConstant: 30
            ),
            
            textField.widthAnchor.constraint(
                equalTo: popUpView.widthAnchor,
                multiplier: 0.6
            ),
            
            addButton.bottomAnchor.constraint(
                equalTo: popUpView.bottomAnchor,
                constant: -30
            ),
            addButton.centerXAnchor.constraint(
                equalTo: popUpView.centerXAnchor
            ),
            addButton.widthAnchor.constraint(
                equalToConstant: 60
            ),
            addButton.heightAnchor.constraint(
                equalToConstant: 60
            ),
            
            loaderContainer.centerXAnchor.constraint(
                equalTo: addButton.centerXAnchor
            ),
            loaderContainer.centerYAnchor.constraint(
                equalTo: addButton.centerYAnchor
            ),
            loaderContainer.widthAnchor.constraint(
                equalToConstant: 60
            ),
            loaderContainer.heightAnchor.constraint(
                equalToConstant: 60
            ),
            
            loader.centerXAnchor.constraint(equalTo: loaderContainer.centerXAnchor),
            loader.centerYAnchor.constraint(equalTo: loaderContainer.centerYAnchor)
        ])
    }
    
    private func setUpTapGesture() {
        let tapGesture = UITapGestureRecognizer(
            target: self,
            action: #selector(handleTap(_:))
        )
        blurView.addGestureRecognizer(tapGesture)
    }
    
    @objc private func handleTap(_ gesture: UITapGestureRecognizer) {
        let location = gesture.location(in: view)
        if !popUpView.frame.contains(location) {
            dismissView()
        }
    }
    
    @objc private func dismissView() {
        dismiss(animated: true)
    }
    
    @objc private func addCity() {
        guard let cityName = textField.text, !cityName.isEmpty else {
            showError()
            return
        }
        
        startLoading()
        
        weatherService.loadWeather(
            for: cityName,
            country: "",
            units: .metric
        ) { [weak self] result in
            guard let self = self else { return }
            
            DispatchQueue.main.async {
                switch result {
                case .success(let weatherResponse):
                    self.stopLoading()
                    self.delegate?.didAddCity(
                        city: cityName,
                        countryCode: weatherResponse.sys.country
                    )
                    self.dismiss(animated: true)
                    
                case .failure(_):
                    self.stopLoading()
                    self.showError()
                }
            }
        }
    }
    
    private func showError() {
        errorView.isHidden = false
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) { [weak self] in
            self?.errorView.isHidden = true
        }
    }
    
    private func startLoading() {
        addButton.isHidden = true
        loaderContainer.isHidden = false
        loader.startAnimating()
    }
    
    private func stopLoading() {
        loader.stopAnimating()
        loaderContainer.isHidden = true
        addButton.isHidden = false
    }
}
