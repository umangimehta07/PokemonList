package com.example.workdaysampleapp.data.repository

import com.example.workdaysampleapp.data.api.ApiService
import com.example.workdaysampleapp.data.model.Pokemon
import com.example.workdaysampleapp.data.model.PokemonListResponse
import com.example.workdaysampleapp.data.model.Sprites
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class PokemonRepositoryImplTest {

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var repository: PokemonRepositoryImpl
    private val baseUrl = "https://pokeapi.co/api/v2/"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = PokemonRepositoryImpl(baseUrl).apply {
            this.apiService = this@PokemonRepositoryImplTest.apiService
        }
    }

    @Test
    fun `getPokemonList returns success`() = runTest {
        val pokemonListResponse = PokemonListResponse(listOf())
        val response = Response.success(pokemonListResponse)
        `when`(apiService.getPokemonList(anyInt(), anyInt())).thenReturn(response)

        val result = repository.getPokemonList(10, 0)

        assert(result.isSuccessful)
        assert(result.body() == pokemonListResponse)
    }

    @Test(expected = Exception::class)
    fun `getPokemonList returns error`() = runTest {
        val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "{}")
        val response = Response.error<PokemonListResponse>(404, errorResponseBody)
        `when`(apiService.getPokemonList(anyInt(), anyInt())).thenReturn(response)

        repository.getPokemonList(10, 0)
    }

    @Test
    fun `getPokemon returns success`() = runTest {
        val pokemon = Pokemon(1, "bulbasaur", Sprites("url"), 60, 1, emptyList(), emptyList())
        val response = Response.success(pokemon)
        `when`(apiService.getPokemon(anyString())).thenReturn(response)

        val result = repository.getPokemon("bulbasaur")

        assert(result.isSuccessful)
        assert(result.body() == pokemon)
    }

    @Test(expected = Exception::class)
    fun `getPokemon returns error`() = runTest {
        val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "{}")
        val response = Response.error<Pokemon>(404, errorResponseBody)
        `when`(apiService.getPokemon(anyString())).thenReturn(response)

        repository.getPokemon("bulbasaur")
    }
}
