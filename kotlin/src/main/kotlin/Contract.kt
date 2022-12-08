/*
 1. Einfaches Beispiel
    Zero-Coupon Bond / Zero-Bond:
    "Ich bekomme Weihnachten 100€."

 2. Zerlege einfaches Beispiel in "atomare Bestandteile" / "Ideen"
    - Währung "Ich bekomme 1€ jetzt."
    - Betrag / Vielfaches: "Ich bekomme 100€ jetzt."
    - Später

    ... suche Selbstbezüge
 */

data class Date(val iso: String)

typealias Amount = Double

enum class Currency { GBP, EUR, USD }

sealed interface Contract
data class One(val currency: Currency): Contract
data class Multiple(val amount: Amount, val contract: Contract)
    : Contract
data class Later(val date: Date, val contract: Contract)
    : Contract

val christmas = Date("2022-12-24")
val c1 = One(Currency.EUR)
val c2 = Multiple(100.0, One(Currency.EUR))
val zcb1 = Later(christmas, Multiple(100.0, One(Currency.EUR)))

fun zeroCouponBond(date: Date, amount: Amount, currency: Currency) =
    Later(date, Multiple(amount, One(currency)))
/*
data class ZeroCouponBond(val date: Date,
                          val amount: Amount,
                          val currency: Currency): Contract

val zcb1 = ZeroCouponBond(Date("2022-12-24"), 100.0, Currency.EUR)
 */