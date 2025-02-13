//
//  ViewController.swift
//  7SD
//
//  Created by ikhut21 on 22.10.24.
//

import UIKit

class ViewController: UIViewController {
    let number: Int16 = 333
    
    @IBOutlet var digitView1: UIView!
    @IBOutlet var digitView2: UIView!
    @IBOutlet var digitView3: UIView!
    @IBOutlet var digitView1Components: [UIView]!
    @IBOutlet var digitView2Components: [UIView]!
    @IBOutlet var digitView3Components: [UIView]!

    override func viewDidLoad() {
        super.viewDidLoad()
        setUpUI()
        sevenSegmentDisplay()
    }

    func sevenSegmentDisplay(){
        let digits = getDigits(number: number)
//        print(digits)
        lightItUp(digits: digits)
    }
    
    func setUpUI() {
        addSubviews()
    }
    
    func addSubviews() {
        createDigitFrames()
    }
    
    func createDigitFrames() {
        let deltaX: CGFloat = Constants.lineHeight + 2 * Constants.lineWidth + 2 * Constants.spacer + Constants.offset
        
        var currX = Constants.startingX
        createDigitFrame(
            digitView: digitView1,
            digitViewComponents: digitView1Components,
            x: currX
        )
        
        currX += deltaX
        createDigitFrame(
            digitView: digitView2,
            digitViewComponents: digitView2Components,
            x: currX
        )
        
        currX += deltaX
        createDigitFrame(
            digitView: digitView3,
            digitViewComponents: digitView3Components,
            x: currX
        )
    }
    
    func createDigitFrame(digitView: UIView, digitViewComponents: [UIView], x: CGFloat) {
        let height = 2 * Constants.lineHeight + Constants.lineWidth
        let width = Constants.lineHeight + 2 * Constants.lineWidth + 2 * Constants.spacer
        digitView.frame = CGRect(
            x: x,
            y: Constants.startingY,
            width: width,
            height: height)
        
        addSubviews(digitViewComponents: digitViewComponents)
    }
    
    func addSubviews(digitViewComponents: [UIView]) {
        var y: CGFloat = 0
        var x: CGFloat = 0
        
        verticalLines(digitViewComponents: digitViewComponents[0...1], x: x, y: y)
        
        y += Constants.lineHeight + Constants.spacer
        verticalLines(digitViewComponents: digitViewComponents[2...3], x: x, y: y)
        
        x += Constants.lineWidth + Constants.spacer
        horizontalLines(digitViewComponents:digitViewComponents[4...], x: x)
    }
    
    func verticalLines(digitViewComponents: ArraySlice<UIView>, x: CGFloat, y: CGFloat) {
        var currX = x
        
        for digitViewComponent in digitViewComponents {
            digitViewComponent.frame = CGRect(
                x: currX,
                y: y,
                width: Constants.lineWidth,
                height: Constants.lineHeight)
            currX += Constants.lineWidth + 2 * Constants.spacer + Constants.lineHeight
            digitViewComponent.backgroundColor = Constants.componentColor
        }
    }
    
    func horizontalLines(digitViewComponents: ArraySlice<UIView>, x: CGFloat) {
        var currY: CGFloat = 0
        
        for digitViewComponent in digitViewComponents {
            digitViewComponent.frame = CGRect(
                x: x,
                y: currY,
                width: Constants.lineHeight,
                height: Constants.lineWidth)
            currY += Constants.lineHeight
            digitViewComponent.backgroundColor = Constants.componentColor
        }
    }
    
    func getDigits(number: Int16) -> [Int16] {
        if number > 999 {
            return [-1, -1, -1]
        }
        
        var res: [Int16] = [0, -1, -1]
        var num = number
        var index = 0
        
        while num > 0 {
            let currDigit = num % 10
            res[index] = currDigit
            num /= 10
            index += 1
        }
        
        return res.reversed()
    }
    
    func lightItUp(digits: [Int16]) {
        show(
            digit: digits[0],
            digitViewComponents: digitView1Components
        )
        show(
            digit: digits[1],
            digitViewComponents: digitView2Components
        )
        show(
            digit: digits[2],
            digitViewComponents: digitView3Components
        )
    }
    
    func show(digit: Int16, digitViewComponents: [UIView]) {
        switch digit {
        case 0:
            showZero(digitViewComponents: digitViewComponents)
        case 1:
            showOne(digitViewComponents: digitViewComponents)
        case 2:
            showTwo(digitViewComponents: digitViewComponents)
        case 3:
            showThree(digitViewComponents: digitViewComponents)
        case 4:
            showFour(digitViewComponents: digitViewComponents)
        case 5:
            showFive(digitViewComponents: digitViewComponents)
        case 6:
            showSix(digitViewComponents: digitViewComponents)
        case 7:
            showSeven(digitViewComponents: digitViewComponents)
        case 8:
            showEight(digitViewComponents: digitViewComponents)
        case 9:
            showNine(digitViewComponents: digitViewComponents)
        default:
            return
        }
    }
    
    func showZero(digitViewComponents: [UIView]) {
        for digitViewComponent in digitViewComponents[0...3] {
            digitViewComponent.backgroundColor = Constants.lightUpComponentColor
        }
        
        digitViewComponents[4].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[6].backgroundColor = Constants.lightUpComponentColor
    }
    
    func showOne(digitViewComponents: [UIView]) {
        digitViewComponents[1].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[3].backgroundColor = Constants.lightUpComponentColor
    }
    
    func showTwo(digitViewComponents: [UIView]) {
        digitViewComponents[1].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[2].backgroundColor = Constants.lightUpComponentColor
        
        for digitViewComponent in digitViewComponents[4...6] {
            digitViewComponent.backgroundColor = Constants.lightUpComponentColor
        }
    }
    
    func showThree(digitViewComponents: [UIView]) {
        digitViewComponents[1].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[3].backgroundColor = Constants.lightUpComponentColor
        
        for digitViewComponent in digitViewComponents[4...6] {
            digitViewComponent.backgroundColor = Constants.lightUpComponentColor
        }
    }
    
    func showFour(digitViewComponents: [UIView]) {
        digitViewComponents[0].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[1].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[3].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[5].backgroundColor = Constants.lightUpComponentColor
    }
    
    func showFive(digitViewComponents: [UIView]) {
        digitViewComponents[0].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[3].backgroundColor = Constants.lightUpComponentColor
        
        for digitViewComponent in digitViewComponents[4...6] {
            digitViewComponent.backgroundColor = Constants.lightUpComponentColor
        }
    }
    
    func showSix(digitViewComponents: [UIView]) {
        digitViewComponents[0].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[2].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[3].backgroundColor = Constants.lightUpComponentColor
        
        for digitViewComponent in digitViewComponents[4...6] {
            digitViewComponent.backgroundColor = Constants.lightUpComponentColor
        }
    }
    
    func showSeven(digitViewComponents: [UIView]) {
        digitViewComponents[1].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[3].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[4].backgroundColor = Constants.lightUpComponentColor
    }
    
    func showEight(digitViewComponents: [UIView]) {
        for digitViewComponent in digitViewComponents {
            digitViewComponent.backgroundColor = Constants.lightUpComponentColor
        }
    }
    
    func showNine(digitViewComponents: [UIView]) {
        digitViewComponents[0].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[1].backgroundColor = Constants.lightUpComponentColor
        digitViewComponents[3].backgroundColor = Constants.lightUpComponentColor
        
        for digitViewComponent in digitViewComponents[4...6] {
            digitViewComponent.backgroundColor = Constants.lightUpComponentColor
        }
    }
    
}

