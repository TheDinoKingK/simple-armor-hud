package com.armorhud.config;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class config {

    public static boolean BETTER_MOUNT_HUD = false;
    public static boolean DOUBLE_HOTBAR = false;
    public static boolean ARMOR_HUD = true;
    public static boolean RTL = false;
    public static boolean DISABLE_ARMOR_BAR = false;
    public static boolean ABOVE_HEALTH_BAR = false; // a new option to render armor hud above the healthbar instead of the hungerbar -Dino

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("armorhud.properties");

    public static void write(Properties properties) {
        properties.setProperty("better_mount_hud", Boolean.toString(BETTER_MOUNT_HUD));
        properties.setProperty("double_hotbar", Boolean.toString(DOUBLE_HOTBAR));
        properties.setProperty("armor_hud", Boolean.toString(ARMOR_HUD));
        properties.setProperty("right_to_left", Boolean.toString(RTL));
        properties.setProperty("disable_armor_bar", Boolean.toString(DISABLE_ARMOR_BAR));
        properties.setProperty("above_health_bar", Boolean.toString(ABOVE_HEALTH_BAR));
    }

    public void read(Properties properties) {
        BETTER_MOUNT_HUD =  Boolean.parseBoolean(properties.getProperty("better_mount_hud"));
        DOUBLE_HOTBAR =  Boolean.parseBoolean(properties.getProperty("double_hotbar"));
        ARMOR_HUD = Boolean.parseBoolean(properties.getProperty("armor_hud"));
        RTL = Boolean.parseBoolean(properties.getProperty("right_to_left"));
        DISABLE_ARMOR_BAR = Boolean.parseBoolean(properties.getProperty("disable_armor_bar"));
        ABOVE_HEALTH_BAR = Boolean.parseBoolean(properties.getProperty("above_health_bar"));
    }

    public static void save() {
        Properties properties = new Properties();
        write(properties);

        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createFile(CONFIG_PATH);
            } catch (IOException e) {
                LogManager.getLogger("Simple Survival Tweaks").error("Failed to create config file");
            }
        }

        try {
            properties.store(Files.newOutputStream(CONFIG_PATH), "Simple Survival Tweaks config file");
        } catch (IOException e) {
            LogManager.getLogger("Simple Survival Tweaks").error("Failed to write config");
        }
    }

    public void load() {
        Properties properties = new Properties();

        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createFile(CONFIG_PATH);
                save();
            } catch (IOException e) {
                LogManager.getLogger("Simple Survival Tweaks").error("Failed to create config file");
            }
        }

        try {
            properties.load(Files.newInputStream(CONFIG_PATH));
        } catch (IOException e) {
            LogManager.getLogger("Simple Survival Tweaks").error("Failed to read config");
        }

        read(properties);
    }

}
