//
//  ViewController.swift
//  Notes
//
//  Created by ikhut21 on 18.02.25.
//

import UIKit
import CoreData

class ViewController: UIViewController {

    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Recent Notes"
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    lazy var addNoteButton: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(
            image: UIImage(systemName: "plus"),
            style: .plain,
            target: self,
            action: #selector(addButtonTapped)
        )
        return buttonItem
    }()
    
    private lazy var layout: PinterestLayout = {
        let layout = PinterestLayout()
        layout.columnsCount = 2
        layout.contentPadding = PinterestLayout.Padding(horizontal: 5, vertical: 5)
        layout.cellsPadding = PinterestLayout.Padding(horizontal: 10, vertical: 10)
        return layout
    }()
    
    lazy var collectionView: UICollectionView = UICollectionView(
        frame: view.bounds,
        collectionViewLayout: UICollectionViewFlowLayout()
    )
    
    private let dbHelper = DBHelper()
    var data: [Note] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        getData()
        setUpView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        collectionView.collectionViewLayout.invalidateLayout()
    }
    
    private func getData() {
        data = dbHelper.fetchNotes()
    }
    
    private func setUpView() {
        view.backgroundColor = UIColor(
            red: 200/255.0,
            green: 240/255.0,
            blue: 255/255.0,
            alpha: 1.0
        )
        
        setUpNavBar()
        setUpCollectionView()
        addSubviews()
    }
    
    private func setUpNavBar() {
        navigationItem.titleView = titleLabel
        navigationItem.leftBarButtonItem = addNoteButton
    }
    
    private func setUpCollectionView() {
        collectionView.backgroundColor = UIColor(
            red: 200/255.0,
            green: 240/255.0,
            blue: 255/255.0,
            alpha: 1.0
        )
        
        collectionView.delegate = self
        collectionView.dataSource = self
        
        collectionView.collectionViewLayout = layout
        
        collectionView.register(
            UINib(nibName: "NoteViewCell", bundle: .main),
            forCellWithReuseIdentifier: "NoteViewCell"
        )
        
        collectionView.addGestureRecognizer(
            UILongPressGestureRecognizer(
                target: self,
                action: #selector(handleLongPress)
            )
        )
        
        collectionView.addGestureRecognizer(
            UITapGestureRecognizer(
                target: self,
                action: #selector(handleTap)
            )
        )
    }
    
    private func addSubviews() {
        view.addSubview(collectionView)
    }

    @objc private func addButtonTapped() {
        let noteVC = NoteViewController(dbHelper: dbHelper, isNewNote: true)
        noteVC.delegate = self
        navigationController?.pushViewController(
            noteVC,
            animated: true
        )
    }
    
    @objc private func handleLongPress(_ gesture: UILongPressGestureRecognizer) {
        let location = gesture.location(in: collectionView)
        if let indexPath = collectionView.indexPathForItem(at: location) {
            deleteItem(at: indexPath)
        }
    }
    
    @objc private func handleTap(_ gesture: UITapGestureRecognizer) {
        let location = gesture.location(in: collectionView)
        if let indexPath = collectionView.indexPathForItem(at: location) {
            let noteVC = NoteViewController(dbHelper: dbHelper, isNewNote: false)
            noteVC.delegate = self
            noteVC.configure(with: getNote(at: indexPath))
            navigationController?.pushViewController(
                noteVC,
                animated: true
            )
        }
    }
    
}

extension ViewController: NoteViewControllerDelegate {
    
    func didUpdateNotes() {
        getData()
        let section = IndexSet(integer: 0)
        collectionView.reloadSections(section)
    }
}

extension ViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        return data.count
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: "NoteViewCell",
            for: indexPath
        )
        
        if let noteCell = cell as? NoteViewCell {
            noteCell.configure(note: getNote(at: indexPath))
        }

        return cell
    }
}

extension ViewController {
    
    private var spacing: CGFloat { 20 }
    private var itemsInRow: Int { 2 }
    
}

extension ViewController {

    private func deleteItem(at indexPath: IndexPath) {
        let alert = UIAlertController(
            title: "Delete?",
            message: "Are you sure you want to delete this note?",
            preferredStyle: .actionSheet
        )

        alert.addAction(
            UIAlertAction(
                title: "Delete",
                style: .destructive,
                handler: { [unowned self] _ in
                    deleteNote(at: indexPath)
                }
            )
        )

        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
        present(alert, animated: true)
    }

    private func deleteNote(at indexPath: IndexPath) {
        let noteToDelete = getNote(at: indexPath)

        dbHelper.deleteNote(note: noteToDelete)
        
        data.remove(at: indexPath.row)
        collectionView.deleteItems(at: [indexPath])
        
        getData()
        let section = IndexSet(integer: 0)
        collectionView.reloadSections(section)
    }

}

extension ViewController {
    
    private func getNote(at indexPath: IndexPath) -> Note {
        return data[indexPath.row]
    }
    
}
