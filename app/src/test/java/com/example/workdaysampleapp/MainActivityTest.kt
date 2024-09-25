package com.example.workdaysampleapp

import android.os.Build
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workdaysampleapp.data.model.PokemonListResponse
import com.example.workdaysampleapp.presentation.viewmodel.PokemonViewModel
import com.example.workdaysampleapp.presentation.viewmodel.PokemonViewModelFactory
import com.example.workdaysampleapp.domain.GetPokemonListUseCase
import com.example.workdaysampleapp.domain.GetPokemonUseCase
import com.example.workdaysampleapp.presentation.view.MainActivity
import com.example.workdaysampleapp.presentation.view.PokemonApp
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.verify
import org.robolectric.annotation.Config
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @JvmField
    @Rule
    val composeRule = createComposeRule()

    private lateinit var pokemonViewModel: PokemonViewModel
    private val getPokemonListUseCase: GetPokemonListUseCase = mock(GetPokemonListUseCase::class.java)
    private val getPokemonUseCase: GetPokemonUseCase = mock(GetPokemonUseCase::class.java)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        pokemonViewModel = PokemonViewModelFactory(
            getPokemonListUseCase,
            getPokemonUseCase
        ).create(PokemonViewModel::class.java)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_initial_load_shows_loading_indicator() {
        composeTestRule.setContent {
            PokemonApp(pokemonViewModel)
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertExists()
    }

    @Test
    fun test_load_more_button_click_loads_more_data() {
        composeTestRule.setContent {
            PokemonApp(pokemonViewModel)
        }

        composeTestRule.onNodeWithTag("LoadMoreButton").performClick()

        // Add assertion to verify ViewModel's fetchPokemonList method call
    }

    @Test
    fun test_error_message_displayed() {
        composeTestRule.setContent {
            PokemonApp(pokemonViewModel)
        }

        composeTestRule.onNodeWithTag("ErrorMessage").assertExists().assertTextContains("Failed to fetch data")
    }
}