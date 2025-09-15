package org.pascal.ecommerce.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.pascal.ecommerce.data.local.repository.LocalRepository
import org.pascal.ecommerce.data.repository.BaseRepository
import org.pascal.ecommerce.domain.usecase.BaseUseCase
import org.pascal.ecommerce.getDatabaseBuilder
import org.pascal.ecommerce.presentation.screen.login.LoginViewModel

val appModule = module {
    single{ LocalRepository(getDatabaseBuilder()) }

    single{ BaseRepository() }

    single{ BaseUseCase(get()) }

    singleOf(::LoginViewModel)
}