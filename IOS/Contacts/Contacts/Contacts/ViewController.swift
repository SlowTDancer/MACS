//
//  ViewController.swift
//  Contacts
//
//  Created by ikhut21 on 06.02.25.
//

import UIKit

class ViewController: UIViewController {
    
    let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Contacts"
        label.font = UIFont(name: "Inter-Bold", size: 18)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    lazy var plusButtonItem: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(
            image: UIImage(systemName: "plus"),
            style: .plain,
            target: self,
            action: #selector(handleAdd)
        )
        return buttonItem
    }()
    
    lazy var layoutButtonItem: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(
            image: UIImage(systemName: "square.grid.3x3.fill"),
            style: .plain,
            target: self,
            action: #selector(changeLayout)
        )
        return buttonItem
    }()
    
    private lazy var flowLayout: UICollectionViewFlowLayout = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        return layout
    }()
    
    var isTableViewActive = true
    
    lazy var tableView: UITableView = UITableView(frame: view.bounds)
    lazy var collectionView: UICollectionView = UICollectionView(
        frame: view.bounds,
        collectionViewLayout: UICollectionViewFlowLayout()
    )
    
    lazy var data: [ContactSectionModel] = [
        ContactSectionModel(
            header: ContactHeaderModel(
                title: "A",
                onExpand: { [unowned self] in
                    self.handleExpand(for: "A")
                }
            ),
            contacts: [
                ContactModel(
                    name: "Ana",
                    phoneNumber: "111"
                ),
                ContactModel(
                    name: "Aka",
                    phoneNumber: "112"
                ),
            ]
        ),
        ContactSectionModel(
            header: ContactHeaderModel(
                title: "R",
                onExpand: { [unowned self] in
                    handleExpand(for: "R")
                }
            ),
            contacts: [
                ContactModel(
                    name: "Ruska",
                    phoneNumber: "333"
                )
            ]
        ),
    ]
    
    var alertNameFilled = false
    var alertPhoneFilled = false
    var addAction: UIAlertAction?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpNavBar()
        setUpView()
        configureTableView()
        configureCollectionView()
        addSubviews()
        setConstraints()
    }
    
    private func setUpNavBar() {
        navigationItem.titleView = titleLabel
        navigationItem.leftBarButtonItem = plusButtonItem
        navigationItem.rightBarButtonItem = layoutButtonItem
    }
    
    private func setUpView() {
        view.backgroundColor = UIColor.lightGray
    }
    
    private func configureTableView() {
        tableView.sectionHeaderTopPadding = 0
        
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.register(
            UINib(nibName: "ContactTableViewCell", bundle: .main),
            forCellReuseIdentifier: "ContactTableViewCell"
        )
        
        tableView.register(
            UINib(nibName: "ContactTableViewHeader", bundle: .main),
            forHeaderFooterViewReuseIdentifier: "ContactTableViewHeader"
        )
    }
    
    private func configureCollectionView() {
        collectionView.delegate = self
        collectionView.dataSource = self
        
        collectionView.collectionViewLayout = flowLayout
        
        collectionView.register(
            UINib(nibName: "ContactCollectionViewCell", bundle: .main),
            forCellWithReuseIdentifier: "ContactCollectionViewCell"
        )
        
        collectionView.register(
            UINib(nibName: "ContactCollectionViewHeader", bundle: .main),
            forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader,
            withReuseIdentifier: "ContactCollectionViewHeader"
        )
        
        collectionView.addGestureRecognizer(
            UILongPressGestureRecognizer(
                target: self,
                action: #selector(handleLongPress)
            )
        )
    }
    
    private func addSubviews() {
        view.addSubview(tableView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            
        ])
    }
    
    @objc private func handleAdd() {
        
        alertNameFilled = false
        alertPhoneFilled = false
        
        let alert = UIAlertController(
            title: "Add Contact",
            message: "Add Contact",
            preferredStyle: .alert
        )
    
        alert.addTextField { textField in
            textField.placeholder = "Name"
            textField.addTarget(
                self,
                action: #selector(self.handleNameField),
                for: .editingChanged
            )
        }
        
        alert.addTextField { textField in
            textField.placeholder = "Phone Number"
            textField.keyboardType = .phonePad
            textField.addTarget(
                self,
                action: #selector(self.handlePhoneField),
                for: .editingChanged
            )
        }
        
        addAction = UIAlertAction(
            title: "Add",
            style: .default,
            handler: { [unowned self] _ in
                if let text = alert.textFields?[0].text,
                    let phoneNumber = alert.textFields?[1].text {
                    addContact(name: text, phoneNumber: phoneNumber)
                }
            }
        )
        addAction!.isEnabled = false
        
        alert.addAction(addAction!)
        alert.addAction(UIAlertAction(
            title: "Cancel",
            style: .cancel,
            handler: nil
        ))
        
        present(alert, animated: true)
    }
    
    @objc private func changeLayout() {
        isTableViewActive.toggle()

        if isTableViewActive {
            collectionView.removeFromSuperview()
            view.addSubview(tableView)
            layoutButtonItem.image = UIImage(systemName: "square.grid.3x3.fill")
        } else {
            tableView.removeFromSuperview()
            view.addSubview(collectionView)
            layoutButtonItem.image = UIImage(systemName: "list.bullet")
        }
    }
    
    @objc private func handleNameField(_ sender: UITextField) {
        guard let res = sender.text?.isEmpty else {
            return
        }
        alertNameFilled = !res
        addAction!.isEnabled = alertNameFilled && alertPhoneFilled
    }
    
    @objc private func handlePhoneField(_ sender: UITextField) {
        guard let res = sender.text?.isEmpty else {
            return
        }
        alertPhoneFilled = !res
        addAction!.isEnabled = alertNameFilled && alertPhoneFilled
    }
    
    
    @objc private func handleLongPress(_ gesture: UILongPressGestureRecognizer) {
        let location = gesture.location(in: collectionView)
        if let indexPath = collectionView.indexPathForItem(at: location) {
            deleteItem(at: indexPath)
        }
    }
}

