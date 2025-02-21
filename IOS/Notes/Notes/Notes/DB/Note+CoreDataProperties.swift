//
//  Note+CoreDataProperties.swift
//  Notes
//
//  Created by ikhut21 on 19.02.25.
//
//

import Foundation
import CoreData


extension Note {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<Note> {
        return NSFetchRequest<Note>(entityName: "Note")
    }

    @NSManaged public var title: String?
    @NSManaged public var content: String?
    @NSManaged public var color: Data?

}

extension Note : Identifiable {

}
