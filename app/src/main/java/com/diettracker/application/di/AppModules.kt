package com.diettracker.application.di

import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.database.FoodDatabase
import com.diettracker.notifications.IFCMService
import com.diettracker.notifications.PushNotificationSender
import com.diettracker.services.auth.AuthService
import com.diettracker.services.auth.FirebaseAuthService
import com.diettracker.services.items.FirebaseItemRepository
import com.diettracker.services.items.ItemRepository
import com.diettracker.services.profile.FirebaseProfileRepository
import com.diettracker.services.profile.ProfileRepository
import com.diettracker.services.session.SessionManager
import com.diettracker.ui.AddIngredientsViewModel
import com.diettracker.ui.ProductIngredientListEmitter
import com.diettracker.ui.additem.AddItemViewModel
import com.diettracker.ui.addrecipe.AddRecipeViewModel
import com.diettracker.ui.auth.login.LoginViewModel
import com.diettracker.ui.auth.register.RegisterViewModel
import com.diettracker.ui.home.HomeViewModel
import com.diettracker.ui.home.tabs.diet.DietViewModel
import com.diettracker.ui.home.tabs.items.ItemsViewModel
import com.diettracker.ui.home.tabs.profile.ProfileViewModel
import com.diettracker.ui.recipedetails.RecipeDetailsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { NavigationDispatcher() }
    single { SnackbarDispatcher() }
    single { ProductIngredientListEmitter() }
}

val firebaseModule = module {
    single { Firebase.auth }
    single { Firebase.database.reference }
}

val servicesModule = module {
    single<AuthService> { FirebaseAuthService(get(), get()) }
    single<ProfileRepository> { FirebaseProfileRepository(get()) }
    single<ItemRepository> { FirebaseItemRepository(get(), get()) }
    single { SessionManager(androidContext()) }
}

val viewModelsModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get()) }
    viewModel { AddItemViewModel(get(), get(), get(), get()) }
    viewModel { ItemsViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { DietViewModel(get()) }
    viewModel { AddRecipeViewModel(get(), get(), get()) }
    viewModel { AddIngredientsViewModel(get()) }
    viewModel { HomeViewModel() }
    viewModel { RecipeDetailsViewModel(get()) }
}

val retrofitModules = module {
    single { retrofit("https://fcm.googleapis.com/") }

    single { get<Retrofit>().create(IFCMService::class.java) }

    factory { PushNotificationSender(get()) }
}

fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())  // 6
    .build()

val databaseModule = module {
    single { FoodDatabase.invoke(androidContext()) }
}