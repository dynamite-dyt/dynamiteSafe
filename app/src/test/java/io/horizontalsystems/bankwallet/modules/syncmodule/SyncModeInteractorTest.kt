package io.horizontalsystems.dynamitewallet.modules.syncmodule

import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.core.ILocalStorage
import io.horizontalsystems.dynamitewallet.core.managers.AuthManager
import io.horizontalsystems.dynamitewallet.core.managers.WordsManager
import io.horizontalsystems.dynamitewallet.entities.SyncMode
import org.junit.Before
import org.junit.Test
import org.mockito.Captor
import org.mockito.Mockito.*

class SyncModeInteractorTest {

    private val delegate = mock(SyncModeModule.IInteractorDelegate::class.java)
    private lateinit var interactor: SyncModeInteractor

    private val authManager = mock(AuthManager::class.java)
    private val localStorage = mock(ILocalStorage::class.java)
    private val wordsManager = mock(WordsManager::class.java)
    private val keystoreSafeExecute = mock(IKeyStoreSafeExecute::class.java)

    @Captor
    private val actionRunnableCaptor: KArgumentCaptor<Runnable> = argumentCaptor()
    @Captor
    private val successRunnableCaptor: KArgumentCaptor<Runnable> = argumentCaptor()
    @Captor
    private val failureRunnableCaptor: KArgumentCaptor<Runnable> = argumentCaptor()

    @Before
    fun before() {
        interactor = SyncModeInteractor(localStorage, authManager, wordsManager, keystoreSafeExecute)
        interactor.delegate = delegate
    }

    @Test
    fun getSyncMode(){
        interactor.getSyncMode()
        verify(localStorage).syncMode
    }

    @Test
    fun restoreWallet_restore() {
        val words = listOf("first", "second", "etc")
        val syncMode = SyncMode.FAST

        interactor.restore(words, syncMode)

        verify(keystoreSafeExecute).safeExecute(actionRunnableCaptor.capture(), successRunnableCaptor.capture(), failureRunnableCaptor.capture())

        val actionRunnable = actionRunnableCaptor.firstValue
        val successRunnable = successRunnableCaptor.firstValue

        actionRunnable.run()
        successRunnable.run()

        verify(authManager).login(words, syncMode)
    }

    @Test
    fun restoreWallet_success() {
        val words = listOf("first", "second", "etc")
        val syncMode = SyncMode.FAST

        interactor.restore(words, syncMode)

        verify(keystoreSafeExecute).safeExecute(actionRunnableCaptor.capture(), successRunnableCaptor.capture(), failureRunnableCaptor.capture())

        val actionRunnable = actionRunnableCaptor.firstValue
        val successRunnable = successRunnableCaptor.firstValue

        actionRunnable.run()
        successRunnable.run()

        verify(delegate).didRestore()
        verify(wordsManager).isBackedUp = true
        verifyNoMoreInteractions(delegate)
    }

    @Test
    fun restoreWallet_failureWordsError() {
        val words = listOf("first", "second", "etc")
        val syncMode = SyncMode.FAST

        interactor.restore(words, syncMode)

        verify(keystoreSafeExecute).safeExecute(actionRunnableCaptor.capture(), successRunnableCaptor.capture(), failureRunnableCaptor.capture())

        val actionRunnable = actionRunnableCaptor.firstValue
        val failureRunnable = failureRunnableCaptor.firstValue

        actionRunnable.run()
        failureRunnable.run()

        verify(delegate).didFailToRestore(any())
        verifyNoMoreInteractions(delegate)
    }

}
