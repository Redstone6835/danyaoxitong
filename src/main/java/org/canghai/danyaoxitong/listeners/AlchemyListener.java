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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.canghai.danyaoxitong.AlchemyCraft;
import org.canghai.danyaoxitong.items.pills.PillType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlchemyListener implements Listener {
    private final Inventory inventory;
    private final AlchemyCraft alchemyCraft;
    private final NamespacedKey alchemyFurnaceKey;  // 用于标记“炼丹炉”

    //  构造方法，初始化容器和炼丹逻辑
    public AlchemyListener(AlchemyCraft alchemyCraft, NamespacedKey key) {
        this.alchemyCraft = alchemyCraft;
        this.alchemyFurnaceKey = key;

        //  创建一个3行的自定义容器
        this.inventory = Bukkit.createInventory(null, 9, "炼丹炉");

        //  添加强化控件（按钮）
        ItemStack reinforceButton = new ItemStack(Material.ANVIL);
        ItemMeta meta = reinforceButton.getItemMeta();
        meta.setDisplayName("点击合成");
        reinforceButton.setItemMeta(meta);

        //  将按钮放置在中间位置
        inventory.setItem(7, reinforceButton);
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
                player.sendMessage("您已成功放置并标记了一个炼丹炉！");
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
                        player.sendMessage("玩家点击了炼丹炉！");  //  调试信息
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

            //  输入格子范围（0到5）
            if (clickedSlot >= 0 && clickedSlot <= 5) {
                event.setCancelled(false);                      //  允许玩家放入或移除材料
            }
            List<List<String>> ingredients = new ArrayList<>();
            //  从界面中获取原料
            if (clickedSlot == 7) {
                event.setCancelled(true);
                ingredients = Arrays.stream(inventory.getContents())
                        .limit(6)
                        .filter(item -> item != null && item.getType() != Material.AIR)
                        .map(ItemStack::getItemMeta)  // 获取每个物品的ItemMeta
                        .filter(meta -> meta != null && meta.hasLore())  // 过滤掉没有lore的ItemMeta
                        .map(ItemMeta::getLore)  // 获取每个物品的lore
                        .toList();
            }

            //  检查配方
            String recipeName = alchemyCraft.checkRecipe(ingredients);
            if (recipeName != null) {
                //  输出随机丹药逻辑实现 todo

                ItemStack elixir = alchemyCraft.createElixir(recipeName);
                player.getInventory().addItem(elixir);           //   给玩家生成的丹药

                player.sendMessage("成功合成了：" + recipeName);
            } else {
                player.sendMessage("配方无效！");
            }

        }
    }

    //  处理容器关闭事件，清理物品
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("炼丹界面")) {
            //  清理输入框和输出框物品
            inventory.setItem(1, null);
            inventory.setItem(2, null);
            inventory.setItem(3, null);
            inventory.setItem(4, null);
            inventory.setItem(5, null);
            inventory.setItem(0, null);
            inventory.setItem(6, null);
            inventory.setItem(8, null);

        }
    }
}
