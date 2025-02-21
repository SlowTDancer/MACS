import UIKit

class NoteViewCell: UICollectionViewCell {

    let titleLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 17, weight: .medium)
        label.textColor = .black
        label.numberOfLines = 0
        label.lineBreakMode = .byWordWrapping
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        return label
    }()

    let contentLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 14, weight: .regular)
        label.textColor = .black
        label.numberOfLines = 0
        label.lineBreakMode = .byWordWrapping
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        return label
    }()

    lazy var mainStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.addArrangedSubview(titleLabel)
        stackView.addArrangedSubview(contentLabel)
        stackView.spacing = 0
        stackView.distribution = .equalSpacing
        return stackView
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
        setConstraints()
    }

    private func setUpView() {
        setUpBorder()
        addSubviews()
    }

    private func setUpBorder() {
        layer.cornerRadius = 16
    }

    private func addSubviews() {
        self.contentView.addSubview(mainStackView)
    }

    private func setConstraints() {
        NSLayoutConstraint.activate([
            mainStackView.topAnchor.constraint(
                equalTo: contentView.topAnchor,
                constant: 8
            ),
            mainStackView.leadingAnchor.constraint(
                equalTo: contentView.leadingAnchor,
                constant: 8
            ),
            mainStackView.trailingAnchor.constraint(
                equalTo: contentView.trailingAnchor,
                constant: -8
            ),
            mainStackView.bottomAnchor.constraint(
                equalTo: contentView.bottomAnchor,
                constant: -8
            )
        ])
    }

    private func setTitleText(title: String) {
        if title.count > 50 {
            titleLabel.text = String(title.prefix(50)) + "..."
        } else {
            titleLabel.text = title
        }
    }
    
    private func setContentText(content: String) {
        if content.count > 300 {
            contentLabel.text = String(content.prefix(300)) + "..."
        } else {
            contentLabel.text = content
        }
    }
    
    func configure(note: Note) {
        setTitleText(title: note.title!)
        setContentText(content: note.content!)
        self.backgroundColor = ColorHelper.dataToColor(note.color)
    }

}
