package red.man10.man10hello;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public final class Man10hello extends JavaPlugin implements Listener {
    double tx;
    double ty;
    double tz;

    boolean enablebroad;
    boolean enableprefix;

    String bmessage;
    String prefix;

    ArrayList<String> playername = new ArrayList<String>();
    ArrayList<String> text = new ArrayList<String>();



    @Override
    public void onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig();
        createPlayerConfig();
        loadPlayerConfig();
        configReload();
        this.getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("man10 hello has been enabled");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("man10 hello has been disabled");
    }

    public void createPlayerConfig(){
        File txt = new File("plugins/Man10hello/player.txt");
        if (!txt.exists()) {
            try {
                txt.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPlayerConfig(){
        playername.clear();
        try {
            File file = new File("plugins/Man10hello/player.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()){
                playername.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void writePlayer(String pn){
        try {
            File file = new File("plugins/Man10hello/player.txt");
            FileWriter filewriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(filewriter);
            bw.write(pn);
            bw.write("\n");
            bw.close();
            configReload();
            loadPlayerConfig();
        }catch (IOException e){

        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("man10h")){
            if(args.length >= 1){
                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("reload")){
                        //config reload
                        configReload();
                        loadPlayerConfig();
                        p.sendMessage(prefix + " §a§lコンフィグがリロードされました。");
                    }else if(args[0].equalsIgnoreCase("settp")){
                        this.getConfig().set("tp.x", p.getLocation().getX());
                        this.getConfig().set("tp.y", p.getLocation().getY());
                        this.getConfig().set("tp.z", p.getLocation().getZ());
                        this.saveConfig();
                        p.sendMessage(prefix + "§a§lTPロケーションを設定しました。");
                        reloadConfig();

                    }else if(args[0].equalsIgnoreCase("help")){

                        p.sendMessage("§d==========§6[§5man10 hello§6]§d==========");
                        p.sendMessage("§a/man10h settp tpロケーションを設置");
                        p.sendMessage("§a/man10h reload コンフィグのリロード");
                        p.sendMessage("§a/man10h help コマンドリスト");
                        p.sendMessage("§d===============================");

                    }else{
                        p.sendMessage(prefix + "§c§lコマンドの使い方が間違ってます、/man10h help でコマンドの一覧が見れます。");
                    }
                }
            }
            if(args.length == 0){
                p.sendMessage(prefix + "§c§lコマンドの使い方が間違ってます、/man10h help でコマンドの一覧が見れます。");
            }
        }
        return true;
    }

    public void configReload(){
        this.reloadConfig();

        tx = this.getConfig().getDouble("tp.x");
        ty = this.getConfig().getDouble("tp.y");
        tz = this.getConfig().getDouble("tp.z");

        enablebroad = this.getConfig().getBoolean("message.enable_broadcast");
        enableprefix = this.getConfig().getBoolean("message.enable_prefix");

        bmessage = this.getConfig().getString("message.message").replaceAll("&", "§");
        prefix = this.getConfig().getString("message.prefix").replaceAll("&", "§");

        text.clear();
        for(String s : this.getConfig().getStringList("chat_trigger")){
            text.add(s);
        }


    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        for(String t : text) {
            if (e.getMessage().contains(t)) {
                if (playername.contains(p.getName())) {

                } else {
                    Location l = new Location(p.getWorld(), tx, ty, tz);
                    if (enablebroad == true) {
                        if (enableprefix == true) {
                            Bukkit.getServer().broadcastMessage(prefix + bmessage.replaceAll("%PLAYER%", p.getName()));
                            p.teleport(l);

                        } else {
                            Bukkit.getServer().broadcastMessage(bmessage.replaceAll("%PLAYER%", p.getName()));
                            p.teleport(l);
                        }
                    } else {
                        p.teleport(l);
                    }
                    writePlayer(p.getName());
                }
            }
        }

    }


}
