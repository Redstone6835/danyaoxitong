package org.canghai.danyaoxitong.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.canghai.danyaoxitong.AlchemyCraft;
import org.canghai.danyaoxitong.items.herbs.HerbType;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class AlchemyListener implements Listener {
    private final Inventory inventory;
    private final AlchemyCraft alchemyCraft;
    private final NamespacedKey alchemyFurnaceKey;  // 用于标记“炼丹炉”
    private final JavaPlugin plugin;

    //  构造方法，初始化容器和炼丹逻辑
    public AlchemyListener(JavaPlugin plugin,AlchemyCraft alchemyCraft, NamespacedKey key) {
        this.alchemyCraft = alchemyCraft;
        this.alchemyFurnaceKey = key;
        this.plugin = plugin;

        //  创建一个自定义容器
        this.inventory = Bukkit.createInventory(null, 9, "炼丹炉");

        //  添加强化控件（按钮）
        ItemStack reinforceButton = new ItemStack(Material.ANVIL);
        ItemMeta meta = reinforceButton.getItemMeta();
        meta.setDisplayName("点击合成");
        reinforceButton.setItemMeta(meta);

        //  按钮位置
        inventory.setItem(6, reinforceButton);
        inventory.setItem(8, new ItemStack(Material.BARRIER));
    }

    //  给方块打上标记
    private void markBlockAsAlchemyFurnace(Block block) {
        BlockState state = block.getState();
        if (state instanceof Furnace furnace) {
            //  persistentDataContainer接口类似Map，用来存储自定义标签
            PersistentDataContainer dataContainer = furnace.getPersistentDataContainer();
            dataContainer.set(alchemyFurnaceKey, PersistentDataType.STRING, "alchemy_furnace");
            furnace.update();  //  更新方块状态
        }
    }

    //  打开自定义容器
    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    //  监听玩家放置方块事件，自动标记名字为“炼丹炉”的熔炉方块
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Player player = event.getPlayer();

        //  检查方块是否是熔炉
        if (block.getType() == Material.FURNACE) {
            ItemStack itemInHand = event.getItemInHand();
            ItemMeta meta = itemInHand.getItemMeta();

            //  检查方块名称是否为“炼丹炉”
            if (meta != null && "炼丹炉".equals(meta.getDisplayName())) {
                markBlockAsAlchemyFurnace(block);  //  给方块打标记
                plugin.getLogger().info(player.getName()+"放置并标记了一个炼丹炉！");
            }
        }
    }

    //  检测玩家右键点击“炼丹炉”方块
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            //  确保方块不为空且是熔炉类型
            if (block != null && block.getType() == Material.FURNACE) {
                BlockState state = block.getState();
                if (state instanceof Furnace furnace) {
                    PersistentDataContainer dataContainer = furnace.getPersistentDataContainer();

                    //  检查方块是否被标记为“炼丹炉”
                    if ("alchemy_furnace".equals(dataContainer.get(alchemyFurnaceKey, PersistentDataType.STRING))) {
                        openInventory(player);                 //  打开自定义容器
                        event.setCancelled(true);              //  阻止默认熔炉界面
                    }
                }
            }
        }
    }

    //  监听炼丹界面的点击事件并进行配方检查与合成
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("炼丹炉")) {     //  判断是否为炼丹界面

            Inventory inventory = event.getInventory();
            Player player = (Player) event.getWhoClicked();
            int clickedSlot = event.getRawSlot();
            Set<Integer> set = new HashSet<>();               //  用于检测输入物品是否为同一品质

            //  输入格子范围（0到5）
            if (clickedSlot >= 0 && clickedSlot <= 5) {
                event.setCancelled(false);                    //  允许玩家放入或移除材料
            }
            List<List<String>> ingredients = new ArrayList<>();
            //  从界面中获取原料
            if (clickedSlot == 6) {
                plugin.getLogger().info(player.getName()+"点击了炼丹炉控件！");
                event.setCancelled(true);

                //  获取放入物品的描述（Lore）
                ingredients = Arrays.stream(inventory.getContents())
                        .limit(6)
                        .filter(item -> item != null && item.getType() != Material.AIR)
                        .map(ItemStack::getItemMeta)                     // 获取每个物品的ItemMeta
                        .filter(meta -> meta != null && meta.hasLore())  // 过滤掉没有lore的ItemMeta
                        .map(ItemMeta::getLore)                          // 获取每个物品的lore
                        .toList();
                //  通过描述(Lore)映射品质
                for (List<String> innerList : ingredients) {
                    if (innerList != null && HerbType.getQuakityByLore(innerList.get(0)) != 0) {
                        set.add(HerbType.getQuakityByLore(innerList.get(0)));
                    } else {
                        //  调试
                        player.sendMessage("请输入正确配方！");
                        return;
                    }
                }

                if (set.size() == 1) {
                    //  检查品质
                    if (ingredients.size() == 6) {
                        List<Integer> list = new ArrayList<>(set);
                        //  调试
                        plugin.getLogger().info(player.getName()+"合成了品质等级为"+list.getFirst()+"的丹药！");

                        if (list.getFirst() != 0 && inventory.getItem(7) == null) {
                            ItemStack pill = alchemyCraft.createPill(list.getFirst());
                            event.getInventory().addItem(pill);           //   给玩家生成的丹药
                            //  每个格子消耗一个物品
                            for (int slot = 0; slot < ingredients.size(); slot++) {
                                ItemStack item = inventory.getItem(slot);
                                item.setAmount(item.getAmount() - 1);
                            }
                        } else {
                            player.sendMessage("配方无效！");
                        }
                    } else {
                        player.sendMessage("请放入正确数量的物品！");
                    }
                } else {
                    player.sendMessage("请输入同品质药草！");
                }

            }

        }
    }

    //  处理容器关闭事件，清理物品
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("炼丹炉")) {
            Inventory inventory = event.getInventory();
            Player player = (Player) event.getPlayer();

            // 获取玩家正在使用的方块位置
            Block block = player.getTargetBlockExact(5); // 获取距离玩家最近的交互方块（例如炼丹炉）
            if (block != null) {
                // 获取方块的上方位置
                Location dropLocation = block.getLocation().add(0.5, 1.5, 0.5); // 方块中心上方

                // 遍历所有格子，将非空物品掉落到容器上方
                for (int i = 0; i < inventory.getSize()-3; i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item != null && item.getType() != Material.AIR) {
                        // 扔出物品到容器上方
                        player.getWorld().dropItemNaturally(dropLocation, item);
                        // 清空该格子
                        inventory.setItem(i, null);
                    }
                }
            } else {
                // 如果未检测到方块，则将物品掉落在玩家脚下
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item != null && item.getType() != Material.AIR) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                        inventory.setItem(i, null);
                    }
                }
            }
        }
    }

}
