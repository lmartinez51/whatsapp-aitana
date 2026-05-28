package com.lmartinez.miniaitana.service.engine

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.lmartinez.miniaitana.core.domain.repository.ConfigRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AutoPilotTileService : TileService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var listeningJob: Job? = null

    @Inject
    lateinit var configRepository: ConfigRepository

    override fun onTileAdded() {
        super.onTileAdded()
        updateTileStateFromConfig()
    }

    override fun onStartListening() {
        super.onStartListening()
        observeTileState()
    }

    override fun onStopListening() {
        listeningJob?.cancel()
        listeningJob = null
        super.onStopListening()
    }

    override fun onClick() {
        super.onClick()
        serviceScope.launch {
            val config = configRepository.getAppConfig().first()
            val enabled = !config.serviceEnabled
            configRepository.setServiceEnabled(enabled)
            pushTileState(enabled)
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun observeTileState() {
        listeningJob?.cancel()
        listeningJob = serviceScope.launch {
            configRepository.getAppConfig()
                .map { it.serviceEnabled }
                .distinctUntilChanged()
                .collect { enabled ->
                    pushTileState(enabled)
                }
        }
    }

    private fun updateTileStateFromConfig() {
        serviceScope.launch {
            val config = configRepository.getAppConfig().first()
            pushTileState(config.serviceEnabled)
        }
    }

    private fun pushTileState(enabled: Boolean) {
        val tile = qsTile ?: return
        val subtitle = if (enabled) "Armed & Running" else "Disabled"

        tile.label = TILE_LABEL
        tile.contentDescription = TILE_LABEL
        tile.subtitle = subtitle
        tile.stateDescription = subtitle
        // Using system icon for now to avoid missing resource error
        tile.icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_info)
        tile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }

    private companion object {
        const val TILE_LABEL = "Auto-Pilot"
    }
}
