package com.william.plugin;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener{
	
	//Stores the location of the vending machine:
	Location vend;
	
	//Stores list of foods that will can come out of the vending machine:
	ArrayList<ItemStack> foods = new ArrayList<>();
	
	@Override
	public void onEnable() {
		System.out.println("VendingMachine plugin has worked (sorta)");
		
		//Add food items to 'foods' ArrayList, stock the vending machine with these:
		ItemStack cookie = new ItemStack(Material.COOKIE, 4);
		foods.add(cookie);
		foods.add(new ItemStack(Material.BAKED_POTATO, 2));
		foods.add(new ItemStack(Material.MUSHROOM_SOUP, 1));
		foods.add(new ItemStack(Material.GOLDEN_CARROT, 2));
		foods.add(new ItemStack(Material.GOLDEN_APPLE, 1));
		foods.add(new ItemStack(Material.PUMPKIN_PIE, 2));
		
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		System.out.println("VendingMachine plugin disabled");
	}
	
	//Creates a vending machine using small armor stands with tiny blocks as helmet:
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Material block = e.getBlockPlaced().getType();
		
		//Places vending machine if stained glass is placed:
		if (block.equals(Material.CAKE_BLOCK)) {
			e.getBlockPlaced().setType(Material.STONE_SLAB2);
			Location location = e.getBlock().getLocation();
			
			spawnStand(location.add(0.28125, -0.15625, 0.71875), new ItemStack(Material.DROPPER));
			spawnStand(location.add(0.4375, 0, 0), new ItemStack(Material.STAINED_CLAY));
			
			spawnStand(location.add(0, 0, -0.4375), new ItemStack(Material.IRON_BLOCK));
			spawnStand(location.add(-0.4375, 0, -0), new ItemStack(Material.IRON_BLOCK));
			
			//Second layer:
			Location location2 = e.getBlock().getLocation();
			spawnStand(location2.add(0.28125, 0.28125, 0.71875), new ItemStack(Material.GLASS));
			spawnStand(location2.add(0.4375, 0, 0), new ItemStack(Material.STAINED_CLAY));
			
			spawnStand(location2.add(0, 0, -0.4375), new ItemStack(Material.IRON_BLOCK));
			spawnStand(location2.add(-0.4375, 0, -0), new ItemStack(Material.BOOKSHELF));
			
			//Third layer:
			Location location3 = e.getBlock().getLocation();
			spawnStand(location3.add(0.28125, 0.71875, 0.71875), new ItemStack(Material.GLASS));
			spawnStand(location3.add(0.4375, 0, 0), new ItemStack(Material.DROPPER));
			
			spawnStand(location3.add(0, 0, -0.4375), new ItemStack(Material.IRON_BLOCK));
			spawnStand(location3.add(-0.4375, 0, -0), new ItemStack(Material.BOOKSHELF));
			
			//Fourth layer:
			Location location4 = e.getBlock().getLocation();
			spawnStand(location4.add(0.28125, 1.15625, 0.71875), new ItemStack(Material.GLASS));
			spawnStand(location4.add(0.4375, 0, 0), new ItemStack(Material.STAINED_CLAY));
			
			spawnStand(location4.add(0, 0, -0.4375), new ItemStack(Material.IRON_BLOCK));
			spawnStand(location4.add(-0.4375, 0, -0), new ItemStack(Material.BOOKSHELF));
			
			//fourth layer:
			Location location5 = e.getBlock().getLocation();
			spawnStand(location5.add(0.28125, 1.59375, 0.71875), new ItemStack(Material.QUARTZ_BLOCK));
			spawnStand(location5.add(0.4375, 0, 0), new ItemStack(Material.QUARTZ_BLOCK));
			
			spawnStand(location5.add(0, 0, -0.4375), new ItemStack(Material.QUARTZ_BLOCK));
			spawnStand(location5.add(-0.4375, 0, -0), new ItemStack(Material.QUARTZ_BLOCK));
			
			//Stores location of vending machine in the 'vend' variable:
			vend = location;
		}
	}
	
	//When player drops gold nugget near vending machine, it will give them food:
	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		ItemStack item = e.getItemDrop().getItemStack();
		Player player = e.getPlayer();
		Location location = e.getItemDrop().getLocation();
		
		//Checks if the item dropped was a gold nugget:
		if (item.getType().equals(Material.GOLD_NUGGET)) {
			//Gets x,y,z coordinates of dropped gold nugget:
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			
			//Gets x,y,z coordinates of vending machine:
			double vendX = vend.getX();
			double vendY = vend.getY();
			double vendZ = vend.getZ();
			
			//Checks if vending machine exist:
			if (vend != null) {
				//Checks if gold is dropped near vending machine:
				if (x >= (vendX - 2) && x <= (vendX + 2)) {
					//Confirmed X coords are similar
					if (z >= (vendZ - 2) && z <= (vendZ + 2)) {
						//Confirmed Z coords are similar
						if (y >= (vendY - 2) && y <= (vendY + 2)) {
							//Confirmed Z coords are similar
							
							//Drops random food item from foods ArrayList:
							int totalFood = foods.size();
							int randomFood = new Random(System.currentTimeMillis()).nextInt(totalFood);
							ItemStack food = foods.get(randomFood);
							e.getItemDrop().setItemStack(food);
							
							player.sendMessage("Item exchanged sucessfully");
						}
					}
				}
			}
		}
	}
	
	//Class that creates each armor stand given location and block (material to wear as helmet):
	private void spawnStand(Location location, ItemStack material) {
		
		//Spawns armor stand:
		ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		
		stand.setBasePlate(false);
		
		//Stand won't take damage or have gravity:
		stand.setGravity(false);
		
		stand.setInvulnerable(true);
		
		//Stand will be invisible and appear as a mini block:
		stand.setSmall(true);
		
		stand.setVisible(false);
		
		//This is the mini 'block' that will be visible:
		stand.setHelmet(material);
	}
}
