//
//  ContactCellTableViewCell.swift
//  Contacts
//
//  Created by ikhut21 on 06.02.25.
//

import UIKit

class ContactTableViewCell: UITableViewCell {
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
        return stackView
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
        selectionStyle = .none
        setUpView()
    }
    
    private func setUpView() {
        addSubviews()
        setConstraints()
    }
    
    private func addSubviews() {
        self.addSubview(mainStackView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            
            mainStackView.topAnchor.constraint(
                equalTo: self.topAnchor,
                constant: 16
            ),
            mainStackView.leadingAnchor.constraint(
                equalTo: self.leadingAnchor,
                constant: 20
            ),
            mainStackView.trailingAnchor.constraint(
                equalTo: self.trailingAnchor,
                constant: -16
            ),
            mainStackView.bottomAnchor.constraint(
                equalTo: self.bottomAnchor,
                constant: -16
            )
        ])
    }
    
    func configure(contactModel: ContactModel) {
        nameLabel.text = contactModel.name
        phoneNumberLabel.text = contactModel.phoneNumber
    }
    
}
