//
//  NoteViewController.swift
//  Notes
//
//  Created by ikhut21 on 18.02.25.
//

import UIKit

class NoteViewController: UIViewController {
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Edit Note"
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    lazy var backButtonItem: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(
            image: UIImage(systemName: "chevron.left"),
            style: .plain,
            target: self,
            action: #selector(backButtonTapped)
        )
        return buttonItem
    }()
    
    let noteTitleTextField: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.font = UIFont.systemFont(ofSize: 24, weight: .bold)
        textField.placeholder = "Enter title"
        textField.backgroundColor = .clear
        return textField
    }()
    
    private let placeholderLabel: UILabel = {
        let label = UILabel()
        label.text = "Enter note"
        label.font = UIFont.systemFont(ofSize: 20)
        label.textColor = .systemGray3
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    let contentTextView: UITextView = {
        let textView = UITextView()
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.font = UIFont.systemFont(ofSize: 20)
        textView.textColor = .black
        textView.backgroundColor = .clear
        textView.isScrollEnabled = true
        return textView
    }()
    
    private var currentNote: Note?
    private var isNewNote: Bool?
    private var dbHelper: DBHelper?
    weak var delegate: NoteViewControllerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpView()
        setConstraints()
    }
    
    init(dbHelper: DBHelper, isNewNote: Bool) {
        super.init(nibName: nil, bundle: nil)
        self.dbHelper = dbHelper
        self.isNewNote = isNewNote
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    private func setUpView() {
        view.backgroundColor = UIColor(
            red: 200/255.0,
            green: 240/255.0,
            blue: 255/255.0,
            alpha: 1.0
        )
        
        contentTextView.delegate = self
        
        navigationItem.titleView = titleLabel
        navigationItem.leftBarButtonItem = backButtonItem
        
        view.addSubview(placeholderLabel)
        view.addSubview(noteTitleTextField)
        view.addSubview(contentTextView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            
            noteTitleTextField.topAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.topAnchor,
                constant: 20
            ),
            noteTitleTextField.leadingAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.leadingAnchor,
                constant: 20
            ),
            noteTitleTextField.trailingAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.trailingAnchor,
                constant: -20
            ),
            
            contentTextView.topAnchor.constraint(
                equalTo: noteTitleTextField.bottomAnchor,
                constant: 0
            ),
            contentTextView.leadingAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.leadingAnchor,
                constant: 20
            ),
            contentTextView.trailingAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.trailingAnchor,
                constant: -20
            ),
            contentTextView.bottomAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.bottomAnchor,
                constant: -20
            ),
            
            placeholderLabel.topAnchor.constraint(
                equalTo: contentTextView.topAnchor,
                constant: 10
            ),
            placeholderLabel.leadingAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.leadingAnchor,
                constant: 25
            )
        ])
    }
    
    @objc func backButtonTapped() {
        if isNewNote! {
            if !isEmptyNote() {
                dbHelper!.addNote(
                    title: noteTitleTextField.text!,
                    content: contentTextView.text
                )
            }
        } else {
            if isEmptyNote() {
                dbHelper!.deleteNote(note: currentNote!)
            } else {
                dbHelper!.updateNote(
                    note: currentNote!,
                    title: noteTitleTextField.text!,
                    content: contentTextView.text
                )
            }
        }
        
        delegate?.didUpdateNotes()
        navigationController?.popViewController(animated: true)
    }
    
    func configure(with note: Note) {
        currentNote = note
        
        noteTitleTextField.text = note.title
        noteTitleTextField.textColor = .black
        
        contentTextView.text = note.content
        contentTextView.textColor = .black
        
        placeholderLabel.isHidden = !note.content!.isEmpty
    }
    
}

protocol NoteViewControllerDelegate: AnyObject {
    
    func didUpdateNotes()
    
}

extension NoteViewController {
    
    private func isEmptyNote() -> Bool {
        return contentTextView.text!.isEmpty && noteTitleTextField.text!.isEmpty
    }
    
}

extension NoteViewController: UITextViewDelegate {
    
    func textViewDidChange(_ textView: UITextView) {
        placeholderLabel.isHidden = !textView.text.isEmpty
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        placeholderLabel.isHidden = !textView.text.isEmpty
    }
    
}
