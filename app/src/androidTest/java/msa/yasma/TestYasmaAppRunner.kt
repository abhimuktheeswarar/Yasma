package msa.yasma

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Created by Abhi Muktheeswarar.
 */

class TestYasmaAppRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestYasmaApplication::class.java.canonicalName, context)
    }


}
