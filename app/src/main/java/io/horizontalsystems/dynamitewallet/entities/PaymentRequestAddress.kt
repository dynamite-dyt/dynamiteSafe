package io.horizontalsystems.dynamitewallet.entities

import java.math.BigDecimal

class PaymentRequestAddress(val address: String, val amount: BigDecimal? = null)