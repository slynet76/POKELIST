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

        // Flabébé / Floette / Florges colors (cosmetic varieties)
        formName == "red" -> "Fleur Rouge"
        formName == "yellow" -> "Fleur Jaune"
        formName == "orange" -> "Fleur Orange"
        formName == "blue" -> "Fleur Bleue"
        formName == "white" -> "Fleur Blanche"
        formName == "eternal" -> "Florges Éternelle"

        // Castform
        formName == "rainy" -> "Forme Pluie"
        formName == "snowy" -> "Forme Neige"
        formName == "sunny" -> "Forme Soleil"

        // Deoxys
        formName == "attack" -> "Forme Attaque"
        formName == "defense" -> "Forme Défense"
        formName == "speed" -> "Forme Vitesse"

        // Wormadam / Burmy
        formName == "plant" -> "Forme Plante"
        formName == "sandy" -> "Forme Sable"
        formName == "trash" -> "Forme Déchet"

        // Cherrim
        formName == "overcast" -> "Forme Voilée"
        formName == "sunshine" -> "Forme Éclatante"

        // Shellos / Gastrodon
        formName == "east" || formName == "east-sea" -> "Mer Est"
        formName == "west" || formName == "west-sea" -> "Mer Ouest"

        // Rotom appliances
        formName == "heat" -> "Rotom Chaleur"
        formName == "wash" -> "Rotom Lavage"
        formName == "frost" -> "Rotom Froid"
        formName == "fan" -> "Rotom Hélice"
        formName == "mow" -> "Rotom Tonte"

        // Keldeo
        formName == "ordinary" -> "Forme Normale"
        formName == "resolute" -> "Forme Décidée"

        // Meloetta
        formName == "aria" -> "Forme Chant"
        formName == "pirouette" -> "Forme Danse"

        // Pumpkaboo / Gourgeist sizes
        formName == "small" -> "Petite Taille"
        formName == "average" -> "Taille Standard"
        formName == "large" -> "Grande Taille"
        formName == "super" -> "Taille XL"

        // Hoopa
        formName == "confined" -> "Enchaîné"
        formName == "unbound" -> "Déchaîné"

        // Necrozma
        formName == "dawn" -> "Forme Aurore"
        formName == "dusk-mane" -> "Crinière du Couchant"
        formName == "dawn-wings" -> "Ailes de l'Aurore"

        // Cramorant
        formName == "gulping" -> "Forme Gobage"
        formName == "gorging" -> "Forme Engloutissement"

        // Vivillon patterns
        formName == "meadow" -> "Motif Pré"
        formName == "polar" -> "Motif Polaire"
        formName == "tundra" -> "Motif Toundra"
        formName == "continental" -> "Motif Continental"
        formName == "garden" -> "Motif Jardin"
        formName == "elegant" -> "Motif Élégant"
        formName == "icy-snow" -> "Motif Banquise"
        formName == "modern" -> "Motif Moderne"
        formName == "marine" -> "Motif Marin"
        formName == "archipelago" -> "Motif Archipel"
        formName == "high-plains" -> "Motif Plaines"
        formName == "sandstorm" -> "Motif Sable"
        formName == "river" -> "Motif Rivière"
        formName == "monsoon" -> "Motif Mousson"
        formName == "savanna" -> "Motif Savane"
        formName == "sun" -> "Motif Soleil"
        formName == "ocean" -> "Motif Océan"
        formName == "jungle" -> "Motif Jungle"
        formName == "fancy" -> "Motif Fantaisie"
        formName == "poke-ball" -> "Motif Poké Ball"

        // Unown letters
        formName.length == 1 && formName.matches(Regex("[a-z]")) -> "Lettre ${formName.uppercase()}"
        formName == "exclamation" -> "Lettre !"
        formName == "question" -> "Lettre ?"

        // Pikachu costumes
        formName == "rock-star" -> "Star du Rock"
        formName == "belle" -> "Belle"
        formName == "pop-star" -> "Pop Star"
        formName == "phd" -> "Docteure"
        formName == "libre" -> "Catcheuse"

        // Furfrou trims
        formName == "heart" -> "Coupe Cœur"
        formName == "star" -> "Coupe Étoile"
        formName == "diamond" -> "Coupe Diamant"
        formName == "debutante" -> "Coupe Aristo"
        formName == "matron" -> "Coupe Élégante"
        formName == "dandy" -> "Coupe Sieur"
        formName == "la-reine" -> "Coupe Reine"
        formName == "kabuki" -> "Coupe Kabuki"
        formName == "pharaoh" -> "Coupe Pharaon"

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
