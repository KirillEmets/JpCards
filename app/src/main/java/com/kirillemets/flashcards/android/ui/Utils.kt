package com.kirillemets.flashcards.android.ui

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import org.joda.time.LocalDate

class SuspendableActivityResultLauncher<I, O>(
    private val sharedFlow: SharedFlow<O>,
    private val launcher: ActivityResultLauncher<I>
) {

    suspend fun launchAndAwait(input: I): O {
        launcher.launch(input)
        return sharedFlow.first()
    }
}

fun <I, O> Fragment.registerForSuspendableActivityResult(contract: ActivityResultContract<I, O>): SuspendableActivityResultLauncher<I, O> {
    val mutableSharedFlow =
        MutableSharedFlow<O>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val launcher = registerForActivityResult(contract) { result ->
        Log.d("kek", "tryEmit: ${mutableSharedFlow.tryEmit(result)}")
    }
    return SuspendableActivityResultLauncher(mutableSharedFlow, launcher)
}

class TimeUtil {
    companion object {
        val todayMillis: Long
            get() = LocalDate.now().toDateTimeAtStartOfDay().millis
    }
}