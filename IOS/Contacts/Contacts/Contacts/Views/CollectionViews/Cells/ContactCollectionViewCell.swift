//
//  ContactCollectionViewCell.swift
//  Contacts
//
//  Created by ikhut21 on 17.02.25.
//

import UIKit

class ContactCollectionViewCell: UICollectionViewCell {

    let nameLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = UIFont(name: "Inter-Regular", size: 16)
        return label
    }()
    
    let phoneNumberLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = UIFont(name: "Inter-Regular", size: 16)
        return label
    }()
    
    lazy var mainStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(nameLabel)
        stackView.addArrangedSubview(phoneNumberLabel)
        stackView.spacing = 0
        stackView.alignment = .center
        return stackView
    }()
    
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
    }
    
    private func setUpView() {
        setUpBorder()
        addSubviews()
        setConstraints()
    }
    
    private func setUpBorder() {
        self.layer.borderColor = UIColor.systemGray2.cgColor
        self.layer.borderWidth = 1
        self.layer.cornerRadius = 8
    }
    private func addSubviews() {
        self.addSubview(mainStackView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            mainStackView.centerXAnchor.constraint(equalTo: self.centerXAnchor),
            mainStackView.centerYAnchor.constraint(equalTo: self.centerYAnchor)
        ])
    }
    
    func configure(contactModel: ContactModel) {
        nameLabel.text = contactModel.name
        phoneNumberLabel.text = contactModel.phoneNumber
    }

}
