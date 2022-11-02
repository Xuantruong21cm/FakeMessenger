package com.merryblue.fakemessenger.ui.calling

import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.common.util.concurrent.ListenableFuture
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.FragmentCallingBinding
import org.app.common.BaseFragment
import org.app.common.binding.setOnSingleClickListener
import org.app.common.extensions.*
import org.app.common.utils.*

class CallingFragment : BaseFragment<FragmentCallingBinding>() {

    companion object {
        const val TYPE_VOICE_CALL = 0
        const val TYPE_VIDEO_CALL = 1
        const val TYPE_INCOMMING_CALL = 2
        const val TYPE_CALLING = 3

        fun newInstance(
            typeFake: Int,
            typeCall: Int,
            imageContactUri: String,
            videoContactUri: String,
            contactName: String
        ): CallingFragment {
            val fragment = CallingFragment()
            val bundle = Bundle()
            bundle.putInt("typeFake", typeFake)
            bundle.putInt("typeCall", typeCall)
            bundle.putString("imageContactUri", imageContactUri)
            bundle.putString("videoContactUri", videoContactUri)
            bundle.putString("contactName", contactName)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var typeFake = 0
    private var typeCall = 0
    private var imageContactUri = ""
    private var videoContactUri = ""
    private var contactName = ""
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    private val handlerTimeCall = Handler(Looper.getMainLooper())
    private lateinit var timer : Runnable
    private var timeCall = 1000L
    private var isCalling = false

    private lateinit var exoplayer : ExoPlayer

    override fun getFragmentArguments() {
        super.getFragmentArguments()
        arguments.let {
            typeCall = it!!.getInt("typeCall")
            typeFake = it.getInt("typeFake")
            imageContactUri = it.getString("imageContactUri")!!
            videoContactUri = it.getString("videoContactUri")!!
            contactName = it.getString("contactName")!!
        }
    }

    override fun setUpViews() {
        super.setUpViews()

        initView()
        setOnClickEvent()
    }

    private fun initView(){
        binding.imgAvatarContactCalling.loadImageUri(
            Uri.parse(imageContactUri),
            ImageViewType.CIRCLE
        )

        binding.clReply.show()
        binding.clAcceptCalling.show()
        binding.imgBgUserContact.show()
        binding.playerView.hide()
        binding.imgBack.hide()
        binding.clPreview.hide()
        binding.tvTimeCall.hide()
        binding.clFillterCalling.hide()
        binding.clBottomControlRoot.hide()

        binding.tvContactName.text = contactName
        binding.tvCallingStatus.text =
            if (typeFake == TYPE_VOICE_CALL) getString(R.string.txt_voice_call_on_messenger)
            else getString(R.string.txt_video_call_on_messenger)
        binding.imgAnswerCalling.setImageResource(
            if (typeFake == TYPE_VOICE_CALL) R.drawable.ic_answer_voice_calling
            else R.drawable.ic_answer_video_calling)
        binding.imgSwitchVideoVoice.visibility = if (typeFake == TYPE_VOICE_CALL) View.VISIBLE else View.INVISIBLE
        binding.imgBgUserContact.loadImageUrl(imageContactUri,ImageViewType.NONE)

        val avatarParams = binding.imgAvatarContactCalling.layoutParams as ViewGroup.MarginLayoutParams
        avatarParams.setMargins(0, (getScreenHeight(requireActivity())*0.18).toInt(),0,0)
        binding.imgAvatarContactCalling.layoutParams = avatarParams

        binding.tvContactName.setPadding(0,(getScreenHeight(requireActivity())*0.015).toInt(),
            0,(getScreenHeight(requireActivity())*0.01).toInt())

        binding.imgSwitchVideoVoice.setPadding(padding16(requireActivity()))
        binding.imgBack.setPadding(padding16(requireActivity()))
        binding.clDeclineCalling.setPadding(padding48(requireActivity()),0, padding48(requireActivity()),0)
        binding.clAnswerCalling.setPadding(padding48(requireActivity()),0, padding48(requireActivity()),0)
        binding.clReply.setPadding(0,0,0, (getScreenHeight(requireActivity())*0.1).toInt())
        binding.imgReplyCalling.setPadding(padding8(requireActivity()),0, padding8(requireActivity()),0)
        binding.tvDeclineLabel.setPadding(0, padding8(requireActivity()),0, padding8(requireActivity()))
        binding.tvAnswerLabel.setPadding(0, padding8(requireActivity()),0, padding8(requireActivity()))

    }

    private fun setUpView_video_incomming(){
        isCalling = true

        binding.imgBgUserContact.hide()
        binding.clPreview.show()
        binding.playerView.show()
        binding.imgAvatarContactCalling.hide()
        binding.tvContactName.hide()
        binding.tvCallingStatus.hide()
        binding.clReply.hide()
        binding.clAcceptCalling.hide()
        binding.imgBack.show()
        binding.clFillterCalling.show()

        setUpBottomControl()

        binding.imgLightCalling.setPadding(padding24(requireActivity()), padding24(requireActivity()),padding24(requireActivity()), padding24(requireActivity()) )

        val previewParams = binding.clPreview.layoutParams as ViewGroup.MarginLayoutParams
        previewParams.setMargins(0, padding16(requireActivity()), padding8(requireActivity()),0)

        if (!this::cameraProviderFuture.isInitialized){
            cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        }
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
            binding.tvTimeCall.show()
        },ContextCompat.getMainExecutor(requireContext()))

        exoplayer = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = exoplayer
        exoplayer.addMediaItem(MediaItem
            .fromUri(getString(R.string.video_test)))
        exoplayer.prepare()
        exoplayer.playWhenReady = true
        exoplayer.repeatMode = Player.REPEAT_MODE_ONE

        binding.tvTimeCall.setPadding(0, padding8(requireActivity()),0, padding8(requireActivity()))
        timer = object : Runnable{
            override fun run() {
                binding.tvTimeCall.text = millisecondsToTime(timeCall)
                handlerTimeCall.postDelayed(this,1000)
                timeCall += 1000
            }
        }
        handlerTimeCall.postDelayed(timer,0)
    }

