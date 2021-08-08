package xyz.savvamirzoyan.wordremember.data.entity

sealed class VerbForm(
    open val ich: String? = null,
    open val du: String? = null,
    open val erSieEs: String? = null,
    open val wir: String? = null,
    open val ihr: String? = null,
    open val sieSie: String? = null,
) {
    class Prasens(
        override val ich: String? = null,
        override val du: String? = null,
        override val erSieEs: String? = null,
        override val wir: String? = null,
        override val ihr: String? = null,
        override val sieSie: String? = null
    ) : VerbForm(ich, du, erSieEs, wir, ihr, sieSie)

    class Prateritum(
        override val ich: String? = null,
        override val du: String? = null,
        override val erSieEs: String? = null,
        override val wir: String? = null,
        override val ihr: String? = null,
        override val sieSie: String? = null
    ) : VerbForm(ich, du, erSieEs, wir, ihr, sieSie)

    class Perfect(
        override val ich: String? = null,
        override val du: String? = null,
        override val erSieEs: String? = null,
        override val wir: String? = null,
        override val ihr: String? = null,
        override val sieSie: String? = null
    ) : VerbForm(ich, du, erSieEs, wir, ihr, sieSie)
}
