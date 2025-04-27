package com.trongthang.realistictorches;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class ModConfig {
    private static final String CONFIG_FILE_NAME = "realistic_torches.json";
    private static ModConfig INSTANCE;

    @Expose
    @SerializedName("torchBurnOutTime")
    public int torchBurnOutTime = 36000;

    @Expose
    @SerializedName("submergeInWaterUnlitTorchesInInventory")
    public boolean submergeInWaterUnlitTorchesInInventory = true;

    @Expose
    @SerializedName("rainExtinguishTorches")
    public boolean rainExtinguishTorches = true;


//    @Expose
//    @SerializedName("allowPermanentTorches")
//    public boolean allowPermanentTorches = false;
//
//    @Expose
//    @SerializedName("permanentTorches")
//    public HashSet<String> permanentTorches = new HashSet<>(Arrays.asList(
//            "minecraft:soul_torch"
//    ));

    public static void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME);
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                INSTANCE = gson.fromJson(reader, ModConfig.class);
                if (INSTANCE == null) {
                    INSTANCE = new ModConfig(); // Fallback to default if JSON was empty or malformed
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            INSTANCE = new ModConfig();
        }

        saveConfig(gson, configFile); // Save current config, including defaults if they were missing
    }

    private static void saveConfig(Gson gson, File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModConfig getInstance() {
        return INSTANCE;
    }
}
