package com.example.tictactoe.ui.Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.tictactoe.data.Player
import com.example.tictactoe.data.PlayerRepository
import kotlinx.coroutines.flow.*

/**
 * The viewModel for the [SettingScreen]
 */

@ViewModelFactoryDsl
class SettingsViewModel(playerRepository: PlayerRepository): ViewModel() {

    private val _isDialogOpen = MutableStateFlow(isDialogOpen())
    val isDialogOpen: StateFlow<isDialogOpen> = _isDialogOpen.asStateFlow()

    fun ChangeShowingPlayersAlertDialog() {
        _isDialogOpen.update { currentState ->
            currentState.copy(
                isShowingPlayersDialogOpen = !(currentState.isShowingPlayersDialogOpen)
            )
        }
    }

    fun ChangeCheckClearDataAlertDialog() {
        _isDialogOpen.update { currentState ->
            currentState.copy(
                isCheckClearDataDialogOpen = !(currentState.isCheckClearDataDialogOpen)
            )
        }
    }

    val settingsUiState: StateFlow<SettingsUiState> =
        playerRepository.getAllPlayersStream().map { SettingsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SettingsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SettingsUiState(val playerList: List<Player> = listOf())

data class isDialogOpen(
    var isShowingPlayersDialogOpen: Boolean = false,
    var isCheckClearDataDialogOpen: Boolean = false,
    )