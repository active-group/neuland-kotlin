/*
 1. Einfaches Beispiel
    Zero-Coupon Bond / Zero-Bond:
    "Ich bekomme Weihnachten 100€."

 2. Zerlege einfaches Beispiel in "atomare Bestandteile" / "Ideen"
    - Währung "Ich bekomme 1€ jetzt."
    - Betrag / Vielfaches: "Ich bekomme 100€ jetzt."
    - Später

    ... suche Selbstbezüge

  3. Nächstes Beispiel
     Currency swap:
     Weihnachten:
     - Ich bekomme 100€.
     - Ich bezahle 100$.
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
data class Minus(val contract: Contract) : Contract
// 2stellige Operation op: (T, T) -> T
// Assoziativitätsgesetz:
// (a  + b) +  c = a +  (b +  c)
// (a op b) op c = a op (b op c)
// Halbgruppe

// Manchmal gibt es ein n : T
// a op n = n op a = a
// neutrales Element
// Halbgruppe + neutrales Element: Monoid
data class Combine(val contract1: Contract, val contract2: Contract)
    : Contract
object Zero : Contract // neutrales Element

val christmas = Date("2022-12-24")
val c1 = One(Currency.EUR)
val c2 = Multiple(100.0, One(Currency.EUR))
// val zcb1 = Later(christmas, Multiple(100.0, One(Currency.EUR)))

fun zeroCouponBond(date: Date, amount: Amount, currency: Currency): Contract =
    Later(date, Multiple(amount, One(currency)))

val zcb1 = zeroCouponBond(Date("2022-12-24"), 100.0, Currency.EUR)

fun currencySwap(date: Date,
                 amount1: Amount, currency1: Currency,
                 amount2: Amount, currency2: Currency): Contract =
    Combine(zeroCouponBond(date, amount1, currency1),
            Minus(zeroCouponBond(date, amount2, currency2)))

enum class Direction { Long, Short }

data class Payment(val date: Date,
                   val direction: Direction,
                   val amount: Amount,
                   val currency: Currency)

// Zahlungen bis now
// zurück kommt Zahlungen + Residualvertrag
fun semantics(contract: Contract, now: Date): Pair<List<Payment>, Contract> =
    when (contract) {
        is Zero -> TODO()
        is One -> TODO()
        is Multiple -> TODO()
        is Minus -> TODO()
        is Later -> TODO()
        is Combine -> TODO()
    }

/*

data class ZeroCouponBond(val date: Date,
                          val amount: Amount,
                          val currency: Currency): Contract

val zcb1 = ZeroCouponBond(Date("2022-12-24"), 100.0, Currency.EUR)
 */