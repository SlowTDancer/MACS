import UIKit

class ForecastViewHeader: UITableViewHeaderFooterView {
    
    private let containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        view.layer.cornerRadius = 8
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let titleLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "Inter-Bold", size: 16)
        label.textColor = UIColor(
            named: "accent"
        )!
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let topSeparatorView: UIView = {
        let view = UIView()
        view.backgroundColor = .black.withAlphaComponent(0.2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let bottomSeparatorView: UIView = {
        let view = UIView()
        view.backgroundColor = .black.withAlphaComponent(0.2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private var weekDay: String?
    
    override init(reuseIdentifier: String?) {
        super.init(reuseIdentifier: reuseIdentifier)
        commonInit()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        commonInit()
    }
    
    private func commonInit() {
        addSubviews()
        setConstraints()
    }
    
    private func addSubviews() {
        contentView.addSubview(topSeparatorView)
        contentView.addSubview(containerView)
        containerView.addSubview(titleLabel)
        contentView.addSubview(bottomSeparatorView)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(
                equalTo: contentView.leadingAnchor,
                constant: 16
            ),
            containerView.trailingAnchor.constraint(
                equalTo: contentView.trailingAnchor,
                constant: -16
            ),
            containerView.topAnchor.constraint(
                equalTo: contentView.topAnchor,
                constant: 4
            ),
            containerView.bottomAnchor.constraint(
                equalTo: contentView.bottomAnchor,
                constant: -4
            ),
            
            titleLabel.leadingAnchor.constraint(
                equalTo: containerView.leadingAnchor,
                constant: 16
            ),
            titleLabel.centerYAnchor.constraint(
                equalTo: containerView.centerYAnchor
            ),
            
            topSeparatorView.topAnchor.constraint(
                equalTo: contentView.topAnchor
            ),
            topSeparatorView.leadingAnchor.constraint(
                equalTo: contentView.leadingAnchor
            ),
            topSeparatorView.trailingAnchor.constraint(
                equalTo: contentView.trailingAnchor
            ),
            topSeparatorView.heightAnchor.constraint(
                equalToConstant: 0.5
            ),
        
            bottomSeparatorView.bottomAnchor.constraint(
                equalTo: contentView.bottomAnchor
            ),
            bottomSeparatorView.leadingAnchor.constraint(
                equalTo: contentView.leadingAnchor
            ),
            bottomSeparatorView.trailingAnchor.constraint(
                equalTo: contentView.trailingAnchor
            ),
            bottomSeparatorView.heightAnchor.constraint(
                equalToConstant: 0.5
            ),

        ])
    }
    
    func configure(
        with model: ForecastHeaderModel?,
        isFirstHeader: Bool = false
    ) {
        guard let model = model else { return }
        weekDay = model.weekDay
        titleLabel.text = model.weekDay
        
        topSeparatorView.isHidden = isFirstHeader
    }
    
}
