//
//  ErrorPageView.swift
//  WeatherApplication
//
//  Created by ikhut21 on 23.02.25.
//

import UIKit

class ErrorPageView: UIView {
    
    private let errorImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(named: "errorImage")
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private let errorLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .white
        label.textAlignment = .center
        label.numberOfLines = 0
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var retryButton: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("Reload", for: .normal)
        button.titleLabel?.font = UIFont(name: "Inter-Bold", size: 16)
        button.setTitleColor(.white, for: .normal)
        button.backgroundColor = UIColor(named: "accent")
        button.layer.cornerRadius = 10
        button.translatesAutoresizingMaskIntoConstraints = false
        button.addTarget(
            self,
            action: #selector(retryButtonTapped),
            for: .touchUpInside
        )
        return button
    }()
    
    private lazy var mainStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 30
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubview(errorImageView)
        stackView.addArrangedSubview(errorLabel)
        stackView.addArrangedSubview(retryButton)
        stackView.alignment = .center
        return stackView
    }()
    
    private var retryAction: (() -> Void)?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
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
    }
    
    private func addSubviews() {
        addSubview(mainStackView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            errorImageView.widthAnchor.constraint(equalToConstant: 150),
            errorImageView.heightAnchor.constraint(equalToConstant: 150),
            
            mainStackView.centerXAnchor.constraint(equalTo: centerXAnchor),
            mainStackView.centerYAnchor.constraint(equalTo: centerYAnchor),
            
            retryButton.heightAnchor.constraint(equalToConstant: 40),
            retryButton.widthAnchor.constraint(
                equalTo: widthAnchor,
                multiplier: 0.3
            ),
            
            mainStackView.leadingAnchor.constraint(
                greaterThanOrEqualTo: leadingAnchor,
                constant: 20
            ),
            mainStackView.trailingAnchor.constraint(
                lessThanOrEqualTo: trailingAnchor,
                constant: -20
            )
        ])
    }
    
    func configure(
        message: String,
        retryAction: (() -> Void)? = nil
    ) {
        errorLabel.text = message
        self.retryAction = retryAction

        retryButton.isHidden = retryAction == nil
    }

    @objc private func retryButtonTapped() {
        retryAction?()
    }
}
