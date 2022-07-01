![BANNER](.documentation/images/banner.png)

README LANGUAGES [ [**中文**](README.md) | [English](README-en.md)  ]

# 用户前缀系统插件

[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/userprefix/badge?s=b76fec1f64726b5f19989aace6adb5f85fdab840)](https://www.codefactor.io/repository/github/carmjos/userprefix)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/UserPrefix)
[![Download](https://img.shields.io/github/downloads/CarmJos/UserPrefix/total)](https://github.com/CarmJos/UserPrefix/releases)
[![Java CI with Maven](https://github.com/CarmJos/UserPrefix/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/UserPrefix/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.8--Latest-green)
![](https://visitor-badge.glitch.me/badge?page_id=userprefix.readme)

轻便、高效、实时的用户前缀系统。

本插件基于Spigot实现，**理论上支持全版本**。

> 本插件已在 [MCBBS](https://www.mcbbs.net/forum.php?mod=viewthread&tid=1261503)
> 与 [SpigotMC](https://www.spigotmc.org/resources/userprefix-hex-color-support-all-version.96277/) 上发布。

## 示例

![example](.documentation/images/using-example.png)

## 依赖

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT)、[BukkitAPI](http://bukkit.org/) 实现。
- **[必须]** 数据部分基于 [LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/) 实现。
- **[推荐]** 变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/UserPrefix/network/dependencies) 。

## 特性

- 理论上全版本支持！
- 游戏内重载配置文件并实时更新到玩家！
- 当玩家权限变更时会实时监测前缀，若权限不足则自动更换前缀并提示
- 可配置的声音、消息！
- 前缀图标可配置“选中”、“有权限”与“无权限”三种状态的物品
- TabList自动按照前缀的权重排序 (如有冲突可关掉)
- 玩家头顶前缀显示 (如有冲突可关掉)
- 简单的聊天变量修改功能！(不推荐使用) `[自 v2.1.0 版本起]`
- 自动排序，且可翻页的GUI！
- 支持PlaceholderAPI变量！(凡支持的都可以使用，如BungeeTabListPlus)
- 支持[Hex颜色](https://www.hexcolortool.com/)！(1.16以上版本) `[自 v1.2.3 版本起]`
    - 格式： `&(#颜色代码)`
    - 示例： LightSlateBlue `&(#8470FF)` 、 DarkSlateBlue `&(#483D8B)`

## 注意事项

### 1. 版本支持问题

本插件理论全版本支持，如果出现图标不加载、声音无法播放等问题请检查配置文件中物品与声音的type在当前版本是否存在。

以声音举例，村民表示可以的声音在低版本中为 “`VILLAGER_YES`”,而在高版本中则变为了“`ENTITY_VILLAGER_YES`”。

### 2. 计分板异常问题

头顶上前缀的显示与TabList的排序均使用到了计分板API。

如有冲突导致其他插件的计分板无法显示，请关掉配置文件中`functions.on-name-prefix`。

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

### 基础配置文件 ([`config.yml`](src/main/java/cc/carm/plugin/userprefix/configuration/PluginConfig.java))

将会在服务器首次启动时生成，如果您想要修改配置，请在服务器启动后打开配置文件。

### 消息配置文件 ([`messages.yml`](src/main/java/cc/carm/plugin/userprefix/configuration/PluginMessages.java))

将会在服务器首次启动时生成，如果您想要修改配置，请在服务器启动后打开配置文件 。

### 前缀配置文件 ([`prefixes/*.yml`](src/main/resources/prefixes/example-prefix.yml))

所有前缀均为单独的配置文件，存放于 `插件配置目录/prefixes` 下，便于管理。

文件名理论上可以随便取，推荐使用英文，部分符号可能会影响正常读取，请避免使用。

您可以 [点击这里](src/main/resources/prefixes/example-prefix.yml) 查看示例前缀配置文件。

## 使用统计

[![bStats](https://bstats.org/signatures/bukkit/UserPrefix.svg)](https://bstats.org/plugin/bukkit/UserPrefix/13776)

## 支持与捐赠

本项目由 [YourCraft(你的世界)](https://www.ycraft.cn) 团队提供长期支持与维护。
![TeamLogo](.documentation/images/team-logo.png)

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>

## 开源协议

本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。

<details>
  <summary>关于 GPL 协议</summary>

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
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下 @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL
> 协议，你必须在源代码代码中包含相应信息，以及协议本身。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
</details>