//
//  Location+CoreDataProperties.swift
//  WeatherApplication
//
//  Created by ikhut21 on 22.02.25.
//
//

import Foundation
import CoreData


extension Location {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<Location> {
        return NSFetchRequest<Location>(entityName: "Location")
    }

    @NSManaged public var countryCode: String?
    @NSManaged public var city: String?

}

extension Location : Identifiable {

}
