![BANNER](.documentation/images/banner.png)

README LANGUAGES [ [中文](README.md) | [**English**](README-en.md)  ]

# UserPrefix Plugin

[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/userprefix/badge?s=b76fec1f64726b5f19989aace6adb5f85fdab840)](https://www.codefactor.io/repository/github/carmjos/userprefix)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/UserPrefix)
[![License](https://img.shields.io/github/license/CarmJos/UserPrefix)](https://opensource.org/licenses/GPL-3.0)
[![Java CI with Maven](https://github.com/CarmJos/UserPrefix/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/UserPrefix/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.8--Latest-yellow)
![](https://visitor-badge.glitch.me/badge?page_id=userprefix.readme)

Lightweight, efficient, and real-time user prefix system.

This plugin is implemented based on Spigot ,**Theoretically** support ALL MineCraft Versions.

The development of this plugin is based on Chinese which purpose is to help Chinese developers learn Bukkit plugin
development.

> This plugin has been published on [SpigotMC](https://www.spigotmc.org/resources/userprefix.96277/) .

## Examples

![example](.documentation/images/using-example.png)

## Dependencies

- **[Necessary]** Plugin developed based on [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT)
  and [BukkitAPI](http://bukkit.org/).
- **[Necessary]** Plugin data storage base on  [LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/).
- **[Recommend]** Placeholders based on [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) .

For development dependencies, please see  [Dependencies](https://github.com/CarmJos/UserPrefix/network/dependencies) .

## Features

- **Theoretically** support ALL MineCraft Versions.
- Reloading the configuration will automatically refresh the prefix of all players.
- Real-time judgment and feedback to the player when permissions are changed.
- Configurable sounds and messages.
- The prefix icon can be configured as "Selected", "Has Permission" and “No Permission”.
- TabList is automatically sorted according to the weight of the prefix (if there is a conflict, it can be turned off)
- The prefix display on the player name (can be turned off if there has any conflict)
- Simple Chat Format Placeholder support. (Not Recommended)
- GUI with automatic sorting and page turning!
- Support PlaceholderAPI variables!
- Support [Hex Color](https://www.hexcolortool.com/)! (Version 1.16 and above)  `&(#Color)`
    - Example: LightSlateBlue `&(#8470FF)` 、 DarkSlateBlue `&(#483D8B)`
- Support Gradient Color! (Version 1.16 and above)  `&<#Color1>Message&<#Color2>`
    - Example: `&<#8470FF>Hello World!&<#483D8B>`

## Notice

### 1. Version support issues

This plugin theoretically supports all versions.

If the icon does not load, the sound cannot be played, etc., please check whether the type of the item and sound in the
configuration file exists in the current version.

Take the SOUND as an example. The sound that the villager said "OK" is "`VILLAGER_YES`" in the lower version, but it
becomes "`ENTITY_VILLAGER_YES`" in the higher version.

### 2. Scoreboard exception problem

The display of the prefix on the head and the sorting of the TabList both use the scoreboard API.

Please turn of the `functions.on-name-prefix` in the configuration if there is a conflict.

## Commands

This plugin's Commands are based on Chinese!
**May support multi-language in the future.**

```text
/UserPrefix or /prefix #Open prefix GUI
/UserPrefixAdmin # View Admin Command Help
/UserPrefixAdmin reload # Reload Config
/UserPrefixAdmin list # List all configured prefixes.
```

## Placeholders (PlaceholderAPI)

After installed the [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) , you can
type `/papi info UserPrefix` to see all the placeholders.

```text
# %UserPrefix_prefix% 
- Get the content of the current prefix
# %UserPrefix_weight% 
- Get the weight of the current prefix.
# %UserPrefix_identifier% 
- Get the identifier of the current prefix.
# %UserPrefix_name% 
- Get the name of the current prefix.
# %UserPrefix_has_<Identifier>% 
- Determine whether the player has a certain prefix(true/false)
```

## Configuration files

### Plugin Configuration ([`config.yml`]() .

Will be generated on the first boot up.

### Messages Configuration ([`messages.yml`]())

Will be generated on the first boot up.

### Prefixes Configuration ([`prefixes/*.yml`](src/main/resources/en_US/example-prefix.yml))

All prefixes are separate configuration files, stored in the `<Data Folder>/prefixes/` for easy management.

Some symbols in file name may affect reading, please avoid using them.

## Support and Donation

This project is support by the  [YourCraft(你的世界)](https://www.ycraft.cn) .
![TeamLogo](.documentation/images/team-logo.png)

## Open source agreement

The source code of this project uses  [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0)
License.

