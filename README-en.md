![BANNER](https://raw.githubusercontent.com/CarmJos/UserPrefix/master/img/banner.png)

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

This plugin has been published on [SpigotMC](https://www.spigotmc.org/resources/userprefix.96277/) .

本插件已在 [MCBBS](https://www.mcbbs.net/forum.php?mod=viewthread&tid=1261503) 上发布，欢迎中文用户来这里下载。

## Examples

![example](https://raw.githubusercontent.com/CarmJos/UserPrefix/master/img/using-example.png)

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
    - Item configuration is natively configured through ItemStack, which supports all MC settings!
- TabList is automatically sorted according to the weight of the prefix (if there is a conflict, it can be turned off)
- The prefix display on the player name (can be turned off if there is a conflict)
- GUI with automatic sorting and page turning!
- Support PlaceholderAPI variables!
- Support Hex color! (Version 1.16 and above)  `&(#Color)`
    - Example: LightSlateBlue `&(#8470FF)` 、 DarkSlateBlue `&(#483D8B)`

## Notice

### 1. Version support issues

This plugin theoretically supports all versions.

If the icon does not load, the sound cannot be played, etc., please check whether the type of the item and sound in the
configuration file exists in the current version.

Take the SOUND as an example. The sound that the villager said "OK" is "`VILLAGER_YES`" in the lower version, but it
becomes "`ENTITY_VILLAGER_YES`" in the higher version.

### 2. Scoreboard exception problem

The display of the prefix on the head and the sorting of the TabList both use the scoreboard API.

Please turn of the `functions.OnNamePrefix` in the configuration if there is a conflict.

### 3. Item icon configuration problem

Items are read through the ItemStack serialization method provided by Bukkit. For related configuration methods, please
refer to  [ItemStack Serialization](https://www.spigotmc.org/wiki/itemstack-serialization/).

## Commands

This plugin's Commands are based on Chinese!
**May support multi-language in the future.**

```text
/UserPrefix or /prefix #Open prefix GUI
/UserPrefixAdmin # View Admin Command Help
/UserPrefixAdmin reload # Reload Config
/UserPrefixAdmin list # List all configured prefixes.~~~~
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

### [Plugin Configuration](https://github.com/CarmJos/UserPrefix/blob/master/src/main/resources/en_US/config.yml) (config.yml)

Notice: The default configuration is based on Chinese. You can find
the [English Version here](https://github.com/CarmJos/UserPrefix/blob/master/src/main/resources/en_US/config.yml).

```yaml
version: ${project.version} # DO NOT EDIT IT

debug: false #DEBUG OUT PUT

GUI:
  title: "&f&lMy Prefixes List" # Title of the GUI
  items:
    next-page: # only show has next page
      ==: org.bukkit.inventory.ItemStack
      type: ARROW
      meta:
        ==: ItemMeta
        meta-type: UNSPECIFIC
        display-name: "§fNext Page"
        lore:
          - ""
          - "§fRight-Click to the last page"
    previous-page: # only show has previous page
      ==: org.bukkit.inventory.ItemStack
      type: ARROW
      meta:
        ==: ItemMeta
        meta-type: UNSPECIFIC
        display-name: "§fPrevious Page"
        lore:
          - ""
          - "§fRight-Click to the first page"

functions:
  # Whether to add a prefix to the top of the head,
  # this method uses the scoreboard above the head,
  # please turn it off if there is a conflict.
  OnNamePrefix: true
  # Automatic prefix select.
  # When the player does not choose a prefix by himself,
  # the prefix with the highest weight will be used automatically
  autoUsePrefix: true

Sounds:
  # Format is [SOUND_NAME:Volume:Pitch] or [SOUND_NAME:Volume] or [SOUND_NAME]
  openGUI: "BLOCK_NOTE_BLOCK_PLING:1:1"
  guiClick: "UI_BUTTON_CLICK"
  prefixChange: "ENTITY_VILLAGER_YES"
  prefixExpired: "ENTITY_VILLAGER_NO"

# The default prefix's weight is 0.
defaultPrefix:
  name: "Default prefix"
  content: "&b"
  itemNotUsing:
    ==: org.bukkit.inventory.ItemStack
    type: NAME_TAG
    meta:
      ==: ItemMeta
      meta-type: UNSPECIFIC
      display-name: "§fThe default prefix §f(Click to select)"
      lore:
        - ""
        - "§a➥ Click to use"
  itemUsing:
    ==: org.bukkit.inventory.ItemStack
    type: NAME_TAG
    meta:
      ==: ItemMeta
      meta-type: UNSPECIFIC
      display-name: "§fThe default prefix"
      lore:
        - ""
        - "§a✔ Selected"
```

### [Messages Configuration](https://github.com/CarmJos/UserPrefix/blob/master/src/main/resources/en_US/messages.yml)  (messages.yml)

```yaml
selected:
  - "&7You have selected the  &f%(name) &7as current prefix."
expired:
  - "&7Your prefix &f%(oldName) &7has expired,"
  - "&7Now the prefix is changed to &f%(newName) &7."
reload:
  - "&a&lReload completed！&7costs &f%(time)ms&7."
help:
  - "&3&lUserPrefixAdmin &fHelp"
  - "&8#/upa&f list"
  - "&8- &7Show configured prefixes."
  - "&8#/upa&f reload"
  - "&8- &7Reload configuration."
list-title:
  - "&3&lUserPrefixAdmin &fList"
list-value:
  - "&8#%(weight) &f%(identifier)"
  - "&8- &7Name &r%(name) &7Perm &r%(permission)"
  - "&8- &7Example&r %(content) %(sender_name)"

```

### [Prefixes Configuration](https://github.com/CarmJos/UserPrefix/blob/master/src/main/resources/en_US/example-prefix.yml) (/prefixes/*.yml)

All prefixes are separate configuration files, stored in the `<Data Folder>/prefixes/` for easy management.

Some symbols in file name may affect reading, please avoid using them.

```yaml
# identifier [Necessary]
# This will be used for data-storage.
identifier: "pro"

# Name [Necessary]
# Use in messages.
name: "&b&lPro&b"

# Content [Necessary]
# Use in Placeholders
content: "§b§lPro §b"

# Weight [Necessary]
# used for sorting in the GUI and TabList
# In GUI : the larger is displayed at the back
# At TabList : the larger is displayed at the top
weight: 1

# Permission [Unnecessary]
# If there is no permission for detection, everyone can use it,
# which means there is no need to configure "itemNoPermission"
# (because it is impossible to display items without permission at all)
permission: "yc.vip"

# itemHasPermission  [Necessary]
# This Item will be displayed when player has permission
itemHasPermission:
  ==: org.bukkit.inventory.ItemStack
  type: DIAMOND
  meta:
    ==: org.bukkit.inventory.ItemStack
      type: DIAMOND
      meta:
        ==: ItemMeta
        meta-type: UNSPECIFIC
        display-name: "§b§lVIP Prefix"
        lore:
          - ""
          - "§a➥ Click to  use"

# itemUsing [Unnecessary]
# This Item will be displayed when the prefix is selected.
# If there is no such configuration, it will automatically display "itemHasPermission".
itemUsing:
  ==: org.bukkit.inventory.ItemStack
    type: DIAMOND
    meta:
      ==: ItemMeta
      meta-type: UNSPECIFIC
      display-name: "§b§lVIP Prefix"
      enchants:
        PROTECTION_ENVIRONMENTAL: 1 #Add an enchantment so it looks like it’s selected
      lore:
        - ""
        - "§a✔ Selected"

# itemNoPermission [Unnecessary]
# If player doesn't have the permission,this item will be displayed.
# If this item is not configured, it will not be displayed in the GUI when the player does not have permission to use it.
itemNoPermission:
  ==: org.bukkit.inventory.ItemStack
    type: INK_SACK
    damage: 8
    meta:
      ==: ItemMeta
      meta-type: UNSPECIFIC
      display-name: "§b§lVIP §c(Buy it!)"
      lore:
        - ""
        - "§e✯ Buy the VIP to use it!"
```

## Support and Donation

This project is support by the  [YourCraft(你的世界)](https://www.ycraft.cn) .
![TeamLogo](https://raw.githubusercontent.com/CarmJos/UserPrefix/master/img/team-logo.png)

## Open source agreement

The source code of this project uses  [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0)
License.

