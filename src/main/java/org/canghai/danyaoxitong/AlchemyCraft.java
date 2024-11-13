package org.canghai.danyaoxitong;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class AlchemyCraft {
    private final Map<String, ItemMeta> recipes = new HashMap<>();  // 配方集合

    public AlchemyCraft() {
        loadRecipes();
    }

    //  todo
    private void loadRecipes() {
        recipes.put("Health Elixir", );
        recipes.put("Strength Elixir", List.of(Material.BLAZE_POWDER, Material.SUGAR));
        // 添加其他配方...
    }

    // 检查给定的药草品质是否相同
    public String checkRecipe(List<List<String>> ingredients) {
        for (Map.Entry<String, List<Material>> entry : recipes.entrySet()) {
            if (entry.getValue().containsAll(ingredients) && ingredients.containsAll(entry.getValue())) {
                return entry.getKey();  // 返回匹配的配方名称
            }
        }
        return null;  // 无匹配的配方
    }

    // 根据配方生成对应的丹药物品
    public ItemStack createElixir(String recipeName) {
        ItemStack elixir = new ItemStack(Material.POTION);
        switch (recipeName) {
            case "Health Elixir" -> elixir.getItemMeta().setDisplayName("健康丹药");
            case "Strength Elixir" -> elixir.getItemMeta().setDisplayName("力量丹药");
            // 其他丹药逻辑...
        }
        return elixir;
    }
}

