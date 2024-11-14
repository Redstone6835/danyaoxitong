package org.canghai.danyaoxitong.items.pills;

import org.canghai.danyaoxitong.Danyaoxitong;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class PillType{
    //  定义三个丹药参数
    private final String displayName;
    private final Material material;
    private final String description;
    private final int quality;

    //  存储药草实例
    private static final Map<String, PillType> loadedHerbs = new HashMap<>();
    PillType(String displayName, Material material, String description, int quality) {
        this.displayName = displayName;
        this.material = material;
        this.description = description;
        this.quality = quality;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDescription() {
        return description;
    }

    public int getQuality() {
        return quality;
    }

    //  从配置文件中加载丹药到静态集合中
    public static void loadPillsFromConfig(Danyaoxitong plugin) {
        for (String key : plugin.getConfig().getConfigurationSection("pills").getKeys(false)) {
            String name = plugin.getConfig().getString("pills." + key + ".name");
            Material material = Material.getMaterial(plugin.getConfig().getString("pills." + key + ".material"));
            String description = plugin.getConfig().getString("pills." + key + ".description");
            int quality = plugin.getConfig().getInt("pills." + key + ".quality");

            if (material != null) {
                loadedHerbs.put(name, new PillType(name, material, description ,quality));
            }
        }
    }

    //  获取加载的所有药草
    public static Map<String, PillType> getLoadedPills() {
        return loadedHerbs;
    }
}