# UserPrefix Actions 操作

## 使用方式

在 `actions` 配置节点下，可以配置多个操作，格式为 `[操作类型] {操作参数}`，例如：

- `[CHAT] HELLO %player_name%!`
- `[SOUND] ENTITY_PLAYER_LEVELUP`

## 操作类型

目前支持以下操作类型：
- `CHAT` 以玩家聊天的形式发送，若需要发送指令则添加“/”前缀
- `CONSOLE` 以后台的形式发送指令
- `MESSAGE` 向玩家发送一条消息
- `SOUND` 向玩家发送声音
- `CLOSE` 关闭当前打开的GUI

所有需要键入文本的类型均支持 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 变量 。

## 限定点击类型

若您需要限定玩家点击的类型，如左键、右键等，则可以添加在操作类型后，以“`:`”分割，如：

- `[CLOSE:LEFT]` 代表左键点击关闭
- `[MESSAGE:MIDDLE] HELLO WORLD` 代表鼠标中间点击发送消息

详细点击类型见 [`org.bukkit.event.inventory.ClickType`](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/inventory/ClickType.html) 。



