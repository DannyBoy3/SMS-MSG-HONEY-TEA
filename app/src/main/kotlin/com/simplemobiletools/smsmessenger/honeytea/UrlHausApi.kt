package com.simplemobiletools.smsmessenger.honeytea

import android.util.Log
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Collections.unmodifiableCollection

class UrlHausApi {

    fun loadMaliciousHostnames(): Collection<String> {

        val url = URL("https://urlhaus.abuse.ch/downloads/text_recent/")
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val stream: InputStream = BufferedInputStream(urlConnection.getInputStream())
        val result = HashSet<String>()
        stream.use { self ->
            val inputStreamReader = InputStreamReader(self)
            inputStreamReader.readLines().forEach { url ->
                //parse hostname from url
                try {
                    result.add(getDomainName(url))
                } catch (e: Exception) {
                    Log.e(javaClass.name, url  + " " + e.message, e)
                }
            }
        }
        //todo this only downloads generic malicious urls, not specific ones for flubot, get flubot urls plsss
        return unmodifiableCollection(result)
    }

    fun getDomainName(url: String): String {
        var start = url.indexOf("://")
        if (start < 0) {
            start = 0
        } else {
            start += 3
        }
        var end = url.indexOf('/', start)
        if (end < 0) {
            end = url.length
        }
        var domainName = url.substring(start, end)
        val port = domainName.indexOf(':')
        if (port >= 0) {
            domainName = domainName.substring(0, port)
        }

        if (domainName.startsWith("www.")) {
            domainName = domainName.split("\\.")[1]
        }
        return domainName
    }

}
