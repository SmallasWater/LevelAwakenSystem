package AwakenSystem.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;


/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public interface baseAPI {

    enum PlayerAttType{
        HEALTH(4,"生命"),
        DAMAGE_W(5,"物理攻击"),
        DAMAGE_F(6,"法术攻击"),
        DEFENSE_W(7,"物理防御"),
        DEFENSE_F(8,"法术防御"),
        CRriT(9,"暴击"),
        ANTI_RIOT(10,"抗暴"),
        RESISTANCE(11,"抗性"),
        PENETRATION(12,"穿透");
        protected int type = 0;
        protected String name = null;
        PlayerAttType(int type,String name){
            this.type = type;
            this.name = name;
        }
        public int getType(){
            return this.type;
        }
        public String getName(){
            return this.name;
        }
    }


    enum AttType{
        BE_RESTRAINED(0,"被克制"),
        MESSAGE(1,"介绍"),
        UPDATA(2,"升级加成"),
        UpAtt(3,"可进阶"),
        useLevel(13,"需要等级"),
        useItem(4,"觉醒需要物品"),
        reloadItem(12,"重置需要物品"),
        Particle(5,"粒子"),
        R(6,"r"),
        G(7,"g"),
        B(8,"b"),
        Path(9,"访问类型"),
        Data(10,"路径"),
        ButtonImage(11,"按键图片");
        protected int type = 0;
        protected String name = null;
        AttType(int type,String name){
            this.type = type;
            this.name = name;
        }
        public int getType(){
            return this.type;
        }
        public String getName(){
            return this.name;
        }
    }

    enum FromType{
        //PLACE ("placeholder"),
        //INPUT("input"),
        //LABEL("label"),
        TYPE("type"),
        TEXT("text"),
       // URL("url"),
        PATH("path"),
        DATA("data"),
        IMAGE("image"),
        TITLE("title"),
        CONTENT("content"),
        BUTTONS("buttons"),
        FROM("form"),
        LIST_MODAL("modal"),
        BUTTON1("button1"),
        BUTTON2("button2");
       // DROP_DOWN("dropdown"),
       // STEP_SLIDER("toggle"),
      //  OPTIONS("options"),
     //   DEFAULT_OPTIONS("defaultOptionIndex"),
      //  CUSTOM_FORM("custom_form");
        protected String name;
        FromType(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }

    }


    enum ItemConfigType{
        ID("ID:Damage","264:0"),
        Start("品质","§a"),
        Enchant("附魔光辉",true),
        Message("介绍","这是强化专用宝石"),
        sendCommand("使用指令",new LinkedList<String>());

        protected String name;
        protected Object type;
        ItemConfigType(String name,Object type){
            this.name = name;
            this.type = type;
        }
        public String getName(){
            return this.name;
        }

        public Object getType(){
            return this.type;
        }
    }
    enum ConfigType{
        SETTING(0,"设定"),
        SETTING_CHAT(1,"设定称号"),
        LEVEL_MAX(2,"等级最大值"),
        UPDATA(3,"升级奖励"),
        EXP_ORE(4,"矿物经验"),
        ADDItems(14,"物品收集"),
        EXP_PVP(5,"击杀经验"),
        DELMONEY(7,"觉醒消耗"),
      //  UPDATA_BROADCAST(8,"升级公告"),
        can_show_message(14,"是否更改聊天内容"),
        CHAT_MESSAGE(9,"聊天显示"),
      //  BAN_RPG(10,"禁止使用RPG能力地图"),
        can_show_Tag(15,"是否开启头部显示"),
        TAG_MESSAGE(11,"头部显示"),
        UpDataAwaken(12,"进阶消耗"),
        can_show_Tip(16,"是否开启底部显示"),
        show_Tip_type(17,"底部显示类型"),
        TIP(13,"底部显示"),
        can_show_Boss(18,"是否开启Boss血条显示"),
        showBoss(19,"Boss血条显示内容");
        protected int type = 0;
        protected String name = null;
        ConfigType(int type,String name){
            this.type = type;
            this.name = name;
        }
        public int getType(){
            return this.type;
        }
        public String getName(){
            return this.name;
        }

    }
    enum GunConfig{
        ID("枪械外形","366:0"),
        Bullet("弹夹","17:0"),
        Speed("射速",0.1),
        Count("子弹装载",25),
        Time("装弹时间",5),
        Range("射程",12),
        BulletLike("子弹颜色",new ArrayList<Integer>(){
            {
                add(255);
                add(0);
                add(255);
            }
        }),
        Damage("枪械伤害",5);
        protected String name;
        protected Object type;
        GunConfig(String name,Object type){
            this.name = name;
            this.type = type;
        }

        public Object getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    enum PlayerConfigType{
        LEVEL(0,"等级"),
        TALENT(1,"天赋"),
        ATTRIBUTE(2,"属性"),
        EXP(3,"经验值"),
        /*Skill(4,"技能"),
        Magic(5,"魔法值"),
        MaxMagic(6,"最大魔法值"),*/
        COUNT(6,"觉醒次数");

        protected int type = 0;
        protected String name = null;
        PlayerConfigType(int type,String name){
            this.type = type;
            this.name = name;
        }
        public int getType(){
            return this.type;
        }
        public String getName(){
            return this.name;
        }
    }
    /*
    * {  ///物品栏最后一格有效(26)
    *    ID:Damage:
    *    介绍:
    *    限制使用等级:
    *    限制属性:
    *    类型: 佩戴/使用/强化/特殊
    *    （如果是佩戴 则额外增加)(使用为 加成:持续时间(分钟):(冷却):) (强化: 永久增加(数值不是百分比))）
      *
      *   经验: 5
      *   生命
          物理攻击,
          法术攻击,
          物理防御
          法术防御
          暴击
          抗暴
          抗性
          穿透
      *
    *
    * }
    *
    * */

    enum ArmorType{
        ID("ID:Damage","264:0"),
        MESSAGE("介绍","特殊物品"),
        STAR("品质","*"),
        LEVEL("限制使用等级",0),
        ATT("限制属性",null),
        TYPE("Buff",new LinkedList<String>());
        protected String name;
        protected Object defaultADD;
        ArmorType(String name,Object defaultADD){
            this.name = name;
            this.defaultADD = defaultADD;
        }

        public Object getDefaultADD() {
            return defaultADD;
        }

        public String getName() {
            return name;
        }
    }
    enum ItemType{
        ID("ID:Damage","264:0"),
        MESSAGE("介绍","特殊物品"),
        STAR("品质","*"),
        LEVEL("限制使用等级","0"),
        ATT("限制属性","null"),
        TYPE("类型","强化");
        protected String name;
        protected String defaultADD;
        ItemType(String name,String defaultADD){
            this.name = name;
            this.defaultADD = defaultADD;
        }

        public String getDefaultADD() {
            return defaultADD;
        }

        public String getName() {
            return name;
        }
    }
    enum ItemADDType{
        EXP("经验","10"),
        HEALTH("生命","0"),
        DAMAGE_W("物理攻击","0"),
        DAMAGE_F("法术攻击","0"),
        DEFENSE_W("物理防御","0"),
        DEFENSE_F("法术防御","0"),
        CRriT("暴击","0"),
        ANTI_RIOT("抗暴","0"),
        RESISTANCE("抗性","0"),
        PENETRATION("穿透","0");
        protected String name;
        protected String defaultADD;
        ItemADDType(String name,String defaultADD){
            this.name = name;
            this.defaultADD = defaultADD;
        }
        public String getDefaultADD() {
            return defaultADD;
        }

        public String getName() {
            return name;
        }

    }


    enum ITEM_TYPE{
        UPDATA("强化"),
        USE("使用"),
        ORNAMENTS("饰品"),
        REST("特殊");
        protected String name;
        ITEM_TYPE(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum ModalType{
        RESET,AWAKEN,UpData
    }
    //设定


    String[] Default_First_Name = new String[]{
            "a","b","c","d","e","f","g","h",
            "i","j","k", "l","m","n","o","p","q",
            "r","s","t","u","v","w","x","y","z","0","1","2",
            "3","4","5","6","7","8","9","#"
    };

    LinkedHashMap<String,String> defaultMessage = new LinkedHashMap<String,String>(){
        {
            put("个人信息","textures/ui/copy");
            put("属性","textures/ui/fire_resistance_effect");
            put("进阶评级","textures/ui/invite_base");
//            put("PVP状态","textures/ui/icon_recipe_equipment");
        }
    };

    LinkedHashMap<String,String> defaultAtt = new LinkedHashMap<String,String>(){
        {
            put("属性重置","textures/ui/copy");
            put("属性进阶","textures/ui/backup_replace");
            put("属性觉醒","textures/ui/levitation_effect");
        }
    };


    int modal = 0xffeca001;
    int from_setting = 0xffeca002;
    int chose = 0xffeca003;
    int chose_Att = 0xffeca004;




}
