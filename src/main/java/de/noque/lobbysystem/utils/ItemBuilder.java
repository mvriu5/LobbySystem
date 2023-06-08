package de.noque.lobbysystem.utils;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.*;

public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(final Material material) {
        this(material, 1);
    }

    public ItemBuilder(final ItemStack item) {
        super();
        this.item = item;
    }

    public ItemBuilder(final Material material, final int amount) {
        super();
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder setName(final TextComponent name) {
        final ItemMeta itemMeta = getItemMeta();
        itemMeta.displayName(name);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(final int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setUnbreakable(final boolean unbreakable) {
        final ItemMeta itemMeta = getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchant(final Enchantment enchantment, final int level) {
        final ItemMeta itemMeta = getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(final Enchantment enchantment, final int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addEnchantments(final Map<Enchantment, Integer> enchantments) {
        this.item.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder removeEnchantment(final Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder setLore(final String... lines) {
        final ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(Arrays.asList(lines));
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(final List<TextComponent> lines) {
        final ItemMeta itemMeta = getItemMeta();
        itemMeta.lore(lines);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(final String line) {
        final ItemMeta itemMeta = getItemMeta();
        final List<String> lores;

        if (itemMeta.getLore() == null) {
            lores = new ArrayList<>();
        } else {
            lores = new ArrayList<>(itemMeta.getLore());
        }

        if (!lores.contains(line)) {
            return this;
        }

        lores.remove(line);
        itemMeta.setLore(lores);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(final int index) {
        final ItemMeta itemMeta = getItemMeta();
        final List<String> lores;

        if (itemMeta.getLore() == null) {
            lores = new ArrayList<>();
        } else {
            lores = new ArrayList<>(itemMeta.getLore());
        }

        if (index < 0 || index > lores.size()) {
            return this;
        }

        lores.remove(index);
        itemMeta.setLore(lores);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(final String line) {
        final ItemMeta itemMeta = getItemMeta();
        final List<String> lores;

        if (itemMeta.getLore() == null) {
            lores = new ArrayList<>();
        } else {
            lores = new ArrayList<>(itemMeta.getLore());
        }

        lores.add(line);

        itemMeta.setLore(lores);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLines(final String... lines) {
        final ItemMeta itemMeta = getItemMeta();
        final List<String> lores;

        if (itemMeta.getLore() == null) {
            lores = new ArrayList<>();
        } else {
            lores = new ArrayList<>(itemMeta.getLore());
        }

        lores.addAll(Arrays.asList(lines));

        itemMeta.setLore(lores);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLoreLine(final String line, final int position) {
        final ItemMeta itemMeta = getItemMeta();
        final List<String> lores;

        if (itemMeta.getLore() == null) {
            lores = new ArrayList<>();
        } else {
            lores = new ArrayList<>(itemMeta.getLore());
        }

        lores.set(position, line);
        itemMeta.setLore(lores);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addItemFlags(final ItemFlag... flags) {
        final ItemMeta itemMeta = getItemMeta();
        itemMeta.addItemFlags(flags);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    @Deprecated
    public ItemBuilder setSkullOwner(final String owner) {
        final ItemMeta itemMeta = getItemMeta();

        if (!(itemMeta instanceof SkullMeta)) {
            return this;
        }

        final SkullMeta skullMeta = (SkullMeta) itemMeta;
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        this.item.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder setSkullOwner(final UUID uuid) {
        final ItemMeta itemMeta = getItemMeta();

        if (!(itemMeta instanceof SkullMeta)) {
            return this;
        }

        final SkullMeta skullMeta = (SkullMeta) itemMeta;
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        this.item.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(final Color color) {
        final ItemMeta itemMeta = getItemMeta();

        if (!(itemMeta instanceof LeatherArmorMeta)) {
            return this;
        }

        final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        leatherArmorMeta.setColor(color);
        this.item.setItemMeta(leatherArmorMeta);
        return this;
    }

    public ItemBuilder setBasePotionData(final PotionType potionType) {
        final ItemMeta itemMeta = getItemMeta();

        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        final PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.setBasePotionData(new PotionData(potionType));
        this.item.setItemMeta(potionMeta);
        return this;
    }

    public ItemBuilder addCustomPotionEffect(final PotionEffect potionEffect) {
        final ItemMeta itemMeta = getItemMeta();

        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        final PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.addCustomEffect(potionEffect, true);
        this.item.setItemMeta(potionMeta);
        return this;
    }

    public ItemBuilder setPotionColor(final Color color) {
        final ItemMeta itemMeta = getItemMeta();

        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        final PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.setColor(color);
        this.item.setItemMeta(potionMeta);
        return this;
    }

    public ItemStack toItemStack() {
        return this.item;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ItemMeta getItemMeta() {

        if (item.getItemMeta() == null) {
            item.setItemMeta(Bukkit.getItemFactory().getItemMeta(item.getType()));
        }

        return item.getItemMeta();
    }
}

