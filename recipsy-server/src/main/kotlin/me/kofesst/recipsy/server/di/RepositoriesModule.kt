package me.kofesst.recipsy.server.di

import me.kofesst.recipsy.server.common.repositories.AuthRepository
import me.kofesst.recipsy.server.common.repositories.ProfilesRepository
import me.kofesst.recipsy.server.common.repositories.RecipesRepository
import me.kofesst.recipsy.server.features.repositories.AuthRepositoryImpl
import me.kofesst.recipsy.server.features.repositories.ProfilesRepositoryImpl
import me.kofesst.recipsy.server.features.repositories.RecipesRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }
    single<RecipesRepository> {
        RecipesRepositoryImpl(get())
    }
    single<ProfilesRepository> {
        ProfilesRepositoryImpl(get())
    }
}