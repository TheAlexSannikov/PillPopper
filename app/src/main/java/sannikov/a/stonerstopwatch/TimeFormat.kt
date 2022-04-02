package sannikov.a.stonerstopwatch

import android.os.Build
import java.time.format.DateTimeFormatter

object TimeFormat {
    private const val MS_PER_SECOND = 1000L
    private const val MS_PER_MINUTE = 60000L
    private const val MS_PER_HOUR = 3600000L

    // Formats to h:mm:ss:SS
    fun format(ms: Long): String {
        val hrs = (ms / MS_PER_HOUR)
        val mins = ((ms % MS_PER_HOUR) / MS_PER_MINUTE)
        val secs = (ms % MS_PER_MINUTE) / MS_PER_SECOND
        val dispMs = (ms % MS_PER_SECOND) / 10 // :SS format

        val sb = StringBuilder(10)
        if (hrs > 0) {
            sb.append("$hrs:${mins.toString().padStart(length = 2, padChar = '0')}:")
        } else if (mins > 0) {
            sb.append("${mins.toString()}:")
        }
        sb.append(
            "${secs.toString().padStart(length = 2, padChar = '0')}:${
                dispMs.toString().padStart(length = 2, padChar = '0')
            }"
        )
        return sb.toString()
    }
}