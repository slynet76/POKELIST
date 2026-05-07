# PokéDex Android — Plan d'implémentation

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Application Android (Kotlin + Compose) permettant à un enfant de suivre sa chasse aux Pokémon (normal + shiny) sur les jeux Nintendo Switch, avec fiche détail et barres de progression.

**Architecture:** Single-activity, Navigation Compose, couche Repository avec cache Room + appels PokéAPI. L'état de capture est exclusivement local. La liste d'appartenance aux jeux Switch est un JSON statique embarqué dans assets/.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation Compose, Hilt, Room, Retrofit + Gson, Coil, PokéAPI, MockK + coroutines-test pour les tests.

---

## Structure des fichiers

```
pokedex-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/pokedex/app/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── PokedexApplication.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   │   ├── entity/PokemonEntity.kt
│   │   │   │   │   │   ├── entity/CaptureStatusEntity.kt
│   │   │   │   │   │   ├── dao/PokemonDao.kt
│   │   │   │   │   │   └── dao/CaptureStatusDao.kt
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── PokeApiService.kt
│   │   │   │   │   │   └── dto/ (PokemonDto, PokemonSpeciesDto)
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── PokemonRepository.kt
│   │   │   │   │       └── PokemonRepositoryImpl.kt
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/ (Pokemon, SwitchGame)
│   │   │   │   │   └── util/TypeChart.kt
│   │   │   │   ├── util/
│   │   │   │   │   ├── PreferencesManager.kt
│   │   │   │   │   └── SwitchGamesLoader.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/ (Color, Theme, TypeColors)
│   │   │   │   │   ├── navigation/ (AppNavigation, Screen)
│   │   │   │   │   ├── loading/LoadingScreen.kt
│   │   │   │   │   ├── list/
│   │   │   │   │   │   ├── PokedexListViewModel.kt
│   │   │   │   │   │   ├── PokedexListScreen.kt
│   │   │   │   │   │   └── components/ (PokemonCard, GameFilterChips, ProgressBarsSection)
│   │   │   │   │   └── detail/
│   │   │   │   │       ├── PokemonDetailViewModel.kt
│   │   │   │   │       ├── PokemonDetailScreen.kt
│   │   │   │   │       └── components/ (TypeBadge, TypeEffectivenessSection, CaptureButtons)
│   │   │   │   └── di/ (DatabaseModule, NetworkModule, RepositoryModule)
│   │   │   └── assets/switch_games.json
│   │   └── test/java/com/pokedex/app/
│   │       ├── domain/util/TypeChartTest.kt
│   │       ├── data/repository/PokemonRepositoryImplTest.kt
│   │       ├── ui/list/PokedexListViewModelTest.kt
│   │       └── ui/detail/PokemonDetailViewModelTest.kt
│   └── build.gradle.kts
├── gradle/libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
```

---

### Task 1 : Initialisation du projet Android

