//
//  PinterestLayout.swift
//  New Collection design
//
//  Created by Anas Almomany on 17/01/2022.
//

import UIKit

public class PinterestLayout: UICollectionViewLayout {
    public struct Padding {
        public let horizontal: CGFloat
        public let vertical: CGFloat

        public init(horizontal: CGFloat = 0, vertical: CGFloat = 0) {
            self.horizontal = horizontal
            self.vertical = vertical
        }

        static var zero: Padding {
            return Padding()
        }
    }

    public var columnsCount = 2
    public var contentPadding: Padding = .zero
    public var cellsPadding: Padding = .zero

    private var cachedAttributes = [UICollectionViewLayoutAttributes]()
    private var contentSize: CGSize = .zero

    override public var collectionViewContentSize: CGSize {
        return contentSize
    }

    override public func prepare() {
        super.prepare()
        cachedAttributes.removeAll()
        calculateCollectionViewFrames()
    }

    override public func layoutAttributesForElements(in rect: CGRect) -> [UICollectionViewLayoutAttributes]? {
        return cachedAttributes.filter { $0.frame.intersects(rect) }
    }

    private func calculateCollectionViewFrames() {
        guard let collectionView = collectionView else { return }

        let contentWidth = collectionView.bounds.width - 2 * contentPadding.horizontal
        let cellWidth = (contentWidth - CGFloat(columnsCount - 1) * cellsPadding.horizontal) / CGFloat(columnsCount)

        var yOffsets = [CGFloat](repeating: contentPadding.vertical, count: columnsCount)
        var xOffsets = [CGFloat]()
        for column in 0..<columnsCount {
            xOffsets.append(CGFloat(column) * (cellWidth + cellsPadding.horizontal) + contentPadding.horizontal)
        }

        for section in 0..<collectionView.numberOfSections {
            for item in 0..<collectionView.numberOfItems(inSection: section) {
                let indexPath = IndexPath(item: item, section: section)
                let column = yOffsets.firstIndex(of: yOffsets.min()!)!
                let x = xOffsets[column]
                let y = yOffsets[column]

                let attributes = UICollectionViewLayoutAttributes(forCellWith: indexPath)
                attributes.frame = CGRect(x: x, y: y, width: cellWidth, height: 50)
                
                if let cell = collectionView.cellForItem(at: indexPath) {
                    let fittingSize = cell.contentView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
                    attributes.frame.size.height = fittingSize.height
                }

                cachedAttributes.append(attributes)
                yOffsets[column] = attributes.frame.maxY + cellsPadding.vertical
            }
        }

        contentSize.height = (yOffsets.max() ?? 0) + contentPadding.vertical
        contentSize.width = collectionView.bounds.width
    }
}
