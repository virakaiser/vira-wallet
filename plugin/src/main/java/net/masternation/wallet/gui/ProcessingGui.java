package net.masternation.wallet.gui;

import me.masterkaiser.bukkit.BukkitApp;
import me.masterkaiser.bukkit.gui.Gui;
import me.masterkaiser.bukkit.gui.setup.GuiSetup;
import me.masterkaiser.framework.status.Initializable;
import net.masternation.wallet.config.Config;
import org.bukkit.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class ProcessingGui extends GuiSetup implements Initializable {

    private final Config config;

    public ProcessingGui(BukkitApp bukkitApp, Config config) {
        super(bukkitApp);
        this.config = config;
    }

    private Gui gui;

    public void open(Player player) {
        this.gui.open(player);
    }

    @Override
    public Gui build() {
        Gui gui = Gui.create(getBukkitApp(), this.config.processingGui, true, false, null);
        gui.setDisposeWhenClose(false);
        return gui;
    }

    @Override
    public void initialize() {
        this.gui = build();
    }

}