**Files:**
- Create: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`
- Modify: `build.gradle.kts`
- Modify: `settings.gradle.kts`

- [ ] **Step 1 : Créer le projet via Android Studio**

  Ouvrir Android Studio → New Project → **Empty Activity**
  - Name: `PokéDex`
  - Package: `com.pokedex.app`
  - Save location: `C:\Users\admin\Documents\pokedex-app`
  - Language: Kotlin
  - Minimum SDK: API 26
  - Build configuration: Kotlin DSL

- [ ] **Step 2 : Remplacer `gradle/libs.versions.toml`**

```toml
[versions]
agp = "8.7.3"
kotlin = "2.0.21"
ksp = "2.0.21-1.0.28"
coreKtx = "1.15.0"
lifecycleRuntimeKtx = "2.8.7"
activityCompose = "1.10.0"
composeBom = "2024.12.01"
navigationCompose = "2.8.5"
hilt = "2.54"
hiltNavigationCompose = "1.2.0"
room = "2.6.1"
retrofit = "2.11.0"
okhttp = "4.12.0"
coil = "2.7.0"
gson = "2.11.0"
coroutinesTest = "1.9.0"
mockk = "1.13.13"
junit = "4.13.2"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
androidx-hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
gson = { group = "com.google.code.gson", name = "gson", version.ref = "gson" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutinesTest" }
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version = "1.2.1" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version = "3.6.1" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

- [ ] **Step 3 : Remplacer `app/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.pokedex.app"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.pokedex.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
    buildFeatures { compose = true }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.coil.compose)
    implementation(libs.gson)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
```

- [ ] **Step 4 : Ajouter INTERNET permission dans `app/src/main/AndroidManifest.xml`**

  Ajouter avant `<application>` :
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />
  ```

- [ ] **Step 5 : Vérifier que le projet compile**

  ```
  gradlew.bat assembleDebug
  ```
  Attendu : `BUILD SUCCESSFUL`

- [ ] **Step 6 : Commit**

  ```bash
  git add .
  git commit -m "feat: initialisation projet Android PokéDex"
  ```

---

### Task 2 : Modèles de domaine + TypeChart

**Files:**
- Create: `app/src/main/java/com/pokedex/app/domain/model/Pokemon.kt`
- Create: `app/src/main/java/com/pokedex/app/domain/model/SwitchGame.kt`
- Create: `app/src/main/java/com/pokedex/app/domain/util/TypeChart.kt`
- Create: `app/src/test/java/com/pokedex/app/domain/util/TypeChartTest.kt`

- [ ] **Step 1 : Écrire le test TypeChart**

```kotlin
// app/src/test/java/com/pokedex/app/domain/util/TypeChartTest.kt
package com.pokedex.app.domain.util

import org.junit.Assert.assertTrue
import org.junit.Test

class TypeChartTest {

    @Test
    fun `Dracaufeu feu-vol est faible contre eau roche`() {
        val weaknesses = TypeChart.getWeaknesses("fire", "flying")
        assertTrue("water" in weaknesses)
        assertTrue("rock" in weaknesses)
    }

    @Test
    fun `Bulbizarre plante-poison est immune contre aucun type`() {
        val immunities = TypeChart.getImmunities("grass", "poison")
        assertTrue(immunities.isEmpty())
    }

    @Test
    fun `Ronflex normal est immune contre spectre`() {
        val immunities = TypeChart.getImmunities("normal", null)
        assertTrue("ghost" in immunities)
    }

    @Test
    fun `Dracaufeu x4 faiblesse roche`() {
        val mult = TypeChart.getEffectiveness("rock", "fire", "flying")
        assert(mult == 4f)
    }
}
```

- [ ] **Step 2 : Exécuter le test — vérifier qu'il échoue**

  ```
  gradlew.bat :app:testDebugUnitTest --tests "com.pokedex.app.domain.util.TypeChartTest"
  ```
  Attendu : FAIL (TypeChart non défini)

- [ ] **Step 3 : Créer `TypeChart.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/domain/util/TypeChart.kt
package com.pokedex.app.domain.util

object TypeChart {
    private val weakTo = mapOf(
        "normal"   to listOf("fighting"),
        "fire"     to listOf("water", "ground", "rock"),
        "water"    to listOf("electric", "grass"),
        "electric" to listOf("ground"),
        "grass"    to listOf("fire", "ice", "poison", "flying", "bug"),
        "ice"      to listOf("fire", "fighting", "rock", "steel"),
        "fighting" to listOf("flying", "psychic", "fairy"),
        "poison"   to listOf("ground", "psychic"),
        "ground"   to listOf("water", "grass", "ice"),
        "flying"   to listOf("electric", "ice", "rock"),
        "psychic"  to listOf("bug", "ghost", "dark"),
        "bug"      to listOf("fire", "flying", "rock"),
        "rock"     to listOf("water", "grass", "fighting", "ground", "steel"),
        "ghost"    to listOf("ghost", "dark"),
        "dragon"   to listOf("ice", "dragon", "fairy"),
        "dark"     to listOf("fighting", "bug", "fairy"),
        "steel"    to listOf("fire", "fighting", "ground"),
        "fairy"    to listOf("poison", "steel"),
    )

    private val resistantTo = mapOf(
        "normal"   to emptyList(),
        "fire"     to listOf("fire", "grass", "ice", "bug", "steel", "fairy"),
        "water"    to listOf("fire", "water", "ice", "steel"),
        "electric" to listOf("electric", "flying", "steel"),
        "grass"    to listOf("water", "electric", "grass", "ground"),
        "ice"      to listOf("ice"),
        "fighting" to listOf("bug", "rock", "dark"),
        "poison"   to listOf("grass", "fighting", "poison", "bug", "fairy"),
        "ground"   to listOf("poison", "rock"),
        "flying"   to listOf("grass", "fighting", "bug"),
        "psychic"  to listOf("fighting", "psychic"),
        "bug"      to listOf("grass", "fighting", "ground"),
        "rock"     to listOf("normal", "fire", "poison", "flying"),
        "ghost"    to listOf("poison", "bug"),
        "dragon"   to listOf("fire", "water", "electric", "grass"),
        "dark"     to listOf("ghost", "dark"),
        "steel"    to listOf("normal", "grass", "ice", "flying", "psychic", "bug", "rock", "dragon", "steel", "fairy"),
        "fairy"    to listOf("fighting", "bug", "dark"),
    )

    private val immuneTo = mapOf(
        "normal"   to listOf("ghost"),
        "fire"     to emptyList(),
        "water"    to emptyList(),
        "electric" to emptyList(),
        "grass"    to emptyList(),
        "ice"      to emptyList(),
        "fighting" to emptyList(),
        "poison"   to emptyList(),
        "ground"   to listOf("electric"),
        "flying"   to listOf("ground"),
        "psychic"  to emptyList(),
        "bug"      to emptyList(),
        "rock"     to emptyList(),
        "ghost"    to listOf("normal", "fighting"),
        "dragon"   to emptyList(),
        "dark"     to listOf("psychic"),
        "steel"    to listOf("poison"),
        "fairy"    to listOf("dragon"),
    )

    val ALL_TYPES = listOf(
        "normal", "fire", "water", "electric", "grass", "ice", "fighting",
        "poison", "ground", "flying", "psychic", "bug", "rock", "ghost",
        "dragon", "dark", "steel", "fairy"
    )

    fun getEffectiveness(attacking: String, defType1: String, defType2: String?): Float {
        var m = multiplier(attacking, defType1)
        if (defType2 != null) m *= multiplier(attacking, defType2)
        return m
    }

    private fun multiplier(attacking: String, defending: String): Float = when {
        immuneTo[defending]?.contains(attacking) == true    -> 0f
        weakTo[defending]?.contains(attacking) == true      -> 2f
        resistantTo[defending]?.contains(attacking) == true -> 0.5f
        else -> 1f
    }

    fun getWeaknesses(t1: String, t2: String?) =
        ALL_TYPES.filter { getEffectiveness(it, t1, t2) > 1f }

    fun getResistances(t1: String, t2: String?) =
        ALL_TYPES.filter { v -> getEffectiveness(v, t1, t2).let { it < 1f && it > 0f } }

    fun getImmunities(t1: String, t2: String?) =
        ALL_TYPES.filter { getEffectiveness(it, t1, t2) == 0f }
}
```

- [ ] **Step 4 : Créer `Pokemon.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/domain/model/Pokemon.kt
package com.pokedex.app.domain.model

data class Pokemon(
    val id: Int,
    val nameFr: String,
    val typePrimary: String,
    val typeSecondary: String?,
    val weightKg: Float,
    val heightM: Float,
    val spriteUrl: String,
    val spriteShinyUrl: String,
    val isCaught: Boolean = false,
    val isShinyCaught: Boolean = false,
    val availableInGames: List<String> = emptyList()
)
```

- [ ] **Step 5 : Créer `SwitchGame.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/domain/model/SwitchGame.kt
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
```

- [ ] **Step 6 : Exécuter le test — vérifier qu'il passe**

  ```
  gradlew.bat :app:testDebugUnitTest --tests "com.pokedex.app.domain.util.TypeChartTest"
  ```
  Attendu : 4 tests PASS

- [ ] **Step 7 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/domain app/src/test/java/com/pokedex/app/domain
  git commit -m "feat: modèles domaine Pokemon, SwitchGame et TypeChart"
  ```

---

### Task 3 : Générer `switch_games.json`

**Files:**
- Create: `scripts/generate_switch_games.py`
- Create: `app/src/main/assets/switch_games.json`

- [ ] **Step 1 : Créer le dossier assets**

  ```
  mkdir app\src\main\assets
  ```

- [ ] **Step 2 : Créer `scripts/generate_switch_games.py`**

```python
# scripts/generate_switch_games.py
# Nécessite : pip install requests
# Usage     : python scripts/generate_switch_games.py
import requests, json, time

POKEDEX_MAP = {
    "LGP_LGE": "letsgo-kanto",
    "SS":      "galar",
    "SS_IOA":  "isle-of-armor",
    "SS_CT":   "crown-tundra",
    "BDSP":    "updated-sinnoh",
    "LA":      "hisui",
    "SV":      "paldea",
    "SV_TM":   "kitakami",
    "SV_ID":   "blueberry",
}

result = {}
for code, name in POKEDEX_MAP.items():
    r = requests.get(f"https://pokeapi.co/api/v2/pokedex/{name}/")
    r.raise_for_status()
    entries = r.json()["pokemon_entries"]
    ids = sorted(int(e["pokemon_species"]["url"].rstrip("/").split("/")[-1]) for e in entries)
    result[code] = ids
    print(f"{code}: {len(ids)} Pokémon")
    time.sleep(0.5)

with open("app/src/main/assets/switch_games.json", "w") as f:
    json.dump(result, f)
print("switch_games.json généré.")
```

- [ ] **Step 3 : Exécuter le script (depuis la racine du projet)**

  ```
  python scripts/generate_switch_games.py
  ```
  Attendu : 9 lignes affichées (une par jeu), fichier `app/src/main/assets/switch_games.json` créé.

- [ ] **Step 4 : Commit**

  ```bash
  git add scripts/generate_switch_games.py app/src/main/assets/switch_games.json
  git commit -m "feat: données Pokémon par jeu Switch (switch_games.json)"
  ```

---

### Task 4 : Room — Entités, DAOs, Database

**Files:**
- Create: `app/src/main/java/com/pokedex/app/data/local/entity/PokemonEntity.kt`
- Create: `app/src/main/java/com/pokedex/app/data/local/entity/CaptureStatusEntity.kt`
- Create: `app/src/main/java/com/pokedex/app/data/local/dao/PokemonDao.kt`
- Create: `app/src/main/java/com/pokedex/app/data/local/dao/CaptureStatusDao.kt`
- Create: `app/src/main/java/com/pokedex/app/data/local/AppDatabase.kt`

- [ ] **Step 1 : Créer `PokemonEntity.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/local/entity/PokemonEntity.kt
package com.pokedex.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val nameFr: String,
    val typePrimary: String,
    val typeSecondary: String?,
    val weightKg: Float,
    val heightM: Float,
    val spriteUrl: String,
    val spriteShinyUrl: String
)
```

- [ ] **Step 2 : Créer `CaptureStatusEntity.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/local/entity/CaptureStatusEntity.kt
package com.pokedex.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "capture_status")
data class CaptureStatusEntity(
    @PrimaryKey val pokemonId: Int,
    val isCaught: Boolean = false,
    val isShinyCaught: Boolean = false
)
```

- [ ] **Step 3 : Créer `PokemonDao.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/local/dao/PokemonDao.kt
package com.pokedex.app.data.local.dao

import androidx.room.*
import com.pokedex.app.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAllFlow(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id IN (:ids) ORDER BY id ASC")
    fun getByIdsFlow(ids: List<Int>): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getById(id: Int): PokemonEntity?

    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)
}
```

- [ ] **Step 4 : Créer `CaptureStatusDao.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/local/dao/CaptureStatusDao.kt
package com.pokedex.app.data.local.dao

import androidx.room.*
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaptureStatusDao {
    @Query("SELECT * FROM capture_status")
    fun getAllFlow(): Flow<List<CaptureStatusEntity>>

    @Query("SELECT * FROM capture_status WHERE pokemonId = :id")
    suspend fun getById(id: Int): CaptureStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(status: CaptureStatusEntity)

    @Query("SELECT COUNT(*) FROM capture_status WHERE isCaught = 1")
    fun countCaughtFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM capture_status WHERE isShinyCaught = 1")
    fun countShinyCaughtFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM capture_status WHERE isCaught = 1 AND pokemonId IN (:ids)")
    fun countCaughtInIdsFlow(ids: List<Int>): Flow<Int>

    @Query("SELECT COUNT(*) FROM capture_status WHERE isShinyCaught = 1 AND pokemonId IN (:ids)")
    fun countShinyCaughtInIdsFlow(ids: List<Int>): Flow<Int>
}
```

- [ ] **Step 5 : Créer `AppDatabase.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/local/AppDatabase.kt
package com.pokedex.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.local.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class, CaptureStatusEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun captureStatusDao(): CaptureStatusDao
}
```

- [ ] **Step 6 : Vérifier la compilation**

  ```
  gradlew.bat :app:kspDebugKotlin
  ```
  Attendu : BUILD SUCCESSFUL (KSP génère le code Room)

- [ ] **Step 7 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/data/local
  git commit -m "feat: Room database, entités et DAOs"
  ```

---

### Task 5 : Retrofit — DTOs et service PokéAPI

**Files:**
- Create: `app/src/main/java/com/pokedex/app/data/remote/dto/PokemonDto.kt`
- Create: `app/src/main/java/com/pokedex/app/data/remote/dto/PokemonSpeciesDto.kt`
- Create: `app/src/main/java/com/pokedex/app/data/remote/PokeApiService.kt`

- [ ] **Step 1 : Créer `PokemonDto.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/remote/dto/PokemonDto.kt
package com.pokedex.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val types: List<TypeSlotDto>,
    val sprites: SpritesDto
)

