package msa.yasma

import kotlinx.io.InputStream
import java.io.File

/**
 * Created by Abhi Muktheeswarar.
 */

fun InputStream.toFile(path: String) {
    use { input ->
        File(path).outputStream().use { input.copyTo(it) }
    }
}