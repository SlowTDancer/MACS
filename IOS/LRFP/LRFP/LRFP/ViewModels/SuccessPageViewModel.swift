//
//  SuccessPageViewModel.swift
//  LRFP
//
//  Created by ikhut21 on 05.02.25.
//

struct SuccessPageViewModel {
    public var pageTitle: String
    public var successTitle: String
    public var successDescription: String
    
    init(
        pageTitle: String,
        successTitle: String,
        successDescription: String
    ) {
        self.pageTitle = pageTitle
        self.successTitle = successTitle
        self.successDescription = successDescription
    }
}
