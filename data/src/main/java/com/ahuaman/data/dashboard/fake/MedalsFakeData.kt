package com.ahuaman.data.dashboard.fake

import com.yakulab.domain.profile.MedalsDomain

object MedalsFakeData {
     val data = listOf(
        MedalsDomain(
            id = 1,
            name = "Oro",
            icon = "https://imagizer.imageshack.com/img924/8550/zKVmBm.png",
            color = "#FFD700",
            total = 0,
        ),
        MedalsDomain(
            id = 2,
            name = "Plata",
            icon = "https://imagizer.imageshack.com/img923/2772/nAsdEh.png",
            color = "#C0C0C0",
            total = 0,
        ),

        MedalsDomain(
            id = 3,
            name = "Bronce",
            icon = "https://imagizer.imageshack.com/img924/1441/ZkRf0u.png",
            color = "#CD7F32",
            total = 0,
        ),
    )
}