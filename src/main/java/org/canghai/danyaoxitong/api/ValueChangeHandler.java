package org.canghai.danyaoxitong.api;

import org.bukkit.entity.Player;

public class ValueChangeHandler {
    private Player player;

    public void addAttack(int attack) {}
    public void addDefense(int defense) {}
    public void addHealth(int health) {}
    public void addMagic(double magic) {}

    public ValueChangeHandler(Player p) {
        this.player = p;
    }
}
