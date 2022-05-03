package com.simplemobiletools.smsmessenger.honeytea

import android.content.Context
import com.simplemobiletools.smsmessenger.models.Message
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class MaliciousHostnameRegistry(val context: Context) {

    val cache = HashSet<String>()
    val file = File(context.filesDir, "honey-tea-hostnames.txt")

    init {
        if (!file.exists()) {
            file.setReadable(true)
            file.setWritable(true)
            file.createNewFile()
        }
        FileReader(file).use { reader ->
            cache.addAll(reader.readLines())
        }
    }

    fun save(hostnames: Collection<String>) {
        FileWriter(file, true).use { writer ->
            for (hostname in hostnames) {
                if (cache.contains(hostname)) {
                    continue
                }

                cache.add(hostname)
                writer.write(hostname)
                writer.write("\n")
            }
            writer.flush()
        }
    }

    fun checkMessageForMaliciousUrl(message: Message) = checkMessageForMaliciousUrl(message.body)

    fun checkMessageForMaliciousUrl(body: String): Boolean {
        for (host in cache) {
            if (body.contains(host)) {
                return true
            }
        }
        return false
    }

}
