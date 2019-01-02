package msa.data

import java.io.File

/**
 * Created by Abhi Muktheeswarar.
 */

object Utils {

    fun getJson(path: String): String {
        val uri = javaClass.getResource(path)!!
        val file = File(uri.path)
        return String(file.readBytes())
    }
}