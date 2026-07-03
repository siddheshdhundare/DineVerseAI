package com.dineverse.ai.ui.customer.home
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dineverse.ai.core.base.BaseFragment
import com.dineverse.ai.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }
}