data class TypeSlotDto(
    val slot: Int,
    val type: NamedResourceDto
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_shiny")   val frontShiny: String?
)

data class NamedResourceDto(val name: String, val url: String)
```

- [ ] **Step 2 : Créer `PokemonSpeciesDto.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/remote/dto/PokemonSpeciesDto.kt
package com.pokedex.app.data.remote.dto

data class PokemonSpeciesDto(
    val names: List<LocalizedNameDto>
)

data class LocalizedNameDto(
    val name: String,
    val language: NamedResourceDto
)
```

- [ ] **Step 3 : Créer `PokeApiService.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/remote/PokeApiService.kt
package com.pokedex.app.data.remote

import com.pokedex.app.data.remote.dto.PokemonDto
import com.pokedex.app.data.remote.dto.PokemonSpeciesDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonDto

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpeciesDto
}
```

- [ ] **Step 4 : Vérifier la compilation**

  ```
  gradlew.bat :app:compileDebugKotlin
  ```
  Attendu : BUILD SUCCESSFUL

- [ ] **Step 5 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/data/remote
  git commit -m "feat: DTOs et service Retrofit PokéAPI"
  ```

---

### Task 6 : PreferencesManager + SwitchGamesLoader

**Files:**
- Create: `app/src/main/java/com/pokedex/app/util/PreferencesManager.kt`
- Create: `app/src/main/java/com/pokedex/app/util/SwitchGamesLoader.kt`

- [ ] **Step 1 : Créer `PreferencesManager.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/util/PreferencesManager.kt
package com.pokedex.app.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("pokedex_prefs", Context.MODE_PRIVATE)

    var lastSyncTimestamp: Long
        get() = prefs.getLong("last_sync", 0L)
        set(value) = prefs.edit().putLong("last_sync", value).apply()

    fun needsSync(): Boolean {
        val sevenDays = 7L * 24 * 60 * 60 * 1000
        return System.currentTimeMillis() - lastSyncTimestamp > sevenDays
    }
}
```

- [ ] **Step 2 : Créer `SwitchGamesLoader.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/util/SwitchGamesLoader.kt
package com.pokedex.app.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SwitchGamesLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Map<gameCode, List<pokemonId>>
    val data: Map<String, List<Int>> by lazy {
        val json = context.assets.open("switch_games.json").bufferedReader().readText()
        Gson().fromJson(json, object : TypeToken<Map<String, List<Int>>>() {}.type)
    }

    fun getIdsForGame(gameCode: String): List<Int> = data[gameCode] ?: emptyList()

    fun getGamesForPokemon(pokemonId: Int): List<String> =
        data.entries.filter { pokemonId in it.value }.map { it.key }
}
```

- [ ] **Step 3 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/util
  git commit -m "feat: PreferencesManager (sync date) et SwitchGamesLoader"
  ```

---

### Task 7 : PokemonRepository

**Files:**
- Create: `app/src/main/java/com/pokedex/app/data/repository/PokemonRepository.kt`
- Create: `app/src/main/java/com/pokedex/app/data/repository/PokemonRepositoryImpl.kt`
- Create: `app/src/test/java/com/pokedex/app/data/repository/PokemonRepositoryImplTest.kt`

- [ ] **Step 1 : Écrire les tests**

```kotlin
// app/src/test/java/com/pokedex/app/data/repository/PokemonRepositoryImplTest.kt
package com.pokedex.app.data.repository

import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.local.entity.PokemonEntity
import com.pokedex.app.data.remote.PokeApiService
import com.pokedex.app.util.PreferencesManager
import com.pokedex.app.util.SwitchGamesLoader
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PokemonRepositoryImplTest {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var captureStatusDao: CaptureStatusDao
    private lateinit var api: PokeApiService
    private lateinit var prefs: PreferencesManager
    private lateinit var gamesLoader: SwitchGamesLoader
    private lateinit var repo: PokemonRepositoryImpl

    @Before
    fun setup() {
        pokemonDao = mockk(relaxed = true)
        captureStatusDao = mockk(relaxed = true)
        api = mockk()
        prefs = mockk(relaxed = true)
        gamesLoader = mockk(relaxed = true)
        repo = PokemonRepositoryImpl(pokemonDao, captureStatusDao, api, prefs, gamesLoader)
    }

    @Test
    fun `toggleCaught insère un nouveau statut si absent`() = runTest {
        coEvery { captureStatusDao.getById(25) } returns null
        repo.toggleCaught(25)
        coVerify { captureStatusDao.upsert(CaptureStatusEntity(pokemonId = 25, isCaught = true, isShinyCaught = false)) }
    }

    @Test
    fun `toggleCaught inverse isCaught si déjà présent`() = runTest {
        val existing = CaptureStatusEntity(pokemonId = 25, isCaught = true, isShinyCaught = false)
        coEvery { captureStatusDao.getById(25) } returns existing
        repo.toggleCaught(25)
        coVerify { captureStatusDao.upsert(existing.copy(isCaught = false)) }
    }

    @Test
    fun `toggleShinyCaught insère un nouveau statut si absent`() = runTest {
        coEvery { captureStatusDao.getById(25) } returns null
        repo.toggleShinyCaught(25)
        coVerify { captureStatusDao.upsert(CaptureStatusEntity(pokemonId = 25, isCaught = false, isShinyCaught = true)) }
    }

    @Test
    fun `needsInitialSync retourne true si base vide`() = runTest {
        coEvery { pokemonDao.count() } returns 0
        assertEquals(true, repo.needsInitialSync())
    }

    @Test
    fun `needsInitialSync retourne false si base pleine`() = runTest {
        coEvery { pokemonDao.count() } returns 1025
        assertEquals(false, repo.needsInitialSync())
    }
}
```

- [ ] **Step 2 : Exécuter le test — vérifier qu'il échoue**

  ```
  gradlew.bat :app:testDebugUnitTest --tests "com.pokedex.app.data.repository.PokemonRepositoryImplTest"
  ```
  Attendu : FAIL (classes non définies)

- [ ] **Step 3 : Créer `PokemonRepository.kt` (interface)**

```kotlin
// app/src/main/java/com/pokedex/app/data/repository/PokemonRepository.kt
package com.pokedex.app.data.repository

