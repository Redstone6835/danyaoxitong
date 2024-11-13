package org.canghai.danyaoxitong.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.Map;

//  目前用不到这个类，后面可能会扩展
public class Recipe {
    private final String name;
    private final String type; // shapeless or shaped
    private final List<Material> shapelessIngredients;
    private final Map<Character, Material> shapedIngredients;
    private final String[] shape;
    private final ItemStack result;

    // 构造函数：无形配方
    public Recipe(String name, List<Material> shapelessIngredients, ItemStack result) {
        this.name = name;
        this.type = "shapeless";
        this.shapelessIngredients = shapelessIngredients;
        this.shapedIngredients = null;
        this.shape = null;
        this.result = result;
    }

    // 构造函数：有形配方
    public Recipe(String name, String[] shape, Map<Character, Material> shapedIngredients, ItemStack result) {
        this.name = name;
        this.type = "shaped";
        this.shape = shape;
        this.shapedIngredients = shapedIngredients;
        this.shapelessIngredients = null;
        this.result = result;
    }

    // 其他方法和getter方法...
}

