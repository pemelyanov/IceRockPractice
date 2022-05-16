package com.emelyanov.icerockpractice.shared.domain.utils

import com.emelyanov.icerockpractice.shared.domain.models.UserInfo
import com.emelyanov.icerockpractice.shared.domain.models.responses.UserInfoResponse

fun UserInfoResponse.toUserInfo()
= UserInfo(login = this.login)