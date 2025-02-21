//
//  ContactHeader.swift
//  Contacts
//
//  Created by ikhut21 on 06.02.25.
//

final class ContactHeaderModel {

    var title: String
    var isExpanded: Bool
    var onExpand: () -> Void
    
    init(
        title: String,
        isExpanded: Bool = true,
        onExpand: @escaping () -> Void
    ) {
        self.title = title
        self.isExpanded = isExpanded
        self.onExpand = onExpand
    }
}
