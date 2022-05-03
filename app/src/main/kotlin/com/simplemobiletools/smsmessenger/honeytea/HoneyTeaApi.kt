package com.simplemobiletools.smsmessenger.honeytea

import android.util.Log
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Collections.unmodifiableCollection

class HoneyTeaApi {

    fun loadMaliciousHostnames(): Collection<String> {

        val url = URL("http://178.62.222.98:8080/links")
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val stream: InputStream = BufferedInputStream(urlConnection.getInputStream())
        val result = HashSet<String>()
        stream.use { self ->
            val inputStreamReader = InputStreamReader(self)
            inputStreamReader.readLines().forEach { url ->
                //parse hostname from url
                try {
                    result.add(url)
                } catch (e: Exception) {
                    Log.e(javaClass.name, url  + " " + e.message, e)
                }
            }
        }
        //todo this only downloads generic malicious urls, not specific ones for flubot, get flubot urls plsss
        return unmodifiableCollection(result)
    }

}