extension ViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return data.count
    }
    
    func tableView(
        _ tableView: UITableView,
        numberOfRowsInSection section: Int
    ) -> Int {
        return data[section].contactCount
    }
    
    func tableView(
        _ tableView: UITableView,
        viewForHeaderInSection section: Int
    ) -> UIView? {
        let headerView = tableView.dequeueReusableHeaderFooterView(
            withIdentifier: "ContactTableViewHeader"
        )
        
        if let contactHeader = headerView as? ContactTableViewHeader {
            contactHeader.configure(contactHeaderModel: getHeader(at: section))
        }
        
        return headerView
    }
    
    func tableView(
        _ tableView: UITableView,
        cellForRowAt indexPath: IndexPath
    ) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(
            withIdentifier: "ContactTableViewCell",
            for: indexPath
        )
        
        if let contactCell = cell as? ContactTableViewCell {
            contactCell.configure(
                contactModel: getContact(at: indexPath)
            )
        }
        
        return cell
    }
    
    func tableView(
        _ tableView: UITableView,
        leadingSwipeActionsConfigurationForRowAt indexPath: IndexPath
    ) -> UISwipeActionsConfiguration? {
        return deleteOperation(at: indexPath)
    }
    
    func tableView(
        _ tableView: UITableView,
        trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath
    ) -> UISwipeActionsConfiguration? {
        return deleteOperation(at: indexPath)
    }
    
    private func deleteOperation(
        at indexPath: IndexPath
    ) -> UISwipeActionsConfiguration?{
        let delete = UIContextualAction(
            style: .destructive,
            title: "Delete",
            handler: { [unowned self] _, _, _ in
                deleteContact(at: indexPath)
            }
        )
        let actions: [UIContextualAction] = [
            delete
        ]
        
        let config = UISwipeActionsConfiguration(actions: actions)
        config.performsFirstActionWithFullSwipe = true
        return config
    }
}

extension ViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return data.count
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        return data[section].contactCount
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: "ContactCollectionViewCell",
            for: indexPath
        )
        
        if let contactCell = cell as? ContactCollectionViewCell {
            contactCell.configure(
                contactModel: getContact(at: indexPath)
            )
        }

        return cell
    }
    
    
    func collectionView(
        _ collectionView: UICollectionView,
        viewForSupplementaryElementOfKind kind: String,
        at indexPath: IndexPath
    ) -> UICollectionReusableView {
        let header = collectionView.dequeueReusableSupplementaryView(
            ofKind: kind,
            withReuseIdentifier: "ContactCollectionViewHeader",
            for: indexPath
        )
        
        if let contactHeader = header as? ContactCollectionViewHeader {
            contactHeader.configure(
                contactHeaderModel: getHeader(at: indexPath.section)
            )
        }
        
        return header
    }
}

