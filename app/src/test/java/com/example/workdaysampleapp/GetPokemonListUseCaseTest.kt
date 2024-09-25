package com.example.workdaysampleapp.domain

import com.example.workdaysampleapp.data.model.PokemonListResponse
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
class GetPokemonListUseCaseTest {

    @Mock
    private lateinit var repository: PokemonRepository

    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getPokemonListUseCase = GetPokemonListUseCase(repository)
    }

    @Test
    fun `execute returns success`() = runTest {
        val pokemonListResponse = PokemonListResponse(listOf())
        val response = Response.success(pokemonListResponse)
        `when`(repository.getPokemonList(anyInt(), anyInt())).thenReturn(response)

        val result = getPokemonListUseCase.execute(10, 0)

        assert(result.isSuccessful)
        assert(result.body() == pokemonListResponse)
    }

    @Test
    fun `execute returns error`() = runTest {
        val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "{}")
        val response = Response.error<PokemonListResponse>(404, errorResponseBody)
        `when`(repository.getPokemonList(anyInt(), anyInt())).thenReturn(response)

        val result = getPokemonListUseCase.execute(10, 0)

        assert(!result.isSuccessful)
        assert(result.code() == 404)
    }
}
