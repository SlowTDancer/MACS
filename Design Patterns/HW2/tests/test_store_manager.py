from pos.store_manager import StoreManager


def test_store_manager_answer_question() -> None:
    store_manager = StoreManager()
    result = store_manager.answer_question()
    assert result in [True, False]
