//
//  DBManager.swift
//  WeatherApplication
//
//  Created by ikhut21 on 22.02.25.
//

import CoreData
import UIKit

final class DBManager {
    
    static let shared = DBManager()
    
    private init() {}
    
    lazy var persistentContainer: NSPersistentContainer = {
        let container = NSPersistentContainer(name: "WeatherApplication")
        container.loadPersistentStores(
            completionHandler: { (storeDescription, error) in
                if let error = error as NSError? {
        
                    fatalError("Unresolved error \(error), \(error.userInfo)")
                }
        })
        return container
    }()

    func saveContext() {
        let context = persistentContainer.viewContext
        if context.hasChanges {
            do {
                try context.save()
            } catch {
                let nserror = error as NSError
                fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
            }
        }
    }
    
}
