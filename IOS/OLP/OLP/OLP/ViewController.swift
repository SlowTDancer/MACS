//
//  ViewController.swift
//  OLP
//
//  Created by Irakli Khutsishvili on 15.11.24.
//

import UIKit

class ViewController: UIViewController {
    
    var uniLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 16)
        label.textColor = .white
        label.text = "FreeUni"
        label.lineBreakMode = .byClipping
        label.textAlignment = .left
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    var courseTitle: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 24, weight: .bold)
        label.textColor = .white
        label.text = "Mobile Development on iOS"
        label.lineBreakMode = .byClipping
        label.textAlignment = .left
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    var lecturerName: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 12)
        label.textColor = .white
        label.text = "by Saba Khutsishvili"
        label.lineBreakMode = .byClipping
        label.textAlignment = .left
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    var backgroundImage : UIImageView = {
        let imageView = UIImageView(image: UIImage(named: "Background"))
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    let courseList: CourseList = {
        let list = CourseList(frame: .zero)
        list.translatesAutoresizingMaskIntoConstraints = false
        return list
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        setUp()
        let courseViewModels = createCourseViewModels()
        courseList.configure(with: courseViewModels)
    }
    
    private func setUp() {
        addSubviews()
        setUpConstraints()
        
    }
    
    private func addSubviews() {
        view.addSubview(uniLabel)
        view.addSubview(courseTitle)
        view.addSubview(lecturerName)


        view.addSubview(backgroundImage)
        view.addSubview(courseList)
        
        view.bringSubviewToFront(courseList)
        view.bringSubviewToFront(courseTitle)
        view.bringSubviewToFront(lecturerName)
        view.bringSubviewToFront(uniLabel)
    }
    
    private func setUpConstraints() {
        NSLayoutConstraint.activate([
            
            
            backgroundImage.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            backgroundImage.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            backgroundImage.topAnchor.constraint(equalTo: view.topAnchor),
            
            courseTitle.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            courseTitle.topAnchor.constraint(equalTo: view.topAnchor, constant: 116),
            
            lecturerName.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            lecturerName.topAnchor.constraint(equalTo: courseTitle.bottomAnchor, constant: 4),
            
            uniLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            uniLabel.topAnchor.constraint(equalTo: view.topAnchor, constant: 68),

            courseList.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            courseList.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            courseList.topAnchor.constraint(equalTo: backgroundImage.bottomAnchor, constant: -32),
            courseList.heightAnchor.constraint(equalToConstant: 606)
        ])
    }
    
    func createCourseViewModels() -> [CourseItemViewModel] {
        return [
            CourseItemViewModel(
                courseImage: UIImage(named: "FirstCourse"),
                duration: "1:10min",
                title: "0 - Swift",
                progress: 1.0,
                isLocked: false
            ),
            CourseItemViewModel(
                courseImage: UIImage(named: "SecondCourse"),
                duration: "15:10min",
                title: "1 - Advanced Swift",
                progress: 0.6,
                isLocked: false
            ),
            CourseItemViewModel(
                courseImage: UIImage(named: "SecondCourse"),
                duration: "22:56 min",
                title: "2 - UIViews",
                progress: 0.0,
                isLocked: true
            ),
            CourseItemViewModel(
                courseImage: UIImage(named: "SecondCourse"),
                duration: "22:45 min",
                title: "3 -  Constraints",
                progress: 0.0,
                isLocked: true
            )
        ]
    }
}

