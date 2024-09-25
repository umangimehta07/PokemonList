package com.example.workdaysampleapp.domain

import com.example.workdaysampleapp.data.model.Pokemon
import com.example.workdaysampleapp.data.model.Sprites
import com.example.workdaysampleapp.data.repository.PokemonRepository
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
class GetPokemonUseCaseTest {

    @Mock
    private lateinit var repository: PokemonRepository

    private lateinit var getPokemonUseCase: GetPokemonUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getPokemonUseCase = GetPokemonUseCase(repository)
    }

    @Test
    fun `execute returns success`() = runTest {
        val pokemon = Pokemon(1, "bulbasaur", Sprites("url"), 60, 1, emptyList(), emptyList())
        val response = Response.success(pokemon)
        `when`(repository.getPokemon(anyString())).thenReturn(response)

        val result = getPokemonUseCase.execute("bulbasaur")

        assert(result.isSuccessful)
        assert(result.body() == pokemon)
    }

    @Test
    fun `execute returns error`() = runTest {
        val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "{}")
        val response = Response.error<Pokemon>(404, errorResponseBody)
        `when`(repository.getPokemon(anyString())).thenReturn(response)

        val result = getPokemonUseCase.execute("bulbasaur")

        assert(!result.isSuccessful)
        assert(result.code() == 404)
    }
}
