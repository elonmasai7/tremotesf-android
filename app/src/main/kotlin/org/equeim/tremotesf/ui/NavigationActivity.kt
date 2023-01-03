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

package org.equeim.tremotesf.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.view.ActionMode
import androidx.core.app.ActivityCompat
import androidx.core.view.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.color.DynamicColors
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.equeim.tremotesf.R
import org.equeim.tremotesf.databinding.NavigationActivityBinding
import org.equeim.tremotesf.service.ForegroundService
import org.equeim.tremotesf.ui.utils.hideKeyboard
import org.equeim.tremotesf.ui.utils.launchAndCollectWhenStarted
import timber.log.Timber


class NavigationActivity : AppCompatActivity(), NavControllerProvider {
    companion object {
        private val createdActivities = mutableListOf<NavigationActivity>()

        fun finishAllActivities() = createdActivities.apply {
            forEach(Activity::finishAndRemoveTask)
            clear()
        }
    }

    private val model by viewModels<NavigationActivityViewModel>()

    private lateinit var binding: NavigationActivityBinding

    var actionMode: ActionMode? = null
        private set

    override lateinit var navController: NavController

    lateinit var appBarConfiguration: AppBarConfiguration
        private set

    lateinit var upNavigationIcon: DrawerArrowDrawable
        private set

    private val _windowInsets = MutableStateFlow<WindowInsetsCompat?>(null)
    val windowInsets: Flow<WindowInsetsCompat> = _windowInsets.filterNotNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("onCreate() called with: savedInstanceState = $savedInstanceState")
        Timber.i("onCreate: intent = $intent")

        super.onCreate(savedInstanceState)
        createdActivities.add(this)
        AppForegroundTracker.registerActivity(this)

        applyColorTheme()
        overrideIntentWithDeepLink()

        binding = NavigationActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        handleWindowInsets()

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController
        navController.addOnDestinationChangedListener { _, _, _ ->
            hideKeyboard()
        }

        appBarConfiguration = AppBarConfiguration(navController.graph)
        upNavigationIcon = DrawerArrowDrawable(this).apply { progress = 1.0f }

        handleDropEvents()

        model.showRpcErrorToast.filterNotNull().launchAndCollectWhenStarted(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            model.rpcErrorToastShown()
        }

        ForegroundService.startStopAutomatically()

        Timber.i("onCreate: return")
    }

    private fun applyColorTheme() {
        val colorTheme = ActivityThemeProvider.colorTheme.value
        if (colorTheme == Settings.ColorTheme.System) {
            Timber.i("Applying dynamic colors")
            DynamicColors.applyToActivityIfAvailable(this@NavigationActivity)
        } else {
            Timber.i("Setting color theme $colorTheme")
            setTheme(colorTheme.activityThemeResId)
        }

        lifecycleScope.launch {
            val newColorTheme = ActivityThemeProvider.colorTheme.first { it != colorTheme }
            Timber.i("Color theme changed to $newColorTheme, recreating")
            recreate()
        }
    }

    private fun overrideIntentWithDeepLink() {
        if (model.navigatedInitially) return
        model.navigatedInitially = true

        val intent = model.getInitialDeepLinkIntent(intent) ?: return
        Timber.i("overrideIntentWithDeepLink: intent = $intent")
        this.intent = intent
    }

    private fun handleWindowInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            _windowInsets.value = insets
            WindowInsetsCompat.CONSUMED
        }
        windowInsets
            .map { it.toActivityMargins() }
            .distinctUntilChanged()
            .launchAndCollectWhenStarted(this) { margins ->
                binding.root.apply {
                    if (marginLeft != margins.left || marginRight != margins.right || marginBottom != margins.bottom) {
                        updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            leftMargin = margins.left
                            rightMargin = margins.right
                            bottomMargin = margins.bottom
                        }
                    }
                }
            }
    }

    private fun handleDropEvents() {
        binding.root.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Timber.d("Handling drag start event")
                    model.acceptDragStartEvent(event.clipDescription)
                }
                DragEvent.ACTION_DROP -> {
                    Timber.d("Handling drop event")
                    val directions = model.getAddTorrentDirections(event.clipData)
                    if (directions != null) {
                        ActivityCompat.requestDragAndDropPermissions(this, event)
                        navController.navigate(
                            directions.destinationId,
                            directions.arguments,
                            NavOptions.Builder()
                                .setPopUpTo(navController.graph.startDestinationId, false)
                                .build()
                        )
                    }
                    directions != null
                }
                /**
                 * Don't enter [also] branch to avoid log spam
                 */
                else -> return@setOnDragListener false
            }.also {
                if (it) {
                    Timber.d("Accepting event")
                } else {
                    Timber.d("Rejecting event")
                }
            }
        }
    }

    override fun onStart() {
        Timber.i("onStart() called")
        super.onStart()
    }

    override fun onStop() {
        Timber.i("onStop() called")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.i("onDestroy() called")
        createdActivities.remove(this)
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        Timber.i("onNewIntent() called with: intent = $intent")
        super.onNewIntent(intent)
        model.getAddTorrentDirections(intent)?.let { (destinationId, arguments) ->
            navController.navigate(
                destinationId,
                arguments,
                NavOptions.Builder()
                    .setPopUpTo(navController.graph.startDestinationId, false)
                    .build()
            )
        }
    }

    override fun onSupportActionModeStarted(mode: ActionMode) {
        super.onSupportActionModeStarted(mode)
        actionMode = mode
    }

    override fun onSupportActionModeFinished(mode: ActionMode) {
        super.onSupportActionModeFinished(mode)
        actionMode = null
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if ((supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.primaryNavigationFragment as? NavigationFragment)?.showOverflowMenu() == true) {
                return true
            }
        }
        return super.onKeyUp(keyCode, event)
    }
}

