//
//  CourseItemViewModel.swift
//  OLP
//
//  Created by Irakli Khutsishvili on 16.11.24.
//


import UIKit

public class CourseItemViewModel: ObservableObject {
    var courseImage: UIImage?
    var duration: String?
    var title: String?
    var progress: Float
    var isLocked: Bool

    public init(courseImage: UIImage? = nil,
                duration: String? = nil,
                title: String? = nil,
                progress: Float = 0.0,
                isLocked: Bool = false) {
        self.courseImage = courseImage
        self.duration = duration
        self.title = title
        self.progress = progress
        self.isLocked = isLocked
    }
}
