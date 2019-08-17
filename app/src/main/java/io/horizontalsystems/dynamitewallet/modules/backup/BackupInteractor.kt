package io.horizontalsystems.dynamitewallet.modules.backup

import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.core.ILocalStorage
import io.horizontalsystems.dynamitewallet.core.IRandomProvider
import io.horizontalsystems.dynamitewallet.core.managers.AuthManager
import io.horizontalsystems.dynamitewallet.core.managers.WordsManager
import java.util.*

class BackupInteractor(
        private val authManager: AuthManager,
        private val wordsManager: WordsManager,
        private val indexesProvider: IRandomProvider,
        private val localStorage: ILocalStorage,
        private val keystoreSafeExecute: IKeyStoreSafeExecute) : BackupModule.IInteractor {

    var delegate: BackupModule.IInteractorDelegate? = null

    override fun fetchWords() {
        authManager.authData?.let {
            delegate?.didFetchWords(it.words)
        } ?:run {
            keystoreSafeExecute.safeExecute(
                    action = Runnable {
                        authManager.safeLoad()
                        authManager.authData?.let { delegate?.didFetchWords(it.words) }
                    }
            )
        }
    }

    override fun fetchConfirmationIndexes() {
        delegate?.didFetchConfirmationIndexes(indexesProvider.getRandomIndexes(2))
    }

    override fun shouldShowTermsConfirmation(): Boolean {
        return !localStorage.iUnderstand || !wordsManager.isBackedUp
    }

    override fun validate(confirmationWords: HashMap<Int, String>) {
        authManager.authData?.let { authData ->
            val wordList = authData.words
            var valid = true
            for ((index, word) in confirmationWords) {
                if (wordList[index - 1] != word.trim()) {
                    valid = false
                }
            }

            if (valid) {
                delegate?.didValidateSuccess()
                wordsManager.isBackedUp = true
            } else {
                delegate?.didValidateFailure()
            }
        } ?: run { delegate?.didValidateFailure() }
    }

    override fun onTermsConfirm() {
        localStorage.iUnderstand = true
    }
}
