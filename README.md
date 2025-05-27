## 💰 vira-wallet

A Minecraft plugin that adds a virtual wallet system to your server with a built-in item shop and GUI-based purchase confirmation.
Supports multi-server environments using a shared MySQL database.

## ✨ Features

*  Virtual wallet system 
*  GUI-based ItemShop
*  Customizable offers (ranks, keys, and more)
*  Fully configurable GUIs and messages
*  Support for shared MySQL database across multiple servers

---

## 🔌 Requirements

*  Spigot/Paper 1.18+
* Java 17+

---

## 🔧 Configuration

The plugin comes with two main config files:

* `config.yml`
  Contains GUI layouts, discount settings, and item shop offers.

* `messages.yml`
  Fully customizable messages.

* `commands.yml`
  Change command names, permissions and descriptions.

All GUI elements (buttons, item slots, lore, display names) can be changed to fit your server theme.

---

## 🧠 Multi-Server Support

You can run this plugin across multiple servers **connected to the same MySQL database**.
This ensures players keep a synchronized wallet balance across all your connected servers (e.g., Survival, Skyblock, Lobby).

---

## 🛠 Admin Commands

| Command                         | Description                         | Permission                 |
|---------------------------------| ----------------------------------- | -------------------------- |
| `/wallet`                       | Check your own wallet balance       | `vira.wallet.wallet`       |
| `/wallet add <player> <amount>` | Add money to a player's wallet      | `vira.wallet.wallet.admin` |
| `/wallet minus <player> <amount>`  | Remove money from a player's wallet | `vira.wallet.wallet.admin` |
| `/wallet set <player> <amount>`    | Set a specific wallet balance       | `vira.wallet.wallet.admin` |
| `/wallet get <player>`          | View another player's balance       | `vira.wallet.wallet.admin` |
| `/wallet reload`                | Reload configuration files          | `vira.wallet.wallet.admin` |

---

## 🛍 Item Shop

Players can run `/itemshop` to open a GUI displaying available offers.
Each offer can include:

* Display item (e.g., VIP helmet)
* Price in wallet currency
* Commands executed on purchase
* Quantity selection interface
* Purchase confirmation dialog

---

## 🔗 Integration

Simply add a coin purchase option on your website, and set the command `/wallet add <player> <amount>` to run upon purchase.

---

## 📦 Example Offers

```yaml
offers:
  - id: legendary-keys
    name: '&cLegendary Keys'
    cost: 4.99
    mountable: true
    slot: 0
    icon:
      material: minecraft/tripwire_hook
      displayName: '&cLegendary Key'
    commands: []
    items:
      - material: minecraft/tripwire_hook
        displayName: '&cLegendary Key'
  - id: vip-rank
    name: '&6VIP'
    cost: 9.99
    mountable: false
    slot: 1
    icon:
      material: minecraft/iron_helmet
      displayName: '&6VIP'
    commands:
      - lp user {user} parent add vip
```

---

## Images
![not_found](https://github.com/virakaiser/vira-wallet/blob/master/images/2025-05-26_15.28.08.png)
![not_found](https://github.com/virakaiser/vira-wallet/blob/master/images/2025-05-26_15.28.16.png)
![not_found](https://github.com/virakaiser/vira-wallet/blob/master/images/2025-05-26_15.28.18.png)
![not_found](https://github.com/virakaiser/vira-wallet/blob/master/images/2025-05-26_15.28.23.png)
![not_found](https://github.com/virakaiser/vira-wallet/blob/master/images/2025-05-26_15.28.24.png)
![not_found](https://github.com/virakaiser/vira-wallet/blob/master/images/2025-05-26_15.28.49.png)

---

## 📄 License

You are allowed to use this plugin freely. However, you may NOT:
- Sell this plugin separately or as part of a plugin bundle.

All rights reserved.

---