import com.pokedex.app.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getAllPokemonFlow(): Flow<List<Pokemon>>
    fun getPokemonByGameFlow(gameCode: String): Flow<List<Pokemon>>
    suspend fun getPokemonById(id: Int): Pokemon?
    suspend fun toggleCaught(pokemonId: Int)
    suspend fun toggleShinyCaught(pokemonId: Int)
    suspend fun needsInitialSync(): Boolean
    suspend fun syncAllPokemon(onProgress: (Int, Int) -> Unit)
    suspend fun backgroundRefreshIfNeeded()
}
```

- [ ] **Step 4 : Créer `PokemonRepositoryImpl.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/data/repository/PokemonRepositoryImpl.kt
package com.pokedex.app.data.repository

import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.local.entity.PokemonEntity
import com.pokedex.app.data.remote.PokeApiService
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.util.PreferencesManager
import com.pokedex.app.util.SwitchGamesLoader
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val captureStatusDao: CaptureStatusDao,
    private val api: PokeApiService,
    private val prefs: PreferencesManager,
    private val gamesLoader: SwitchGamesLoader
) : PokemonRepository {

    override fun getAllPokemonFlow(): Flow<List<Pokemon>> =
        combine(pokemonDao.getAllFlow(), captureStatusDao.getAllFlow()) { entities, statuses ->
            mapToDomain(entities, statuses)
        }

    override fun getPokemonByGameFlow(gameCode: String): Flow<List<Pokemon>> {
        val ids = gamesLoader.getIdsForGame(gameCode)
        return combine(pokemonDao.getByIdsFlow(ids), captureStatusDao.getAllFlow()) { entities, statuses ->
            mapToDomain(entities, statuses)
        }
    }

    override suspend fun getPokemonById(id: Int): Pokemon? {
        val entity = pokemonDao.getById(id) ?: return null
        val status = captureStatusDao.getById(id)
        return entity.toDomain(status, gamesLoader.getGamesForPokemon(id))
    }

    override suspend fun toggleCaught(pokemonId: Int) {
        val current = captureStatusDao.getById(pokemonId)
        captureStatusDao.upsert(
            current?.copy(isCaught = !current.isCaught)
                ?: CaptureStatusEntity(pokemonId = pokemonId, isCaught = true)
        )
    }

    override suspend fun toggleShinyCaught(pokemonId: Int) {
        val current = captureStatusDao.getById(pokemonId)
        captureStatusDao.upsert(
            current?.copy(isShinyCaught = !current.isShinyCaught)
                ?: CaptureStatusEntity(pokemonId = pokemonId, isShinyCaught = true)
        )
    }

    override suspend fun needsInitialSync(): Boolean = pokemonDao.count() == 0

    override suspend fun syncAllPokemon(onProgress: (Int, Int) -> Unit) {
        val total = 1025
        val semaphore = Semaphore(10)
        var done = 0
        coroutineScope {
            (1..total).map { id ->
                async {
                    semaphore.withPermit {
                        fetchAndCache(id)
                        synchronized(this@PokemonRepositoryImpl) { done++ }
                        onProgress(done, total)
                    }
                }
            }.forEach { it.await() }
        }
        prefs.lastSyncTimestamp = System.currentTimeMillis()
    }

    override suspend fun backgroundRefreshIfNeeded() {
        if (prefs.needsSync()) {
            syncAllPokemon(onProgress = { _, _ -> })
        }
    }

    private suspend fun fetchAndCache(id: Int) {
        val dto = api.getPokemon(id)
        val species = api.getPokemonSpecies(id)
        val nameFr = species.names.find { it.language.name == "fr" }?.name ?: dto.name
        val primaryType = dto.types.find { it.slot == 1 }?.type?.name ?: "normal"
        val secondaryType = dto.types.find { it.slot == 2 }?.type?.name
        pokemonDao.insertAll(listOf(
            PokemonEntity(
                id = id,
                nameFr = nameFr,
                typePrimary = primaryType,
                typeSecondary = secondaryType,
                weightKg = dto.weight / 10f,
                heightM = dto.height / 10f,
                spriteUrl = dto.sprites.frontDefault ?: "",
                spriteShinyUrl = dto.sprites.frontShiny ?: ""
            )
        ))
    }

    private fun mapToDomain(entities: List<PokemonEntity>, statuses: List<CaptureStatusEntity>): List<Pokemon> {
        val statusMap = statuses.associateBy { it.pokemonId }
        return entities.map { it.toDomain(statusMap[it.id], gamesLoader.getGamesForPokemon(it.id)) }
    }

    private fun PokemonEntity.toDomain(status: CaptureStatusEntity?, games: List<String>) = Pokemon(
        id = id, nameFr = nameFr, typePrimary = typePrimary, typeSecondary = typeSecondary,
        weightKg = weightKg, heightM = heightM, spriteUrl = spriteUrl, spriteShinyUrl = spriteShinyUrl,
        isCaught = status?.isCaught ?: false, isShinyCaught = status?.isShinyCaught ?: false,
        availableInGames = games
    )
}
```

- [ ] **Step 5 : Exécuter les tests — vérifier qu'ils passent**

  ```
  gradlew.bat :app:testDebugUnitTest --tests "com.pokedex.app.data.repository.PokemonRepositoryImplTest"
  ```
  Attendu : 5 tests PASS

- [ ] **Step 6 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/data/repository app/src/test/java/com/pokedex/app/data/repository
  git commit -m "feat: PokemonRepository avec cache Room et refresh hebdomadaire"
  ```

---

### Task 8 : Hilt — modules DI

**Files:**
- Create: `app/src/main/java/com/pokedex/app/di/DatabaseModule.kt`
- Create: `app/src/main/java/com/pokedex/app/di/NetworkModule.kt`
- Create: `app/src/main/java/com/pokedex/app/di/RepositoryModule.kt`

- [ ] **Step 1 : Créer `DatabaseModule.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/di/DatabaseModule.kt
package com.pokedex.app.di

import android.content.Context
import androidx.room.Room
import com.pokedex.app.data.local.AppDatabase
import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "pokedex.db").build()

    @Provides fun providePokemonDao(db: AppDatabase): PokemonDao = db.pokemonDao()
    @Provides fun provideCaptureStatusDao(db: AppDatabase): CaptureStatusDao = db.captureStatusDao()
}
```

