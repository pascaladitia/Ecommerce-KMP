package org.pascal.ecommerce.di

import androidx.room.RoomDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.pascal.ecommerce.data.local.database.AppDatabase
import org.pascal.ecommerce.data.local.database.getRoomDatabase
import org.pascal.ecommerce.data.local.repository.cart.CartLocalRepository
import org.pascal.ecommerce.data.local.repository.favorite.FavoriteLocalRepository
import org.pascal.ecommerce.data.local.repository.product.ProductLocalRepository
import org.pascal.ecommerce.data.local.repository.profile.ProfileLocalRepository
import org.pascal.ecommerce.data.repository.ProductRepository
import org.pascal.ecommerce.domain.usecase.local.LocalUseCase
import org.pascal.ecommerce.domain.usecase.product.ProductUseCase
import org.pascal.ecommerce.getDatabaseBuilder
import org.pascal.ecommerce.presentation.screen.detail.DetailViewModel
import org.pascal.ecommerce.presentation.screen.home.HomeViewModel
import org.pascal.ecommerce.presentation.screen.login.LoginViewModel

val appModule = module {
    single<RoomDatabase.Builder<AppDatabase>> { getDatabaseBuilder() }
    single<AppDatabase> { getRoomDatabase(get()) }

    single { ProfileLocalRepository(get()) }
    single { CartLocalRepository(get()) }
    single { FavoriteLocalRepository(get()) }
    single { ProductLocalRepository(get()) }

    single { ProductRepository() }

    single { LocalUseCase(get(), get(),get(), get()) }
    single { ProductUseCase(get()) }

    singleOf(::LoginViewModel)
    singleOf(::HomeViewModel)
    singleOf(::DetailViewModel)
}