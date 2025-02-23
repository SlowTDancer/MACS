//
//  DBHelper.swift
//  WeatherApplication
//
//  Created by ikhut21 on 22.02.25.
//

import CoreData
import UIKit

class DBHelper {
    
    let context = DBManager.shared.persistentContainer.viewContext
    
    public func fetchLocations() -> [Location] {
        let fetchRequest: NSFetchRequest<Location> = Location.fetchRequest()
        do {
            return try context.fetch(fetchRequest)
        } catch {
            print("Failed to fetch notes: \(error.localizedDescription)")
        }
        return []
    }
    
    public func addLocation(
        countryCode: String,
        city: String
    ) {
        let location = Location(context: context)
        
        location.countryCode = countryCode
        location.city = city
        
        saveContextHelper()
    }
    
    public func deleteLocation(
        locaion: Location
    ) {
        context.delete(locaion)
        
        saveContextHelper()
    }
    
    public func fetchLocationModels() -> [LocationModel] {
        let locations = fetchLocations()
        return locations.map { location in
            LocationModel(
                city: location.city ?? "",
                countryCode: location.countryCode ?? "",
                location: location
            )
        }
    }
    
    private func saveContextHelper() {
        if context.hasChanges {
            do {
                try context.save()
            } catch {
                print("Failed to save Core Data: \(error.localizedDescription)")
            }
        }
    }
    
}
