package org.app.admob

enum class AdState {
  LOADED,
  DISPLAYED,
  HIDDEN,
  CLICKED,
  LOAD_FAILED,
  DISPLAY_FAILED,
  EXPANDED,
  COLLAPSED,
  REVENUE_PAID,
  REMOVED,
  REWARD_VIDEO_STARTED,
  REWARD_VIDEO_COMPLETED,
  USER_REWARDED
}

interface AdMobViewListener {
  fun onAdViewStateChanged(state: AdState)

  fun onShowAdOpenAppComplete() {}
}