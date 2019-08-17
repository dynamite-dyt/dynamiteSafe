package io.horizontalsystems.dynamitewallet.modules.balance

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class GetDYT (@SerializedName("stat") var stat: JsonObject)