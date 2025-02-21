//
//  DBHelper.swift
//  Notes
//
//  Created by ikhut21 on 20.02.25.
//

import CoreData
import UIKit

class DBHelper {
    
    private let context = DBManager.shared.persistentContainer.viewContext
    
    public func fetchNotes() -> [Note] {
        let fetchRequest: NSFetchRequest<Note> = Note.fetchRequest()
        do {
            return try context.fetch(fetchRequest)
        } catch {
            print("Failed to fetch notes: \(error.localizedDescription)")
        }
        return []
    }
    
    public func addNote(
        title: String,
        content: String
    ) {
        let note = Note(context: context)
        note.title = title
        note.content = content
        note.color = ColorHelper.colorToData(ColorHelper.getRandomColor())

        saveContextHelper()
    }
    
    public func updateNote(
        note: Note,
        title: String,
        content: String
    ) {
        note.title = title
        note.content = content

        saveContextHelper()
    }
    
    public func deleteNote(
        note: Note
    ) {
        context.delete(note)
        saveContextHelper()
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
