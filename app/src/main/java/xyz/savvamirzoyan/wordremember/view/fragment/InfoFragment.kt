package xyz.savvamirzoyan.wordremember.view.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.play.core.review.ReviewManagerFactory
import xyz.savvamirzoyan.wordremember.BuildConfig
import xyz.savvamirzoyan.wordremember.databinding.FragmentInfoBinding


// Fragment is simple enough, so viewmodel is an overkill
class InfoFragment : Fragment() {

    private val sourceCodeIntent by lazy {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/savvasenok/word-remember"))
    }

    private val playMarketReviewIntent by lazy {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=${requireContext().packageName}")
        )
    }
    private val playMarketByLinkReviewIntent by lazy {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
        )
    }
    private val websiteIntent by lazy {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://savvamirzoyan.xyz"))
    }
    private val contactMeIntent by lazy {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/savvamirzoyanbio"))
    }

    private val manager by lazy { ReviewManagerFactory.create(requireContext()) }

    private var _binding: FragmentInfoBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        setAppVersion()
        setOnOpenSourceCodeClickListener()
        setOnRateAppClickListener()
        setOnWebsiteClickListener()
        setOnContactMeClickListener()

        return binding.root
    }

    private fun getAppVersion(): String = BuildConfig.VERSION_NAME

    private fun setAppVersion() {
        binding.textViewAppVersion.text = getAppVersion()
    }

    private fun setOnOpenSourceCodeClickListener() {
        binding.linearLayoutSourceCode.setOnClickListener {
            startActivity(sourceCodeIntent)
        }
    }

    private fun setOnRateAppClickListener() {
        binding.linearLayoutRateApp.setOnClickListener {
            requestInAppReviewInfo()
        }
    }

    private fun requestInAppReviewInfo() {
        manager.requestReviewFlow().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewFlow = manager.launchReviewFlow(requireActivity(), task.result)

                if (!reviewFlow.isSuccessful) {
                    openPlayMarket()
                }
            } else {
                openPlayMarket()
            }
        }
    }

    private fun openPlayMarket() {
        try {
            startActivity(playMarketReviewIntent)
        } catch (e: ActivityNotFoundException) {
            startActivity(playMarketByLinkReviewIntent)
        }
    }

    private fun setOnWebsiteClickListener() {
        binding.linearLayoutWebsite.setOnClickListener {
            startActivity(websiteIntent)
        }
    }

    private fun setOnContactMeClickListener() {
        binding.linearLayoutContactMe.setOnClickListener {
            startActivity(contactMeIntent)
        }
    }
}