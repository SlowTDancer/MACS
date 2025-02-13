//
//  SuccessViewController.swift
//  LRFP
//
//  Created by ikhut21 on 03.02.25.
//

import UIKit

class SuccessViewController: UIViewController {
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    let successImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.image = UIImage(named: "SuccessIcon")
        imageView.contentMode = .scaleAspectFit
        return imageView
    }()

    let successTitleLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = UIFont(name: "Inter-Bold", size: 20)
        label.numberOfLines = 0
        label.textAlignment = .center
        return label
    }()

    let successDescriptionLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = UIFont(name: "Inter-Regular", size: 14)
        label.numberOfLines = 0
        label.textAlignment = .center
        return label
    }()
    
    lazy var labelStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(successTitleLabel)
        stackView.addArrangedSubview(successDescriptionLabel)
        stackView.spacing = 12
        stackView.alignment = .center
        return stackView
    }()
    
    lazy var mainStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(successImageView)
        stackView.addArrangedSubview(labelStackView)
        stackView.spacing = 36
        return stackView
    }()
    
    lazy var backButtonItem: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(image: UIImage(systemName: "chevron.left"),
                                             style: .plain,
                                             target: self,
                                             action: #selector(backButtonTapped))
        return buttonItem
    }()
    
    init(_ model: SuccessPageViewModel) {
        super.init(nibName: nil, bundle: nil)
        configure(with: model)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpView()
    }

    private func setUpView() {
        view.backgroundColor = .white
        setUpNavBar()
        addSubviews()
        setConstraints()
    }

    private func setUpNavBar() {
        navigationItem.titleView = titleLabel
        navigationItem.leftBarButtonItem = backButtonItem
    }
    
    private func addSubviews() {
        view.addSubview(mainStackView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            successImageView.heightAnchor.constraint(equalToConstant: 64),
            successImageView.widthAnchor.constraint(equalToConstant: 64),
            
            mainStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 100),
            mainStackView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            mainStackView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16)
        ])
    }
    
    @objc func backButtonTapped() {
        navigationController?.popToRootViewController(animated: true)
    }
    
    public func configure(with model: SuccessPageViewModel) {
        titleLabel.text = model.pageTitle
        successTitleLabel.text = model.successTitle
        successDescriptionLabel.text = model.successDescription
    }

}
