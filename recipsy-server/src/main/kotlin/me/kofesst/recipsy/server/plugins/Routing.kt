package me.kofesst.recipsy.server.plugins

import io.ktor.server.application.*
import io.ktor.server.resources.*
import me.kofesst.recipsy.server.common.resources.configureAuthResources
import me.kofesst.recipsy.server.common.resources.configureProfilesResources
import me.kofesst.recipsy.server.common.resources.configureRecipesResources

fun Application.configureRouting() {
    install(Resources)
    configureAuthResources()
    configureRecipesResources()
    configureProfilesResources()
}