package etnamc.sihirlisu.main;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin implements Listener {
	File configFile = new File(this.getDataFolder(), "config.yml");
	public static Economy economy = null;
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		getServer().getPluginManager().registerEvents(this, this);
		configCheck();
		setupEconomy();
		configFix();
		getServer().getPluginCommand("sihirlisu");
		getServer().getConsoleSender().sendMessage("");
		getServer().getConsoleSender().sendMessage( Prefix + " §aSihirli su plugini baþarýyla etkinleþtirildi!");
		getServer().getConsoleSender().sendMessage("");
		saveDefaultConfig();
		
	}
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(Prefix + " §cSihirli su plugini deaktif edildi!");
		getConfig().options().copyDefaults(true);

		
	}
	public void configCheck() {

		saveDefaultConfig();
		
		if(getConfig().getString("SihirliSu." + "Ayarlar") == null) {
			saveDefaultConfig();
			
		}
	}
	String Prefix =null;
	String KazanmaMesaji=null;
	String KaybetmeMesaji=null;
	String HerkeseMesaj=null;
	String MarketIsmi=null;
	String AlmaMesaji = null;
	String YetersizPara =null;
	public void configFix() {
		Prefix = getConfig().getString("SihirliSu." + "Ayarlar." + "Prefix");
		KaybetmeMesaji = getConfig().getString("SihirliSu." + "Ayarlar." + "KaybetmeMesaji");
		MarketIsmi = getConfig().getString("SihirliSu." + "Ayarlar." + "MarketIsmi");
		MarketIsmi = MarketIsmi.replace("&", "§");
		if(Prefix.contains("&")) {
			Prefix = Prefix.replace("&", "§");
		}
		AlmaMesaji = getConfig().getString("SihirliSu." + "Ayarlar." + "AlmaMesaji");
		YetersizPara = getConfig().getString("SihirliSu." + "Ayarlar." + "YetersizPara");
		YetersizPara = YetersizPara.replace("&", "§");
		HerkeseMesaj = getConfig().getString("SihirliSu." + "Ayarlar." + "HerkeseMesaj");
	
		
		
	}
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	@EventHandler
	public void sihirliSu(PlayerItemConsumeEvent event) {
		if(event.getItem().getType().equals(Material.POTION)) {
			for(String su : getConfig().getConfigurationSection("SihirliSu." + "Sular").getKeys(false)) {
				if(event.getItem().getItemMeta().getDisplayName().equals(getConfig().getString("SihirliSu." + "Sular." + su + ".Su_Ismi").toString().replace("&", "§"))) {
					int sans;
					sans = getConfig().getInt("SihirliSu." + "Sular." + su + ".Sans");
					if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE) ) {
						ItemStack item = new ItemStack(Material.GLASS_BOTTLE);
						event.setItem(item);
						
					}
					
					int randomSayi = new Random().nextInt(100);
					if(randomSayi <= sans) {
						int Fiyat = getConfig().getInt("SihirliSu." + "Sular." + su + ".Fiyat");
						economy.depositPlayer(event.getPlayer(),Fiyat * 2);
						KazanmaMesaji = getConfig().getString("SihirliSu." + "Ayarlar." + "KazanmaMesaji");
						KazanmaMesaji = KazanmaMesaji.replace("%suIsmi%", getConfig().get("SihirliSu." + "Sular." + su + ".Su_Ismi").toString());
						KazanmaMesaji = KazanmaMesaji.replace("%odul%", Fiyat * 2 + "" );
						KazanmaMesaji = KazanmaMesaji.replace("&", "§");
						event.getPlayer().sendMessage(KazanmaMesaji);
						if(getConfig().getBoolean("SihirliSu." + "Sular." + su + ".KazanincaHerkeseMesaj")) {
							HerkeseMesaj = HerkeseMesaj.replace("%odul%", Fiyat * 2 + "" );
							HerkeseMesaj = HerkeseMesaj.replace("%player%", event.getPlayer().getName() );
							HerkeseMesaj = HerkeseMesaj.replace("%suIsmi%", getConfig().get("SihirliSu." + "Sular." + su + ".Su_Ismi").toString());
							HerkeseMesaj = HerkeseMesaj.replace("&", "§");
							getServer().broadcastMessage(Prefix + HerkeseMesaj);
						}
						return;
					}
					else {
						int Fiyat = getConfig().getInt("SihirliSu." + "Sular." + su + ".Fiyat");
						KaybetmeMesaji = getConfig().getString("SihirliSu." + "Ayarlar." + "KaybetmeMesaji");
						KaybetmeMesaji = KaybetmeMesaji.replace("%suIsmi%", getConfig().get("SihirliSu." + "Sular." + su + ".Su_Ismi").toString());
						KaybetmeMesaji = KaybetmeMesaji.replace("%odul%", Fiyat * 2 + "" );
						KaybetmeMesaji = KaybetmeMesaji.replace("&", "§");
						event.getPlayer().sendMessage(KaybetmeMesaji);
						event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 2), true);
						event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 2), true);
						return;
					}
				}
			}
			return;
		}
		
	}
	public static Economy getEconomy() {
        return economy;
    }
	@EventHandler
	public void onInven(InventoryClickEvent event) {
		if(event.getClickedInventory() == null) {
			return;
		}
		if(event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
			if(event.getWhoClicked().getOpenInventory().getTitle().toString().equals(MarketIsmi)) {
				event.setCancelled(true);
				return;
			}
		}
		if(event.getClickedInventory().getName().equals(MarketIsmi)) {
			event.setCancelled(true);
			if(event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) {
				event.setCancelled(true);
				
				return;
			}
			else {
				event.setCancelled(true);
				ItemStack TiklananItem = event.getCurrentItem();
				String TiklananItemIsim = event.getCurrentItem().getItemMeta().getDisplayName().toString();
				for(String su : getConfig().getConfigurationSection("SihirliSu." + "Sular").getKeys(false)) {
					if(TiklananItemIsim.equals(getConfig().getString("SihirliSu." + "Sular." + su + ".Su_Ismi").replace("&", "§"))) {
						int Fiyat = getConfig().getInt("SihirliSu." + "Sular." + su + ".Fiyat");
						AlmaMesaji = getConfig().getString("SihirliSu." + "Ayarlar." + "AlmaMesaji");
						AlmaMesaji = AlmaMesaji.replace("%suIsmi%", TiklananItemIsim);
						AlmaMesaji = AlmaMesaji.replace("%fiyat%", Fiyat + "" );
						AlmaMesaji = AlmaMesaji.replace("&", "§");
						if(economy.getBalance((OfflinePlayer) event.getWhoClicked()) >= Fiyat) {
							event.getWhoClicked().getInventory().addItem(TiklananItem);
							event.getWhoClicked().sendMessage(AlmaMesaji);
							economy.withdrawPlayer((OfflinePlayer) event.getWhoClicked(), Fiyat);
						}
						else {
							event.getWhoClicked().sendMessage(YetersizPara);
						}
					}
					

				}
			}
		}
		
	}
	int row = 0;
	int sayi = 0;
	public void count() {
		for(@SuppressWarnings("unused") String su : getConfig().getConfigurationSection("SihirliSu." + "Sular").getKeys(false) ) {
			sayi++;
		}
		if(sayi > 0 && sayi < 10) {
			row = 1 * 9;
			sayi = 0;
			return;
		}

		if(sayi > 9 && sayi < 19) {
			row = 2 * 9;
			sayi = 0;
			return;
		}
		if(sayi > 18 && sayi < 28) {
			row = 3 * 9;
			sayi = 0;
			return;
		}
		
	}
	@Override
	public boolean onCommand(CommandSender adam,Command cmd, String label, String[] args) {
		if(cmd.getName().equals("sihirlisu")) {
			if(!(adam instanceof Player)) {
	            return true;
			}
			if(args.length == 0) {
				adam.sendMessage(ChatColor.YELLOW + "Sihirli Su içerek ödediðin parayý ikiye katlayabilirsin.");
				adam.sendMessage(ChatColor.YELLOW + "Sihirli Su Marketi için " + ChatColor.GOLD + "/SihirliSu Market" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
				adam.sendMessage(ChatColor.YELLOW + "Admin komutlarý için " + ChatColor.GOLD + "/SihirliSu Admin" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
				adam.sendMessage(ChatColor.YELLOW + "Dosyalarý yenilemek için " + ChatColor.GOLD + "/SihirliSu Reload" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
				adam.sendMessage(ChatColor.YELLOW + "Yardým mesajý için " + ChatColor.GOLD + "/SihirliSu Yardým" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
				return false;
			}
			
			
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("market")) {
					count();
					Inventory i;
					if(Bukkit.getServer().getVersion().contains("1.8")) {
						 if (MarketIsmi == null) {
							 MarketIsmi = "§cSihirli Su Marketi";
							 
						 }
						 
						 i = Bukkit.createInventory(null, 9, MarketIsmi);
					}
					else {
						i = getServer().createInventory(null, row,MarketIsmi);
					}
					
					i.clear();
					if (adam instanceof Player) {
						for(String su : getConfig().getConfigurationSection("SihirliSu." + "Sular").getKeys(false)) {
							String isim = getConfig().getString("SihirliSu." + "Sular." + su + ".Su_Ismi");
							isim = isim.replace("&", "§");
							int Fiyat = getConfig().getInt("SihirliSu." + "Sular." + su + ".Fiyat");
							int Sans = getConfig().getInt("SihirliSu." + "Sular." + su + ".Sans");
							isim =isim.replace("%fiyat%", Fiyat + "");
							isim =isim.replace("%ödül%", Fiyat * 2 + "");
							isim =isim.replace("%odul%", Fiyat * 2 + "");
							isim =isim.replace("%sans%", Sans + "");
							isim =isim.replace("%Sans%", Sans + "");
							ItemStack esya = new ItemStack(Material.POTION);
							ItemMeta meta = esya.getItemMeta();
							meta.setDisplayName(isim);
							ArrayList<String> lore = new ArrayList<>();
							if(getConfig().getStringList("SihirliSu." + "Sular." + su + ".Lore") !=null) {
								for(String loreler : getConfig().getStringList("SihirliSu." + "Sular." + su + ".Lore")) {
									loreler = loreler.replace("&", "§");
									loreler =loreler.replace("%fiyat%", Fiyat + "");
									loreler =loreler.replace("%ödül%", Fiyat * 2 + "");
									loreler =loreler.replace("%odul%", Fiyat * 2 + "");
									loreler =loreler.replace("%sans%", Sans + "");
									loreler =loreler.replace("%sans%", Sans + "");
									lore.add(loreler + "");
								}
							}
							meta.setLore(lore);
							meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
							meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
							esya.setItemMeta(meta);
							i.addItem(esya);
							((Player) adam).openInventory(i);
				
						}
					}
					return false;
				}
				if(args[0].equalsIgnoreCase("al")) {
					adam.sendMessage(Prefix + ChatColor.YELLOW + " Doðru kullaným /sihirlisu al SuÝsmi");
					return false;
				}
				if(args[0].equalsIgnoreCase("help") ||args[0].equalsIgnoreCase("yardým")||args[0].equalsIgnoreCase("yardim")) {
					adam.sendMessage(ChatColor.YELLOW + "Sihirli Su içerek ödediðin parayý ikiye katlayabilirsin.");
					adam.sendMessage(ChatColor.YELLOW + "Sihirli Su Marketi için " + ChatColor.GOLD + "/SihirliSu Market" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
					adam.sendMessage(ChatColor.YELLOW + "Admin komutlarý için " + ChatColor.GOLD + "/SihirliSu Admin" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
					adam.sendMessage(ChatColor.YELLOW + "Dosyalarý yenilemek için " + ChatColor.GOLD + "/SihirliSu Reload" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
					adam.sendMessage(ChatColor.YELLOW + "Yardým mesajý için " + ChatColor.GOLD + "/SihirliSu Yardým" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
					return false;
				}
				if(args[0].equalsIgnoreCase("reload")) {
					if(adam.hasPermission("sihirlisu.admin")) {
						reloadConfig();
						saveDefaultConfig();
						adam.sendMessage(Prefix + ChatColor.YELLOW + " Tüm dosyalar yenilendi!");
						return false;
					}
				}
				if(args[0].equalsIgnoreCase("admin")) {
					if(adam.hasPermission("sihirlisu.admin")) {
						adam.sendMessage(ChatColor.YELLOW + "Bir oyuncuya su vermek için " + ChatColor.GOLD + "/SihirliSu Admin Ver Ýsim SuÝsmi" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
						return false;
					}
				}
				
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("al")) {
					for(String su : getConfig().getConfigurationSection("SihirliSu." + "Sular").getKeys(false)) {
						if(args[1].equalsIgnoreCase(su)) {
							int Fiyat = getConfig().getInt("SihirliSu." + "Sular." + su + ".Fiyat");
							if(economy.getBalance((OfflinePlayer) adam) >= Fiyat) {
								AlmaMesaji = AlmaMesaji.replace("%suIsmi%", getConfig().get("SihirliSu." + "Sular." + su + ".Su_Ismi").toString());
								AlmaMesaji = AlmaMesaji.replace("%fiyat%", getConfig().getInt("SihirliSu." + "Sular." + su + ".Fiyat") + "" );
								AlmaMesaji = AlmaMesaji.replace("&", "§");
								((HumanEntity) adam).sendMessage(AlmaMesaji);
								economy.withdrawPlayer((OfflinePlayer) ((HumanEntity) adam), Fiyat);
							}
							else {
								adam.sendMessage(YetersizPara);
								return false;
							}
							String isim = getConfig().getString("SihirliSu." + "Sular." + su + ".Su_Ismi");
							isim = isim.replace("&", "§");
							int Sans = getConfig().getInt("SihirliSu." + "Sular." + su + ".Sans");
							isim =isim.replace("%fiyat%", Fiyat + "");
							isim =isim.replace("%ödül%", Fiyat * 2 + "");
							isim =isim.replace("%odul%", Fiyat * 2 + "");
							isim =isim.replace("%sans%", Sans + "");
							isim =isim.replace("%sans%", Sans + "");
							ItemStack esya = new ItemStack(Material.POTION);
							ItemMeta meta = esya.getItemMeta();
							meta.setDisplayName(isim);
							ArrayList<String> lore = new ArrayList<>();
							if(getConfig().getStringList("SihirliSu." + "Sular." + su + ".Lore") !=null) {
								for(String loreler : getConfig().getStringList("SihirliSu." + "Sular." + su + ".Lore")) {
									loreler = loreler.replace("&", "§");
									loreler =loreler.replace("%fiyat%", Fiyat + "");
									loreler =loreler.replace("%ödül%", Fiyat * 2 + "");
									loreler =loreler.replace("%odul%", Fiyat * 2 + "");
									loreler =loreler.replace("%sans%", Sans + "");
									loreler =loreler.replace("%sans%", Sans + "");
									lore.add(loreler + "");
								}
							}
							meta.setLore(lore);
							meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
							meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
							esya.setItemMeta(meta);
							adam.getServer().getPlayer(adam.getName()).getInventory().addItem(esya);
							return false;
						}
					}
				}
			}
			if(args.length == 4) {
				if(args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("ver") && getServer().getPlayerExact(args[2]) != null) {
					for(String su : getConfig().getConfigurationSection("SihirliSu." + "Sular").getKeys(false)) {
						if(su.equals(args[3])) {
							if(adam.hasPermission("sihirlisu.admin")) {
								int Fiyat = getConfig().getInt("SihirliSu." + "Sular." + su + ".Fiyat");
								String isim = getConfig().getString("SihirliSu." + "Sular." + su + ".Su_Ismi");
								isim = isim.replace("&", "§");
								int Sans = getConfig().getInt("SihirliSu." + "Sular." + su + ".Sans");
								isim =isim.replace("%fiyat%", Fiyat + "");
								isim =isim.replace("%ödül%", Fiyat * 2 + "");
								isim =isim.replace("%odul%", Fiyat * 2 + "");
								isim =isim.replace("%sans%", Sans + "");
								isim =isim.replace("%sans%", Sans + "");
								ItemStack esya = new ItemStack(Material.POTION);
								ItemMeta meta = esya.getItemMeta();
								meta.setDisplayName(isim);
								ArrayList<String> lore = new ArrayList<>();
								if(getConfig().getStringList("SihirliSu." + "Sular." + su + ".Lore") !=null) {
									for(String loreler : getConfig().getStringList("SihirliSu." + "Sular." + su + ".Lore")) {
										loreler = loreler.replace("&", "§");
										loreler =loreler.replace("%fiyat%", Fiyat + "");
										loreler =loreler.replace("%ödül%", Fiyat * 2 + "");
										loreler =loreler.replace("%odul%", Fiyat * 2 + "");
										loreler =loreler.replace("%sans%", Sans + "");
										loreler =loreler.replace("%sans%", Sans + "");
										lore.add(loreler + "");
									}
								}
								meta.setLore(lore);
								meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
								meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
								esya.setItemMeta(meta);
								getServer().getPlayerExact(args[2]).getInventory().addItem(esya);
								return false;
							}
							else {
								adam.sendMessage(ChatColor.RED + "Yetersiz yetki");
							}
						}
					}
				}
				
				
			}
			
			
			adam.sendMessage(ChatColor.YELLOW + "Sihirli Su içerek ödediðin parayý ikiye katlayabilirsin.");
			adam.sendMessage(ChatColor.YELLOW + "Sihirli Su Marketi için " + ChatColor.GOLD + "/SihirliSu Market" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
			adam.sendMessage(ChatColor.YELLOW + "Admin komutlarý için " + ChatColor.GOLD + "/SihirliSu Admin" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
			adam.sendMessage(ChatColor.YELLOW + "Dosyalarý yenilemek için " + ChatColor.GOLD + "/SihirliSu Reload" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
			adam.sendMessage(ChatColor.YELLOW + "Yardým mesajý için " + ChatColor.GOLD + "/SihirliSu Yardým" + ChatColor.YELLOW + " Komutunu kullanabilirsin");
		}
		return false;
	}

}
