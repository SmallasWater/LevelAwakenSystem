package AwakenSystem.data;


import AwakenSystem.AwakenSystem;


import AwakenSystem.events.*;
import AwakenSystem.lib.removeItem.CommandItems;
import AwakenSystem.utils.nbtItems;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.*;

/*
  _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |     __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|
 */
/**
 @author 若水
 此类是一个接口 可以实现各种调用方法
 枚举详解:
 AwakenSystem.data.baseAPI.PlayerConfigType  玩家的属性非数值加成 经验 等级 天赋...
 AwakenSystem.data.baseAPI.ItemADDType 物品增幅的数据
 */
public class defaultAPI implements baseAPI{


    /*     players   */

    /**
     * 获取玩家的整数属性 使用 AwakenSystem.data.baseAPI.PlayerConfigType
     * @param player 玩家名称
     * @param type 枚举类型 参考 baseAPI.PlayerConfigType
     *             @return 玩家属性数值
     * */
    public static int getPlayerAttributeInt(String player, PlayerConfigType type){
        return AwakenSystem.getMain().getPlayerConfig(player).getInt(type.getName());
    }
    /**
     * 获取玩家属性最终加成 因为有Item影响 使用AwakenSystem.data.baseAPI.ItemADDType
     * @param player 玩家类型
     * @param type 枚举类型 参考 baseAPI
     *             @return 玩家最终属性数值
     * */
    public static int getPlayerFinalAttributeInt(Player player,ItemADDType type){
        int add = DamageMath.getAddPlayerAttribute(player,type);
        if(type == ItemADDType.EXP) return getPlayerAttributeInt(player.getName(),PlayerConfigType.EXP);
            return AwakenSystem.getMain().getPlayerConfig(player).getInt(type.getName()) + add;
    }
    /**
     * 获取玩家属性数据 使用AwakenSystem.data.baseAPI.PlayerAttType
     * @param player 玩家类型
     * @param type 枚举类型 参考 baseAPI
     *             @return 玩家属性数值
     * */
    public static int getPlayerAttributeInt(String player, PlayerAttType type){
        return AwakenSystem.getMain().getPlayerConfig(player).getInt(type.getName());
    }

    //获取玩家的配置  通过 PlayerConfigType
    /**
     * @param player 玩家
     * @param type 在枚举中的数据
     *             @return 玩家配置信息
     *
     *             获取玩家配置信息*/
    public static String getPlayerAttributeString(String player, PlayerConfigType type){
        return AwakenSystem.getMain().getPlayerConfig(player).getString(type.getName());
    }

    /**
     * @param player 玩家名称
     * @param type PlayerConfigType属性
     * @param value 设置的数值
     * 设置玩家 int 类型数据
     * */
    //设置玩家的属性 通过 PlayerConfigType
    public static void setPlayerAttributeInt(String player, PlayerConfigType type,int value){
        Config config = AwakenSystem.getMain().getPlayerConfig(player);
        config.set(type.getName(),value);
        config.save();
    }

    //设置玩家的属性 通过 PlayerAttType
    /**
     * @param player 玩家名称
     * @param type PlayerAttType属性
     * @param value 设置的数值
     * 设置玩家 int 类型数据
     * */
    public static void setPlayerAttributeInt(String player, PlayerAttType type,int value){
        Config config = AwakenSystem.getMain().getPlayerConfig(player);
        config.set(type.getName(),value);
        config.save();
    }
    /**
     * @param player 玩家名称
     * @param type PlayerConfigType属性
     * @param value 设置的数值
     * 设置玩家 String 类型数据
     * */
    //设置玩家的配置 通过 PlayerConfigType
    public static void setPlayerAttributeString(String player, PlayerConfigType type,String value){
        Config config = AwakenSystem.getMain().getPlayerConfig(player);
        config.set(type.getName(),value);
        config.save();
    }
    /**
     * @param player 玩家名称
     * @param type PlayerConfigType属性
     * @param value 设置的数值
     * 减少玩家 int 类型数据
     * */
    //减少玩家属性
    public static void removePlayerAttributeInt(String player,PlayerConfigType type,int value){
        int playerInt = getPlayerAttributeInt(player,type);
        if(playerInt >= value){
            setPlayerAttributeInt(player,type,playerInt - value);
        }else{
            setPlayerAttributeInt(player,type,0);
        }
    }
    /**
     * @param player 玩家名称
     * @param type PlayerAttType 属性
     * @param value 设置的数值
     * 设置玩家 int 类型数据
     * */
    //减少玩家属性
    public static void removePlayerAttributeInt(String player,PlayerAttType type,int value){
        int playerInt = getPlayerAttributeInt(player,type);
        if(playerInt >= value){
            setPlayerAttributeInt(player,type,playerInt - value);
        }else{
            setPlayerAttributeInt(player,type,0);
        }
    }


