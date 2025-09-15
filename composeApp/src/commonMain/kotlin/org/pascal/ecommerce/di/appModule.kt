package org.pascal.ecommerce.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.pascal.ecommerce.data.local.LocalRepository
import org.pascal.ecommerce.data.repository.Repository
import org.pascal.ecommerce.getDatabaseBuilder
import org.pascal.ecommerce.presentation.screen.login.LoginViewModel

val appModule = module {
    single{ LocalRepository(getDatabaseBuilder()) }
    single{ Repository() }

    singleOf(::LoginViewModel)
}