package org.canghai.danyaoxitong;

import org.bukkit.plugin.java.JavaPlugin;
import org.canghai.danyaoxitong.items.pills.PillType;
import org.canghai.danyaoxitong.listeners.AlchemyListener;
import org.canghai.danyaoxitong.listeners.HerbHarvestListener;
import org.canghai.danyaoxitong.listeners.PillUsageListener;
import org.canghai.danyaoxitong.listeners.PlayerJoinListener;
import org.canghai.danyaoxitong.items.herbs.HerbType;
import org.bukkit.NamespacedKey;

import java.util.Random;

public final class Danyaoxitong extends JavaPlugin {
    // 生成随机数，用于药草掉落概率
    private final Random random = new Random();
    private static Danyaoxitong instance;

    @Override
    public void onEnable() {
        //  插件启动
        instance = this;
        getLogger().info("丹药系统 插件已启动！");
        // 生成配置文件配置
        saveDefaultConfig();
        // 加载药草和丹药配置
        HerbType.loadHerbsFromConfig(this);
        PillType.loadPillsFromConfig(this);

        //  注册事件
        registerEvents();
        //  注册命令
        this.getCommand("alchemy").setExecutor(new AlchemyCommand());

    }

    @Override
    public void onDisable() {
        //  Plugin shutdown logic
    }

    //  获取instence
    public static Danyaoxitong getInstance() {
        return instance;
    }

    //  注册事件方法
    private void registerEvents() {
        AlchemyCraft alchemyCraft = new AlchemyCraft();
        NamespacedKey alchemyFurnaceKey = new NamespacedKey(this, "alchemy_furnace");
        AlchemyListener alchemyListener = new AlchemyListener(this ,alchemyCraft, alchemyFurnaceKey);

        getServer().getPluginManager().registerEvents(alchemyListener, this);
        getServer().getPluginManager().registerEvents(new HerbHarvestListener(this), this);
        getServer().getPluginManager().registerEvents(new PillUsageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

    }

}