- [ ] **Step 2 : Créer `NetworkModule.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/di/NetworkModule.kt
package com.pokedex.app.di

import com.pokedex.app.data.remote.PokeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides @Singleton
    fun providePokeApiService(retrofit: Retrofit): PokeApiService =
        retrofit.create(PokeApiService::class.java)
}
```

- [ ] **Step 3 : Créer `RepositoryModule.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/di/RepositoryModule.kt
package com.pokedex.app.di

import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.data.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository
}
```

- [ ] **Step 4 : Créer `PokedexApplication.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/PokedexApplication.kt
package com.pokedex.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokedexApplication : Application()
```

  Ajouter dans `AndroidManifest.xml` : `android:name=".PokedexApplication"` sur la balise `<application>`.

- [ ] **Step 5 : Vérifier compilation**

  ```
  gradlew.bat :app:kspDebugKotlin
  ```
  Attendu : BUILD SUCCESSFUL

- [ ] **Step 6 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/di app/src/main/java/com/pokedex/app/PokedexApplication.kt
  git commit -m "feat: modules Hilt DI (Database, Network, Repository)"
  ```

---

### Task 9 : Thème visuel

**Files:**
- Create: `app/src/main/java/com/pokedex/app/ui/theme/Color.kt`
- Modify: `app/src/main/java/com/pokedex/app/ui/theme/Theme.kt`
- Create: `app/src/main/java/com/pokedex/app/ui/theme/TypeColors.kt`

- [ ] **Step 1 : Créer `Color.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/theme/Color.kt
package com.pokedex.app.ui.theme

import androidx.compose.ui.graphics.Color

val PokeRed    = Color(0xFFCC0000)
val PokeRedDark = Color(0xFF990000)
val PokeWhite  = Color(0xFFFFFFFF)
val PokeGold   = Color(0xFFFFD700)
val PokeGray   = Color(0xFF9E9E9E)
val PokeLightGray = Color(0xFFF5F5F5)
```

- [ ] **Step 2 : Remplacer `Theme.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/theme/Theme.kt
package com.pokedex.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PokedexColorScheme = lightColorScheme(
    primary = PokeRed,
    onPrimary = PokeWhite,
    primaryContainer = PokeRedDark,
    background = PokeWhite,
    surface = PokeWhite,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun PokedexTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PokedexColorScheme, content = content)
}
```

- [ ] **Step 3 : Créer `TypeColors.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/theme/TypeColors.kt
package com.pokedex.app.ui.theme

import androidx.compose.ui.graphics.Color

val typeColorMap = mapOf(
    "normal"   to Color(0xFFA8A878),
    "fire"     to Color(0xFFF08030),
    "water"    to Color(0xFF6890F0),
    "electric" to Color(0xFFF8D030),
    "grass"    to Color(0xFF78C850),
    "ice"      to Color(0xFF98D8D8),
    "fighting" to Color(0xFFC03028),
    "poison"   to Color(0xFFA040A0),
    "ground"   to Color(0xFFE0C068),
    "flying"   to Color(0xFFA890F0),
    "psychic"  to Color(0xFFF85888),
    "bug"      to Color(0xFFA8B820),
    "rock"     to Color(0xFFB8A038),
    "ghost"    to Color(0xFF705898),
    "dragon"   to Color(0xFF7038F8),
    "dark"     to Color(0xFF705848),
    "steel"    to Color(0xFFB8B8D0),
    "fairy"    to Color(0xFFEE99AC),
)

fun typeColor(type: String): Color = typeColorMap[type] ?: Color(0xFFA8A878)
```

- [ ] **Step 4 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/ui/theme
  git commit -m "feat: thème Pokémon (rouge/blanc) et couleurs des types"
  ```

---

### Task 10 : Navigation + écran de chargement

**Files:**
- Create: `app/src/main/java/com/pokedex/app/ui/navigation/Screen.kt`
- Create: `app/src/main/java/com/pokedex/app/ui/navigation/AppNavigation.kt`
- Create: `app/src/main/java/com/pokedex/app/ui/loading/LoadingScreen.kt`

- [ ] **Step 1 : Créer `Screen.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/navigation/Screen.kt
package com.pokedex.app.ui.navigation

sealed class Screen(val route: String) {
    object Loading : Screen("loading")
    object PokedexList : Screen("list")
    object PokemonDetail : Screen("detail/{pokemonId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}
```

- [ ] **Step 2 : Créer `LoadingScreen.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/loading/LoadingScreen.kt
package com.pokedex.app.ui.loading

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun LoadingScreen(progress: Int, total: Int) {
    val fraction = if (total > 0) progress / total.toFloat() else 0f
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔴 PokéDex", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = PokeRed)
        Spacer(Modifier.height(24.dp))
        Text("Téléchargement du Pokédex...", fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))
        LinearProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth(), color = PokeRed)
        Spacer(Modifier.height(8.dp))
        Text("$progress / $total Pokémon", fontSize = 14.sp)
    }
}
```

- [ ] **Step 3 : Créer `AppNavigation.kt`** (squelette — les écrans list/detail seront ajoutés aux tasks suivantes)

```kotlin
// app/src/main/java/com/pokedex/app/ui/navigation/AppNavigation.kt
package com.pokedex.app.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.pokedex.app.ui.loading.LoadingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Loading.route) {
        composable(Screen.Loading.route) {
            // Remplacé à la Task 11 — placeholder pour compiler
            LoadingScreen(progress = 0, total = 1025)
        }
        composable(Screen.PokedexList.route) {
            // Ajouté Task 13
        }
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) {
            // Ajouté Task 16
        }
    }
}
```

- [ ] **Step 4 : Mettre à jour `MainActivity.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/MainActivity.kt
package com.pokedex.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pokedex.app.ui.navigation.AppNavigation
import com.pokedex.app.ui.theme.PokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PokedexTheme { AppNavigation() } }
    }
}
```

- [ ] **Step 5 : Vérifier que l'app tourne sur émulateur/device**

  Menu Run → Run 'app'. L'écran de chargement doit s'afficher (0/1025 figé — normal).

- [ ] **Step 6 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/ui/navigation app/src/main/java/com/pokedex/app/ui/loading app/src/main/java/com/pokedex/app/MainActivity.kt
  git commit -m "feat: navigation Compose, écran de chargement, MainActivity"
  ```

---

### Task 11 : PokedexListViewModel

**Files:**
- Create: `app/src/main/java/com/pokedex/app/ui/list/PokedexListViewModel.kt`
- Create: `app/src/test/java/com/pokedex/app/ui/list/PokedexListViewModelTest.kt`

- [ ] **Step 1 : Écrire les tests**

```kotlin
// app/src/test/java/com/pokedex/app/ui/list/PokedexListViewModelTest.kt
package com.pokedex.app.ui.list

