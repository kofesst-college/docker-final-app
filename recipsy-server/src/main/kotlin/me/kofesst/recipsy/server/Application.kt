package me.kofesst.recipsy.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.kofesst.recipsy.server.di.databaseModule
import me.kofesst.recipsy.server.di.repositoriesModule
import me.kofesst.recipsy.server.plugins.*
import org.koin.core.module.Module

fun main(args: Array<String>) =
    EngineMain.main(args)

@Suppress("unused") // Used in application.conf
@JvmOverloads
fun Application.module(
    koinModules: List<Module> = listOf(
        databaseModule,
        repositoriesModule,
    ),
) {
    configureInjecting(koinModules)
    configureSecurity()
    configureSerialization()
    configureRouting()
}