    /**
     * @param player 玩家名称
     * @param type PlayerAttType 属性
     * @param value 设置的数值
     * 增加玩家 int 类型数据
     * */
    //给玩家添加数值 通过 PlayerAttType
    public static void addPlayerAttributeInt(String player,PlayerAttType type,int value){
        setPlayerAttributeInt(player,type,getPlayerAttributeInt(player,type) + value);
    }

    /**
     * @param player 玩家名称
     * @param type PlayerConfigType 属性
     * @param value 设置的数值
     * 增加玩家 int 类型数据
     * */

    //设置玩家的配置 通过 PlayerConfigType
    public static void addPlayerAttributeInt(String player,PlayerConfigType type,int value){
        setPlayerAttributeInt(player,type,getPlayerAttributeInt(player,type) + value);
    }
    /**
     * @param player 玩家名称
     * @param type ItemADDType 属性
     * @param value 设置的数值
     * 增加玩家 int 类型数据
     * */

    //设置玩家的配置 通过 ItemADDType
    public static void addPlayerAttributeInt(String player,ItemADDType type,int value){
        Config config = AwakenSystem.getMain().getPlayerConfig(player);
        if(type.getName().equals(ItemADDType.EXP.getName())){
            int a = config.getInt(PlayerConfigType.EXP.getName()) + value;
            config.set(PlayerConfigType.EXP.getName(),a);
        }else{
            int a = config.getInt(type.getName()) + value;
            config.set(type.getName(),a);
        }

        config.save();
    }
    /**
     * @param itemName 小道具名称
     * @param type 小道具 ItemType 属性 (不包括消耗)
     * @return 小道具属性
     * */

    //获取小道具配置
    public static String getItemConfigString(String itemName,ItemType type){
            return AwakenSystem.getMain().getItemConfig(itemName).getString(type.getName());
    }

    /**
     * @param itemName 小道具名称
     * @param type 小道具 ItemADDType 属性 (不包括消耗)
     * @return 小道具属性
     * */
    //获取小道具 属性
    public static String getItemConfigString(String itemName,ItemADDType type){
            return AwakenSystem.getMain().getItemConfig(itemName).getString(type.getName());
    }


    //判断属性是否克制 R为玩家元素 Res为敌方元素
    private static boolean is_Restraint(String R,String Restraint){
//        String R = getPlayerAttributeString(player, PlayerConfigType.ATTRIBUTE);
        HashMap<String,Object> map = (HashMap<String, Object>) AwakenSystem.getMain().getAttributeConfig().getAll();
        Map mm = (Map) map.get(R);
        List list = (List) mm.get(AttType.BE_RESTRAINED.getName());
        return list.contains(Restraint);

    }
    //获取物品列表
    private static LinkedList<Item> getItems(List list){
        LinkedList<Item> items = new LinkedList<>();
        if(list.size() > 0) {
            for (Object string : list) {
                String s = String.valueOf(string);
                String exits = s.split("&")[0];
                switch (s.split("&")[1]) {
                    case "remove":
                        String name = exits.split(":")[0];
                        int c = 1;
                        if (exits.split(":").length == 2) {
                            c = Integer.parseInt(exits.split(":")[1]);
                        }
                        if (CommandItems.is_Exit(name)) {
                            items.add(CommandItems.getInstance(name).getItem(c));
                        }
                        break;
                    case "item":
                        name = exits.split(":")[0];
                        int id;
                        int damage = 0;
                        int count = 1;
                        if (name.split(":").length < 3 && name.split(":").length >= 2) {
                            id = Integer.parseInt(name.split(":")[0]);
                            damage = Integer.parseInt(name.split(":")[1]);
                        } else if (name.split(":").length < 2) {
                            id = Integer.parseInt(name.split(":")[0]);
                        } else {
                            id = Integer.parseInt(name.split(":")[0]);
                            damage = Integer.parseInt(name.split(":")[1]);
                            count = Integer.parseInt(name.split(":")[2]);
                        }
                        items.add(new Item(id, damage, count));
                        break;
                }
            }
            return items;
        }
        return null;
    }

