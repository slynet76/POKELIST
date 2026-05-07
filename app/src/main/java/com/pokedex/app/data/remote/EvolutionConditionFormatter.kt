package com.pokedex.app.data.remote

import com.pokedex.app.data.remote.dto.EvolutionDetailDto

object EvolutionConditionFormatter {

    private val itemNamesFr = mapOf(
        "thunder-stone" to "Pierre Foudre",
        "water-stone" to "Pierre Eau",
        "fire-stone" to "Pierre Feu",
        "leaf-stone" to "Pierre Plante",
        "moon-stone" to "Pierre Lune",
        "sun-stone" to "Pierre Soleil",
        "ice-stone" to "Pierre Glace",
        "dusk-stone" to "Pierre Nuit",
        "shiny-stone" to "Pierre Éclat",
        "dawn-stone" to "Pierre Aube",
        "oval-stone" to "Pierre Ovale",
        "razor-claw" to "Lame Aiguisée",
        "razor-fang" to "Croc Aiguisé",
        "kings-rock" to "Roche Royale",
        "metal-coat" to "Peau Métal",
        "dragon-scale" to "Écaille Draco",
        "up-grade" to "Améliorator",
        "dubious-disc" to "Disque Suspect",
        "protector" to "Protecteur",
        "electirizer" to "Électriseur",
        "magmarizer" to "Magmariseur",
        "reaper-cloth" to "Tissu Pur",
        "sweet-apple" to "Pomme Sucrée",
        "tart-apple" to "Pomme Acidulée",
        "chipped-pot" to "Théière Ébréchée",
        "cracked-pot" to "Théière Fêlée",
        "galarica-cuff" to "Bracelet Galarica",
        "galarica-wreath" to "Couronne Galarica",
        "black-augurite" to "Augurite Noire",
        "peat-block" to "Bloc de Tourbe",
        "linking-cord" to "Câble Échange",
        "auspicious-armor" to "Armure Faste",
        "malicious-armor" to "Armure Maudite",
        "syrupy-apple" to "Pomme Sirupeuse",
        "metal-alloy" to "Alliage Métal",
    )

    fun format(detail: EvolutionDetailDto): String {
        val parts = mutableListOf<String>()

        when (detail.trigger?.name) {
            "trade" -> parts += "Échange"
            "use-item" -> Unit // l'objet est listé ci-dessous
            "shed" -> parts += "Munja"
            "tower-of-darkness" -> parts += "Tour des Ténèbres"
            "tower-of-waters" -> parts += "Tour des Eaux"
            "three-critical-hits" -> parts += "3 coups critiques"
            "take-damage" -> parts += "Subir des dégâts"
            "spin" -> parts += "Pirouetter"
            "agile-style-move" -> parts += "Coup Agile"
            "strong-style-move" -> parts += "Coup Puissant"
            "recoil-damage" -> parts += "Dégâts de recul"
        }

        detail.minLevel?.let { parts += "Niveau $it" }
        detail.item?.let { parts += "Utiliser " + niceItem(it.name) }
        detail.heldItem?.let { parts += "En tenant " + niceItem(it.name) }
        detail.knownMove?.let { parts += "Connaît " + niceItem(it.name) }
        detail.minHappiness?.let { parts += "Amitié $it+" }
        detail.minAffection?.let { parts += "Affection $it+" }
        detail.minBeauty?.let { parts += "Beauté $it+" }
        detail.location?.let { parts += "Lieu : " + niceItem(it.name) }
        when (detail.timeOfDay) {
            "day" -> parts += "Le jour"
            "night" -> parts += "La nuit"
            "dusk" -> parts += "Au crépuscule"
        }
        when (detail.gender) {
            1 -> parts += "Femelle"
            2 -> parts += "Mâle"
        }
        if (detail.needsOverworldRain) parts += "Sous la pluie"
        if (detail.turnUpsideDown) parts += "Console retournée"

        return parts.joinToString(" + ").ifEmpty { "Évolution spéciale" }
    }

    private fun niceItem(rawName: String): String =
        itemNamesFr[rawName] ?: rawName.split('-').joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
}