import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.SwitchGame
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokedexListViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repo: PokemonRepository
    private lateinit var vm: PokedexListViewModel

    private val fakePokemon = listOf(
        Pokemon(1, "Bulbizarre", "grass", "poison", 6.9f, 0.7f, "", "", isCaught = true, isShinyCaught = false),
        Pokemon(4, "Salamèche", "fire", null, 8.5f, 0.6f, "", "", isCaught = false, isShinyCaught = true),
        Pokemon(7, "Carapuce", "water", null, 9.0f, 0.5f, "", "", isCaught = false, isShinyCaught = false),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = mockk(relaxed = true)
        every { repo.getAllPokemonFlow() } returns flowOf(fakePokemon)
        every { repo.getPokemonByGameFlow(any()) } returns flowOf(fakePokemon)
        vm = PokedexListViewModel(repo)
    }

    @After
    fun teardown() { Dispatchers.resetMain() }

    @Test
    fun `état initial contient tous les Pokémon`() {
        assertEquals(3, vm.uiState.value.pokemon.size)
    }

    @Test
    fun `filtre par jeu met à jour selectedGame`() {
        vm.selectGame(SwitchGame.SV)
        assertEquals(SwitchGame.SV, vm.uiState.value.selectedGame)
    }

    @Test
    fun `progression normale compte les pokémon capturés`() {
        assertEquals(1, vm.uiState.value.caughtCount)
        assertEquals(3, vm.uiState.value.totalCount)
    }

    @Test
    fun `progression shiny compte les pokémon shiny capturés`() {
        assertEquals(1, vm.uiState.value.shinyCaughtCount)
    }

    @Test
    fun `recherche filtre par nom`() {
        vm.search("bul")
        val names = vm.uiState.value.pokemon.map { it.nameFr }
        assertEquals(listOf("Bulbizarre"), names)
    }
}
```

- [ ] **Step 2 : Exécuter le test — vérifier qu'il échoue**

  ```
  gradlew.bat :app:testDebugUnitTest --tests "com.pokedex.app.ui.list.PokedexListViewModelTest"
  ```
  Attendu : FAIL

- [ ] **Step 3 : Créer `PokedexListViewModel.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/list/PokedexListViewModel.kt
package com.pokedex.app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.SwitchGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokedexListUiState(
    val pokemon: List<Pokemon> = emptyList(),
    val selectedGame: SwitchGame = SwitchGame.ALL,
    val searchQuery: String = "",
    val caughtCount: Int = 0,
    val shinyCaughtCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val syncProgress: Int = 0,
    val syncTotal: Int = 1025
)

@HiltViewModel
class PokedexListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _selectedGame = MutableStateFlow(SwitchGame.ALL)
    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _pokemon: StateFlow<List<Pokemon>> = _selectedGame.flatMapLatest { game ->
        if (game == SwitchGame.ALL) repository.getAllPokemonFlow()
        else repository.getPokemonByGameFlow(game.code)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val uiState: StateFlow<PokedexListUiState> = combine(
        _pokemon, _selectedGame, _searchQuery
    ) { pokemon, game, query ->
        val filtered = if (query.isBlank()) pokemon
                       else pokemon.filter { it.nameFr.contains(query, ignoreCase = true) }
        PokedexListUiState(
            pokemon = filtered,
            selectedGame = game,
            searchQuery = query,
            caughtCount = pokemon.count { it.isCaught },
            shinyCaughtCount = pokemon.count { it.isShinyCaught },
            totalCount = pokemon.size
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, PokedexListUiState())

    fun selectGame(game: SwitchGame) { _selectedGame.value = game }
    fun search(query: String) { _searchQuery.value = query }
    fun toggleCaught(pokemonId: Int) = viewModelScope.launch { repository.toggleCaught(pokemonId) }
    fun toggleShinyCaught(pokemonId: Int) = viewModelScope.launch { repository.toggleShinyCaught(pokemonId) }
}
```

- [ ] **Step 4 : Exécuter les tests — vérifier qu'ils passent**

  ```
  gradlew.bat :app:testDebugUnitTest --tests "com.pokedex.app.ui.list.PokedexListViewModelTest"
  ```
  Attendu : 5 tests PASS

- [ ] **Step 5 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/ui/list/PokedexListViewModel.kt app/src/test/java/com/pokedex/app/ui/list
  git commit -m "feat: PokedexListViewModel avec filtre jeux et recherche"
  ```

---

### Task 12 : Composants de la liste

**Files:**
- Create: `app/src/main/java/com/pokedex/app/ui/list/components/PokemonCard.kt`
- Create: `app/src/main/java/com/pokedex/app/ui/list/components/GameFilterChips.kt`
- Create: `app/src/main/java/com/pokedex/app/ui/list/components/ProgressBarsSection.kt`

- [ ] **Step 1 : Créer `PokemonCard.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/list/components/PokemonCard.kt
package com.pokedex.app.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.ui.theme.PokeGold
import com.pokedex.app.ui.theme.typeColor

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (pokemon.isCaught && pokemon.isShinyCaught)
        typeColor(pokemon.typePrimary).copy(alpha = 0.15f)
    else Color.White

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = pokemon.spriteUrl,
                    contentDescription = pokemon.nameFr,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                Text(
                    text = pokemon.nameFr,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
            // Statut capture en haut à droite
            Row(
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (pokemon.isCaught) Text("✓", fontSize = 12.sp, color = Color(0xFF4CAF50))
                if (pokemon.isShinyCaught) Text("✨", fontSize = 12.sp, color = PokeGold)
            }
        }
    }
}
```

- [ ] **Step 2 : Créer `GameFilterChips.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/list/components/GameFilterChips.kt
package com.pokedex.app.ui.list.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pokedex.app.domain.model.SwitchGame
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun GameFilterChips(
    selectedGame: SwitchGame,
    onSelect: (SwitchGame) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp)
    ) {
        SwitchGame.entries.forEach { game ->
            FilterChip(
                selected = game == selectedGame,
                onClick = { onSelect(game) },
                label = { Text(game.displayName) },
                modifier = Modifier.padding(end = 6.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PokeRed,
                    selectedLabelColor = androidx.compose.ui.graphics.Color.White
                )
            )
        }
    }
}
```

- [ ] **Step 3 : Créer `ProgressBarsSection.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/list/components/ProgressBarsSection.kt
package com.pokedex.app.ui.list.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.ui.theme.PokeGold
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun ProgressBarsSection(
    caughtCount: Int,
    shinyCaughtCount: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Surface(shadowElevation = 4.dp, modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            ProgressRow(label = "Normal", count = caughtCount, total = total, color = PokeRed)
            Spacer(Modifier.height(6.dp))
            ProgressRow(label = "Shiny ✨", count = shinyCaughtCount, total = total, color = PokeGold)
        }
    }
}

@Composable
private fun ProgressRow(label: String, count: Int, total: Int, color: Color) {
    val fraction = if (total > 0) count / total.toFloat() else 0f
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text("$count / $total", fontSize = 13.sp, color = Color.Gray)
    }
    Spacer(Modifier.height(2.dp))
    LinearProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth(), color = color)
}
```

- [ ] **Step 4 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/ui/list/components
  git commit -m "feat: composants liste (PokemonCard, GameFilterChips, ProgressBarsSection)"
  ```

---

### Task 13 : PokedexListScreen + gestion du premier lancement

**Files:**
- Create: `app/src/main/java/com/pokedex/app/ui/list/PokedexListScreen.kt`
- Create: `app/src/main/java/com/pokedex/app/ui/list/SyncViewModel.kt`
- Modify: `app/src/main/java/com/pokedex/app/ui/navigation/AppNavigation.kt`

- [ ] **Step 1 : Créer `SyncViewModel.kt`** (gère le chargement initial et le routing)

```kotlin
// app/src/main/java/com/pokedex/app/ui/list/SyncViewModel.kt
package com.pokedex.app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SyncState {
    object Checking : SyncState()
    data class Syncing(val progress: Int, val total: Int) : SyncState()
    object Done : SyncState()
}

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SyncState>(SyncState.Checking)
    val state: StateFlow<SyncState> = _state

    init {
        viewModelScope.launch {
            if (repository.needsInitialSync()) {
                _state.value = SyncState.Syncing(0, 1025)
                repository.syncAllPokemon { done, total ->
                    _state.value = SyncState.Syncing(done, total)
                }
            } else {
                repository.backgroundRefreshIfNeeded()
            }
            _state.value = SyncState.Done
        }
    }
}
```

- [ ] **Step 2 : Créer `PokedexListScreen.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/list/PokedexListScreen.kt
package com.pokedex.app.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pokedex.app.ui.list.components.GameFilterChips
import com.pokedex.app.ui.list.components.PokemonCard
import com.pokedex.app.ui.list.components.ProgressBarsSection
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun PokedexListScreen(
    onPokemonClick: (Int) -> Unit,
    viewModel: PokedexListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("🔴 PokéDex") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PokeRed,
                        titleContentColor = androidx.compose.ui.graphics.Color.White
                    ),
                    actions = {
                        IconButton(onClick = { showSearch = !showSearch }) {
                            Icon(Icons.Default.Search, contentDescription = "Rechercher",
                                tint = androidx.compose.ui.graphics.Color.White)
                        }
                    }
                )
                if (showSearch) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = viewModel::search,
                        placeholder = { Text("Rechercher un Pokémon…") },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                        singleLine = true
                    )
                }
                GameFilterChips(
                    selectedGame = state.selectedGame,
                    onSelect = viewModel::selectGame,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        },
        bottomBar = {
            ProgressBarsSection(
                caughtCount = state.caughtCount,
                shinyCaughtCount = state.shinyCaughtCount,
                total = state.totalCount
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(padding),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.pokemon, key = { it.id }) { pokemon ->
                PokemonCard(pokemon = pokemon, onClick = { onPokemonClick(pokemon.id) })
            }
        }
    }
}
```

- [ ] **Step 3 : Mettre à jour `AppNavigation.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/navigation/AppNavigation.kt
package com.pokedex.app.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.pokedex.app.ui.list.PokedexListScreen
import com.pokedex.app.ui.list.SyncState
import com.pokedex.app.ui.list.SyncViewModel
import com.pokedex.app.ui.loading.LoadingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Loading.route) {
        composable(Screen.Loading.route) {
            val syncVm: SyncViewModel = hiltViewModel()
            val syncState by syncVm.state.collectAsState()

            LaunchedEffect(syncState) {
                if (syncState is SyncState.Done) {
                    navController.navigate(Screen.PokedexList.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            }

            when (val s = syncState) {
                is SyncState.Syncing -> LoadingScreen(s.progress, s.total)
                else -> LoadingScreen(0, 1025)
            }
        }
        composable(Screen.PokedexList.route) {
            PokedexListScreen(onPokemonClick = { id ->
                navController.navigate(Screen.PokemonDetail.createRoute(id))
            })
        }
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) {
            // Ajouté Task 16
        }
    }
}
```

- [ ] **Step 4 : Lancer l'app et vérifier le premier lancement**

  Run → l'écran de chargement doit s'afficher avec une barre de progression qui avance pendant le téléchargement des 1025 Pokémon, puis la liste doit apparaître.

- [ ] **Step 5 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/ui/list app/src/main/java/com/pokedex/app/ui/navigation
  git commit -m "feat: PokedexListScreen, SyncViewModel, premier lancement avec chargement"
  ```

