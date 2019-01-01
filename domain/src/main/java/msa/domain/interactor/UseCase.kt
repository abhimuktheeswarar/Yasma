package msa.domain.interactor

import io.reactivex.Observable
import io.reactivex.Scheduler
import msa.domain.core.Action
import msa.domain.core.State


abstract class UseCase constructor(
    private val threadScheduler: Scheduler,
    private val postExecutionScheduler: Scheduler
) {

    protected abstract fun buildUseCaseObservable(action: Action, state: State): Observable<Action>

    fun execute(action: Action, state: State): Observable<Action> {
        return buildUseCaseObservable(action, state)
            .subscribeOn(threadScheduler)
            .observeOn(postExecutionScheduler)
    }
}

