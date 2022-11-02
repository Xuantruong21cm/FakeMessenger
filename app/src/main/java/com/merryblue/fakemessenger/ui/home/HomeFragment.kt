package com.merryblue.fakemessenger.ui.home

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.data.model.Contact
import com.merryblue.fakemessenger.databinding.FragmentHomeBinding
import com.merryblue.fakemessenger.ui.adapter.homeAdapter.HomeAdapter
import com.merryblue.fakemessenger.ui.adapter.homeAdapter.HomeAdapterCallback
import dagger.hilt.android.AndroidEntryPoint
import org.app.common.BaseFragment
import org.app.common.extensions.ImageViewType
import org.app.common.extensions.loadImageRes
import org.app.common.utils.getScreenHeight
import org.app.common.utils.getScreenWidth

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val conversationList = ArrayList<Contact>()
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeListener: HomeAdapterCallback

    override fun setUpViews() {

        screenWidth = getScreenWidth(requireActivity())
        screenHeight = getScreenHeight(requireActivity())

        //fake my user
        binding.imgMyAvatar.loadImageRes(R.drawable.avatar_test, ImageViewType.CIRCLE)
        //fake conversation
        for (i in 0 until 20) {
            conversationList.add(
                Contact(
                    i,
                    "https://kenh14cdn.com/2020/9/27/img3814-16008495660052057963035-16012244314321556076455.jpg",
                    "Người yêu thứ $i",
                    true,
                    true,
                    false,
                    false,
                    false,
                    false,
                    false,
                    true,
                    "Người yêu",
                    "Vợ",
                    69,
                    "Vợ $i",
                    "#8DCC78",
                    "Em yêu anh Trường 69 nghìnsdasdasdasdasdasd",
                    "06:09 PM"
                )
            )
        }

        homeAdapter = HomeAdapter(activity as HomeActivity, screenWidth)
        binding.rvContainerHome.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContainerHome.adapter = homeAdapter
        homeAdapter.setDataLastConversation(conversationList)

        initEvent()
    }

    private fun initEvent() {
        homeListener = object : HomeAdapterCallback {
            override fun onFakeMessage() {
                super.onFakeMessage()
                //(activity as HomeActivity).replaceFragment(FakeMessengerFragment())
            }

            override fun onFakeNotification() {
                super.onFakeNotification()
            }

            override fun onFakeVideoCall() {
                super.onFakeVideoCall()
                //Fake data , Chưa ghép fragment Pick Image - Video

            }

            override fun onFakeVoiceCall() {
                super.onFakeVoiceCall()

            }

            override fun onConversationclick(contact: Contact) {
                super.onConversationclick(contact)
                showMessage(contact.name)
            }
        }

        homeAdapter.setListener(homeListener)
    }

    override fun getLayoutId() = R.layout.fragment_home
}