    /**
     * @param att 属性名称
     * @return 返回 Item 物品
     * 获取重置属性消耗的材料
     * */
    //获取重置需要材料
    public static LinkedList<Item> getRemoveItem_Awaken(String att){
        HashMap map = getAwakenConfig(att);
        if(map.containsKey(AttType.reloadItem.getName())){
            return getItems((List) map.get(AttType.reloadItem.getName()));
        }
        return null;
    }
    /**
     * @param att 属性名称
     * @return 返回 Item 物品
     * 获取觉醒属性消耗的材料
     * */

    //获取觉醒需要材料
    public static LinkedList<Item> getUseItem_Awaken(String att){
        HashMap map = getAwakenConfig(att);
        if(map.containsKey(AttType.useItem.getName())){
            return getItems((List) map.get(AttType.useItem.getName()));
        }
        return null;
    }

    /**
     * @param level 玩家评级
     * @return 返回 Item 物品
     * 获取进阶评级需要物品
     * */
    //获取进阶评级需要物品
    public static LinkedList<Item> getUpScore(int level){
        HashMap map = (HashMap) AwakenSystem.getMain().getConfig().get(ConfigType.ADDItems.getName());
        if(map != null){
            if(map.containsKey(level)){
                return getItems((List) map.get(level));
            }
        }
        return null;
    }

    /**
     * @param awaken 属性名称
     *
     * @return 返回Button Image数组*/
    //获取属性图片
    static LinkedHashMap<String,Object> getImage_Awaken(String awaken){
        HashMap map = getAwakenConfig(awaken);
        LinkedHashMap<String,Object> image = new LinkedHashMap<>();
        if(map.containsKey(AttType.ButtonImage.getName())){
            HashMap map2 = (HashMap) map.get(AttType.ButtonImage.getName());
            if(map2.containsKey(AttType.Path.getName())){
                image.put("type",map2.get(AttType.Path.getName()));
            }else{
                image.put("type","data");
            }
            if(map2.containsKey(AttType.Data.getName())){
                image.put("data",map2.get(AttType.Data.getName()));
            }else{
                image.put("data","");
            }
            return image;

        }

        return null;
    }

    /**
     * @param player 玩家名称
     * @param Restraint 目标元素
     * 判断属性是否被克制
     * */
    //
      static boolean is_BeRestraint(String player, String Restraint){
      //  return is_Restraint(player,Restraint);
        String R = getPlayerAttributeString(player, PlayerConfigType.ATTRIBUTE);
        return is_Restraint(R,Restraint);

    }

    /**
     * @param player 玩家名称
     * @param Restraint 目标元素
     * 判断玩家是否克制该属性
     * */
    //判断玩家是否克制该属性
    static boolean is_R(String player, String Restraint){
        String R = getPlayerAttributeString(player, PlayerConfigType.ATTRIBUTE);
        return is_Restraint(Restraint,R);
    }

    /**
     * @param player 玩家名
     * @return 获取玩家升级增幅属性*/

    // UpData
    //获取升级增幅 全部 包括属性
    public static HashMap<PlayerAttType,Integer> upDataAddAttribute(String player){
        HashMap<PlayerAttType,Integer> upData = new HashMap<>();
        int type = getPlayerAttributeInt(player, PlayerConfigType.TALENT);
        for (PlayerAttType type1: PlayerAttType.values()){
            upData.put(type1,(new Random().nextInt((getUpDtaRandom(type)[1] - getUpDtaRandom(type)[0]) + 1) + getUpDtaRandom(type)[0]));
        }
        if(!getPlayerAttributeString(player, PlayerConfigType.ATTRIBUTE).equals("null")){
            String types = getPlayerAttributeString(player, PlayerConfigType.ATTRIBUTE);
            if(getAttConfigUpData(types) != null){
                for(PlayerAttType add:getAttConfigUpData(types).keySet()){
                    upData.put(add, upData.get(add) + getAttConfigUpData(types).get(add));
                }
            }
        }
        return upData;
    }