class NavHostFragment : NavHostFragment() {
    override fun onCreateNavHostController(navHostController: NavHostController) {
        super.onCreateNavHostController(navHostController)
        navHostController.addOnDestinationChangedListener { _, destination, arguments ->
            Timber.i("Destination changed: destination = $destination, arguments = $arguments")
        }
    }

    @Suppress("OverridingDeprecatedMember", "OVERRIDE_DEPRECATION")
    override fun createFragmentNavigator(): Navigator<out androidx.navigation.fragment.FragmentNavigator.Destination> {
        return FragmentNavigator(requireContext(), childFragmentManager, id, navController)
    }

    // NavController doesn't set any pop animations when handling deep links
    // Use this workaround to always set pop animations
    @Navigator.Name("fragment")
    class FragmentNavigator(
        context: Context,
        fragmentManager: FragmentManager,
        @IdRes containerId: Int,
        private val navController: NavController
    ) : androidx.navigation.fragment.FragmentNavigator(context, fragmentManager, containerId) {
        override fun navigate(
            entries: List<NavBackStackEntry>,
            navOptions: NavOptions?,
            navigatorExtras: Navigator.Extras?
        ) = super.navigate(entries, navOptions?.overridePopAnimations(), navigatorExtras)

        override fun navigate(
            destination: Destination,
            args: Bundle?,
            navOptions: NavOptions?,
            navigatorExtras: Navigator.Extras?
        ) = super.navigate(destination, args, navOptions?.overridePopAnimations(), navigatorExtras)

        private fun NavOptions.overridePopAnimations() =
            NavOptions.Builder()
                .apply {
                    if (navController.currentDestination != null) {
                        setPopEnterAnim(popEnterAnim.orDefault(androidx.navigation.ui.R.animator.nav_default_pop_enter_anim))
                        setPopExitAnim(popExitAnim.orDefault(androidx.navigation.ui.R.animator.nav_default_pop_exit_anim))
                    }
                    setEnterAnim(enterAnim)
                    setExitAnim(exitAnim)
                    setLaunchSingleTop(shouldLaunchSingleTop())
                    setPopUpTo(popUpToId, isPopUpToInclusive())
                }
                .build()

        private companion object {
            fun Int.orDefault(@AnimatorRes defaultAnimator: Int): Int = if (this != -1) this else defaultAnimator
        }
    }
}

private data class ActivityMargins(
    val left: Int,
    val right: Int,
    val bottom: Int
)

private fun WindowInsetsCompat.toActivityMargins(): ActivityMargins {
    val systemBars = getInsets(WindowInsetsCompat.Type.systemBars())
    val ime = getInsets(WindowInsetsCompat.Type.ime())
    return ActivityMargins(
        left = ime.left.orOtherIfZero(systemBars.left),
        right = ime.right.orOtherIfZero(systemBars.right),
        bottom = ime.bottom
    )
}

private fun Int.orOtherIfZero(other: Int) = if (this != 0) this else other
