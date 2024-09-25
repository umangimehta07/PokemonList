package com.example.workdaysampleapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.workdaysampleapp.data.model.Pokemon
import com.example.workdaysampleapp.data.model.PokemonListItem
import com.example.workdaysampleapp.data.model.PokemonListResponse
import com.example.workdaysampleapp.data.model.Sprites
import com.example.workdaysampleapp.domain.GetPokemonListUseCase
import com.example.workdaysampleapp.domain.GetPokemonUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class PokemonViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Mock
    private lateinit var getPokemonUseCase: GetPokemonUseCase

    @Mock
    private lateinit var pokemonListObserver: Observer<List<PokemonListItem>>

    @Mock
    private lateinit var selectedPokemonObserver: Observer<Pokemon>

    @Mock
    private lateinit var errorObserver: Observer<String>

    private lateinit var pokemonViewModel: PokemonViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        pokemonViewModel = PokemonViewModel(getPokemonListUseCase, getPokemonUseCase)
        pokemonViewModel.pokemonList.observeForever(pokemonListObserver)
        pokemonViewModel.selectedPokemon.observeForever(selectedPokemonObserver)
        pokemonViewModel.error.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        pokemonViewModel.pokemonList.removeObserver(pokemonListObserver)
        pokemonViewModel.selectedPokemon.removeObserver(selectedPokemonObserver)
        pokemonViewModel.error.removeObserver(errorObserver)
    }

    @Test
    fun fetchPokemonList_success() = runTest {
        val pokemonListItems = listOf(PokemonListItem("bulbasaur", "url"))
        val response = Response.success(PokemonListResponse(pokemonListItems))
        `when`(getPokemonListUseCase.execute(anyInt(), anyInt())).thenReturn(response)

        pokemonViewModel.fetchPokemonList()

        advanceUntilIdle()

        Assert.assertEquals(true, response.isSuccessful)
    }

    @Test
    fun fetchPokemonList_failure() = runTest {
        val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "{}")
        val response = Response.error<PokemonListResponse>(404, errorResponseBody)
        `when`(getPokemonListUseCase.execute(anyInt(), anyInt())).thenReturn(response)

        pokemonViewModel.fetchPokemonList()

        advanceUntilIdle()

        Assert.assertEquals(false, response.isSuccessful)
    }

    @Test
    fun fetchPokemon_success() = runTest {
        val pokemon = Pokemon(1, "bulbasaur", Sprites("url"), 60, 1, emptyList(), emptyList())
        val response = Response.success(pokemon)
        `when`(getPokemonUseCase.execute(anyString())).thenReturn(response)

        pokemonViewModel.fetchPokemon("bulbasaur")

        advanceUntilIdle()
        Assert.assertEquals(true, response.isSuccessful)
    }

    @Test
    fun fetchPokemon_failure() = runTest {
        val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "{}")
        val response = Response.error<Pokemon>(404, errorResponseBody)
        `when`(getPokemonUseCase.execute(anyString())).thenReturn(response)

        pokemonViewModel.fetchPokemon("bulbasaur")

        advanceUntilIdle()

        Assert.assertEquals(false, response.isSuccessful)
    }
}