    //type 为属性获取升级增加元素属性
    private static HashMap<PlayerAttType,Integer> getAttConfigUpData(String type){
        HashMap<PlayerAttType,Integer> count = new HashMap<>();
        if(AwakenSystem.getMain().getAttributeConfig().get(type) != null){
             HashMap<String,Object> map = (HashMap<String, Object>) AwakenSystem.getMain().getAttributeConfig().getAll();
             Map mm = (Map) map.get(type);
             Map m = (Map) mm.get(AttType.UPDATA.getName());
             for(PlayerAttType ignored :PlayerAttType.values()){
                 if(m.containsKey(ignored.getName())){
                     count.put(ignored,(Integer) m.get(ignored.getName()));
                 }
             }
        }
        return count;
    }

    /**
     * @param player 玩家
     * @param exp 经验值
     * 给玩家添加经验
     * */
    public static void addExp(Player player,float exp){
        if(DamageMath.getAddPlayerAttribute(player,ItemADDType.EXP) > 0){
            exp += Float.parseFloat(String.valueOf(exp * DamageMath.getAddPlayerAttribute(player,ItemADDType.EXP) / 100));
        }
        PlayerAddExpEvent event = new PlayerAddExpEvent(player,exp);
        Server.getInstance().getPluginManager().callEvent(event);
    }

    /**
     * @param player 玩家
     * @param score 提升的评级数
     * 给玩家添加评级
     * */

    //升级评级API
    public static void addScore(Player player,int score){
        int count = getPlayerAttributeInt(player.getName(),PlayerConfigType.TALENT) + score;
        if(getChatMessageAll().size() >= count){
            setPlayerAttributeInt(player.getName(),PlayerConfigType.TALENT,count);
        }
    }

    /**
     * @param player 玩家
     * @param damageW 物理攻击
     * @param damageF 法术攻击
     * 给玩家添加攻击
     * */
    public static void addPlayerAttack(Player player,Entity entity,float damageW,float damageF){
        PlayerAttackEvent event = new PlayerAttackEvent(player,entity,EntityDamageByEntityEvent.DamageCause.MAGIC,damageW,damageF,0.3F);
        Server.getInstance().getPluginManager().callEvent(event);
    }
    /**
     * @param player 玩家
     * @param damageW 物理攻击
     * @param damageF 法术攻击
     * @param cause 原因
     * 给玩家添加攻击
     * */
    public static void addPlayerAttack(Player player, Entity entity, EntityDamageEvent.DamageCause cause, float damageW, float damageF,float knock){
        PlayerAttackEvent event = new PlayerAttackEvent(player,entity,cause,damageW,damageF,knock);
        Server.getInstance().getPluginManager().callEvent(event);
    }

    /**
     * @param player 玩家
     * @param level 提升的等级
     * 给玩家升级
     * */
    //升级API
    public static void addLevel(Player player,int level){
        for(int i = 1;i <= level;i++){
            PlayerLevelUpEvent event = new PlayerLevelUpEvent(player,getPlayerAttributeInt(player.getName(),PlayerConfigType.LEVEL),1,false);
            Server.getInstance().getPluginManager().callEvent(event);
        }
    }

    /**
     * @return 获取全部属性
     * */

    //获取全部的元素
    public static List<String> getAttTypeAll(){
        List<String> strings = new LinkedList<>();
        HashMap<String,Object> map = (HashMap<String, Object>) AwakenSystem.getMain().getAttributeConfig().getAll();
        strings.addAll(map.keySet());
        return strings;
    }


    //获取元素介绍
    /**@param att 属性名称
     * @return 获取属性介绍
     * */
    public static String getMessages(String att){
        return (String) getAwakenConfig(att).get(AttType.MESSAGE.getName());
    }

