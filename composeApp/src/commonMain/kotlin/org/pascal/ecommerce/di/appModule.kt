package org.pascal.ecommerce.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.pascal.ecommerce.data.local.repository.profile.ProfileLocalRepository
import org.pascal.ecommerce.data.repository.ProductRepository
import org.pascal.ecommerce.domain.usecase.ProductUseCase
import org.pascal.ecommerce.getDatabaseBuilder
import org.pascal.ecommerce.presentation.screen.login.LoginViewModel

val appModule = module {
    single{ ProfileLocalRepository(getDatabaseBuilder()) }

    single{ ProductRepository() }

    single{ ProductUseCase(get()) }

    singleOf(::LoginViewModel)
}