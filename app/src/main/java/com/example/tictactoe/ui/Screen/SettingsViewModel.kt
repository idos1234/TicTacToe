package com.example.tictactoe.ui.Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * The viewModel for the [SettingScreen]
 */

@ViewModelFactoryDsl
class SettingsViewModel(): ViewModel() {

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
}

data class isDialogOpen(
    var isShowingPlayersDialogOpen: Boolean = false,
    var isCheckClearDataDialogOpen: Boolean = false,
    )