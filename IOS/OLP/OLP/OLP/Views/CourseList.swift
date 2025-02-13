//
//  CourseList.swift
//  OLP
//
//  Created by Irakli Khutsishvili on 16.11.24.
//


import UIKit

class CourseList : UIView {
    
    var clockIcon : UIImageView = {
        let imageView = UIImageView(image: UIImage(named: "TimeCircle"))
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        return imageView
    }()
    
    var containerView : UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.clipsToBounds = true
        view.layer.cornerRadius = 32
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    var lessontitle : UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .bold)
        label.text = "16 Lessons"
        label.textColor = UIColor(_colorLiteralRed: 12/255, green: 10/255, blue: 28/255, alpha: 1.0)
        label.lineBreakMode = .byClipping
        label.numberOfLines = 1
        label.textAlignment = .left
        return label
    }()
    
    var descriptionLabel : UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 12)
        label.text = "This course will be teach the basic how to start your mobile development career on iOS."
        label.textColor = .gray
        label.lineBreakMode = .byClipping
        label.numberOfLines = 0
        label.textAlignment = .left
        return label
    }()
    
    var duration: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 12)
        label.text = "3hr 20min"
        label.textColor = .gray
        label.lineBreakMode = .byClipping
        label.numberOfLines = 1
        label.textAlignment = .left
        return label
    }()

    let upperStack : UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fillProportionally
        return stackView
    }()
    
    let durationStack : UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fillProportionally
        stackView.spacing = 3
        return stackView
    }()
    
    let labelStack : UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fillProportionally
        stackView.spacing = 8
        return stackView
    }()
    
    let mainStack : UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fillProportionally
        stackView.spacing = 12
        return stackView
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.layer.cornerRadius = 32
        self.backgroundColor = .white
        self.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        self.clipsToBounds = true
        setupView()
    }
    
    func setupView() {
        addSubviews()
        setConstraints()
    }
    
    func addSubviews() {
        addSubview(containerView)
        containerView.addSubview(mainStack)
        containerView.addSubview(labelStack)
        
        labelStack.addArrangedSubview(upperStack)
        labelStack.addArrangedSubview(descriptionLabel)
        upperStack.addArrangedSubview(lessontitle)
        upperStack.addArrangedSubview(durationStack)
        durationStack.addArrangedSubview(clockIcon)
        durationStack.addArrangedSubview(duration)
    }
    
    func setConstraints() {
        NSLayoutConstraint.activate([
            
            containerView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 16),
            containerView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -16),
            containerView.topAnchor.constraint(equalTo: topAnchor, constant: 12),
            containerView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            labelStack.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 8),
            labelStack.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -8),
            labelStack.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 24),
            
            mainStack.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 8),
            mainStack.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -8),
            mainStack.topAnchor.constraint(equalTo: labelStack.bottomAnchor, constant: 16),
            
            clockIcon.heightAnchor.constraint(equalToConstant: 11.7),
            clockIcon.widthAnchor.constraint(equalToConstant: 11.7),
            
            durationStack.heightAnchor.constraint(equalToConstant: 14),
            durationStack.widthAnchor.constraint(equalToConstant: 81),
            
        ])
    }
  
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configure(with viewModels: [CourseItemViewModel]) {
        for viewModel in viewModels {
            let courseItem = CourseItem()
            courseItem.configure(with: viewModel)
            mainStack.addArrangedSubview(courseItem)
            courseItem.translatesAutoresizingMaskIntoConstraints = false
            NSLayoutConstraint.activate([
                courseItem.heightAnchor.constraint(equalToConstant: 100)
            ])
        }
    }
}

