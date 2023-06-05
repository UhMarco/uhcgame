package me.marco.uhcgame.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class RecipeManager implements Listener {

    public RecipeManager() {
        ShapedRecipe arrowEfficiency = new ShapedRecipe(new ItemStack(Material.ARROW, 20));
        arrowEfficiency.shape("TTT", "SSS", "FFF");
        arrowEfficiency.setIngredient('T', Material.FLINT);
        arrowEfficiency.setIngredient('S', Material.STICK);
        arrowEfficiency.setIngredient('F', Material.FEATHER);
        Bukkit.getServer().addRecipe(arrowEfficiency);

        ShapedRecipe ironSmeltingEfficiency = new ShapedRecipe(new ItemStack(Material.IRON_INGOT, 10));
        ironSmeltingEfficiency.shape("OOO", "OCO", "OOO");
        ironSmeltingEfficiency.setIngredient('O', Material.IRON_ORE);
        ironSmeltingEfficiency.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(ironSmeltingEfficiency);

        ShapedRecipe goldSmeltingEfficiency = new ShapedRecipe(new ItemStack(Material.GOLD_INGOT, 10));
        goldSmeltingEfficiency.shape("OOO", "OCO", "OOO");
        goldSmeltingEfficiency.setIngredient('O', Material.GOLD_ORE);
        goldSmeltingEfficiency.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(goldSmeltingEfficiency);

        ShapedRecipe porkEfficiency = new ShapedRecipe(new ItemStack(Material.GRILLED_PORK, 10));
        porkEfficiency.shape("OOO", "OCO", "OOO");
        porkEfficiency.setIngredient('O', Material.PORK);
        porkEfficiency.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(porkEfficiency);

        ShapedRecipe steakEfficiency = new ShapedRecipe(new ItemStack(Material.COOKED_BEEF, 10));
        steakEfficiency.shape("OOO", "OCO", "OOO");
        steakEfficiency.setIngredient('O', Material.RAW_BEEF);
        steakEfficiency.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(steakEfficiency);

        ShapedRecipe muttonEfficiency = new ShapedRecipe(new ItemStack(Material.COOKED_MUTTON, 10));
        muttonEfficiency.shape("OOO", "OCO", "OOO");
        muttonEfficiency.setIngredient('O', Material.MUTTON);
        muttonEfficiency.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(muttonEfficiency);

        ShapedRecipe chickenEfficiency = new ShapedRecipe(new ItemStack(Material.COOKED_CHICKEN, 10));
        chickenEfficiency.shape("OOO", "OCO", "OOO");
        chickenEfficiency.setIngredient('O', Material.RAW_CHICKEN);
        chickenEfficiency.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(chickenEfficiency);

        ShapedRecipe rabbitEfficiency = new ShapedRecipe(new ItemStack(Material.COOKED_RABBIT, 10));
        rabbitEfficiency.shape("OOO", "OCO", "OOO");
        rabbitEfficiency.setIngredient('O', Material.MUTTON);
        rabbitEfficiency.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(rabbitEfficiency);

        ShapedRecipe fishEfficiency = new ShapedRecipe(new ItemStack(Material.COOKED_FISH, 10));
        fishEfficiency.shape("OOO", "OCO", "OOO");
        fishEfficiency.setIngredient('O', Material.RAW_FISH);
        fishEfficiency.setIngredient('C', Material.COAL);
        Bukkit.getServer().addRecipe(fishEfficiency);

        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE, 1);
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();
        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 1, false);
        pickaxeMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        pickaxeMeta.setDisplayName(ChatColor.GREEN + "Quick Pick");
        pickaxe.setItemMeta(pickaxeMeta);
        ShapedRecipe quickPick = new ShapedRecipe(pickaxe);
        quickPick.shape("OOO", " S ", " S ");
        quickPick.setIngredient('O', Material.IRON_ORE);
        quickPick.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(quickPick);

        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        ItemMeta helmetMeta = helmet.getItemMeta();
        helmetMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        helmetMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        helmetMeta.setDisplayName(ChatColor.GREEN + "Rookie Helmet");
        helmet.setItemMeta(helmetMeta);
        ShapedRecipe rookieHelmet = new ShapedRecipe(helmet);
        rookieHelmet.shape("III", "ITI");
        rookieHelmet.setIngredient('I', Material.IRON_INGOT);
        rookieHelmet.setIngredient('T', Material.REDSTONE_TORCH_ON);
        Bukkit.getServer().addRecipe(rookieHelmet);

        ShapedRecipe saddle = new ShapedRecipe(new ItemStack(Material.SADDLE, 1));
        saddle.shape("LLL", "SLS", "I I");
        saddle.setIngredient('L', Material.LEATHER);
        saddle.setIngredient('S', Material.STRING);
        saddle.setIngredient('I', Material.IRON_INGOT);
        Bukkit.getServer().addRecipe(saddle);

        ShapedRecipe exp = new ShapedRecipe(new ItemStack(Material.EXP_BOTTLE, 1));
        exp.shape(" R ", "RBR", " R ");
        exp.setIngredient('R', Material.REDSTONE_BLOCK);
        exp.setIngredient('B', Material.GLASS_BOTTLE);
        Bukkit.getServer().addRecipe(exp);

        ShapedRecipe anvil = new ShapedRecipe(new ItemStack(Material.ANVIL, 1));
        anvil.shape("III", " B ", "III");
        anvil.setIngredient('I', Material.IRON_INGOT);
        anvil.setIngredient('B', Material.IRON_BLOCK);
        Bukkit.getServer().addRecipe(anvil);

        ShapedRecipe enchantingTable = new ShapedRecipe(new ItemStack(Material.ENCHANTMENT_TABLE, 1));
        enchantingTable.shape(" B ", "ODO", "OEO");
        enchantingTable.setIngredient('B', Material.BOOKSHELF);
        enchantingTable.setIngredient('O', Material.OBSIDIAN);
        enchantingTable.setIngredient('D', Material.DIAMOND);
        enchantingTable.setIngredient('E', Material.EXP_BOTTLE);
        Bukkit.getServer().addRecipe(enchantingTable);

        ShapedRecipe leatherEfficiency = new ShapedRecipe(new ItemStack(Material.LEATHER, 8));
        leatherEfficiency.shape("SLS", "SLS", "SLS");
        leatherEfficiency.setIngredient('S', Material.STICK);
        leatherEfficiency.setIngredient('L', Material.LEATHER);
        Bukkit.getServer().addRecipe(leatherEfficiency);

        ItemStack protBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta protBookMeta = (EnchantmentStorageMeta) protBook.getItemMeta();
        protBookMeta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        protBook.setItemMeta(protBookMeta);
        ShapedRecipe protectionBook = new ShapedRecipe(protBook);
        protectionBook.shape(" PP", " PI");
        protectionBook.setIngredient('P', Material.PAPER);
        protectionBook.setIngredient('I', Material.IRON_INGOT);
        Bukkit.getServer().addRecipe(protectionBook);

        ItemStack projBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta projBookMeta = (EnchantmentStorageMeta) projBook.getItemMeta();
        projBookMeta.addStoredEnchant(Enchantment.PROTECTION_PROJECTILE, 1, false);
        projBook.setItemMeta(projBookMeta);
        ShapedRecipe projectileProtectionBook = new ShapedRecipe(projBook);
        projectileProtectionBook.shape(" PP", " PI");
        projectileProtectionBook.setIngredient('P', Material.PAPER);
        projectileProtectionBook.setIngredient('I', Material.ARROW);
        Bukkit.getServer().addRecipe(projectileProtectionBook);

        ItemStack sharpBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta sharpBookMeta = (EnchantmentStorageMeta) sharpBook.getItemMeta();
        sharpBookMeta.addStoredEnchant(Enchantment.DAMAGE_ALL, 1, false);
        sharpBook.setItemMeta(sharpBookMeta);
        ShapedRecipe sharpnessBook = new ShapedRecipe(sharpBook);
        sharpnessBook.shape("F  ", " PP", " PI");
        sharpnessBook.setIngredient('P', Material.PAPER);
        sharpnessBook.setIngredient('I', Material.IRON_SWORD);
        sharpnessBook.setIngredient('F', Material.FLINT);
        Bukkit.getServer().addRecipe(sharpnessBook);

        ItemStack powBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta powBookMeta = (EnchantmentStorageMeta) powBook.getItemMeta();
        powBookMeta.addStoredEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        powBook.setItemMeta(powBookMeta);
        ShapedRecipe powerBook = new ShapedRecipe(powBook);
        powerBook.shape("F  ", " PP", " PI");
        powerBook.setIngredient('P', Material.PAPER);
        powerBook.setIngredient('I', Material.BONE);
        powerBook.setIngredient('F', Material.FLINT);
        Bukkit.getServer().addRecipe(powerBook);
    }

    @EventHandler
    public void craft(CraftItemEvent event){
        if (event.getCurrentItem().getType() != Material.IRON_INGOT && event.getCurrentItem().getType() != Material.GOLD_INGOT) return;
        Player player = (Player) event.getWhoClicked();
        player.giveExp(getRandomNumber(5, 8));
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.5f, 1);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