    /**
     *
     * @param player 玩家
     * @param oldAtt 旧属性
     * @param newAtt 新属性
     * 玩家进阶属性
     * */
    //启动元素进阶事件
    public static void startUpAwaken(Player player,String oldAtt,String newAtt){
        PlayerUpAwakenEvent event = new PlayerUpAwakenEvent(player,oldAtt,newAtt);
        Server.getInstance().getPluginManager().callEvent(event);
    }
    /**
     *
     * @param player 玩家
     * 玩家重置属性
     * */
    //启动元素重置事件
    public static void startResetAwaken(Player player){
        PlayerResetAwakenEvent event = new PlayerResetAwakenEvent(player,
                defaultAPI.getPlayerAttributeString(player.getName(),PlayerConfigType.ATTRIBUTE));
        Server.getInstance().getPluginManager().callEvent(event);
    }
    /**
     *
     * @param player 玩家
     * @param att 属性
     * 玩家觉醒属性
     * */
    //启动元素觉醒事件
    public static void startNewAwaken(Player player,String att){
        PlayerAwakenEvent event = new PlayerAwakenEvent(player,att);
        Server.getInstance().getPluginManager().callEvent(event);
    }

    /**
     *
     * @param att 属性
     * @return 获取属性配置
     * */
    //获取元素配置
    static HashMap getAwakenConfig(String att){
        return (HashMap) AwakenSystem.getMain().getAttributeConfig().get(att);
    }
    /**
     *
     * @param att 属性
     * @return 获取元素进阶元素
     * */
    //获取元素进阶元素
    public static String getUpDataAwaken(String att){
        return (String) getAwakenConfig(att).get(AttType.UpAtt.getName());
    }
    /**
     *
     * @param att 属性
     * @return 获取元素学习等级
     * */
    //获取元素学习等级
    public static int getStudyLevel(String att){
        return (Integer) getAwakenConfig(att).get(AttType.useLevel.getName());
    }

    /**
     *
     * @param player 玩家
     * @param string 未转义字符串
     * @return 返回转义字符串
     * */
    //转义字符
    public static String getStr_replace(Player player,String string){
        Item item = player.getInventory().getItem(35);
        String add = null;
        if(nbtItems.can_use(player,item)){
            add = nbtItems.getName(item);
        }
        boolean pvp = false;
        if(AwakenSystem.getMain().canPVP.containsKey(player)){
            pvp = AwakenSystem.getMain().canPVP.get(player);
        }
        Item hand = player.getInventory().getItemInHand();
        string = string.replace("{属性}",
                (defaultAPI.getPlayerAttributeString(player.getName(),PlayerConfigType.ATTRIBUTE).equals("null"))?"无属性":
                        defaultAPI.getPlayerAttributeString(player.getName(),PlayerConfigType.ATTRIBUTE)).
                replace("{name}",player.getName()).
                replace("{天赋}",getChatBySetting(player.getName())).
                replace("{换行}","\n").
                replace("{level}",String.valueOf(defaultAPI.getPlayerAttributeInt(player.getName(),PlayerConfigType.LEVEL))).
                replace("{h}",String.valueOf(player.getHealth())).
                replace("{mh}",String.valueOf(player.getMaxHealth())).
                replace("{exp}",String.valueOf(defaultAPI.getPlayerAttributeInt(player.getName(),PlayerConfigType.EXP))).
                replace("{mexp}",String.valueOf(DamageMath.getUpDataEXP(player))).
                replace("{dw}",String.valueOf(defaultAPI.getPlayerFinalAttributeInt(player,ItemADDType.DAMAGE_W))).
                replace("{df}",String.valueOf(defaultAPI.getPlayerFinalAttributeInt(player,ItemADDType.DAMAGE_F))).
                replace("{dlw}",String.valueOf(defaultAPI.getPlayerFinalAttributeInt(player,ItemADDType.DEFENSE_W))).
                replace("{dlf}",String.valueOf(defaultAPI.getPlayerFinalAttributeInt(player,ItemADDType.DEFENSE_F))).
                replace("{b}",String.valueOf(defaultAPI.getPlayerFinalAttributeInt(player,ItemADDType.CRriT))).
                replace("{kb}",String.valueOf(defaultAPI.getPlayerFinalAttributeInt(player,ItemADDType.ANTI_RIOT))).
                replace("{kx}",String.valueOf(defaultAPI.getPlayerFinalAttributeInt(player,ItemADDType.RESISTANCE))).
                replace("{c}",String.valueOf(defaultAPI.getPlayerFinalAttributeInt(player,ItemADDType.PENETRATION))).
                replace("{饰品}", add != null ? add : "无").
                replace("{pvp}",pvp?"§c敌对":"§a和平").
                replace("{id}",hand.getId()+"").
                replace("{damage}",hand.getDamage()+"");
        return string;

    }
    /**
     *
     * @return 获取全部评级名称 与对应 id
     * */
    //获取全部天赋名称 与对应 id
    static HashMap<Integer,String> getChatMessageAll(){
        HashMap<Integer,String> m = new HashMap<>();
        HashMap map = (HashMap) AwakenSystem.getMain().getConfig().get(ConfigType.SETTING.getName());
        int count = 0;
        for (Object string:map.keySet()){
            m.put(count,String.valueOf(string));
            count++;
        }
        return m;
    }
    /**
     * @param player 玩家名称
     * @return 获取玩家评级名称
     * */

