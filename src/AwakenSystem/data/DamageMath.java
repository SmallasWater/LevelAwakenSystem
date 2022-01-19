package AwakenSystem.data;



import AwakenSystem.AwakenSystem;
import AwakenSystem.utils.nbtItems;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
/*
  _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |     __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|
 */
/**

 @author 若水
 这个类获取最终的伤害效果
 伤害 防御 等级 算法
 设定介绍: 物理攻击 玩家造成的伤害数值
 法术攻击: 当玩家拥有属性的时候额外造成的伤害
 物理防御: 玩家收到实体伤害后减免的伤害
 法术防御: 玩家减免的法术伤害
 暴击: 每 6点数值 增加 1%的暴击 几率 如果大于0 则为 1% 暴击增幅30%伤害
 抗暴: 减少敌方的暴击数值
 抗性: 每5点 增加 1 的魔法防御
 穿透: 每6 点减少敌方 1的物理防御

 #觉醒成功率: 此算法已写死 等级 + (设定 * 5)  不超过 80%
 # 物理伤害 (PVP)
 */

public class DamageMath implements baseAPI{
    private HashMap<String,Integer> cause = new HashMap<>();
    private Entity damager;
    private Entity entity;

    private static DamageMath math;

    public DamageMath(){
       math = this;
    }

    public static DamageMath getMath() {
        return math;
    }

    /**
     * @return 获取玩家 PVP 状态
     * */
    public HashMap<String,Integer> getCause() {
        return this.cause;
    }

    /**
     * 获取玩家之间PVP伤害
     * @param damager 攻击者
     * @param entity 被攻击者
     * @param damage 处理后的数据 从getDamage_byPVP获取
     * @return 最终伤害
     * */
    public int getDelDamage_byPVP(Player damager,Player entity,int[] damage){
        int damage_w = damage[0];
        int damage_f = damage[1];
        int delDamage = defaultAPI.getPlayerFinalAttributeInt(entity, baseAPI.ItemADDType.DEFENSE_W);
        if(delDamage > 0){
            damage_w -= defaultAPI.getPlayerFinalAttributeInt(entity, baseAPI.ItemADDType.DEFENSE_W);
        }
        if(damage_f > 0){
            int delDamage_F = defaultAPI.getPlayerFinalAttributeInt(entity, baseAPI.ItemADDType.DEFENSE_F);
            if(delDamage_F > 0){
                damage_f -= defaultAPI.getPlayerFinalAttributeInt(entity, baseAPI.ItemADDType.DEFENSE_F);
            }
        }
        if(!defaultAPI.getPlayerAttributeString(damager.getName(), baseAPI.PlayerConfigType.ATTRIBUTE).equals("null")){
            if(defaultAPI.getPlayerFinalAttributeInt(entity, baseAPI.ItemADDType.RESISTANCE) > 0){
                int c = defaultAPI.getPlayerFinalAttributeInt(damager, baseAPI.ItemADDType.RESISTANCE) / 5;// 抗性
                if(c > 0){
                    damage_f -= c;
                }
            }
            if(damage_f < 0){
                damage_f = 0;
            }
        }
        return damage_w + damage_f;
    }
    /**
     * @param damager 攻击者
     * @param entity 玩家
     * @param damage 初始攻击伤害
     *
     * 根据玩家属性对初始伤害进行加成
     *               @return 处理后的 物理攻击伤害 与 法术攻击伤害
     * */
    public int[] getDamage_byPVP(Player damager,Player entity,float[] damage){

            //damage物理伤害
            int damage_w_d = defaultAPI.getPlayerFinalAttributeInt(damager, baseAPI.ItemADDType.DAMAGE_W) + (int)damage[0];

            //damage法术伤害
            int damage_f_d = (int)damage[1];
            //damage暴击伤害
            // --- 伤害计算 ----//
            if(!defaultAPI.getPlayerAttributeString(damager.getName(), baseAPI.PlayerConfigType.ATTRIBUTE).equals("null")){
                //判断是否拥有属性
                damage_f_d += defaultAPI.getPlayerFinalAttributeInt(damager, baseAPI.ItemADDType.DAMAGE_F);
                if(!defaultAPI.getPlayerAttributeString(entity.getName(), baseAPI.PlayerConfigType.ATTRIBUTE).equals("null")){
                    if(defaultAPI.is_R(damager.getName(),defaultAPI.getPlayerAttributeString(entity.getName(), baseAPI.PlayerConfigType.ATTRIBUTE))){
                        this.cause.put("克制",(int)(damage_f_d * 0.2));
                    }else if(defaultAPI.is_BeRestraint(damager.getName(),defaultAPI.getPlayerAttributeString(entity.getName(), baseAPI.PlayerConfigType.ATTRIBUTE))){
                        this.cause.put("被克制",(int)(damage_f_d * 0.2));
                    }
                }
            }
            int cRrit = defaultAPI.getPlayerFinalAttributeInt(damager, baseAPI.ItemADDType.CRriT);
            int dRrit = defaultAPI.getPlayerFinalAttributeInt(entity,ItemADDType.CRriT);
            if(cRrit > 0){
                if(dRrit > 0){
                    cRrit -= dRrit;
                }
                if(cRrit > 0){
                    int c = cRrit / 6;
                    int damage_b_d = 0;
                    if(c >= 1){
                        damage_b_d = c;
                    }
                    if(damage_b_d >= new Random().nextInt(100) + 1){
                        this.cause.put("暴击",(int)(damage_w_d * 0.5));
                    }
                }
            }
            int penetration = defaultAPI.getPlayerFinalAttributeInt(damager, baseAPI.ItemADDType.PENETRATION);
            if(penetration > 0){
                int c = penetration / 6;
                int f = defaultAPI.getPlayerFinalAttributeInt(entity, baseAPI.ItemADDType.DEFENSE_W);
                if(c > 0){
                    f -= c;
                }
                if(f > 0){
                    damage_w_d += f;
                }
            }
            // --- 防御计算 ---//
            //个人建议 防御另算
            return new int[]{damage_w_d,damage_f_d};
    }
    /**
     * @param player 玩家
     * @return  升级所需经验
     * 获取玩家升级需要的经验
     * */
    //#等级算法（写死 ） (等级 * (等级 + 200) ) + 设定 * 100
    public static int getUpDataEXP(Player player){

        int type = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT);
        int level = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL);
        String str = AwakenSystem.getMain().levelUpMath;
        str = str.replace("{level}",level+"").replace("{pf}",type+"").replace(" ","");
        int exp;
        exp = (int) Calculator.conversion(str);
        return exp;
    }

    /**
     * @param player 玩家
     * @return 获取觉醒属性的成功率*/
    //获取觉醒成功率
    public static int getAwaken(Player player){

        int count = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.COUNT);
        int level = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL);
        int type = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT);
        return (100 - ((level + (type * 5)) > 99?100:(level + (type * 5))) + (count * 3)) <= 0 ?
                1 : (100 - ((level + (type * 5)) >= 99?99:(level + (type * 5))) + (count * 3));
    }


