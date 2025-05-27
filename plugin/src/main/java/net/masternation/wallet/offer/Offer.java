package net.masternation.wallet.offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.masterkaiser.bukkit.api.item.ItemBuilder;
import me.masterkaiser.bukkit.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {

    private String id;
    private String name;
    private double cost;
    private boolean mountable;
    private int slot;
    private ItemBuilder icon;
    private List<String> commands;
    private List<ItemBuilder> items;

    public void giveToUser(@NotNull Player player, int a) {
        if (this.commands != null) {
            this.commands.forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                    .replace("{player}", player.getName())
                    .replace("{amount}", String.valueOf(a)))
            );
        }

        if (this.items != null) {
            this.items.forEach(itemBuilder -> {
                ItemStack is = itemBuilder.build();
                is.setAmount(a);
                ItemUtil.addOrDrop(player, is);
            });
        }
    }

}
