# PokéDex Android — Document de conception
*Date : 2026-05-07*

## Objectif

Application Android pour enfant permettant de suivre la chasse aux Pokémon (versions normale et shiny) sur les jeux Nintendo Switch. L'enfant peut parcourir le Pokédex national, filtrer par jeu, consulter la fiche de chaque Pokémon, marquer ses captures et suivre sa progression.

---

## Stack technique

- **Langage :** Kotlin
- **UI :** Jetpack Compose
- **Navigation :** Navigation Compose (single-activity)
- **Réseau :** Retrofit + OkHttp
- **Cache local :** Room (SQLite)
- **Injection de dépendances :** Hilt
- **Images :** Coil (chargement sprites PokéAPI)
- **Source de données :** PokéAPI (https://pokeapi.co) + fichier JSON statique embarqué (appartenance aux jeux Switch)

---

## Architecture

Architecture simple en couches :

```
PokéAPI (réseau)
    ↓ Retrofit
Repository
    ↓ ↑ Room (cache)
ViewModel
    ↓
Composables (UI)
```

- Le `Repository` est le seul point d'accès aux données — il choisit entre cache local et appel réseau.
- Les `ViewModel` exposent des `StateFlow` consommés par les écrans Compose.
- L'état de capture (normal / shiny) est exclusivement local (Room), jamais synchronisé en ligne.
- La liste d'appartenance des Pokémon aux jeux Switch est un fichier `switch_games.json` embarqué dans `assets/`.

---

## Gestion du cache et des données

### Premier lancement
1. Détection du cache vide dans Room.
2. Affichage d'un écran de chargement dédié : "Téléchargement du Pokédex..." avec barre de progression (1025 Pokémon).
3. Appels PokéAPI séquentiels/par lot pour récupérer : id, nom français, type(s), faiblesses, résistances, poids, taille, sprite normal, sprite shiny.
4. Sauvegarde complète en base Room.
5. Stockage de la date du dernier téléchargement en `SharedPreferences`.

### Lancements suivants
- Démarrage instantané depuis le cache Room.
- Vérification silencieuse en arrière-plan : si la date du dernier téléchargement dépasse 7 jours → refresh des données Pokémon en arrière-plan (sans bloquer l'UI).

### Hors connexion
- L'app fonctionne entièrement hors-ligne avec les données en cache.
- Les captures sont sauvegardées localement et ne nécessitent pas de connexion.

---

## Jeux Switch supportés

| Code interne | Nom affiché dans l'app |
|---|---|
| `LGP_LGE` | Let's Go Pikachu / Évoli |
| `SS` | Sword / Shield |
| `SS_IOA` | Isle of Armor (DLC Sword/Shield) |
| `SS_CT` | Crown Tundra (DLC Sword/Shield) |
| `BDSP` | Diamant Étincelant / Perle Scintillante |
| `LA` | Legends : Arceus |
| `SV` | Scarlet / Violet |
| `SV_TM` | Teal Mask (DLC Scarlet/Violet) |
| `SV_ID` | Indigo Disk (DLC Scarlet/Violet) |

Le filtre "Tous" est toujours disponible et affiche le Pokédex national complet (1025 Pokémon).

---

## Écrans

### Écran 1 — Liste Pokédex (`PokedexListScreen`)

**En-tête :**
- Barre rouge avec titre "PokéDex" et icône de recherche.
- Ligne de chips horizontaux défilants pour les filtres jeux (Tous, puis les 9 jeux Switch).

**Corps :**
- Grille 3 colonnes de cartes Pokémon.
- Chaque carte affiche : sprite, numéro (#001), nom français, icône ✓ verte (normal capturé), icône ✨ dorée (shiny capturé).
- Fond de carte légèrement teinté si les deux versions sont capturées.

**Pied de page fixe :**
- Barre de progression "Normal" : `X / Y capturés` (Y = total du filtre actif).
- Barre de progression "Shiny" : `X / Y capturés` (Y = total du filtre actif).
- Si le filtre est "Tous" : Y = 1025. Si un jeu est sélectionné : Y = nombre de Pokémon dans ce jeu.

**Recherche :**
- Champ de recherche par nom (en français) — filtre la liste en temps réel.
- La recherche s'applique dans le contexte du filtre jeu actif.

### Écran 2 — Fiche Pokémon (`PokemonDetailScreen`)

**Sections :**
1. **En-tête :** flèche retour, numéro + nom, illustration officielle (sprite haute résolution).
2. **Types :** badges pills colorés (couleur officielle par type).
3. **Faiblesses / Résistances :** icônes de types avec étiquettes.
4. **Infos :** taille (m), poids (kg).
5. **Disponibilité :** liste des badges jeux Switch dans lesquels ce Pokémon est présent.
6. **Capture :** deux boutons toggle :
   - `[✓ Capturé]` — rouge si actif, gris sinon.
   - `[✨ Shiny capturé]` — doré si actif, gris sinon.

---

## Modèle de données (Room)

### Table `pokemon`
| Colonne | Type | Description |
|---|---|---|
| `id` | INT (PK) | Numéro Pokédex national |
| `name_fr` | TEXT | Nom français |
| `type_primary` | TEXT | Type principal (ex: "fire") |
| `type_secondary` | TEXT? | Type secondaire (nullable) |
| `weight_kg` | FLOAT | Poids en kg |
| `height_m` | FLOAT | Taille en mètres |
| `sprite_url` | TEXT | URL sprite normal |
| `sprite_shiny_url` | TEXT | URL sprite shiny |

### Table `capture_status`
| Colonne | Type | Description |
|---|---|---|
| `pokemon_id` | INT (PK, FK) | Référence `pokemon.id` |
| `is_caught` | BOOLEAN | Version normale capturée |
| `is_shiny_caught` | BOOLEAN | Version shiny capturée |

### Table `type_effectiveness`
| Colonne | Type | Description |
|---|---|---|
| `pokemon_id` | INT | Référence `pokemon.id` |
| `attacking_type` | TEXT | Type attaquant |
| `multiplier` | FLOAT | Multiplicateur (0.25, 0.5, 1, 2, 4) |

### Fichier statique `assets/switch_games.json`
```json
{
  "SS": [1, 4, 7, ...],
  "SV": [1, 4, 7, 906, 907, ...],
  ...
}
```
Tableau d'IDs Pokémon par code de jeu. Chargé une fois au démarrage et stocké en mémoire.

---

## Thème visuel

- **Couleur primaire :** `#CC0000` (rouge Pokémon)
- **Couleur secondaire :** `#FFFFFF` (blanc)
- **Accent :** `#FFD700` (doré pour les shinys)
- **Typographie :** police arrondie, taille lisible pour enfant (minimum 14sp)
- **Coins arrondis** sur les cartes et badges
- **Couleurs des types :** palette officielle Pokémon (Feu = `#F08030`, Eau = `#6890F0`, etc.)

---

## Périmètre exclu (hors scope)

- Formes alternatives (Alola, Galar, Hisui) : non trackées séparément
- Synchronisation cloud / compte utilisateur
- Statistiques de combat (PV, Attaque, etc.)
- Évolutions / arbre d'évolution
- Support iOS

---

## Contraintes

- Android minimum : API 26 (Android 8.0) — couvre ~95% des appareils actifs
- Connexion internet requise uniquement au premier lancement et lors du refresh hebdomadaire
- Pas de compte utilisateur — toutes les données sont locales à l'appareil
