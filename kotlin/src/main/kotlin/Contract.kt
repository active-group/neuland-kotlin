/*
 1. Einfaches Beispiel
    Zero-Coupon Bond / Zero-Bond:
    "Ich bekomme Weihnachten 100€."
 */

data class Date(val iso: String)

typealias Amoun = Double

sealed interface Contract
data class ZeroCouponBond(val date: Date, val amount: Amount): Contract