extension ViewController: UICollectionViewDelegateFlowLayout {
    
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        let spacings = (spacing * 2) + (spacing * CGFloat(itemsInRow-1))
        let spareWidth = collectionView.frame.width - spacings
        let itemSize = spareWidth / CGFloat(itemsInRow)
        return CGSize(width: itemSize, height: itemSize)
    }

    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        minimumLineSpacingForSectionAt section: Int
    ) -> CGFloat {
        return spacing
    }

    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        minimumInteritemSpacingForSectionAt section: Int
    ) -> CGFloat {
        return data[section].contactCount == 0 ? 0 : spacing
    }

    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        referenceSizeForHeaderInSection section: Int
    ) -> CGSize {
        return data[section].contactCount == 0
        ? CGSize(width: 0, height: ViewController.headerHeight)
        : CGSize(
            width: collectionView.frame.width,
            height: CGFloat(ViewController.headerHeight)
        )
    }

    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        insetForSectionAt section: Int
    ) -> UIEdgeInsets {
        return data[section].contactCount == 0
        ? UIEdgeInsets(
            top: 0,
            left: 0,
            bottom: 0,
            right: 0
        )
        : UIEdgeInsets(
            top: spacing,
            left: spacing,
            bottom: spacing,
            right: spacing
        )
    }
    
}

extension ViewController {
    
    private var spacing: CGFloat { 20 }
    private var itemsInRow: Int { 3 }
    public static var headerHeight: Int { 30 }
    
}

extension ViewController {

    func handleExpand(for title: String) {
        let sectionIndex = data.firstIndex { $0.title == title }
        if let sectionIndex {
            data[sectionIndex].isExpanded.toggle()
            let indexSet = IndexSet(integer: sectionIndex)
            
            tableView.reloadSections(indexSet, with: .automatic)
            collectionView.reloadSections(indexSet)
        }
    }
    
    private func addContact(name: String, phoneNumber: String) {
        guard let title = name.first?.uppercased() else { return }
        
        let contactModel = ContactModel(name: name, phoneNumber: phoneNumber)
        let sectionIndex = data.firstIndex(
            where: {title.first == $0.header.title.first}
        )
        
        if let sectionIndex {
            createRow(
                sectionIndex: sectionIndex,
                contactModel: contactModel
            )
        } else {
            createSection(
                title: title,
                contactModel: contactModel
            )
        }
    }
    
    private func createRow(sectionIndex: Int, contactModel: ContactModel) {
        data[sectionIndex].contacts.append(contactModel)
        data[sectionIndex].contacts.sort {$0.name < $1.name}
        if let newIndex = data[sectionIndex].contacts.firstIndex(
            where: {$0.name == contactModel.name}
        ){
            let newRow = IndexPath(row: newIndex, section: sectionIndex)
            
            if data[sectionIndex].isExpanded {
                tableView.insertRows(at: [newRow], with: .automatic)
                collectionView.insertItems(at: [newRow])
            }
        }
    }
    
    private func createSection(title: String, contactModel: ContactModel) {
        let section = ContactSectionModel(
            header: ContactHeaderModel(
                title: title,
                onExpand: { [unowned self] in
                    handleExpand(for: title)
                }
            ),
            contacts: [contactModel]
        )

        data.append(section)
        data.sort(by: {$0.header.title < $1.header.title})
        
        let newIndex = data.firstIndex { $0.title == title }
        if let newIndex {
            let newSection = IndexSet(integer: newIndex)
            
            tableView.insertSections(newSection, with: .automatic)
            collectionView.insertSections(newSection)
        }
    }
    
    private func deleteContact(at indexPath: IndexPath) {
        removeContact(at: indexPath)
    }
    
    private func deleteItem(at indexPath: IndexPath) {
        let alert = UIAlertController(
            title: "Delete?",
            message: "Are you sure you want to delete \(getContact(at: indexPath).name) from \n your contacts?",
            preferredStyle: .actionSheet
        )

        alert.addAction(
            UIAlertAction(
                title: "Delete",
                style: .destructive,
                handler: { [unowned self] _ in
                    removeContact(at: indexPath)
                }
            )
        )
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
        present(alert, animated: true)
    }
}

extension ViewController {
    
    private func getHeader(at section: Int) -> ContactHeaderModel {
        return data[section].header
    }
    
    private func getContact(at indexPath: IndexPath) -> ContactModel {
        return data[indexPath.section].contacts[indexPath.row]
    }
    
    private func removeContact(at indexPath: IndexPath) {
        if data[indexPath.section].contacts.count == 1 {
            data.remove(at: indexPath.section)
            let section = IndexSet(integer: indexPath.section)
            
            tableView.deleteSections(section, with: .automatic)
            collectionView.deleteSections(section)
        } else {
            data[indexPath.section].contacts.remove(at: indexPath.row)
            
            tableView.deleteRows(at: [indexPath], with: .automatic)
            collectionView.deleteItems(at: [indexPath])
        }
    }
    
}
