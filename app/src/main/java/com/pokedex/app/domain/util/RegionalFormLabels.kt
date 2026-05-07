package com.pokedex.app.domain.util

/**
 * Maps a National Dex Pokémon ID to its regional form label, when the species was introduced
 * as a regional-fork evolution of an older Pokémon. We display this label so the user knows
 * "Berserkatt" (#863) is the Galar evolution of Miaouss, not the Kanto one.
 */
object RegionalFormLabels {

    private val byId: Map<Int, String> = mapOf(
        // Galar (Sword/Shield)
        862 to "Forme Galar",  // Linéon → Ixon (Obstagoon)
        863 to "Forme Galar",  // Miaouss → Berserkatt (Perrserker)
        864 to "Forme Galar",  // Corayon → Coraïon (Cursola)
        865 to "Forme Galar",  // Canarticho → Palarticho (Sirfetch'd)
        866 to "Forme Galar",  // M. Mime → M. Glaquette (Mr. Rime)
        867 to "Forme Galar",  // Tutafeh → Tutankafer (Runerigus)
        // Hisui (Legends Arceus)
        899 to "Forme Hisui",  // Cerfrousse → Cerbyllin (Wyrdeer)
        900 to "Forme Hisui",  // Insécateur → Hachécateur (Kleavor)
        901 to "Forme Hisui",  // Ursaring → Ursaking (Ursaluna)
        902 to "Forme Hisui",  // Bargantua → Paragruel (Basculegion)
        903 to "Forme Hisui",  // Farfuret → Farigaillard (Sneasler)
        904 to "Forme Hisui",  // Qwilfish → Qwilpik (Overqwil)
        // Paldea (Scarlet/Violet)
        980 to "Forme Paldea", // Axoloto → Terraiglon (Clodsire)
    )

    fun forId(pokemonId: Int): String? = byId[pokemonId]
}
