package com.teenkung.afkreward.Loader;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.World;

public class WorldGuardLoader {

    private static RegionContainer Container;

    public static void loadWorldGuard() {
        Container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    public static void clearWorldGuard() {
        Container = null;
    }

    public static RegionContainer getRegionContainer() { return Container; }
    public static RegionManager getRegionManager(World world) { return getRegionContainer().get(BukkitAdapter.adapt(world)); }

}