    //获取玩家的天赋-String
    public static String getChatBySetting(String player){
        int type = getPlayerAttributeInt(player,PlayerConfigType.TALENT);
        try {
            return getChatMessageAll().get(type);
        }catch (Exception e){
            return "无";
        }
    }

    /**
     * @param type 评级
     * @return 获取玩家升级随机增加的属性范围(根据评级)
     * */
    //获取玩家升级随机增加的属性范围
    private static int[] getUpDtaRandom(int type){
        HashMap array = (HashMap) AwakenSystem.getMain().getConfig().get(ConfigType.SETTING.getName());
        ArrayList list = (ArrayList) array.get(getChatMessageAll().get(type));
        return new int[]{(int) list.get(0), (int) list.get(1)};
    }

    /**
     * @param player 玩家
     * @param name Buff名称
     * @param type buff的增益
     *  移除玩家的Buff增幅 单一增益
     * */

    //移除玩家的Buff增幅
    public static void removeBuffer(Player player,String name,ItemADDType type){
        PlayerBuffer buffer = new PlayerBuffer(player,name);
        AwakenSystem.getMain().runAdd.put(player,buffer.removeBuffer_Type(type));
    }

    /**
     * @param player 玩家
     * @param name Buff名称
     *  移除玩家的Buff增幅
     * */

    //移除玩家的Buff增幅
    public static void removeBuffer(Player player,String name){
        PlayerBuffer buffer = new PlayerBuffer(player,name);
        AwakenSystem.getMain().runAdd.put(player,buffer.removeBuffer());
    }

    /**
     * @param player 玩家
     * @param bufferName Buff名称
     * @return 获取玩家buff的增强效果与时间
     * */
    //获取玩家buff的增强效果与时间
    public static LinkedHashMap<ItemADDType,int[]> getPlayerBuff_Add(Player player,String bufferName){
        if(AwakenSystem.getMain().runAdd.containsKey(player)){
            if(AwakenSystem.getMain().runAdd.get(player).containsKey(bufferName))
                return AwakenSystem.getMain().runAdd.get(player).get(bufferName);
        }
        return null;
    }
    /**
     * @param player 玩家
     * @return 获取玩家所有的buff名称
     * */

    //获取玩家所有的buff名称
    public static String[] getBufferNames(Player player){
        ArrayList<String> list = new ArrayList<>();
        if(AwakenSystem.getMain().runAdd.containsKey(player)){
            list.addAll(AwakenSystem.getMain().runAdd.get(player).keySet());
        }
        return list.toArray(new String[list.size()]);
    }
    /**
     * @param player 玩家
     * @param buffname buff名称
     * @param type 增益类型
     * @return 获取玩家buff的增加属性
     * */


    //获取玩家buff的增加属性
    public static int getBufferAdd(Player player,String buffname,ItemADDType type){
        if(AwakenSystem.getMain().runAdd.containsKey(player)){
            if(AwakenSystem.getMain().runAdd.get(player).containsKey(buffname)){
                return AwakenSystem.getMain().runAdd.get(player).get(buffname).get(type)[0];
            }
        }
        return 0;
    }

    /**
     * @param player 玩家
     * @param buffname buff名称
     * @param type 增益类型
     * @return 获取玩家buff的增加属性的持续时间
     * */