//   //开放接口 这个是除饰品与buff的额外增加
//    /**
//     * @param type 名称(避免重复)
//     *             @param player 玩家
//     *                           @param addTypeIntegerLinkedHashMap 增加的属性集合
//     *                                                              额外给玩家增加属性
//     * */
//
//    public static void addPlayerAttribute(String type,Player player,LinkedHashMap<ItemADDType,Integer> addTypeIntegerLinkedHashMap){
//        LinkedHashMap<Player,LinkedHashMap<ItemADDType,Integer>> add = new LinkedHashMap<>();
//        add.put(player,addTypeIntegerLinkedHashMap);
//        playerLinkedHashMapLinkedHashMap.put(type,add);
//    }
//
//    private static LinkedHashMap<String,LinkedHashMap<Player,LinkedHashMap<ItemADDType,Integer>>> playerLinkedHashMapLinkedHashMap = new LinkedHashMap<>();

    //获取所有额外加成
    /**
     * 获取单一属性加成
     * */
    static int getAddPlayerAttribute(Player player,ItemADDType type){
        int addAttribute = 0;
        Item item = player.getInventory().getItem(35);
        if(nbtItems.can_use(player,item)){//饰品
            String names = nbtItems.getName(item);
            if(nbtItems.getColde_by_Ring(names).containsKey(type)){
                addAttribute += nbtItems.getColde_by_Ring(names).get(type);
            }
        }
        if(AwakenSystem.getMain().runAdd.containsKey(player)){
            LinkedHashMap<String,LinkedHashMap<baseAPI.ItemADDType,int[]>> get = AwakenSystem.getMain().runAdd.get(player);
            for (String itemName : get.keySet()) {// 获取buff
                LinkedHashMap<baseAPI.ItemADDType, int[]> adds = get.get(itemName);
                if(adds.containsKey(type)){
                    if(adds.get(type)[1] > 0){
                        addAttribute += adds.get(type)[0];
                    }
                }
            }
        }
//        if(!playerLinkedHashMapLinkedHashMap.isEmpty()) {
//            for (String name : playerLinkedHashMapLinkedHashMap.keySet()) {
//                if (playerLinkedHashMapLinkedHashMap.get(name).containsKey(player)) {
//                    LinkedHashMap<ItemADDType, Integer> getting = playerLinkedHashMapLinkedHashMap.get(name).get(player);
//                    if (getting.containsKey(type)) {
//                        addAttribute += getting.get(type);
//                    }
//                }
//                playerLinkedHashMapLinkedHashMap.remove(name);
//            }
//
//        }
        return addAttribute;
    }



    /**
     * @deprecated 不获取所有 获取单一属性
     * */
    static LinkedHashMap<ItemADDType,Integer> getAddOnPlayer(Player player){
        LinkedHashMap<ItemADDType,Integer> add = new LinkedHashMap<>();
        Item item = player.getInventory().getItem(35);
        //判断是否有饰品
        if(nbtItems.can_use(player,item)){
            String names = nbtItems.getName(item);
            for(baseAPI.ItemADDType itemADDType:nbtItems.getColde_by_Ring(names).keySet()){
                add.put(itemADDType,nbtItems.getColde_by_Ring(names).get(itemADDType));
            }
        }
        if(AwakenSystem.getMain().runAdd.containsKey(player)){
            LinkedHashMap<String,LinkedHashMap<baseAPI.ItemADDType,int[]>> get = AwakenSystem.getMain().runAdd.get(player);
            for (String itemName : get.keySet()){
                LinkedHashMap<baseAPI.ItemADDType,int[]> adds = get.get(itemName);
                for (baseAPI.ItemADDType addType:adds.keySet()){
                    if(adds.get(addType)[1] > 0){
                        if(add.containsKey(addType)){
                            add.put(addType,add.get(addType)+get.get(itemName).get(addType)[0]);
                        }else{
                            add.put(addType,get.get(itemName).get(addType)[0]);
                        }
                    }
                }
            }
        }


//        if(playerLinkedHashMapLinkedHashMap != null){
//            for(String type:playerLinkedHashMapLinkedHashMap.keySet()){
//                for(ItemADDType addType:playerLinkedHashMapLinkedHashMap.get(type).get(player).keySet()){
//                    if(add.containsKey(addType)){
//                        add.put(addType,add.get(addType)+playerLinkedHashMapLinkedHashMap.get(type).get(player).get(addType));
//                    }else{
//                        add.put(addType,playerLinkedHashMapLinkedHashMap.get(type).get(player).get(addType));
//                    }
//                }
//                playerLinkedHashMapLinkedHashMap.remove(type);
//            }
//        }
        return add;
    }




    /**@deprecated 随机属性
     * @return 随机获得的属性*/
    public static String getAwakenString(){
        int random = new Random().nextInt(defaultAPI.getAttTypeAll().size());
        try {
            return defaultAPI.getAttTypeAll().get(random);
        }catch (Exception e){
            return defaultAPI.getAttTypeAll().get(0);
        }
    }

    /**
     * @param player 玩家
     * @return 是否可以觉醒属性
     * */
    public static boolean studyAwaken(Player player){
        String att = defaultAPI.getPlayerAttributeString(player.getName(),PlayerConfigType.ATTRIBUTE);
        if (!"null".equals(att)) {
            String up = defaultAPI.getUpDataAwaken(att);
            if (up != null && !"null".equals(up)) {
                int l = (int) defaultAPI.getAwakenConfig(up).get(AttType.useLevel.getName());
                int PlayerLevel = defaultAPI.getPlayerAttributeInt(player.getName(), PlayerConfigType.LEVEL);
                return PlayerLevel >= l;
            }
            return false;
        } else {
            return false;
        }
    }
    //觉醒属性等级判断
    /**
     * @param player 玩家
     * @param att 属性
     * @return 判断是否满足觉醒属性等级*/
    public static boolean can_Awaken(Player player,String att){
        int l = (int) defaultAPI.getAwakenConfig(att).get(AttType.useLevel.getName());
        int PlayerLevel = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL);
        return PlayerLevel >= l;
    }

    //获取觉醒需要金钱
    /**
     * @param player 玩家
     * @return 返回觉醒需要金钱*/
    public static double getReduceMoney(Player player){
        int money = AwakenSystem.getMain().getConfig().getInt(baseAPI.ConfigType.DELMONEY.getName());
        int count = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.COUNT);
        return (double)(money * count);
    }
    /**
     * @param player 玩家
     * @return 返回进阶属性需要金钱*/
    public static int getUpDataAwakenMoney(Player player){
        return  defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT) *
                AwakenSystem.getMain().getConfig().getInt(baseAPI.ConfigType.UpDataAwaken.getName());
    }

    /**
     * @param player 玩家
     * @return 判断是否满足进阶属性的等级
     * */
    public static boolean can_UpDataAwaken(Player player) {
        int level = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL);
        int Awaten = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT);
       // int size = defaultAPI.getAttTypeAll().size() - 1;
        return level >= (Awaten * 10);


    }

    /**
     * @return 获取评分数量*/
    //获取玩家最大设定上限值
    public static int getMaxAwaken(){
        return defaultAPI.getChatMessageAll().size() - 1;
    }
    //计算进阶

    //进阶等级

    /**@deprecated 忘记啥用了*/
    public static int getUpDataAwakenLevel(Player player){
        return defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT) * 10;
    }


}
