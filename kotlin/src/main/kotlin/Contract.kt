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

data class Date(val iso: String) {
    fun before(other: Date): Boolean =
        iso < other.iso
}

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
                   val currency: Currency) {
    fun scale(factor: Amount): Payment =
        Payment(date, direction, factor * amount, currency)
    fun invertDirection(direction: Direction): Direction =
        when (direction) {
            Direction.Long -> Direction.Short
            Direction.Short -> Direction.Long
        }
    fun invert(): Payment =
        Payment(date, invertDirection(direction), amount, currency)
}

fun combine(contract1: Contract, contract2: Contract): Contract =
    when (contract1) {
        is Zero -> contract2
        else -> when (contract2) {
            is Zero -> contract1
            else -> Combine(contract1, contract2)
        }
    }
// Zahlungen bis now
// zurück kommt Zahlungen + Residualvertrag
fun semantics(contract: Contract, now: Date): Pair<List<Payment>, Contract> =
    when (contract) {
        is Zero -> Pair(emptyList(), Zero)
        is One -> Pair(listOf(Payment(now, Direction.Long,
            1.0, contract.currency)), Zero)
        is Multiple -> {
            val (payments, residualContract) = semantics(contract.contract, now)
            Pair(payments.map { it.scale(contract.amount) },
                 Multiple(contract.amount, residualContract))
        }
        is Minus -> {
            val (payments, residualContract) = semantics(contract.contract, now)
            Pair(payments.map { it.invert()},
                 Minus(residualContract))
        }
        is Later -> {
            if (now.before(contract.date))
                Pair(emptyList(), contract)
            else
                semantics(contract.contract, now)
        }
        is Combine -> {
            val (payments1, residualContract1) = semantics(contract.contract1, now)
            val (payments2, residualContract2) = semantics(contract.contract2, now)
            Pair(payments1 + payments2, combine(residualContract1, residualContract2))
        }
    }

val c3 = Multiple(100.0, Combine(One(Currency.EUR),
    Combine(Later(Date("2022-12-09"), One(Currency.EUR)),
        Later(Date("2022-12-10"), One(Currency.EUR)))))
/*

data class ZeroCouponBond(val date: Date,
                          val amount: Amount,
                          val currency: Currency): Contract

val zcb1 = ZeroCouponBond(Date("2022-12-24"), 100.0, Currency.EUR)
 */