package org.app.common.utils

import timber.log.Timber

class TimberDebugTree : Timber.DebugTree() {
  override fun createStackElementTag(element: StackTraceElement): String? {
    return "${element.lineNumber} - ${super.createStackElementTag(element)}"
  }
}