    //获取玩家buff的增加属性的持续时间
    public static int getBufferTime(Player player,String buffname,ItemADDType type){
        if(AwakenSystem.getMain().runAdd.containsKey(player)){
            if(AwakenSystem.getMain().runAdd.get(player).containsKey(buffname)){
                return AwakenSystem.getMain().runAdd.get(player).get(buffname).get(type)[1];
            }
        }
        return 0;
    }

    /**
     * @param player 玩家
     * @param buffname buff名称
     * @param type 增益类型
     * @return 获取玩家buff的增加属性的冷却时间
     * */

    //获取玩家buff的增加属性的冷却时间
    public static int getBufferCold(Player player,String buffname,ItemADDType type){
        if(AwakenSystem.getMain().runAdd.containsKey(player)){
            if(AwakenSystem.getMain().runAdd.get(player).containsKey(buffname)){
                return AwakenSystem.getMain().runAdd.get(player).get(buffname).get(type)[2];
            }
        }
        return 0;
    }

    //给玩家添加Buff
    /**
     * @param player 玩家
     * @param name buff名称
     * @param buffer Buff类型与
     * 给玩家增加Buff
     * */
    public static void addPlayerBuffer(Player player,String name,LinkedHashMap<ItemADDType,int[]> buffer){
        PlayerAddBufferEvent event = new PlayerAddBufferEvent(player,name,buffer);
        Server.getInstance().getPluginManager().callEvent(event);
    }

    /**
     * @param player 玩家
     * 玩家身上显示粒子
     * */
    //显示粒子
    public static void showParticle(Player player){
        if(!defaultAPI.getPlayerAttributeString(player.getName(),PlayerConfigType.ATTRIBUTE).equals("null")){
            HashMap rgbs = (HashMap) AwakenSystem.getMain().getAttributeConfig().
                    get(defaultAPI.getPlayerAttributeString(player.getName(),PlayerConfigType.ATTRIBUTE));
            if(rgbs.containsKey(AttType.Particle.getName())){
                HashMap rgb = (HashMap) rgbs.get(AttType.Particle.getName());
                int r = (int) rgb.get(AttType.R.getName());
                int g = (int) rgb.get(AttType.G.getName());
                int b = (int) rgb.get(AttType.B.getName());
                for(int i = 0;i <= 20; i++){
                    ArrayList<double[]> pos = new ArrayList<double[]>(){
                        {
                            add(new double[]{1.1 * Math.cos(Math.toRadians(30.0)), 0.0 , 1.1 * Math.sin(Math.toRadians(30.0))});
                            add(new double[]{-1.1 * Math.cos(Math.toRadians(30.0)), 0.0 , -1.1 * Math.sin(Math.toRadians(30.0))});
                        }
                    };
                    for (double[] p:pos){
                        player.level.addParticle(new DustParticle(new Vector3(p[0] + player.x,p[1] + 1.2 + player.y,p[2] + player.z),r,g,b));
                    }
                }
            }
        }
    }
    /**
     *
     * @param player 玩家
     *               @return 可进阶属性
     *
     * 获取基础可以觉醒属性
     * 判定条件: 玩家为空属性
     * 属性觉醒等级小于等于玩家当前等级
     * 无属性可进阶
     * */
    public static LinkedList<String> getFinalAwaken(Player player){
        LinkedList<String> atts = new LinkedList<>();
        String att = defaultAPI.getPlayerAttributeString(player.getName(),PlayerConfigType.ATTRIBUTE);
        int level = defaultAPI.getPlayerAttributeInt(player.getName(),PlayerConfigType.LEVEL);
        if(att.equals("null")){//第一条
            for(String attName:getAttTypeAll()){
                if(getStudyLevel(attName) <= level){//第二条
                    if(is_finalAtt(attName)){
                        atts.add(attName);
                    }
                }
            }
        }
        return atts;
    }
    //判断是否为父属性
    private static boolean is_finalAtt(String att){
        for (String name:getAttTypeAll()){
            if(!name.equals(att)){
                String up = getUpDataAwaken(name);
                if(up != null){
                    if(up.equals(att))
                        return false;
                }
            }
        }
        return true;
    }


    //---技能栏---//

}
