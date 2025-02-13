//
//  LoginViewController.swift
//  LRFP
//
//  Created by ikhut21 on 03.02.25.
//

import UIKit

class LoginViewController: UIViewController {
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Login"
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    let emailLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter-Regular", size: 14)
        label.text = "Email address"
        return label
    }()
    
    let passwordLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter-Regular", size: 14)
        label.text = "Password"
        return label
    }()
    
    let emailTextField: UITextField = {
        let textfield = UITextField()
        textfield.translatesAutoresizingMaskIntoConstraints = false
        textfield.borderStyle = .roundedRect
        textfield.placeholder = "Input email address"
        return textfield
    }()
    
    let passwordTextField: UITextField = {
        let textfield = UITextField()
        textfield.translatesAutoresizingMaskIntoConstraints = false
        textfield.borderStyle = .roundedRect
        textfield.placeholder = "Input your password"
        textfield.isSecureTextEntry = true
        return textfield
    }()
    
    let passcodeEye: UIImageView = {
        let image = UIImageView()
        image.translatesAutoresizingMaskIntoConstraints = false
        image.contentMode = .scaleAspectFit
        image.tintColor = .black
        image.isHidden = false
        image.image = UIImage(systemName: "eye.slash")
        return image
    }()
    
    let passwordContainterView : UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = .clear
        return view
    }()
    
    let loginButton: UIButton  = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Login", for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.backgroundColor = UIColor(named: "mainTintColor")
        button.layer.cornerRadius = 8
        return button
    }()
    
    let forgotPasswordButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.backgroundColor = .clear
        button.setTitle("Forgot Password?", for: .normal)
        button.setTitleColor(UIColor(named: "mainTintColor"), for: .normal)
        return button
    }()
    
    let noAccountLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.font = UIFont(name: "Inter-Bold", size: 16)
        label.text = "You don't have an account?"
        return label
    }()
    
    let signUpButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.backgroundColor = .clear
        button.setTitle("Sign up", for: .normal)
        button.titleLabel?.font = UIFont(name: "Inter-Regular", size: 16)
        button.setTitleColor(UIColor(named: "mainTintColor"), for: .normal)
        return button
    }()
    
    lazy var emailStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(emailLabel)
        stackView.addArrangedSubview(emailTextField)
        return stackView
    }()
    
    lazy var passwordStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(passwordLabel)
        stackView.addArrangedSubview(passwordContainterView)
        return stackView
    }()
    
    lazy var textFieldsStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(emailStackView)
        stackView.addArrangedSubview(passwordStackView)
        stackView.spacing = 14
        return stackView
    }()
    
    lazy var loginStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(textFieldsStackView)
        stackView.addArrangedSubview(loginButton)
        stackView.addArrangedSubview(forgotPasswordButton)
        stackView.spacing = 36
        return stackView
    }()
    
    lazy var signUpStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .horizontal
        stackView.addArrangedSubview(noAccountLabel)
        stackView.addArrangedSubview(signUpButton)
        stackView.spacing = 4
        return stackView
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        setUpView()
        setUpActions()
    }


    private func setUpView() {
        view.backgroundColor = .white
        navigationItem.titleView = titleLabel
        setUpPasscodeEye()
        addSubviews()
        setConstraints()
    }
    
    private func setUpActions() {
        forgotPasswordButton.addTarget(self, action: #selector(forgotTapped), for: .touchUpInside)
        signUpButton.addTarget(self, action: #selector(signUpTapped), for: .touchUpInside)
    }
    
    
    @objc private func forgotTapped() {
        self.navigationController?.pushViewController(ForgotPasswordViewController(), animated: true)
    }
    
    @objc private func signUpTapped() {
        self.navigationController?.pushViewController(RegisterViewController(), animated: true)
    }
    
    private func setUpPasscodeEye() {
        passcodeEye.isUserInteractionEnabled = true
        passcodeEye.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTap)))
    }
    
    @objc private func didTap() {
        passwordTextField.isSecureTextEntry = !passwordTextField.isSecureTextEntry
        passcodeEye.image = passwordTextField.isSecureTextEntry ? UIImage(systemName: "eye.slash") : UIImage(systemName: "eye")
    }
        
    
    private func addSubviews() {
        view.addSubview(loginStackView)
        view.addSubview(signUpStackView)
        passwordContainterView.addSubview(passwordTextField)
        passwordContainterView.addSubview(passcodeEye)
        
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            
            emailTextField.heightAnchor.constraint(equalToConstant: 54),
            
            loginButton.heightAnchor.constraint(equalToConstant: 54),

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
            
            passcodeEye.heightAnchor.constraint(equalToConstant: 22),
            passcodeEye.widthAnchor.constraint(equalToConstant: 22),
            passcodeEye.trailingAnchor.constraint(
                equalTo: passwordContainterView.trailingAnchor,
                constant: -6
            ),
            passcodeEye.centerYAnchor.constraint(equalTo: passwordContainterView.centerYAnchor),
            
            loginStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 36),
            loginStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            loginStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
                        
            signUpStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            signUpStackView.centerXAnchor.constraint(equalTo: view.centerXAnchor)
        ])
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        view.endEditing(true)
    }
}

