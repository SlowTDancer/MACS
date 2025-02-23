import Foundation

enum ServiceError: LocalizedError {
    case noData
    case invalidParameters(details: String?)
    case sessionErrorOccurred(reason: String?)
    case decoderError(details: String?)
    
    var errorDescription: String? {
        switch self {
        case .noData:
            return "No data available. The server returned an empty response."
        
        case .invalidParameters(let details):
            let baseMessage = "Invalid request parameters."
            return details != nil
                ? "\(baseMessage) \(details!)"
                : baseMessage
        
        case .sessionErrorOccurred(let reason):
            let baseMessage = "Network connection error occurred."
            return reason != nil
                ? "\(baseMessage) \(reason!)"
                : baseMessage
        
        case .decoderError(let details):
            let baseMessage = "Unable to process the received data."
            return details != nil
                ? "\(baseMessage) \(details!)"
                : baseMessage
        }
    }
}
