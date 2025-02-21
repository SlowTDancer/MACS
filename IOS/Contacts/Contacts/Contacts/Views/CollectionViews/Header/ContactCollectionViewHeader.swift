import UIKit

class ContactCollectionViewHeader: UICollectionReusableView {

    let titleLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = UIFont(name: "Inter-Regular", size: 16)
        return label
    }()
    
    var collapseButtonAction: (() -> Void)?
    
    let collapseButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.backgroundColor = .clear
        button.setTitle("Collapse", for: .normal)
        button.titleLabel?.font = UIFont(name: "Inter-Regular", size: 16)
        button.setTitleColor(UIColor(named: "collapseColor"), for: .normal)
        return button
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        commonInit()
    }
    
    private func commonInit() {
        setUpView()
        setUpActions()
    }
    
    private func setUpView() {
        self.backgroundColor = UIColor.systemGray6
        self.layer.masksToBounds = true
        
        addSubviews()
        setConstraints()
    }
    
    private func addSubviews() {
        self.addSubview(titleLabel)
        self.addSubview(collapseButton)
    }
    
    private func setConstraints() {
        NSLayoutConstraint.activate([
            titleLabel.centerYAnchor.constraint(
                equalTo: self.centerYAnchor
            ),
            titleLabel.leadingAnchor.constraint(
                equalTo: self.leadingAnchor,
                constant: 20
            ),
            
            collapseButton.centerYAnchor.constraint(
                equalTo: self.centerYAnchor
            ),
            collapseButton.trailingAnchor.constraint(
                equalTo: self.trailingAnchor,
                constant: -20
            ),
            
            self.heightAnchor.constraint(
                equalToConstant: CGFloat(ViewController.headerHeight)
            )
        ])
    }
    
    private func setUpActions() {
        collapseButton.addTarget(
            self,
            action: #selector(collapseButtonTapped),
            for: .touchUpInside
        )
    }
    
    @objc func collapseButtonTapped() {
        self.collapseButtonAction?()
    }
    
    func configure(contactHeaderModel: ContactHeaderModel) {
        collapseButtonAction = contactHeaderModel.onExpand
        titleLabel.text = contactHeaderModel.title
        let expandText = contactHeaderModel.isExpanded ? "Collapse" : "Expand"
        collapseButton.setTitle(expandText, for: .normal)
    }
    
}
