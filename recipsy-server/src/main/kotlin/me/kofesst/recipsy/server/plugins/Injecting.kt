package me.kofesst.recipsy.server.plugins

import io.ktor.server.application.*
import org.koin.core.module.Module
import org.koin.fileProperties
import org.koin.ktor.plugin.Koin

fun Application.configureInjecting(koinModules: List<Module>) {
    install(Koin) {
        fileProperties()
        modules(koinModules)
    }
}