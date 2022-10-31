package org.app.common

class FragmentControllerOption private constructor() {
  var isUseAnimation = true
  private set
  var isAddBackStack = true
  private set
  var isTransactionReplace = false
  private set
          private var isPresent = false
  private var tag: String? = null
  fun isPresent(): Boolean {
    return isPresent
  }

  fun setPresent(present: Boolean) {
    isPresent = present
  }

  fun getTag(): String? {
    return tag
  }

  fun setTag(tag: String?) {
    this.tag = tag
  }

  class Builder {
    val option: FragmentControllerOption

    fun setTag(tag: String?): Builder {
      option.tag = tag
      return this
    }

    fun useAnimation(use: Boolean): Builder {
      option.isUseAnimation = use
      return this
    }

    fun addBackStack(addBackStack: Boolean): Builder {
      option.isAddBackStack = addBackStack
      return this
    }

    fun isTransactionReplace(isTransactionReplace: Boolean): Builder {
      option.isTransactionReplace = isTransactionReplace
      return this
    }

    fun isPresent(isPresent: Boolean): Builder {
      option.isPresent = isPresent
      return this
    }

    init {
      option = FragmentControllerOption()
    }
  }
}