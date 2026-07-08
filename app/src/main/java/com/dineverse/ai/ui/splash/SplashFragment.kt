package com.dineverse.ai.ui.splash

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dineverse.ai.R
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(), SensorEventListener {

    private val viewModel: SplashViewModel by viewModels()
    private var sensorManager: SensorManager? = null
    private var rotationSensor: Sensor? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEdgeToEdge()
        setup3DMotion()
        observeState()
        startEntryAnimation()
    }

    private fun setup3DMotion() {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    private fun startEntryAnimation() {
        binding.ivLogo.alpha = 0f
        binding.ivLogo.scaleX = 0.5f
        binding.ivLogo.scaleY = 0.5f
        binding.ivLogo.rotationY = -45f

        binding.ivLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .rotationY(0f)
            .setDuration(1000)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()

        binding.tvTitle.alpha = 0f
        binding.tvTitle.translationY = 50f
        binding.tvTitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(500)
            .setDuration(800)
            .start()
    }

    override fun onResume() {
        super.onResume()
        rotationSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (_binding != null && event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            
            val orientations = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientations)
            
            // orientations[1] is pitch (tilt back/forth) -> rotationX
            // orientations[2] is roll (tilt left/right) -> rotationY
            
            val pitch = Math.toDegrees(orientations[1].toDouble()).toFloat()
            val roll = Math.toDegrees(orientations[2].toDouble()).toFloat()

            // Limit the tilt range for a subtle professional effect
            val targetRotationX = (pitch * 0.1f).coerceIn(-10f, 10f)
            val targetRotationY = (roll * 0.1f).coerceIn(-10f, 10f)

            binding.ivLogo.rotationX = -targetRotationX
            binding.ivLogo.rotationY = targetRotationY
            
            // Subtle translation for depth feel
            binding.ivLogo.translationX = targetRotationY * 2
            binding.ivLogo.translationY = -targetRotationX * 2
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun setupEdgeToEdge() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, requireView())
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onDestroyView() {
        val window = requireActivity().window
        val controller = WindowInsetsControllerCompat(window, requireView())
        controller.show(WindowInsetsCompat.Type.systemBars())
        super.onDestroyView()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is SplashState.Authenticated -> {
                            if (findNavController().currentDestination?.id == R.id.splashFragment) {
                                Timber.d("Navigating to Home")
                                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                            }
                        }
                        is SplashState.Unauthenticated -> {
                            if (findNavController().currentDestination?.id == R.id.splashFragment) {
                                Timber.d("Navigating to Login")
                                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}
