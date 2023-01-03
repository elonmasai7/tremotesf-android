/*
 * Copyright (C) 2017-2022 Alexey Rochev <equeim@gmail.com>
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

package org.equeim.tremotesf.ui.addtorrent

import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import androidx.core.text.trimmedLength
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import org.equeim.libtremotesf.RpcConnectionState
import org.equeim.tremotesf.R
import org.equeim.tremotesf.databinding.AddTorrentLinkFragmentBinding
import org.equeim.tremotesf.rpc.GlobalRpc
import org.equeim.tremotesf.rpc.statusString
import org.equeim.tremotesf.torrentfile.rpc.Rpc
import org.equeim.tremotesf.ui.utils.*
import timber.log.Timber


class AddTorrentLinkFragment : AddTorrentFragment(
    R.layout.add_torrent_link_fragment,
    R.string.add_torrent_link,
    0
) {
    private val args: AddTorrentLinkFragmentArgs by navArgs()

    private val binding by viewLifecycleObject(AddTorrentLinkFragmentBinding::bind)
    private var directoriesAdapter: AddTorrentDirectoriesAdapter by viewLifecycleObject()
    private var connectSnackbar: Snackbar? by viewLifecycleObjectNullable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate: arguments = $arguments")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        with(binding) {
            args.uri?.let { torrentLinkEdit.setText(it.toString()) }

            priorityView.setText(R.string.normal_priority)
            priorityView.setAdapter(ArrayDropdownAdapter(priorityItems))

            startDownloadingCheckBox.isChecked = GlobalRpc.serverSettings.startAddedTorrents

            addButton.setOnClickListener { addTorrentLink() }
            addButton.extendWhenImeIsHidden(requiredActivity.windowInsets, viewLifecycleOwner)
        }

        handleDragEvents()

        directoriesAdapter = AddTorrentFileFragment.setupDownloadDirectoryEdit(
            binding.downloadDirectoryLayout,
            this,
            savedInstanceState
        )

        GlobalRpc.status.launchAndCollectWhenStarted(viewLifecycleOwner, ::updateView)
    }

    private fun handleDragEvents() {
        val listener = OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Timber.d("Handling drag start event on $view")
                    TORRENT_LINK_MIME_TYPES.any(event.clipDescription::hasMimeType)
                }
                DragEvent.ACTION_DROP -> {
                    Timber.d("Handling drop event on $view")
                    val uri = event.clipData.getTorrentUri(requireContext())
                    if (uri != null && uri.type == TorrentUri.Type.Link) {
                        binding.torrentLinkEdit.setText(uri.uri.toString())
                        true
                    } else {
                        false
                    }
                }
                /**
                 * Don't enter [also] branch to avoid log spam
                 */
                else -> return@OnDragListener false
            }.also {
                if (it) {
                    Timber.d("Accepting event")
                } else {
                    Timber.d("Rejecting event")
                }
            }
        }
        binding.root.setOnDragListener(listener)
        binding.torrentLinkEdit.setOnDragListener(listener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        directoriesAdapter.saveInstanceState(outState)
    }

    private fun addTorrentLink(): Unit = with(binding) {
        var error = false

        torrentLinkEdit.textInputLayout.error =
            if (torrentLinkEdit.text?.trimmedLength() == 0) {
                error = true
                getString(R.string.empty_field_error)
            } else {
                null
            }

        val downloadDirectoryEdit = downloadDirectoryLayout.downloadDirectoryEdit
        val downloadDirectoryLayout = downloadDirectoryLayout.downloadDirectoryLayout

        downloadDirectoryLayout.error =
            if (downloadDirectoryEdit.text?.trimmedLength() == 0) {
                error = true
                getString(R.string.empty_field_error)
            } else {
                null
            }

        if (error) {
            return
        }

        GlobalRpc.nativeInstance.addTorrentLink(
            torrentLinkEdit.text?.toString() ?: "",
            downloadDirectoryEdit.text.toString(),
            priorityItemEnums[priorityItems.indexOf(priorityView.text.toString())],
            startDownloadingCheckBox.isChecked
        )

        directoriesAdapter.save()

        requiredActivity.onBackPressedDispatcher.onBackPressed()
    }

    private fun updateView(status: Rpc.Status) {
        with(binding) {
            when (status.connectionState) {
                RpcConnectionState.Disconnected -> {
                    placeholder.text = status.statusString
                    hideKeyboard()
                    if (connectSnackbar == null) {
                        connectSnackbar = coordinatorLayout.showSnackbar(
                            message = "",
                            length = Snackbar.LENGTH_INDEFINITE,
                            actionText = R.string.connect,
                            action = GlobalRpc.nativeInstance::connect,
                        ) {
                            if (connectSnackbar == it) {
                                connectSnackbar = null
                            }
                        }
                    }
                }
                RpcConnectionState.Connecting -> {
                    connectSnackbar?.dismiss()
                    connectSnackbar = null
                    placeholder.text = getString(R.string.connecting)
                }
                RpcConnectionState.Connected -> {
                    connectSnackbar?.dismiss()
                    connectSnackbar = null
                }
            }

            if (status.isConnected) {
                if (scrollView.visibility != View.VISIBLE) {
                    downloadDirectoryLayout.downloadDirectoryEdit.setText(GlobalRpc.serverSettings.downloadDirectory)
                    startDownloadingCheckBox.isChecked = GlobalRpc.serverSettings.startAddedTorrents
                    scrollView.visibility = View.VISIBLE
                }
                placeholderLayout.visibility = View.GONE
                addButton.show()
            } else {
                placeholderLayout.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
                addButton.hide()
            }

            progressBar.visibility = if (status.connectionState == RpcConnectionState.Connecting) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}
