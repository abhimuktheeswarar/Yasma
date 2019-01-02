package msa.domain.interactor

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import msa.domain.core.Action
import msa.domain.core.State

/**
 * Created by Abhi Muktheeswarar.
 */

abstract class UseCase constructor(
    private val postExecutionScheduler: Scheduler
) {

    protected abstract fun buildUseCaseObservable(action: Action, state: State): Observable<Action>

    fun execute(action: Action, state: State): Observable<Action> {
        return buildUseCaseObservable(action, state)
            .subscribeOn(Schedulers.io())
            .observeOn(postExecutionScheduler)
    }
}

