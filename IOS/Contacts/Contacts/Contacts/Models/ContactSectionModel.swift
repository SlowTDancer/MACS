//
//  ContactSection.swift
//  Contacts
//
//  Created by ikhut21 on 06.02.25.
//

final class ContactSectionModel {

    var header: ContactHeaderModel
    var contacts: [ContactModel]
    
    var isExpanded: Bool {
        get { header.isExpanded }
        set { header.isExpanded = newValue }
    }
    var title: String { header.title }

    var contactCount: Int { isExpanded ? contacts.count : 0 }

    init(header: ContactHeaderModel, contacts: [ContactModel]) {
        self.header = header
        self.contacts = contacts
    }
}
