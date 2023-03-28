package me.kofesst.recipsy.server.di

import org.koin.dsl.module
import org.ktorm.database.Database
import org.ktorm.support.postgresql.PostgreSqlDialect

val databaseModule = module {
    single {
        val remoteEnabled = getProperty<String>("database.remote_enabled").toBooleanStrict()
        val driver = getProperty<String>("database.driver")
        val connectValuesPrefix = if (remoteEnabled) {
            "remote"
        } else {
            "local"
        }
        val url = getProperty<String>("database.${connectValuesPrefix}_url")
        val port = getProperty<String>("database.${connectValuesPrefix}_port").toInt()
        val database = getProperty<String>("database.${connectValuesPrefix}_database")
        val user = getProperty<String>("database.${connectValuesPrefix}_user")
        val password = getProperty<String>("database.${connectValuesPrefix}_password")
        Database.connect(
            url = "jdbc:postgresql://$url:$port/$database",
            driver = driver,
            user = user,
            password = password,
            dialect = PostgreSqlDialect()
        )
    }
}