package com.pokedex.app.domain.model

enum class SwitchGame(val code: String, val displayName: String) {
    ALL("ALL", "Tous"),
    LGP_LGE("LGP_LGE", "Let's Go"),
    SS("SS", "Sword / Shield"),
    SS_IOA("SS_IOA", "Isle of Armor"),
    SS_CT("SS_CT", "Crown Tundra"),
    BDSP("BDSP", "Diamant / Perle"),
    LA("LA", "Legends : Arceus"),
    SV("SV", "Scarlet / Violet"),
    SV_TM("SV_TM", "Teal Mask"),
    SV_ID("SV_ID", "Indigo Disk");
}
