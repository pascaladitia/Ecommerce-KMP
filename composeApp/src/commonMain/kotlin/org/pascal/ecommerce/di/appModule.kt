package org.pascal.ecommerce.di

import androidx.room.RoomDatabase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.pascal.ecommerce.data.local.database.AppDatabase
import org.pascal.ecommerce.data.local.database.getRoomDatabase
import org.pascal.ecommerce.data.local.repository.cart.CartLocalRepository
import org.pascal.ecommerce.data.local.repository.cart.CartLocalRepositoryImpl
import org.pascal.ecommerce.data.local.repository.favorite.FavoriteLocalRepository
import org.pascal.ecommerce.data.local.repository.favorite.FavoriteLocalRepositoryImpl
import org.pascal.ecommerce.data.local.repository.product.ProductLocalRepository
import org.pascal.ecommerce.data.local.repository.product.ProductLocalRepositoryImpl
import org.pascal.ecommerce.data.local.repository.profile.ProfileLocalRepository
import org.pascal.ecommerce.data.local.repository.profile.ProfileLocalRepositoryImpl
import org.pascal.ecommerce.data.repository.auth.AuthRepository
import org.pascal.ecommerce.data.repository.auth.AuthRepositoryImpl
import org.pascal.ecommerce.data.repository.product.ProductRepository
import org.pascal.ecommerce.data.repository.product.ProductRepositoryImpl
import org.pascal.ecommerce.data.repository.order.OrderRepository
import org.pascal.ecommerce.data.repository.order.OrderRepositoryImpl
import org.pascal.ecommerce.data.repository.transaction.TransactionRepository
import org.pascal.ecommerce.data.repository.transaction.TransactionRepositoryImpl
import org.pascal.ecommerce.domain.usecase.auth.AuthUseCase
import org.pascal.ecommerce.domain.usecase.auth.AuthUseCaseImpl
import org.pascal.ecommerce.domain.usecase.local.LocalUseCase
import org.pascal.ecommerce.domain.usecase.local.LocalUseCaseImpl
import org.pascal.ecommerce.domain.usecase.product.ProductUseCase
import org.pascal.ecommerce.domain.usecase.product.ProductUseCaseImpl
import org.pascal.ecommerce.domain.usecase.order.OrderUseCase
import org.pascal.ecommerce.domain.usecase.order.OrderUseCaseImpl
import org.pascal.ecommerce.domain.usecase.transaction.TransactionUseCase
import org.pascal.ecommerce.domain.usecase.transaction.TransactionUseCaseImpl
import org.pascal.ecommerce.getDatabaseBuilder
import org.pascal.ecommerce.presentation.screen.cart.CartViewModel
import org.pascal.ecommerce.presentation.screen.detail.DetailViewModel
import org.pascal.ecommerce.presentation.screen.favorite.FavoriteViewModel
import org.pascal.ecommerce.presentation.screen.home.HomeViewModel
import org.pascal.ecommerce.presentation.screen.login.LoginViewModel
import org.pascal.ecommerce.presentation.screen.maps.MapsViewModel
import org.pascal.ecommerce.presentation.screen.profile.ProfileViewModel
import org.pascal.ecommerce.presentation.screen.register.RegisterViewModel
import org.pascal.ecommerce.presentation.screen.report.ReportViewModel

val appModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    single<RoomDatabase.Builder<AppDatabase>> { getDatabaseBuilder() }
    single<AppDatabase> { getRoomDatabase(get()) }

    singleOf(::ProfileLocalRepositoryImpl) { bind<ProfileLocalRepository>() }
    singleOf(::CartLocalRepositoryImpl) { bind<CartLocalRepository>() }
    singleOf(::FavoriteLocalRepositoryImpl) { bind<FavoriteLocalRepository>() }
    singleOf(::ProductLocalRepositoryImpl) { bind<ProductLocalRepository>() }

    singleOf(::ProductRepositoryImpl) { bind<ProductRepository>() }
    singleOf(::OrderRepositoryImpl) { bind<OrderRepository>() }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::TransactionRepositoryImpl) { bind<TransactionRepository>() }

    singleOf(::LocalUseCaseImpl) { bind<LocalUseCase>() }
    singleOf(::ProductUseCaseImpl) { bind<ProductUseCase>() }
    singleOf(::OrderUseCaseImpl) { bind<OrderUseCase>() }
    singleOf(::AuthUseCaseImpl) { bind<AuthUseCase>() }
    singleOf(::TransactionUseCaseImpl) { bind<TransactionUseCase>() }

    singleOf(::LoginViewModel)
    singleOf(::RegisterViewModel)
    singleOf(::HomeViewModel)
    singleOf(::FavoriteViewModel)
    singleOf(::CartViewModel)
    singleOf(::DetailViewModel)
    singleOf(::ProfileViewModel)
    singleOf(::ReportViewModel)
    singleOf(::MapsViewModel)
}