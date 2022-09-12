package sannikov.a.stonerstopwatch

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.*

object TimeFormat {
    private const val MS_PER_SECOND = 1000L
    private const val MS_PER_MINUTE = 60000L
    private const val MS_PER_HOUR = 3600000L

    /**
     * Formats to h:mm:ss:SS
     * Does so manually
     */
    fun format(ms: Long): String {
        val hrs = (ms / MS_PER_HOUR)
        val mins = ((ms % MS_PER_HOUR) / MS_PER_MINUTE)
        val secs = (ms % MS_PER_MINUTE) / MS_PER_SECOND
        val dispMs = (ms % MS_PER_SECOND) / 10 // :SS format

        val sb = StringBuilder(10)
        // hours
        if (hrs > 0) {
            sb.append("$hrs:")
        }
        // minutes
        if(mins > 0 || hrs > 0) {
            sb.zeroPadToLength(mins).append(":")
        }
        // seconds
        sb.zeroPadToLength(secs).append(":")
        // milliseconds
        sb.zeroPadToLength(dispMs)

        return sb.toString()
    }



    /**
     * extension function that is more performant than mins.toString().padStart(2)
     */
    fun StringBuilder.zeroPadToLength(dur: Long): StringBuilder {
        if(dur < 10L) {
            this.append(0)
        }
        if(dur == 0L) {
            this.append(0)
        } else {
            this.append(dur)
        }
        return this
    }
}