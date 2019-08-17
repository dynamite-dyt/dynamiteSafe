package io.horizontalsystems.dynamitewallet.modules.restore

import io.horizontalsystems.dynamitewallet.core.managers.WordsManager
import io.horizontalsystems.hdwalletkit.Mnemonic

class RestoreInteractor(private val wordsManager: WordsManager) : RestoreModule.IInteractor {

    var delegate: RestoreModule.IInteractorDelegate? = null

    override fun validate(words: List<String>) {
        try {
            wordsManager.validate(words)
            delegate?.didValidate(words)
        } catch (e: Mnemonic.MnemonicException) {
            delegate?.didFailToValidate(e)
        }
    }

}
