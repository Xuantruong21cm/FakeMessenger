package com.merryblue.fakemessenger.ui.home

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.data.model.Contact
import com.merryblue.fakemessenger.databinding.FragmentHomeBinding
import com.merryblue.fakemessenger.ui.adapter.homeAdapter.HomeAdapter
import com.merryblue.fakemessenger.ui.adapter.homeAdapter.HomeAdapterCallback
import com.merryblue.fakemessenger.ui.calling.CallingFragment
import com.readystatesoftware.chuck.internal.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import org.app.common.BaseFragment
import org.app.common.extensions.ImageViewType
import org.app.common.extensions.loadImageRes
import org.app.common.utils.getScreenHeight
import org.app.common.utils.getScreenWidth
import org.app.common.utils.requestPermission

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

                //Fake data, fake type call , Chưa ghép fragment Pick Image - Video
                requestPermission(requireActivity(),
                    getString(R.string.txt_permission_denied_message),
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    requestSuccess = {

                        val imageTest = "https://upload.motgame.vn/photos/motgame-vn/2021/12/r3.jpg"
                        (activity as HomeActivity).replaceFragment(
                            CallingFragment.newInstance(
                                CallingFragment.TYPE_VIDEO_CALL,
                                CallingFragment.TYPE_INCOMMING_CALL,
                                imageTest,
                                getString(R.string.video_test),
                                "Idol Xinh Gái"
                            ), R.id.layout_Home
                        )

                    })
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