//
//  Contact.swift
//  Contacts
//
//  Created by ikhut21 on 06.02.25.
//

import Foundation

struct ContactModel {
    let name: String
    let phoneNumber: String
    
    init(name: String, phoneNumber: String) {
        self.name = name
        self.phoneNumber = phoneNumber
    }
}
