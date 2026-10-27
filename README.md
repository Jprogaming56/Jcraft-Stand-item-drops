# JCraft Stand Drops

Forge 1.20.1 addon for JCraft 0.18.x. Enemies that have a stand drop **Stellar Iron Ingots**
(the ingot in the Stand Arrowhead recipe). It talks to JCraft by reflection, so the JCraft jar is
not needed to build.

## Build (GitHub Actions)
1. Create a new GitHub repo and push this folder to it.
2. Open the Actions tab, run "build" (or just push). Download the `jcraft-stand-drops` artifact.
3. Put the jar in your `mods` folder next to JCraft.

## Config
`config/jcraftstanddrops-common.toml` (created on first launch):
dropItem, dropChance (0.5), lootingBonusPerLevel (0.1), minCount/maxCount (1), requirePlayerKill (true; kills made by your stand count), includePlayers (false).