---

### Task 14 : PokemonDetailViewModel

**Files:**
- Create: `app/src/main/java/com/pokedex/app/ui/detail/PokemonDetailViewModel.kt`
- Create: `app/src/test/java/com/pokedex/app/ui/detail/PokemonDetailViewModelTest.kt`

- [ ] **Step 1 : Écrire les tests**

```kotlin
// app/src/test/java/com/pokedex/app/ui/detail/PokemonDetailViewModelTest.kt
package com.pokedex.app.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.Pokemon
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repo: PokemonRepository
    private lateinit var vm: PokemonDetailViewModel

    private val fakePokemon = Pokemon(
        id = 6, nameFr = "Dracaufeu", typePrimary = "fire", typeSecondary = "flying",
        weightKg = 90f, heightM = 1.7f, spriteUrl = "", spriteShinyUrl = "",
        isCaught = false, isShinyCaught = false,
        availableInGames = listOf("SS", "SV")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = mockk(relaxed = true)
        coEvery { repo.getPokemonById(6) } returns fakePokemon
        val savedState = SavedStateHandle(mapOf("pokemonId" to 6))
        vm = PokemonDetailViewModel(repo, savedState)
    }

    @After
    fun teardown() { Dispatchers.resetMain() }

    @Test
    fun `chargement du Pokémon remplit l état`() = runTest {
        assertNotNull(vm.uiState.value.pokemon)
        assertEquals("Dracaufeu", vm.uiState.value.pokemon?.nameFr)
    }

    @Test
    fun `les faiblesses sont calculées correctement pour feu-vol`() = runTest {
        val weaknesses = vm.uiState.value.weaknesses
        assertTrue("water" in weaknesses)
        assertTrue("rock" in weaknesses)
    }

    @Test
    fun `toggleCaught appelle le repository`() = runTest {
        vm.toggleCaught()
        coVerify { repo.toggleCaught(6) }
    }

    @Test
    fun `toggleShinyCaught appelle le repository`() = runTest {
        vm.toggleShinyCaught()
        coVerify { repo.toggleShinyCaught(6) }
    }
}
```

- [ ] **Step 2 : Exécuter le test — vérifier qu'il échoue**

  ```
  gradlew.bat :app:testDebugUnitTest --tests "com.pokedex.app.ui.detail.PokemonDetailViewModelTest"
  ```
  Attendu : FAIL

- [ ] **Step 3 : Créer `PokemonDetailViewModel.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/detail/PokemonDetailViewModel.kt
package com.pokedex.app.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.util.TypeChart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokemonDetailUiState(
    val pokemon: Pokemon? = null,
    val weaknesses: List<String> = emptyList(),
    val resistances: List<String> = emptyList(),
    val immunities: List<String> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["pokemonId"])

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState

    init {
        loadPokemon()
    }

    private fun loadPokemon() = viewModelScope.launch {
        val pokemon = repository.getPokemonById(pokemonId)
        if (pokemon != null) {
            _uiState.value = PokemonDetailUiState(
                pokemon = pokemon,
                weaknesses = TypeChart.getWeaknesses(pokemon.typePrimary, pokemon.typeSecondary),
                resistances = TypeChart.getResistances(pokemon.typePrimary, pokemon.typeSecondary),
                immunities = TypeChart.getImmunities(pokemon.typePrimary, pokemon.typeSecondary),
                isLoading = false
            )
        }
    }

    fun toggleCaught() = viewModelScope.launch {
        repository.toggleCaught(pokemonId)
        loadPokemon()
    }

    fun toggleShinyCaught() = viewModelScope.launch {
        repository.toggleShinyCaught(pokemonId)
        loadPokemon()
    }
}
```

- [ ] **Step 4 : Exécuter les tests — vérifier qu'ils passent**

  ```
  gradlew.bat :app:testDebugUnitTest --tests "com.pokedex.app.ui.detail.PokemonDetailViewModelTest"
  ```
  Attendu : 4 tests PASS

- [ ] **Step 5 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/ui/detail/PokemonDetailViewModel.kt app/src/test/java/com/pokedex/app/ui/detail
  git commit -m "feat: PokemonDetailViewModel avec faiblesses/résistances"
  ```

---

### Task 15 : Composants de la fiche détail

**Files:**
- Create: `app/src/main/java/com/pokedex/app/ui/detail/components/TypeBadge.kt`
- Create: `app/src/main/java/com/pokedex/app/ui/detail/components/TypeEffectivenessSection.kt`
- Create: `app/src/main/java/com/pokedex/app/ui/detail/components/CaptureButtons.kt`

- [ ] **Step 1 : Créer `TypeBadge.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/detail/components/TypeBadge.kt
package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.ui.theme.typeColor

