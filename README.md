![BANNER](https://raw.githubusercontent.com/CarmJos/UserPrefix/master/img/banner.png)
# 用户前缀系统插件
[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/userprefix/badge?s=b76fec1f64726b5f19989aace6adb5f85fdab840)](https://www.codefactor.io/repository/github/carmjos/userprefix)
[![Java CI with Maven](https://github.com/CarmJos/UserPrefix/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/UserPrefix/actions/workflows/maven.yml)
![](https://visitor-badge.glitch.me/badge?page_id=userprefix.readme)

轻便、高效、实时的用户前缀系统。

本插件基于Spigot实现，**理论上支持全版本**。

## 依赖
- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT)、[BukkitAPI](http://bukkit.org/) 实现。
- **[必须]** 数据部分基于 [LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/) 实现。
- [选配] 变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/UserPrefix/network/dependencies) 。

## 特性

- 理论上全版本支持！
- 游戏内重载配置文件并实时更新到玩家！
- 当玩家权限变更时会实时监测前缀，若权限不足则自动更换前缀并提示！
- 可配置的声音、消息！
- 前缀图标可配置“选中”、“有权限”与“无权限”三种状态的物品
    - 物品的配置通过ItemStack原生配置，支持MC所有的设定！
    - 具体的设定请参考其他文档哦~
- TabList自动按照前缀的权重排序 (如有冲突可关掉)
- 玩家头顶前缀显示 (如有冲突可关掉)
- 自动排序，且可翻页的GUI
- 支持PlaceholderAPI变量

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

```yaml
version: 1.0.0-SNAPSHOT # 配置文件版本，一般不会动。

debug: false #debug输出，开发者用的

functions:
  OnNamePrefix: true # 是否给头顶上添加前缀，该方法用到了头顶的那个计分板，如有冲突请关掉哦~
  autoUsePrefix: true # 自动前缀显示 当玩家没有自己选择一个前缀的时候，会自动使用所拥有的的前缀中权重最高的那一个

messages:
  selected:
    - "&7您选择了 &f%(name) &7作为当前显示的前缀。"
  expired:
    - "&7您先前使用的前缀 &f%(oldName) &7已到期。"
    - "&7现在已为您重新调整为 &f%(newName) &7。"
  help:
    - "&7输入 &b/prefix &7打开前缀选择菜单。"

Sounds: #相关的声音，注释掉则不播放声音 格式为 【声音名:音量:音调】 或 【声音名:音量】 或 【声音名】
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

prefixes:
  VIP:
    name: "&b&lPro&b" # [必须] 名字(切换的时候左下角会弹提示 用的就是这个名字)
    content: "§b§lPro §b" # [必须] 显示在名字前面的内容
    weight: 1 # [必须] 权重，用于GUI里面的排序(越大显示在越后面)和自动前缀显示
    permission: "yc.pro" # [非必须] 检测的权限，如果没有就是人人都能用，也代表不用配置“itemNoPermission”了(因为压根不可能显示没权限时候的物品)
    itemHasPermission: 
      #  [必须] 当有权限的时候会显示这个Item
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
    itemUsing: 
      # [非必需] 当有权限的时候会显示这个Item,如果没有这个配置就自动显示“itemHasPermission”的。
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
    itemNoPermission: 
      # [非必需] 如果没有权限就会显示这个item。如果不配置该物品，则玩家没有使用权限时不会显示在GUI里面。
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
![1920x1080](https://user-images.githubusercontent.com/36306882/133410923-19dd31d7-5e18-4ba0-8fda-7537633b39f9.png)

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://user-images.githubusercontent.com/36306882/133410996-3718d9ad-1460-45ef-a7f1-33fff78643fb.jpg" />

## 开源协议
本项目采用 [GNU General Public License v3.0](https://opensource.org/licenses/gpl-3.0.php) 开源协议。
