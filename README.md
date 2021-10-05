![BANNER](https://raw.githubusercontent.com/CarmJos/UserPrefix/master/img/banner.png)

# 用户前缀系统插件

[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/userprefix/badge?s=b76fec1f64726b5f19989aace6adb5f85fdab840)](https://www.codefactor.io/repository/github/carmjos/userprefix)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/UserPrefix)
[![License](https://img.shields.io/github/license/CarmJos/UserPrefix)](https://opensource.org/licenses/GPL-3.0)
[![Java CI with Maven](https://github.com/CarmJos/UserPrefix/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/UserPrefix/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.8--Latest-yellow)
![](https://visitor-badge.glitch.me/badge?page_id=userprefix.readme)

轻便、高效、实时的用户前缀系统。

本插件基于Spigot实现，**理论上支持全版本**。

The **English version** of the introduction is [here](https://github.com/CarmJos/UserPrefix/blob/master/README-en.md).

> 本插件已在 [MCBBS](https://www.mcbbs.net/forum.php?mod=viewthread&tid=1261503) 与 [SpigotMC](https://www.spigotmc.org/resources/userprefix-hex-color-support-all-version.96277/) 上发布。

## 示例

![example](https://raw.githubusercontent.com/CarmJos/UserPrefix/master/img/using-example.png)

## 依赖

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT)、[BukkitAPI](http://bukkit.org/) 实现。
- **[必须]** 数据部分基于 [LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/) 实现。
- **[推荐]** 变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/UserPrefix/network/dependencies) 。

## 特性

- 理论上全版本支持！
- 游戏内重载配置文件并实时更新到玩家！
- 当玩家权限变更时会实时监测前缀，若权限不足则自动更换前缀并提示！[✈](https://github.com/CarmJos/UserPrefix/blob/master/src/main/java/cc/carm/plugin/userprefix/listener/processor/UserNodeUpdateProcessor.java)
- 可配置的声音、消息！
- 前缀图标可配置“选中”、“有权限”与“无权限”三种状态的物品
  - 物品的配置通过ItemStack原生配置，支持MC所有的设定！
  - 具体的设定请参考其他文档哦~
- TabList自动按照前缀的权重排序 (如有冲突可关掉)
- 玩家头顶前缀显示 (如有冲突可关掉) [✈](https://github.com/CarmJos/UserPrefix/blob/master/src/main/java/cc/carm/plugin/userprefix/nametag/UserNameTag.java)
- 简单的聊天变量修改功能！(不推荐使用) [✈](https://github.com/CarmJos/UserPrefix/blob/master/src/main/java/cc/carm/plugin/userprefix/listener/ChatListener.java)
- 自动排序，且可翻页的GUI！[✈](https://github.com/CarmJos/UserPrefix/blob/master/src/main/java/cc/carm/plugin/userprefix/ui/PrefixSelectGUI.java)
- 支持PlaceholderAPI变量！(凡支持的都可以使用，如BungeeTabListPlus) [✈](https://github.com/CarmJos/UserPrefix/blob/master/src/main/java/cc/carm/plugin/userprefix/hooker/UserPrefixExpansion.java)
- 支持[Hex颜色](https://www.hexcolortool.com/)！(1.16以上版本)   [✈](https://github.com/CarmJos/UserPrefix/blob/master/src/main/java/cc/carm/plugin/userprefix/util/ColorParser.java)
  - 格式： `&(#颜色代码)`
  - 示例： LightSlateBlue `&(#8470FF)` 、 DarkSlateBlue `&(#483D8B)`

## 注意事项

### 1. 版本支持问题

本插件理论全版本支持，如果出现图标不加载、声音无法播放等问题请检查配置文件中物品与声音的type在当前版本是否存在。

以声音举例，村民表示可以的声音在低版本中为 “`VILLAGER_YES`”,而在高版本中则变为了“`ENTITY_VILLAGER_YES`”。

### 2. 计分板异常问题

头顶上前缀的显示与TabList的排序均使用到了计分板API。

如有冲突导致其他插件的计分板无法显示，请关掉配置文件中`functions.OnNamePrefix`。

### 3. 物品图标配置问题

物品相关均通过Bukkit提供的ItemStack序列化方法读取，相关配置方式请参考[ItemStack Serialization(物品序列化)](https://www.spigotmc.org/wiki/itemstack-serialization/)。

## 指令

本插件指令部分较为简单。

```text
/UserPrefix 或 /prefix 打开前缀更换GUI
/UserPrefixAdmin 查看管理员指令帮助
/UserPrefixAdmin reload 重载配置文件
/UserPrefixAdmin list 查看已配置的前缀内容
```

## 变量 (PlaceholderAPI)

安装 [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) 后，可以输入 `/papi info UserPrefix` 查看相关变量。

变量内容如下

```text
# %UserPrefix_prefix% 
- 得到当前正在使用的前缀
# %UserPrefix_weight% 
- 得到当前正在使用的前缀权重
# %UserPrefix_identifier% 
- 得到当前正在使用的前缀标识
# %UserPrefix_name% 
- 得到当前正在使用的前缀名
# %UserPrefix_has_<Identifier>% 
- 判断玩家是否拥有某个前缀(true/false)
```

## 配置文件示例

### [基础配置文件](https://github.com/CarmJos/UserPrefix/blob/master/src/main/resources/config.yml) (config.yml)

```yaml
version: 1.0.0-SNAPSHOT # 配置文件版本，一般不会动。

debug: false #debug输出，开发者用的

functions:
  OnNamePrefix: true # 是否给头顶上添加前缀，该方法用到了头顶的那个计分板，如有冲突请关掉哦~
  autoUsePrefix: true # 自动前缀显示 当玩家没有自己选择一个前缀的时候，会自动使用所拥有的的前缀中权重最高的那一个
  chat:
    # 聊天功能
    # - 我不推荐使用本插件的聊天功能，而是建议使用其他的聊天插件。
    # - 本插件仅仅提供了**最基本**的格式变量支持，不包含其他任何功能。
    # - 注意聊天格式需要遵守Bukkit原格式，即不得缺失 “%1$s” 和 “%2$s” 。
    # - 本插件的聊天功能不影响其他插件对聊天事件的操作。
    enable: false # 是否启用
    format: "<%UserPrefix_prefix%%1$s> %2$s" #聊天的格式，注意 “%1$s” 和 “%2$s” 不可缺少，分别代表 玩家名 与 消息内容 。

GUI:
  title: "&f&l我的前缀 &8| 列表"
  items:
    # 【必须】 GUI中可能存在的其他物品
    next-page: # 下一页物品，如果没有下一页则不显示
      ==: org.bukkit.inventory.ItemStack
      type: ARROW
      meta:
        ==: ItemMeta
        meta-type: UNSPECIFIC
        display-name: "§f下一页"
        lore:
          - ""
          - "§f右键可前往最后一页哦~"
    previous-page: # 上一页物品，如果没有上一页则不显示
      ==: org.bukkit.inventory.ItemStack
      type: ARROW
      meta:
        ==: ItemMeta
        meta-type: UNSPECIFIC
        display-name: "§f上一页"
        lore:
          - ""
          - "§f右键可前往第一页哦~"

Sounds: #相关的声音，注释掉则不播放声音 
  # 格式为 【声音名:音量:音调】 或 【声音名:音量】 或 【声音名】
  openGUI: "BLOCK_NOTE_BLOCK_PLING:1:1"
  guiClick: "UI_BUTTON_CLICK"
  prefixChange: "ENTITY_VILLAGER_YES"
  prefixExpired: "ENTITY_VILLAGER_NO"

# 默认前缀的配置
# 默认前缀的权重为0哦
defaultPrefix:
  name: "默认前缀"
  content: "&b"
  itemNotUsing:
    ==: org.bukkit.inventory.ItemStack
    type: NAME_TAG
    meta:
      ==: ItemMeta
      meta-type: UNSPECIFIC
      display-name: "§f默认玩家前缀 §f(点击切换)"
      lore:
        - ""
        - "§a➥ 点击切换到该前缀"
  itemUsing:
    ==: org.bukkit.inventory.ItemStack
    type: NAME_TAG
    meta:
      ==: ItemMeta
      meta-type: UNSPECIFIC
      display-name: "§f默认玩家前缀"
      lore:
        - ""
        - "§a✔ 您正在使用该前缀"
```

### [消息配置文件](https://github.com/CarmJos/UserPrefix/blob/master/src/main/resources/messages.yml) (messages.yml)
```yaml
selected:
  - "&7您选择了 &f%(name) &7作为当前显示的前缀。"
expired:
  - "&7您先前使用的前缀 &f%(oldName) &7已到期。"
  - "&7现在已为您重新调整为 &f%(newName) &7。"
reload:
  - "&a&l重载完成！&7共耗时 &f%(time)ms&7。"
help:
  - "&3&l用户前缀系统 &f帮助"
  - "&8#/upa&f list"
  - "&8- &7查看当前前缀列表。"
  - "&8#/upa&f reload"
  - "&8- &7重载前缀配置。"
list-title:
  - "&3&l用户前缀系统 &f前缀列表"
list-value:
  - "&8#%(weight) &f%(identifier)"
  - "&8- &7显示名 &r%(name) &7权限 &r%(permission)"
  - "&8- &7内容示例&r %(content) %(sender_name)"
```

### [前缀配置文件](https://github.com/CarmJos/UserPrefix/blob/master/src/main/resources/prefixes/example-prefix.yml) (prefixes/*.yml)
所有前缀均为单独的配置文件，存放于 `插件配置目录/prefixes` 下，便于管理。

文件名理论上可以随便取，推荐使用英文，部分符号可能会影响正常读取，请避免使用。

```yaml
# 唯一标识 [必须]
# 将用于记录玩家所选的前缀，以及用于数据的缓存。
# 必须 必须 必须 保持唯一！
identifier: "pro"

# 名字 [必须]
# 切换的时候左下角会弹提示 用的就是这个名字
name: "&b&lPro&b"

# 内容 [必须]
# 显示在名字前面的内容
content: "§b§lPro §b"

# 权重 [必须]
# 用于GUI、TabList的排序和自动前缀显示
# 在GUI中，权重越高的会显示在越后面
# 在TabList中，权重越高的会显示在越上面
weight: 1

# 检测的权限 [非必须]
# 如果没有就是人人都能用，也代表不用配置“itemNoPermission”了(因为压根不可能显示没权限时候的物品)
permission: "yc.pro"

# 有权限时显示的物品  [必须]
# 当用户有权限且未选中时，会显示该物品
itemHasPermission: #
  ==: org.bukkit.inventory.ItemStack
  type: DIAMOND
  meta:
    ==: ItemMeta
    meta-type: UNSPECIFIC
    display-name: "§b§lPro §b会员前缀"
    lore:
      - "§7Pro会员专属称号"
      - ""
      - "§f尊贵的Pro会员专属称号。"
      - "§f您将获得多种特权与更好的游戏体验。"
      - ""
      - "§a➥ 点击切换到该前缀"

# 正在使用时显示的物品 [非必需]
# 当用户正在使用时会显示这个物品，不配置即自动加载“itemHasPermission”
itemUsing:
  ==: org.bukkit.inventory.ItemStack
  type: DIAMOND
  meta:
    ==: ItemMeta
    meta-type: UNSPECIFIC
    display-name: "§b§lPro §b会员前缀"
    enchants:
      PROTECTION_ENVIRONMENTAL: 1 #加一个附魔这样看上去就像是选中了的
    lore:
      - "§7Pro会员专属称号"
      - ""
      - "§f尊贵的Pro会员专属称号。"
      - "§f您将获得多种特权与更好的游戏体验。"
      - ""
      - "§a✔ 您正在使用该前缀"

# 没有权限时显示的物品 [非必需]
# 如果没有权限就会显示这个item。如果不配置该物品，则玩家没有使用权限时不会显示在GUI里面。
itemNoPermission:
  ==: org.bukkit.inventory.ItemStack
  type: INK_SACK
  damage: 8
  meta:
    ==: ItemMeta
    meta-type: UNSPECIFIC
    display-name: "§b§lPro+ §b会员前缀 §c(未拥有)"
    lore:
      - "§7Pro+会员专属称号"
      - ""
      - "§f尊贵的Pro会员专属称号。"
      - "§f您将获得多种特权与更好的游戏体验。"
      - "§f您可以输入 §b/vip §f指令查看详细特权！"
      - ""
      - "§e✯ 加入Pro+会员以使用该前缀！"
```


## 支持与捐赠

本项目由 [YourCraft(你的世界)](https://www.ycraft.cn) 团队提供长期支持与维护。
![TeamLogo](https://raw.githubusercontent.com/CarmJos/UserPrefix/master/img/team-logo.png)

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://github.com/CarmJos/CarmJos/blob/main/img/PAY.jpg" />

## 开源协议

本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。
> ### 关于 GPL 协议
> GNU General Public Licence (GPL) 有可能是开源界最常用的许可模式。GPL 保证了所有开发者的权利，同时为使用者提供了足够的复制，分发，修改的权利：
>
> #### 可自由复制
> 你可以将软件复制到你的电脑，你客户的电脑，或者任何地方。复制份数没有任何限制。
> #### 可自由分发
> 在你的网站提供下载，拷贝到U盘送人，或者将源代码打印出来从窗户扔出去（环保起见，请别这样做）。
> #### 可以用来盈利
> 你可以在分发软件的时候收费，但你必须在收费前向你的客户提供该软件的 GNU GPL 许可协议，以便让他们知道，他们可以从别的渠道免费得到这份软件，以及你收费的理由。
> #### 可自由修改
> 如果你想添加或删除某个功能，没问题，如果你想在别的项目中使用部分代码，也没问题，唯一的要求是，使用了这段代码的项目也必须使用 GPL 协议。
>
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下 @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL 协议，你必须在源代码代码中包含相应信息，以及协议本身。
> 
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