val typeNamesFr = mapOf(
    "normal" to "Normal", "fire" to "Feu", "water" to "Eau", "electric" to "Électrik",
    "grass" to "Plante", "ice" to "Glace", "fighting" to "Combat", "poison" to "Poison",
    "ground" to "Sol", "flying" to "Vol", "psychic" to "Psy", "bug" to "Insecte",
    "rock" to "Roche", "ghost" to "Spectre", "dragon" to "Dragon", "dark" to "Ténèbres",
    "steel" to "Acier", "fairy" to "Fée"
)

@Composable
fun TypeBadge(type: String, modifier: Modifier = Modifier) {
    Text(
        text = typeNamesFr[type] ?: type.replaceFirstChar { it.uppercase() },
        color = Color.White,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .background(typeColor(type), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 4.dp)
    )
}
```

- [ ] **Step 2 : Créer `TypeEffectivenessSection.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/detail/components/TypeEffectivenessSection.kt
package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TypeEffectivenessSection(
    weaknesses: List<String>,
    resistances: List<String>,
    immunities: List<String>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (weaknesses.isNotEmpty()) {
            TypeRow(label = "Faiblesses", types = weaknesses)
        }
        if (resistances.isNotEmpty()) {
            TypeRow(label = "Résistances", types = resistances)
        }
        if (immunities.isNotEmpty()) {
            TypeRow(label = "Immunités", types = immunities)
        }
    }
}

@Composable
private fun TypeRow(label: String, types: List<String>) {
    Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(types) { type -> TypeBadge(type = type) }
    }
}
```

- [ ] **Step 3 : Créer `CaptureButtons.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/detail/components/CaptureButtons.kt
package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pokedex.app.ui.theme.PokeGold
import com.pokedex.app.ui.theme.PokeGray
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun CaptureButtons(
    isCaught: Boolean,
    isShinyCaught: Boolean,
    onToggleCaught: () -> Unit,
    onToggleShinyCaught: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = onToggleCaught,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCaught) PokeRed else PokeGray
            )
        ) {
            Text(if (isCaught) "✓ Capturé" else "Non capturé", color = Color.White)
        }
        Button(
            onClick = onToggleShinyCaught,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isShinyCaught) PokeGold else PokeGray
            )
        ) {
            Text(if (isShinyCaught) "✨ Shiny !" else "✨ Shiny", color = Color.White)
        }
    }
}
```

- [ ] **Step 4 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/ui/detail/components
  git commit -m "feat: composants fiche détail (TypeBadge, TypeEffectiveness, CaptureButtons)"
  ```

---

### Task 16 : PokemonDetailScreen + câblage navigation

**Files:**
- Create: `app/src/main/java/com/pokedex/app/ui/detail/PokemonDetailScreen.kt`
- Modify: `app/src/main/java/com/pokedex/app/ui/navigation/AppNavigation.kt`

- [ ] **Step 1 : Créer `PokemonDetailScreen.kt`**

```kotlin
// app/src/main/java/com/pokedex/app/ui/detail/PokemonDetailScreen.kt
package com.pokedex.app.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pokedex.app.domain.model.SwitchGame
import com.pokedex.app.ui.detail.components.*
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun PokemonDetailScreen(
    onBack: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val pokemon = state.pokemon

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pokemon?.let { "#${it.id.toString().padStart(3,'0')} ${it.nameFr}" } ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour",
                            tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PokeRed, titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (pokemon == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PokeRed)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sprite
            AsyncImage(
                model = pokemon.spriteUrl,
                contentDescription = pokemon.nameFr,
                modifier = Modifier.size(160.dp)
            )

            // Types
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TypeBadge(pokemon.typePrimary)
                pokemon.typeSecondary?.let { TypeBadge(it) }
            }

            HorizontalDivider()

            // Faiblesses / résistances
            TypeEffectivenessSection(
                weaknesses = state.weaknesses,
                resistances = state.resistances,
                immunities = state.immunities
            )

            HorizontalDivider()

            // Taille / Poids
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Taille", color = Color.Gray, fontSize = 13.sp)
                    Text("${pokemon.heightM} m", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Poids", color = Color.Gray, fontSize = 13.sp)
                    Text("${pokemon.weightKg} kg", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            HorizontalDivider()

            // Jeux disponibles
            if (pokemon.availableInGames.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Disponible dans", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        pokemon.availableInGames.forEach { code ->
                            val name = SwitchGame.entries.find { it.code == code }?.displayName ?: code
                            SuggestionChip(onClick = {}, label = { Text(name, fontSize = 12.sp) })
                        }
                    }
                }
                HorizontalDivider()
            }

            // Boutons de capture
            CaptureButtons(
                isCaught = pokemon.isCaught,
                isShinyCaught = pokemon.isShinyCaught,
                onToggleCaught = viewModel::toggleCaught,
                onToggleShinyCaught = viewModel::toggleShinyCaught
            )
        }
    }
}
```

- [ ] **Step 2 : Mettre à jour le composable PokemonDetail dans `AppNavigation.kt`**

  Remplacer le commentaire `// Ajouté Task 16` par :
  ```kotlin
  composable(
      route = Screen.PokemonDetail.route,
      arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
  ) {
      PokemonDetailScreen(onBack = { navController.popBackStack() })
  }
  ```
  Ajouter l'import : `import com.pokedex.app.ui.detail.PokemonDetailScreen`

- [ ] **Step 3 : Tester sur device/émulateur**

  - Lancer l'app, cliquer sur un Pokémon → la fiche doit s'ouvrir
  - Vérifier : sprite affiché, types en badges colorés, faiblesses listées, taille/poids, boutons de capture
  - Cliquer sur "Non capturé" → bouton doit devenir rouge "✓ Capturé"
  - Retourner sur la liste → la carte doit afficher l'icône ✓ verte et la barre de progression doit avancer

- [ ] **Step 4 : Commit**

  ```bash
  git add app/src/main/java/com/pokedex/app/ui/detail/PokemonDetailScreen.kt app/src/main/java/com/pokedex/app/ui/navigation
  git commit -m "feat: PokemonDetailScreen avec fiche complète et boutons de capture"
  ```

---

### Task 17 : Tests de non-régression + push GitHub

**Files:**
- Modify: `.gitignore`

- [ ] **Step 1 : Ajouter `.superpowers/` au `.gitignore`**

  Ajouter à `.gitignore` :
  ```
  .superpowers/
  ```

- [ ] **Step 2 : Exécuter tous les tests unitaires**

  ```
  gradlew.bat :app:testDebugUnitTest
  ```
  Attendu : tous les tests PASS (TypeChartTest × 4, PokemonRepositoryImplTest × 5, PokedexListViewModelTest × 5, PokemonDetailViewModelTest × 4 = 18 tests)

- [ ] **Step 3 : Build release**

  ```
  gradlew.bat :app:assembleDebug
  ```
  Attendu : BUILD SUCCESSFUL, APK dans `app/build/outputs/apk/debug/`

- [ ] **Step 4 : Pousser sur GitHub**

  ```bash
  git remote add origin https://github.com/<votre-username>/pokedex-app.git
  git branch -M main
  git push -u origin main
  ```

---

## Résumé des tests

| Fichier de test | Tests | Ce qui est vérifié |
|---|---|---|
| `TypeChartTest` | 4 | Calcul faiblesses/résistances/immunités |
| `PokemonRepositoryImplTest` | 5 | Toggle capture, needsInitialSync |
| `PokedexListViewModelTest` | 5 | Filtre, recherche, compteurs progression |
| `PokemonDetailViewModelTest` | 4 | Chargement Pokémon, faiblesses, toggles |
| **Total** | **18** | |
