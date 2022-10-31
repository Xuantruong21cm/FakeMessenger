package org.app.common.utils.listener

import android.text.Editable
import android.text.TextWatcher


/**
 * Created by Hoang Dep Trai on 05/31/2022.
 */

abstract class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}