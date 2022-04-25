package com.simplemobiletools.smsmessenger

import android.app.Application
import com.simplemobiletools.commons.extensions.checkUseEnglish
import com.simplemobiletools.smsmessenger.honeytea.MaliciousHostnameRegistry

class App : Application() {

    lateinit var registry: MaliciousHostnameRegistry;

    override fun onCreate() {
        super.onCreate()
        registry = MaliciousHostnameRegistry(this)
        checkUseEnglish()
    }
}
