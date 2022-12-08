/*
 1. Einfaches Beispiel
    Zero-Coupon Bond / Zero-Bond:
    "Ich bekomme Weihnachten 100€."
 */

data class Date(val iso: String)

sealed interface Contract
data class ZeroCouponBond(date: Date): Contract