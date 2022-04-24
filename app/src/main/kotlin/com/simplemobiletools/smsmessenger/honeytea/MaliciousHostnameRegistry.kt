package com.simplemobiletools.smsmessenger.honeytea

import android.content.Context
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
        FileWriter(file).use { writer ->
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

}
