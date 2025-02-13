//
//  ForgotPasswordViewController.swift
//  LRFP
//
//  Created by ikhut21 on 03.02.25.
//

import UIKit

class ForgotPasswordViewController: UIViewController {

     let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Forgot Password"
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    lazy var backButtonItem: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(image: UIImage(systemName: "chevron.left"),
                                             style: .plain,
                                             target: self,
                                             action: #selector(backButtonTapped))
        return buttonItem
    }()

    let lockImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.image = UIImage(named: "Lock")
        imageView.contentMode = .scaleAspectFit
        return imageView
    }()

    let forgotYourPasswordLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.text = "Forgot your password?"
        label.font = UIFont(name: "Inter-Bold", size: 20)
        label.numberOfLines = 1
        return label
    }()

    let instructionLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = UIFont(name: "Inter-Regular", size: 14)
        label.text = "Enter your registered email below to receive password reset instruction"
        label.numberOfLines = 0
        label.textAlignment = .center
        return label
    }()
    
    lazy var labelStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(forgotYourPasswordLabel)
        stackView.addArrangedSubview(instructionLabel)
        stackView.spacing = 12
        stackView.alignment = .center
        return stackView
    }()
    
    lazy var infoStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(lockImageView)
        stackView.addArrangedSubview(labelStackView)
        stackView.spacing = 36
        return stackView
    }()

    let emailLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter", size: 14)
        label.text = "Email"
        return label
    }()
    
    let emailTextField: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.borderStyle = .roundedRect
        textField.placeholder = "Input email address"
        textField.layer.cornerRadius = 5.0
        textField.contentMode = .scaleAspectFill
        return textField
    }()
    
    let emailErrorImage: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.image = UIImage(systemName: "exclamationmark.circle.fill")
        imageView.contentMode = .scaleAspectFit
        imageView.tintColor = .red
        return imageView
    }()
    
    let emailErrorLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter", size: 12)
        label.text = "Please provide a valid email address."
        label.textColor = .red
        return label
    }()
    
    lazy var emailErrorStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .horizontal
        stackView.addArrangedSubview(emailErrorImage)
        stackView.addArrangedSubview(emailErrorLabel)
        let spacer = UIView()
        spacer.setContentHuggingPriority(.defaultLow, for: .horizontal)
        stackView.addArrangedSubview(spacer)
        stackView.distribution = .fill
        stackView.spacing = 2
        stackView.isHidden = true
        return stackView
    }()
    
    lazy var emailStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(emailLabel)
        stackView.addArrangedSubview(emailTextField)
        stackView.addArrangedSubview(emailErrorStackView)
        return stackView
    }()

    let sendButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Send", for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.backgroundColor = UIColor(named: "mainTintColor")
        button.layer.cornerRadius = 8
        return button
    }()
    
    lazy var sendEmailStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(emailStackView)
        stackView.addArrangedSubview(sendButton)
        stackView.spacing = 24
        return stackView
    }()
    
    lazy var mainStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(infoStackView)
        stackView.addArrangedSubview(sendEmailStackView)
        stackView.spacing = 36
        return stackView
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setDelegates()
        setUpView()
        setUpActions()
    }
    
    private func setDelegates() {
        emailTextField.delegate = self
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
    
    @objc func backButtonTapped() {
        navigationController?.popViewController(animated: true)
    }
    
    private func addSubviews() {
        view.addSubview(mainStackView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            lockImageView.heightAnchor.constraint(equalToConstant: 64),
            lockImageView.widthAnchor.constraint(equalToConstant: 64),
            
            sendButton.heightAnchor.constraint(equalToConstant: 54),
            
            emailTextField.heightAnchor.constraint(equalToConstant: 54),
            
            mainStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 27),
            mainStackView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            mainStackView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16)
        ])
    }
    
    private func setUpActions() {
        sendButton.addTarget(self, action: #selector(sendTapped), for: .touchUpInside)
    }
    
    private func isValidEmail(email: String) -> Bool {
        return email.contains("@")
    }
    
    @objc private func sendTapped() {
        guard let email = emailTextField.text, isValidEmail(email: email) else {
            
            return
        }
        self.navigationController?.pushViewController(
            SuccessViewController(
                .init(
                    pageTitle: "Forgot Password",
                    successTitle: "We have sent a password recover intructions to your email",
                    successDescription: "Did not recive the email? check you spam filter or resend"
                )),
            animated: true
        )
    }
}

extension ForgotPasswordViewController: UITextFieldDelegate {
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        textField.backgroundColor = .white
        guard let email = emailTextField.text, isValidEmail(email: email) else {
            emailTextField.layer.borderColor = UIColor.red.cgColor
            emailTextField.layer.borderWidth = 1.0
            emailErrorStackView.isHidden = false
            return
        }
        emailErrorStackView.isHidden = true
        textField.layer.borderWidth = 0
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.backgroundColor = UIColor(named: "textFieldBackgroundColor")
        textField.layer.borderColor = UIColor(named: "mainTintColor")?.cgColor
        textField.layer.borderWidth = 1.0
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        view.endEditing(true) 
    }
}
