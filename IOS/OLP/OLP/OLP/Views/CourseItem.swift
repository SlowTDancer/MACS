//
//  CourseItem.swift
//  OLP
//
//  Created by Irakli Khutsishvili on 15.11.24.
//
import UIKit

class CourseItem : UIView {
    
    let overlayView: UIView = {
        let overlay = UIView()
        overlay.backgroundColor = UIColor(white: 0.0, alpha: 0.1)
        overlay.isHidden = false
        overlay.translatesAutoresizingMaskIntoConstraints = false
        return overlay
    }()
    
    var courseImage : UIImageView = {
        let imageView = UIImageView(image: UIImage(named: "FirstCourse"))
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.layer.cornerRadius = 16
        return imageView
    }()
    
    var duration: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 10)
        label.textColor = .gray
        label.lineBreakMode = .byClipping
        label.numberOfLines = 1
        label.textAlignment = .left
        return label
    }()

    var title : UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14)
        label.textColor = UIColor(_colorLiteralRed: 12/255, green: 10/255, blue: 28/255, alpha: 1.0)
        label.lineBreakMode = .byClipping
        label.numberOfLines = 1
        label.textAlignment = .left
        return label
    }()
    
    var lockImage : UIImageView = {
        let imageView = UIImageView(image: UIImage(named: "Lock"))
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        return imageView
    }()
    
    var progressBar: UIProgressView = {
        let progressView = UIProgressView(progressViewStyle: .default)
        progressView.progress = 0.6
        progressView.trackTintColor = UIColor(white: 0.95, alpha: 1.0)
        progressView.progressTintColor = UIColor(_colorLiteralRed: 0/255, green: 128/255, blue: 0/255, alpha: 1.0)
        progressView.layer.cornerRadius = 8
        progressView.clipsToBounds = true
        progressView.layer.sublayers?[1].cornerRadius = 8
        progressView.subviews[1].clipsToBounds = true
        progressView.translatesAutoresizingMaskIntoConstraints = true
        
        return progressView
    }()
    
    let containerStack : UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fillProportionally
        stackView.spacing = 16
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = UIEdgeInsets(top: 13, left: 0, bottom: 13, right: 0)
            
        return stackView
    }()
    
    let labelStack : UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fillProportionally
        stackView.spacing = 0
        return stackView
    }()
    
    let upperStack : UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fillProportionally
        return stackView
    }()
    
    let mainStack : UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fillProportionally
        stackView.spacing = 30
        return stackView
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    func setupView() {
        self.layer.cornerRadius = 16
        self.layer.borderWidth = 1
        self.layer.borderColor = UIColor.lightGray.cgColor
        self.clipsToBounds = true
        self.bringSubviewToFront(overlayView)
        addSubviews()
        setConstraints()
    }
    
    func addSubviews() {
        addSubview(overlayView)
        addSubview(mainStack)
        mainStack.addArrangedSubview(courseImage)
        mainStack.addArrangedSubview(containerStack)
        
        containerStack.addArrangedSubview(upperStack)
        containerStack.addArrangedSubview(progressBar)
        upperStack.addArrangedSubview(labelStack)
        upperStack.addArrangedSubview(lockImage)
        
        labelStack.addArrangedSubview(duration)
        labelStack.addArrangedSubview(title)
    }
    
    func setConstraints() {
        NSLayoutConstraint.activate([
            
            overlayView.leadingAnchor.constraint(equalTo: leadingAnchor),
            overlayView.trailingAnchor.constraint(equalTo: trailingAnchor),
            overlayView.topAnchor.constraint(equalTo: topAnchor),
            overlayView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            mainStack.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 8),
            mainStack.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -8),
            mainStack.topAnchor.constraint(equalTo: topAnchor, constant: 8),
            mainStack.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -8),
            
            courseImage.heightAnchor.constraint(equalToConstant: 84),
            courseImage.widthAnchor.constraint(equalToConstant: 84),
            
            lockImage.heightAnchor.constraint(equalToConstant: 24),
            lockImage.widthAnchor.constraint(equalToConstant: 24),
            
            progressBar.heightAnchor.constraint(equalToConstant: 5)
        ])
    }
  
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    func configure(with model : CourseItemViewModel) {
        courseImage.image = model.courseImage
        duration.text = model.duration
        title.text = model.title
        progressBar.progress = model.progress
        lockImage.isHidden = !model.isLocked
        overlayView.isHidden = !model.isLocked
        if(model.isLocked) {
            self.layer.shadowColor = UIColor.black.cgColor
            self.layer.shadowOpacity = 0.2
            self.layer.shadowOffset = CGSize(width: 0, height: 4)
            self.layer.shadowRadius = 8
        }
    }
    
}
