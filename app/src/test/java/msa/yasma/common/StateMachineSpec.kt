package msa.yasma.common

import io.reactivex.Observable
import msa.domain.core.State
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by Abhi Muktheeswarar.
 */

interface Screen

interface StateRecorder {

    fun renderedStates(): Observable<State>
}

class StateHistory(private val stateRecorder: StateRecorder) {

    private var stateHistory: List<State> = emptyList()

    internal data class StateHistorySnapshot(

        val actualRecordedStates: List<State>,

        val verifiedHistory: List<State>
    )

    internal fun waitUntilNextRenderedState(): StateHistorySnapshot {
        val recordedStates = stateRecorder.renderedStates()
            .take(stateHistory.size + 1L)
            .toList()
            .timeout(1, TimeUnit.MINUTES)
            .doOnError { it.printStackTrace() }
            .blockingGet()

        val history = stateHistory
        stateHistory = recordedStates

        return StateHistorySnapshot(
            actualRecordedStates = recordedStates,
            verifiedHistory = history
        )
    }
}

data class Given(
    private val screen: Screen,
    private val stateHistory: StateHistory,
    private val composedMessage: String
) {

    inner class On(private val composedMessage: String) {

        inner class It(private val composedMessage: String) {

            internal fun assertStateRendered(expectedState: State) {

                val (recordedStates, verifiedHistory) = stateHistory.waitUntilNextRenderedState()
                val expectedStates = verifiedHistory + expectedState
                Assert.assertEquals(
                    composedMessage,
                    expectedStates,
                    recordedStates
                )
                Timber.d("✅ $composedMessage")

            }
        }

        infix fun String.byRendering(expectedState: State) {
            val message = this
            val it = It("$composedMessage *IT* $message")
            it.assertStateRendered(expectedState)
        }
    }

    fun on(message: String, block: On.() -> Unit) {
        val on = On("*GIVEN* $composedMessage *ON* $message")
        on.block()
    }
}

data class ScreenConfig(
    val mockWebServer: MockWebServer
)