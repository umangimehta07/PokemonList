package com.example.workdaysampleapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.workdaysampleapp.domain.GetPokemonListUseCase
import com.example.workdaysampleapp.domain.GetPokemonUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PokemonViewModelFactoryTest {

    @Mock
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Mock
    private lateinit var getPokemonUseCase: GetPokemonUseCase

    private lateinit var factory: PokemonViewModelFactory

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        factory = PokemonViewModelFactory(getPokemonListUseCase, getPokemonUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `create returns PokemonViewModel`() {
        val viewModel = factory.create(PokemonViewModel::class.java)
        assertTrue(viewModel is PokemonViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create throws IllegalArgumentException for unknown ViewModel class`() {
        factory.create(UnknownViewModel::class.java)
    }

    private class UnknownViewModel : ViewModel()
}
