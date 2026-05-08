package com.pokedex.app.data.remote

/**
 * Maps a PokéAPI form suffix (e.g. "galar", "alola", "mega-x") to a French user-facing label.
 * Returns null for the default form (no label needed).
 */
object FormLabels {
    fun formatFor(formName: String): String? = when {
        formName == "default" -> null
        formName == "galar" || formName == "galarian" -> "Forme Galar"
        formName == "alola" || formName == "alolan" -> "Forme Alola"
        formName == "hisui" || formName == "hisuian" -> "Forme Hisui"
        formName == "paldea" || formName == "paldean" -> "Forme Paldea"
        formName == "mega" -> "Méga-Évolution"
        formName == "mega-x" -> "Méga-Évolution X"
        formName == "mega-y" -> "Méga-Évolution Y"
        formName == "gmax" -> "Gigamax"
        formName == "primal" -> "Forme Primaire"
        formName == "origin" -> "Forme Originelle"
        formName == "altered" -> "Forme Alternative"
        formName == "therian" -> "Forme Avatar"
        formName == "incarnate" -> "Forme Avatar"
        formName == "sky" -> "Forme Céleste"
        formName == "land" -> "Forme Terrestre"
        formName == "blade" -> "Forme Lame"
        formName == "shield" -> "Forme Bouclier"
        formName == "school" -> "Forme Banc"
        formName == "solo" -> "Forme Solo"
        formName == "midday" -> "Forme Diurne"
        formName == "midnight" -> "Forme Nocturne"
        formName == "dusk" -> "Forme Crépusculaire"
        formName == "ultra" -> "Forme Ultime"
        else -> "Forme " + formName.split("-").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
    }

    /** Skip cosmetic / event-only / battle-only variants that aren't useful for a kid's PokéDex. */
    fun shouldSkipVariety(varietyName: String): Boolean {
        val lower = varietyName.lowercase()
        return lower.contains("totem") ||
               lower.contains("cosplay") ||
               lower.contains("cap") ||              // pikachu-original-cap, etc.
               lower.contains("starter") ||
               lower.contains("battle-bond") ||      // ash-greninja
               lower.contains("eternamax") ||
               lower.contains("busted")              // mimikyu-busted
    }
}
