package org.canghai.danyaoxitong.items.herbs;

import org.bukkit.Material;
import java.util.HashMap;
import java.util.Map;
import org.canghai.danyaoxitong.Danyaoxitong;

public class HerbType {
    //  定义三个药草参数
    private final String displayName;
    private final Material material;
    private final double dropChance;
    private final int quality;

    //  存储药草实例
    private static final Map<String, HerbType> loadedHerbs = new HashMap<>();
    //  构造器
    HerbType(String displayName, Material material, double dropChance , int quality) {
        this.displayName = displayName;
        this.material = material;
        this.dropChance = dropChance;
        this.quality = quality;
    }
    //  获取方法
    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public double getDropChance() {
        return dropChance;
    }

    public int getQuality() { return quality; }

    //  从配置文件中加载药草到静态集合中
    public static void loadHerbsFromConfig(Danyaoxitong plugin) {
        for (String key : plugin.getConfig().getConfigurationSection("herbs").getKeys(false)) {
            String name = plugin.getConfig().getString("herbs." + key + ".name");
            Material material = Material.getMaterial(plugin.getConfig().getString("herbs." + key + ".material"));
            double dropChance = plugin.getConfig().getDouble("herbs." + key + ".drop_chance");
            int quality = plugin.getConfig().getInt("herbs." + key + ".quality");

            if (material != null) {
                loadedHerbs.put(name, new HerbType(name, material, dropChance, quality));
            }
        }
    }

    //  获取加载的所有药草
    public static Map<String, HerbType> getLoadedHerbs() {
        return loadedHerbs;
    }
}
