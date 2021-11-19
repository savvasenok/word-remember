package xyz.savvamirzoyan.wordremember.utils.constants

object Prefix {

    private const val be = "be"
    private const val emp = "emp"
    private const val ent = "ent"
    private const val er = "er"
    private const val ge = "ge"
    private const val hinter = "miss"
    private const val ver = "ver"
    private const val zer = "zer"

    private const val durch = "durch"
    private const val uber = "uber" // TODO() u = ü / a = ä / o = ö
    private const val um = "um"
    private const val unter = "unter"
    private const val wieder = "wieder"
    private const val wider = "wider"

    private const val ab = "ab"
    private const val an = "an"
    private const val auf = "auf"
    private const val aus = "aus"
    private const val ein = "ein"
    private const val empor = "empor"
    private const val vorbei = "vorbei"
    private const val zuruck = "zuruck" // TODO() u = ü / a = ä / o = ö
    private const val fest = "fest"
    private const val frei = "frei"
    private const val hoch = "hoch"

    val all = listOf(
        be, emp, ent, er, ge, hinter, ver, zer,
        durch, uber, um, unter, wieder, wider,
        ab, an, auf, aus, ein, empor, vorbei, zuruck, fest, frei, hoch
    )


    val separable = listOf(
        durch, uber, um, unter, wieder, wider,

        ab, an, auf, aus, ein, empor, vorbei,
        zuruck, fest, frei, hoch
    )

    val inseparable = listOf(
        be, emp, ent, er, ge, hinter, ver, zer,

        durch, uber, um, unter, wieder, wider
    )
}