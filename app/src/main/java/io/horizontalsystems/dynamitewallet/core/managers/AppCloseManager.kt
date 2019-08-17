package io.horizontalsystems.dynamitewallet.core.managers

import io.reactivex.subjects.PublishSubject

class AppCloseManager{
    var appCloseSignal = PublishSubject.create<Unit>()
}
