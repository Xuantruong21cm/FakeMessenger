package org.app.common.utils.listener

import android.widget.SeekBar


/**
 * Created by Hoang Dep Trai on 05/24/2022.
 */

abstract class SimpleSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}