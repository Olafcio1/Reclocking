package pl.olafcio.reclocking;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public final class Reclocking extends JavaPlugin implements Listener {
    ArrayList<World> worlds;
    Calendar calendar;

    @Override
    public void onEnable() {
        worlds = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC+1")));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {}

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        var world = event.getWorld();

        worlds.add(world);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
    }

    @EventHandler
    public void onTickStart(ServerTickStartEvent event) {
        calendar.setTimeInMillis(System.currentTimeMillis());

        var hour = calendar.get(Calendar.HOUR_OF_DAY) * 1000;
        var min = calendar.get(Calendar.MINUTE) * 16;
        var sec = calendar.get(Calendar.SECOND) * 0.27;

        var total = (int) (hour + min + sec);
        for (var world : worlds)
            world.setTime(total);
    }
}
