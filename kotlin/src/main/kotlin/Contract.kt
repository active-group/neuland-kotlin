/*
 1. Einfaches Beispiel
    Zero-Coupon Bond / Zero-Bond:
    "Ich bekomme Weihnachten 100€."
 */

data class Date(val iso: String)

typealias Amount = Double

enum class Currency { GBP, EUR, USD }

sealed interface Contract
data class ZeroCouponBond(val date: Date,
                          val amount: Amount,
                          val currency: Currency): Contract

val zcb1 = ZeroCouponBond(Date("2022-12-24"), 100.0, Currency.EUR)