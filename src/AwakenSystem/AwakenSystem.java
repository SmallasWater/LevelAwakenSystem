package AwakenSystem;

//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//               佛祖保佑         永无BUG
//     
import AwakenSystem.data.baseAPI;
import AwakenSystem.data.getFiles;
import AwakenSystem.data.uiAPI;
import AwakenSystem.task.*;
import AwakenSystem.utils.Commands;
import AwakenSystem.utils.adminCommand;
import AwakenSystem.utils.createCommand;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.DummyBossBar;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

/*
 *   _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |     __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

    @author 若水

 */

public class AwakenSystem extends PluginBase{

    private static AwakenSystem Main;
    public HashMap<Player,Boolean> canPVP = new HashMap<>();
    public static boolean loadMoney = false;
    public HashMap<Player,DummyBossBar> bar = new HashMap<>();
    HashMap<Player,Integer> defaultMaxHealth = new HashMap<>();
    LinkedHashMap<Player,String> Awaken = new LinkedHashMap<>();//用作临时存储觉醒属性
    LinkedHashMap<Player,baseAPI.ModalType> modaltype = new LinkedHashMap<>();
    public LinkedHashMap<Player,LinkedHashMap<String,LinkedHashMap<baseAPI.ItemADDType,int[]>>> runAdd = new LinkedHashMap<>();


    public void onLoad(){
        Server.getInstance().getLogger().info("[LevelAwakenSystem] 启动 等级觉醒插件 ");
        for(String i: getFiles.Default_First_Name){
            File file = new File(this.getDataFolder()+"/Players/"+i);
            if(!file.exists()){
                if(!file.mkdirs()){
                    Server.getInstance().getLogger().warning("玩家文件初始化失败");
                }
            }
        }
        File rpg = new File(this.getDataFolder()+"/RPG");
        if(!rpg.exists()){
            if(!rpg.mkdirs()){
                Server.getInstance().getLogger().warning("RPG资源初始化失败");
            }
        }
        File items = new File(this.getDataFolder()+"/RPG/Items");
        if(!items.exists()){
            if(!items.mkdirs()){
                Server.getInstance().getLogger().warning("物品资源初始化失败");
            }
        }
    }

    @Override
    public void onEnable() {
        Main = this;
        new getFiles();
        new uiAPI();

        this.getLogger().info("开始加载等级觉醒插件 by--若水");
        this.getServer().getPluginManager().registerEvents(new PlayerEvents(),this);
        File config = new File(this.getDataFolder()+"/config.yml");
        if(!config.exists()){
            this.saveDefaultConfig();
            this.reloadConfig();
        }
        File att = new File(this.getDataFolder()+"/attribute.yml");
        if(!att.exists()){
            this.saveResource("attribute.yml");
        }

        this.getServer().getCommandMap().register("",new Commands("level"));
        this.getServer().getCommandMap().register("",new adminCommand("lac"));

        this.getServer().getCommandMap().register("",new createCommand("items"));
        //3秒后异步启动 看你怎么禁止
        this.getServer().getScheduler().scheduleDelayedTask(this,()->
                this.getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
                    public void onRun() {
                        Server.getInstance().getScheduler().scheduleRepeatingTask(new TipTask(),10);
                        Server.getInstance().getScheduler().scheduleRepeatingTask(new runTask(),20);
                        Server.getInstance().getScheduler().scheduleRepeatingTask(new getRingTask(),10);
                        Server.getInstance().getScheduler().scheduleRepeatingTask(new fixToInventoryTask(),10);
                        Server.getInstance().getScheduler().scheduleRepeatingTask(new showBossMessageTask(),10);
                    }
        }), 60);
    }

    public Config getAttributeConfig(){
        return new Config(this.getDataFolder()+"/attribute.yml",Config.YAML);
    }

    public Config getPlayerConfig(Player player){
        return getPlayerConfig(player.getName());
    }

    public Config getPlayerConfig(String player){
        if(new File(this.getPlayerFileName(player)).exists()){
            return new Config(this.getPlayerFileName(player),Config.YAML);
        }else{
            LinkedHashMap<String,Object> conf = getFiles.initPlayer();
            Config config = new Config(this.getPlayerFileName(player),Config.YAML);
            config.setAll(conf);
            config.save();
            return getPlayerConfig(player);

        }
    }

    public Config getItemConfig(String itemName){return new Config(this.getDataFolder()+"/RPG/"+itemName+".yml",Config.YAML);}

    public Config getCommandItemConfig(String itemName){return new Config(this.getDataFolder()+"/RPG/Items/"+itemName+".yml",Config.YAML);}

    public String getPlayerFileName(Player player){
        return this.getPlayerFileName(player.getName());
    }

    private String getPlayerFileName(String player){
        for(String i: getFiles.Default_First_Name)
        {
            if(i.equals(player.substring(0,1).toLowerCase())){
                return this.getDataFolder()+"/Players/"+i+"/"+player+".yml";
            }
        }
        return this.getDataFolder()+"/Players/#/"+player+".yml";
    }

    public static AwakenSystem getMain() {
        return Main;
    }



}
