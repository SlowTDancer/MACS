//
//  RegisterViewController.swift
//  LRFP
//
//  Created by ikhut21 on 03.02.25.
//

import UIKit

class RegisterViewController: UIViewController {

    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Register"
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
   
    let yourNameLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter", size: 14)
        label.text = "Your Name"
        return label
    }()
    
    let yourNameTextField: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.borderStyle = .roundedRect
        textField.placeholder = "Input your first name"
        textField.layer.cornerRadius = 5.0
        return textField
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
    
    let emailErrorImageView: UIImageView = {
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
    
    lazy var emailErrorStack = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .horizontal
        stackView.addArrangedSubview(emailErrorImageView)
        stackView.addArrangedSubview(emailErrorLabel)
        let spacer = UIView()
        spacer.setContentHuggingPriority(.defaultLow, for: .horizontal)
        stackView.addArrangedSubview(spacer)
        stackView.distribution = .fill
        stackView.spacing = 2
        stackView.isHidden = true
        return stackView
    }()
    
    let passwordLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter", size: 14)
        label.text = "Password"
        return label
    }()
    
    let passwordTextField: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.borderStyle = .roundedRect
        textField.placeholder = "Input your password"
        textField.isSecureTextEntry = true
        textField.layer.cornerRadius = 5.0
        return textField
    }()

    let passwordEye: UIImageView = {
        let image = UIImageView()
        image.translatesAutoresizingMaskIntoConstraints = false
        image.contentMode = .scaleAspectFit
        image.tintColor = .black
        image.isHidden = false
        image.image = UIImage(systemName: "eye.slash")
        return image
    }()

    let passwordContainterView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = .clear
        return view
    }()
    
    let confirmPasswordLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter", size: 14)
        label.text = "Confirm Password"
        label.textColor = .black
        return label
    }()
    
    let confirmPasswordTextField: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.borderStyle = .roundedRect
        textField.placeholder = "Input confirm password"
        textField.isSecureTextEntry = true
        textField.layer.cornerRadius = 5.0
        return textField
    }()
    
    let confirmPasswordErrorImage: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.image = UIImage(systemName: "exclamationmark.circle.fill")
        imageView.contentMode = .scaleAspectFit
        imageView.tintColor = .red
        return imageView
    }()
    
    let confirmPasswordErrorLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter", size: 12)
        label.text = "The password you entered do not match."
        label.textColor = .red
        return label
    }()
    
    lazy var confirmPasswordErrorStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .horizontal
        stackView.addArrangedSubview(confirmPasswordErrorImage)
        stackView.addArrangedSubview(confirmPasswordErrorLabel)
        let spacer = UIView()
        spacer.setContentHuggingPriority(.defaultLow, for: .horizontal)
        stackView.addArrangedSubview(spacer)
        stackView.distribution = .fill
        stackView.spacing = 2
        stackView.isHidden = true
        return stackView
    }()
    

    let confirmPasswordEye: UIImageView = {
        let image = UIImageView()
        image.translatesAutoresizingMaskIntoConstraints = false
        image.contentMode = .scaleAspectFit
        image.tintColor = .black
        image.isHidden = false
        image.image = UIImage(systemName: "eye.slash")
        return image
    }()

    let confirmPasswordContainterView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = .clear
        return view
    }()
    
    let signUpButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Sign up", for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.backgroundColor = UIColor(named: "mainTintColor")
        button.layer.cornerRadius = 8
        return button
    }()
    
    lazy var yourNameStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(yourNameLabel)
        stackView.addArrangedSubview(yourNameTextField)
        return stackView
    }()

    lazy var emailStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(emailLabel)
        stackView.addArrangedSubview(emailTextField)
        stackView.addArrangedSubview(emailErrorStack)
        return stackView
    }()
    
    lazy var passwordStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(passwordLabel)
        stackView.addArrangedSubview(passwordContainterView)
        return stackView
    }()
    
    lazy var confirmPasswordStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(confirmPasswordLabel)
        stackView.addArrangedSubview(confirmPasswordContainterView)
        stackView.addArrangedSubview(confirmPasswordErrorStackView)
        return stackView
    }()
    
    lazy var mainStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(yourNameStackView)
        stackView.addArrangedSubview(emailStackView)
        stackView.addArrangedSubview(passwordStackView)
        stackView.addArrangedSubview(confirmPasswordStackView)
        stackView.addArrangedSubview(signUpButton)
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
//        yourNameTextField.delegate = self
        emailTextField.delegate = self
        passwordTextField.delegate = self
        confirmPasswordTextField.delegate = self
    }

    private func setUpView() {
        view.backgroundColor = .white
        setUpNavBar()
        setUpPasscodeEyes()
        addSubviews()
        setConstraints()
    }
    
    private func setUpNavBar() {
        navigationItem.titleView = titleLabel
        navigationItem.leftBarButtonItem = backButtonItem
    }

    private func setUpActions() {
        signUpButton.addTarget(self, action: #selector(signUpTapped), for: .touchUpInside)
    }
    
    private func isValidEmail(email: String) -> Bool {
        return email.contains("@")
    }
    
    private func passwordsChecker(password: String, confirmPassword: String) -> Bool {
        return password == confirmPassword && !password.isEmpty
    }

    @objc private func signUpTapped() {
        guard let name = yourNameTextField.text, !name.isEmpty else {
            return
        }
        guard let email = emailTextField.text, isValidEmail(email: email) else {
            return
        }
        guard let password = passwordTextField.text else {
            return
        }
        guard let confirmPassword = confirmPasswordTextField.text, passwordsChecker(password: password, confirmPassword: confirmPassword) else {
            return
        }
        
        self.navigationController?.pushViewController(
            SuccessViewController(
                .init(
                    pageTitle: "Register",
                    successTitle: "Registration Success!",
                    successDescription: "Now you can use your email and password to Sign In"
                )
            ),
            animated: true
        )
    }

    private func setUpPasscodeEyes() {
        passwordEye.isUserInteractionEnabled = true
        passwordEye.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(passwordEyeDidTap)))
        
        confirmPasswordEye.isUserInteractionEnabled = true
        confirmPasswordEye.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(confirmPasswordEyeDidTap)))
    }

    @objc private func passwordEyeDidTap() {
        passwordTextField.isSecureTextEntry = !passwordTextField.isSecureTextEntry
        passwordEye.image = passwordTextField.isSecureTextEntry ? UIImage(systemName: "eye.slash") : UIImage(systemName: "eye")
    }

    @objc private func confirmPasswordEyeDidTap() {
        confirmPasswordTextField.isSecureTextEntry = !confirmPasswordTextField.isSecureTextEntry
        confirmPasswordEye.image = confirmPasswordTextField.isSecureTextEntry ? UIImage(systemName: "eye.slash") : UIImage(systemName: "eye")
    }

    private func addSubviews() {
        view.addSubview(mainStackView)
        passwordContainterView.addSubview(passwordTextField)
        passwordContainterView.addSubview(passwordEye)
        confirmPasswordContainterView.addSubview(confirmPasswordTextField)
        confirmPasswordContainterView.addSubview(confirmPasswordEye)
    }

    private func setConstraints() {
        NSLayoutConstraint.activate([
            
            yourNameTextField.heightAnchor.constraint(equalToConstant: 54),
            
            signUpButton.heightAnchor.constraint(equalToConstant: 54),

            emailTextField.heightAnchor.constraint(equalToConstant: 54),
            emailErrorImageView.leadingAnchor.constraint(equalTo: emailErrorStack.leadingAnchor),

            passwordContainterView.heightAnchor.constraint(equalToConstant: 54),
                
            passwordTextField.trailingAnchor.constraint(
                equalTo: passwordContainterView.trailingAnchor
            ),
            passwordTextField.leadingAnchor.constraint(
                equalTo: passwordContainterView.leadingAnchor
            ),
            passwordTextField.topAnchor.constraint(
                equalTo: passwordContainterView.topAnchor
            ),
            passwordTextField.bottomAnchor.constraint(
                equalTo: passwordContainterView.bottomAnchor
            ),
            
            passwordEye.heightAnchor.constraint(equalToConstant: 22),
            passwordEye.widthAnchor.constraint(equalToConstant: 22),
            passwordEye.trailingAnchor.constraint(
                equalTo: passwordContainterView.trailingAnchor,
                constant: -6
            ),
            passwordEye.centerYAnchor.constraint(equalTo: passwordContainterView.centerYAnchor),

            confirmPasswordContainterView.heightAnchor.constraint(equalToConstant: 54),
                
            confirmPasswordTextField.trailingAnchor.constraint(
                equalTo: confirmPasswordContainterView.trailingAnchor
            ),
            confirmPasswordTextField.leadingAnchor.constraint(
                equalTo: confirmPasswordContainterView.leadingAnchor
            ),
            confirmPasswordTextField.topAnchor.constraint(
                equalTo: confirmPasswordContainterView.topAnchor
            ),
            confirmPasswordTextField.bottomAnchor.constraint(
                equalTo: confirmPasswordContainterView.bottomAnchor
            ),
            
            confirmPasswordEye.heightAnchor.constraint(equalToConstant: 22),
            confirmPasswordEye.widthAnchor.constraint(equalToConstant: 22),
            confirmPasswordEye.trailingAnchor.constraint(
                equalTo: confirmPasswordContainterView.trailingAnchor,
                constant: -6
            ),
            confirmPasswordEye.centerYAnchor.constraint(equalTo: confirmPasswordContainterView.centerYAnchor),

            mainStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 10),
            mainStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            mainStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
        ])
    }
    
    @objc func backButtonTapped() {
        navigationController?.popViewController(animated: true)
    }
}

extension RegisterViewController: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) {
//        guard let text = textField.text, !text.isEmpty else {
//            textField.layer.borderColor = UIColor.red.cgColor
//            textField.layer.borderWidth = 1.0
//            return
//        }
        if textField == emailTextField {
            guard let email = emailTextField.text, isValidEmail(email: email) else {
                emailTextField.layer.borderColor = UIColor.red.cgColor
                emailTextField.layer.borderWidth = 1.0
                emailErrorStack.isHidden = false
                return
            }
            emailErrorStack.isHidden = true
        } else if textField == confirmPasswordTextField || textField == passwordTextField {
            guard let password = passwordTextField.text,
                  let confirmPassword = confirmPasswordTextField.text,
                  passwordsChecker(password: password,confirmPassword: confirmPassword) else {
                confirmPasswordTextField.layer.borderColor = UIColor.red.cgColor
                confirmPasswordTextField.layer.borderWidth = 1.0
                confirmPasswordErrorStackView.isHidden = false
                return
            }
            confirmPasswordErrorStackView.isHidden = true
            confirmPasswordTextField.layer.borderWidth = 0
        }
        textField.layer.borderWidth = 0
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        view.endEditing(true)
    }
}

