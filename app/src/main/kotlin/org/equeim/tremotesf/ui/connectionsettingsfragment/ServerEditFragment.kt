/*
 * Copyright (C) 2017-2020 Alexey Rochev <equeim@gmail.com>
 *
 * This file is part of Tremotesf.
 *
 * Tremotesf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Tremotesf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.equeim.tremotesf.ui.connectionsettingsfragment

import android.Manifest
import android.app.Application
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.text.trimmedLength
import androidx.core.view.isVisible
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.onEach

import org.equeim.tremotesf.BuildConfig
import org.equeim.tremotesf.R
import org.equeim.tremotesf.databinding.ServerEditCertificatesFragmentBinding
import org.equeim.tremotesf.databinding.ServerEditFragmentBinding
import org.equeim.tremotesf.databinding.ServerEditProxyFragmentBinding
import org.equeim.tremotesf.data.rpc.Server
import org.equeim.tremotesf.data.rpc.Servers
import org.equeim.tremotesf.data.rpc.WifiNetworkHelper
import org.equeim.tremotesf.ui.NavigationDialogFragment
import org.equeim.tremotesf.ui.NavigationFragment
import org.equeim.tremotesf.ui.utils.ArrayDropdownAdapter
import org.equeim.tremotesf.ui.utils.IntFilter
import org.equeim.tremotesf.ui.utils.RuntimePermissionViewModel
import org.equeim.tremotesf.ui.utils.savedState
import org.equeim.tremotesf.ui.utils.textInputLayout
import org.equeim.tremotesf.ui.utils.viewBinding
import org.equeim.tremotesf.ui.utils.viewModelFactory
import org.equeim.tremotesf.utils.Logger
import org.equeim.tremotesf.utils.collectWhenStarted


class ServerEditFragment : NavigationFragment(
    R.layout.server_edit_fragment,
    0,
    R.menu.server_edit_fragment_menu
), Logger {
    private val args: ServerEditFragmentArgs by navArgs()
    private lateinit var model: ServerEditFragmentViewModel

    private lateinit var locationPermissionModel: RuntimePermissionViewModel
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    @RequiresApi(Build.VERSION_CODES.Q)
    private lateinit var backgroundLocationPermissionModel: RuntimePermissionViewModel
    @RequiresApi(Build.VERSION_CODES.Q)
    private lateinit var requestBackgroundLocationPermissionLauncher: ActivityResultLauncher<String>

    private val binding by viewBinding(ServerEditFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ServerEditFragmentViewModel.from(this, args.server)

        locationPermissionModel = ViewModelProvider(this, viewModelFactory { application ->
            RuntimePermissionViewModel(
                Manifest.permission.ACCESS_FINE_LOCATION,
                ServerEditFragmentDirections.toRuntimePermissionRationaleDialog(application.getString(R.string.location_permission_rationale)),
                ServerEditFragmentDirections.toRuntimePermissionSystemSettingsDialog(application.getString(R.string.location_permission_rationale)),
                application
            )
        })["locationPermissionModel", RuntimePermissionViewModel::class.java]

        requestLocationPermissionLauncher = locationPermissionModel.registerWithFragment(this)

        if (canRequestBackgroundLocationPermission()) {
            backgroundLocationPermissionModel =
                ViewModelProvider(this, viewModelFactory { application ->
                    RuntimePermissionViewModel(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        ServerEditFragmentDirections.toRuntimePermissionRationaleDialog(
                            application.getString(
                                R.string.background_location_permission_rationale
                            )
                        ),
                        ServerEditFragmentDirections.toRuntimePermissionSystemSettingsDialog(
                            application.getString(R.string.background_location_permission_rationale)
                        ),
                        application
                    )
                })["backgroundLocationPermissionModel", RuntimePermissionViewModel::class.java]

            requestBackgroundLocationPermissionLauncher =
                backgroundLocationPermissionModel.registerWithFragment(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            portEdit.filters = arrayOf(IntFilter(Server.portRange))

            proxySettingsButton.setOnClickListener {
                navigate(ServerEditFragmentDirections.toProxySettingsFragment(args.server))
            }

            httpsCheckBox.isChecked = false

            certificatedButton.isEnabled = false
            certificatedButton.setOnClickListener {
                navigate(ServerEditFragmentDirections.toCertificatesFragment(args.server))
            }
            httpsCheckBox.setOnCheckedChangeListener { _, checked ->
                certificatedButton.isEnabled = checked
            }

            authenticationCheckBox.isChecked = false
            authenticationCheckBox.setDependentViews(usernameEditLayout, passwordEditLayout)

            updateIntervalEdit.filters = arrayOf(IntFilter(Server.updateIntervalRange))
            timeoutEdit.filters = arrayOf(IntFilter(Server.timeoutRange))

            wifiAutoConnectCheckbox.setOnClickListener {
                if (wifiAutoConnectCheckbox.isChecked) {
                    locationPermissionModel.checkAndRequestPermission(this@ServerEditFragment, requestLocationPermissionLauncher)
                    if (canRequestBackgroundLocationPermission()) {
                        backgroundLocationPermissionModel.checkPermission(requireContext())
                    }
                }
            }
            wifiAutoConnectCheckbox.setDependentViews(
                locationPermissionButton,
                wifiAutoConnectSsidEditLayout,
                setSsidFromCurrentNetworkButton,
                backgroundWifiNetworksExplanation,
                backgroundLocationPermissionButton
            )
            locationPermissionModel.permissionGranted.onEach { granted ->
                locationPermissionButton.apply {
                    if (granted) {
                        setIconResource(R.drawable.ic_done_24dp)
                        setText(R.string.location_permission_granted)
                    } else {
                        setIconResource(R.drawable.ic_error_24dp)
                        setText(R.string.request_location_permission)
                    }
                    isClickable = !granted
                }
            }.collectWhenStarted(viewLifecycleOwner)
            locationPermissionButton.setOnClickListener {
                locationPermissionModel.checkAndRequestPermission(this@ServerEditFragment, requestLocationPermissionLauncher)
            }

            if (canRequestBackgroundLocationPermission()) {
                backgroundWifiNetworksExplanation.setText(R.string.background_wifi_networks_explanation_fdroid)

                backgroundLocationPermissionModel.permissionGranted.onEach { granted ->
                    info("background granted = $granted")
                    backgroundLocationPermissionButton.apply {
                        if (granted) {
                            setIconResource(R.drawable.ic_done_24dp)
                            setText(R.string.background_location_permission_granted)
                        } else {
                            icon = null
                            setText(R.string.request_background_location_permission)
                        }
                        isClickable = !granted
                    }
                }.collectWhenStarted(viewLifecycleOwner)
                backgroundLocationPermissionButton.setOnClickListener {
                    backgroundLocationPermissionModel.checkAndRequestPermission(this@ServerEditFragment, requestBackgroundLocationPermissionLauncher)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    backgroundWifiNetworksExplanation.setText(R.string.background_wifi_networks_explanation_google)
                } else {
                    backgroundWifiNetworksExplanation.isVisible = false
                }
                backgroundLocationPermissionButton.isVisible = false
            }

            setSsidFromCurrentNetworkButton.setOnClickListener {
                val ssid = WifiNetworkHelper.currentWifiSsid

                if (ssid != null) {
                    wifiAutoConnectSsidEdit.setText(ssid)
                } else {
                    Toast.makeText(requireContext(), R.string.current_ssid_error, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        setupToolbar()

        if (savedInstanceState == null) {
            with(binding) {
                val server = model.server
                nameEdit.setText(server.name)
                addressEdit.setText(server.address)
                portEdit.setText(server.port.toString())
                apiPathEdit.setText(server.apiPath)
                httpsCheckBox.isChecked = server.httpsEnabled
                authenticationCheckBox.isChecked = server.authentication
                usernameEdit.setText(server.username)
                passwordEdit.setText(server.password)
                updateIntervalEdit.setText(server.updateInterval.toString())
                timeoutEdit.setText(server.timeout.toString())
                wifiAutoConnectCheckbox.isChecked = server.autoConnectOnWifiNetworkEnabled
                wifiAutoConnectSsidEdit.setText(server.autoConnectOnWifiNetworkSSID)
            }
        }
    }

    override fun onStart() {
        info("onStart() called")
        super.onStart()
        if (binding.wifiAutoConnectCheckbox.isChecked) {
            locationPermissionModel.checkPermission(requireContext())
            if (canRequestBackgroundLocationPermission()) {
                backgroundLocationPermissionModel.checkPermission(requireContext())
            }
        }
    }

    override fun onToolbarMenuItemClicked(menuItem: MenuItem): Boolean {
        if (menuItem.itemId != R.id.done) {
            return false
        }

        val error = getString(R.string.empty_field_error)
        val checkLength: (EditText) -> Boolean = { edit ->
            val ret: Boolean
            edit.textInputLayout.error = if (edit.text.trimmedLength() == 0) {
                ret = false
                error
            } else {
                ret = true
                null
            }
            ret
        }

        with(binding) {
            val nameOk = checkLength(nameEdit)
            val addressOk = checkLength(addressEdit)
            val portOk = checkLength(portEdit)
            val apiPathOk = checkLength(apiPathEdit)
            val updateIntervalOk = checkLength(updateIntervalEdit)
            val timeoutOk = checkLength(timeoutEdit)

            val nameEditText = nameEdit.text?.toString() ?: ""

            if (nameOk &&
                    addressOk &&
                    portOk &&
                    apiPathOk &&
                    updateIntervalOk &&
                    timeoutOk) {
                if (nameEditText != model.existingServer?.name &&
                        Servers.servers.value.find { it.name == nameEditText } != null) {
                    navigate(ServerEditFragmentDirections.toOverwriteDialog())
                } else {
                    save()
                }
            }
        }

        return true
    }

    private fun setupToolbar() {
        toolbar?.apply {
            setTitle(if (model.existingServer == null) R.string.add_server else R.string.edit_server)
            menu.findItem(R.id.done).setTitle(if (model.existingServer == null) {
                R.string.add
            } else {
                R.string.save
            })
        }
    }

    fun save() {
        with(binding) {
            model.server.apply {
                name = nameEdit.text?.toString()?.trim() ?: ""
                address = addressEdit.text?.toString()?.trim() ?: ""
                port = portEdit.text?.toString()?.toInt() ?: 0
                apiPath = apiPathEdit.text?.toString()?.trim() ?: ""
                httpsEnabled = httpsCheckBox.isChecked
                authentication = authenticationCheckBox.isChecked
                username = usernameEdit.text?.toString()?.trim() ?: ""
                password = passwordEdit.text?.toString()?.trim() ?: ""
                updateInterval = updateIntervalEdit.text?.toString()?.toInt() ?: 0
                timeout = timeoutEdit.text?.toString()?.toInt() ?: 0
                autoConnectOnWifiNetworkEnabled = wifiAutoConnectCheckbox.isChecked
                autoConnectOnWifiNetworkSSID =
                    wifiAutoConnectSsidEdit.text?.toString()?.trim() ?: ""
            }
        }

        model.existingServer.let { existing ->
            if (existing == null) {
                Servers.addServer(model.server)
            } else {
                Servers.setServer(existing, model.server)
            }
        }

        navController.popBackStack(R.id.server_edit_fragment, true)
    }

    private companion object {
        fun CheckBox.setDependentViews(vararg views: View) {
            views.forEach { it.isEnabled = isChecked }
            setOnCheckedChangeListener { _, isChecked ->
                views.forEach {
                    it.isEnabled = isChecked
                }
            }
        }

        fun canRequestBackgroundLocationPermission() =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !BuildConfig.GOOGLE
    }
}

class ServerEditFragmentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        private const val SERVER_NAME = "serverName"

        fun from(fragment: NavigationFragment, server: String?): ServerEditFragmentViewModel {
            val entry = fragment.navController.getBackStackEntry(R.id.server_edit_fragment)
            val factory = SavedStateViewModelFactory(fragment.requireActivity().application,
                                                     entry, bundleOf(SERVER_NAME to server))
            return ViewModelProvider(entry, factory)[ServerEditFragmentViewModel::class.java]
        }
    }

    val existingServer: Server?
    init {
        val serverName: String? = savedStateHandle[SERVER_NAME]
        existingServer = if (serverName != null) Servers.servers.value.find { it.name == serverName } else null
    }
    val server by savedState(savedStateHandle) { existingServer?.copy() ?: Server() }
}

class ServerOverwriteDialogFragment : NavigationDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.server_exists)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.overwrite) { _, _ ->
                    (parentFragmentManager.primaryNavigationFragment as? ServerEditFragment)?.save()
                }
                .create()
    }
}

class ServerCertificatesFragment : NavigationFragment(R.layout.server_edit_certificates_fragment,
                                                R.string.certificates) {
    private val args: ServerCertificatesFragmentArgs by navArgs()
    private lateinit var model: ServerEditFragmentViewModel

    private val binding by viewBinding(ServerEditCertificatesFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ServerEditFragmentViewModel.from(this, args.server)

        with(binding) {
            selfSignedCertificateCheckBox.setOnCheckedChangeListener { _, checked ->
                selfSignedCertificateLayout.isEnabled = checked
            }
            selfSignedCertificateLayout.isEnabled = false
            clientCertificateCheckBox.setOnCheckedChangeListener { _, checked ->
                clientCertificateLayout.isEnabled = checked
            }
            clientCertificateLayout.isEnabled = false

            with(model.server) {
                selfSignedCertificateCheckBox.isChecked = selfSignedCertificateEnabled
                selfSignedCertificateEdit.setText(selfSignedCertificate)
                clientCertificateCheckBox.isChecked = clientCertificateEnabled
                clientCertificateEdit.setText(clientCertificate)
            }
        }
    }

    override fun onNavigatedFrom() {
        if (view != null) {
            with(binding) {
                model.server.apply {
                    selfSignedCertificateEnabled = selfSignedCertificateCheckBox.isChecked
                    selfSignedCertificate = selfSignedCertificateEdit.text?.toString() ?: ""
                    clientCertificateEnabled = clientCertificateCheckBox.isChecked
                    clientCertificate = clientCertificateEdit.text?.toString() ?: ""
                }
            }
        }
    }
}

class ServerProxySettingsFragment : NavigationFragment(R.layout.server_edit_proxy_fragment,
                                                 R.string.proxy_settings) {
    private companion object {
        // Should match R.array.proxy_type_items
        val proxyTypeItems = arrayOf(org.equeim.libtremotesf.Server.ProxyType.Default,
                                     org.equeim.libtremotesf.Server.ProxyType.Http,
                                     org.equeim.libtremotesf.Server.ProxyType.Socks5)
    }

    private val args: ServerProxySettingsFragmentArgs by navArgs()
    private lateinit var model: ServerEditFragmentViewModel
    private lateinit var proxyTypeItemValues: Array<String>

    private val binding by viewBinding(ServerEditProxyFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        proxyTypeItemValues = resources.getStringArray(R.array.proxy_type_items)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ServerEditFragmentViewModel.from(this, args.server)

        with(binding) {
            proxyTypeView.setAdapter(ArrayDropdownAdapter(proxyTypeItemValues))
            proxyTypeView.setOnItemClickListener { _, _, position, _ ->
                update(proxyTypeItems[position] != org.equeim.libtremotesf.Server.ProxyType.Default)
            }

            portEdit.filters = arrayOf(IntFilter(Server.portRange))

            with(model.server) {
                proxyTypeView.setText(proxyTypeItemValues[proxyTypeItems.indexOf(nativeProxyType())])
                addressEdit.setText(proxyHostname)
                portEdit.setText(proxyPort.toString())
                usernameEdit.setText(proxyUser)
                passwordEdit.setText(proxyPassword)

                if (nativeProxyType() == org.equeim.libtremotesf.Server.ProxyType.Default) {
                    update(false)
                }
            }
        }
    }

    override fun onNavigatedFrom() {
        if (view != null) {
            with(binding) {
                model.server.apply {
                    proxyType = Server.fromNativeProxyType(proxyTypeItems[proxyTypeItemValues.indexOf(proxyTypeView.text.toString())])
                    proxyHostname = addressEdit.text?.toString() ?: ""
                    proxyPort = portEdit.text?.toString()?.toInt() ?: 0
                    proxyUser = usernameEdit.text?.toString() ?: ""
                    proxyPassword = passwordEdit.text?.toString() ?: ""
                }
            }
        }
    }

    private fun update(enabled: Boolean) {
        with(binding) {
            addressEditLayout.isEnabled = enabled
            portEditLayout.isEnabled = enabled
            usernameEditLayout.isEnabled = enabled
            passwordEditLayout.isEnabled = enabled
        }
    }
}