    private fun setUpBottomControl(){
        binding.clBottomControlRoot.show()
        binding.imgTopLineBottomControl.setPadding(0, padding12(requireActivity()),0, padding12(requireActivity()))

        binding.imgOnOffCamera.setImageResource(if (typeFake == TYPE_VIDEO_CALL) R.drawable.ic_camera_bottom_control else R.drawable.ic_invite_contact)
        binding.imgSwitchCamera.setImageResource(if (typeFake == TYPE_VIDEO_CALL) R.drawable.ic_switch_camera_bottom_control else R.drawable.ic_mute_calling)

    }

    private fun setOnClickEvent(){
        binding.clAnswerCalling.setOnSingleClickListener{

            when(typeFake){
                TYPE_VIDEO_CALL->{
                    if (typeCall == TYPE_INCOMMING_CALL){
                        setUpView_video_incomming()
                    }else{
                        //todo
                    }
                }
                TYPE_VOICE_CALL -> {
                    if (typeCall == TYPE_INCOMMING_CALL){
                        //todo
                    }else{
                        // TODO:
                    }
                }
            }
        }

        binding.imgEndCall.setOnSingleClickListener{
            requireActivity().onBackPressed()
        }

        binding.imgBack.setOnSingleClickListener{
            requireActivity().onBackPressed()
        }
    }

    private fun millisecondsToTime(milliseconds : Long) : String{
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60

        return String.format("%02d:%02d",minutes,seconds)
    }

    private fun bindPreview(cameraProvider  : ProcessCameraProvider){
        val preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build()

        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        cameraProvider.bindToLifecycle(this as LifecycleOwner,cameraSelector,preview)
    }

    override fun onStop() {
        super.onStop()
        if (this::timer.isInitialized){
            handlerTimeCall.removeCallbacks(timer)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isCalling && this::timer.isInitialized && this::exoplayer.isInitialized){
            exoplayer.play()
            handlerTimeCall.postDelayed(timer,0)
        }
        isCalling = true
    }

    override fun onDetach() {
        handlerTimeCall.removeCallbacks(timer)

        super.onDetach()
    }

    override fun onPause() {
        super.onPause()
        if (isCalling && this::exoplayer.isInitialized){
            exoplayer.pause()
        }
        if (this::timer.isInitialized){
            handlerTimeCall.removeCallbacks(timer)
        }
        isCalling = false
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_calling
    }
}