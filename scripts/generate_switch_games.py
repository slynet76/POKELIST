# scripts/generate_switch_games.py
# Usage: python scripts/generate_switch_games.py
import requests, json, time

POKEDEX_MAP = {
    "LGP_LGE": "letsgo-kanto",
    "SS":      "galar",
    "SS_IOA":  "isle-of-armor",
    "SS_CT":   "crown-tundra",
    "BDSP":    "original-sinnoh",
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
    ids = sorted({int(e["pokemon_species"]["url"].rstrip("/").split("/")[-1]) for e in entries})
    result[code] = ids
    print(f"{code}: {len(ids)} Pokemon")
    time.sleep(0.5)

with open("app/src/main/assets/switch_games.json", "w") as f:
    json.dump(result, f)
print("switch_games.json generated.")
