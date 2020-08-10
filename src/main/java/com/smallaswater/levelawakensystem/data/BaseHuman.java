package com.smallaswater.levelawakensystem.data;

import cn.nukkit.entity.Entity;
import com.smallaswater.levelawakensystem.data.effects.BaseBuffer;
import com.smallaswater.levelawakensystem.events.defaults.PlayerAddLevelEvent;
import com.smallaswater.levelawakensystem.task.TaskHandle;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author SmallasWater
 */
public abstract class BaseHuman{


    private Entity entity;

    private int level;
    /**
     * Level,Setting,Attribute,
     */
    private float exp;

    private float maxExp;


    private LinkedList<TaskHandle> handles = new LinkedList<>();

    private int levelClass;



    private String occupation;

    private LinkedHashMap<PlayerAttribute,Number> playerAttributes;


    public enum PlayerAttribute{
        /**
         * 生命，攻击.
         * 防御，闪避，精准，暴击，抗暴，
         * */
       HEALTH,DAMAGE,DEFENSE,MISS,MUST_DAMAGE,RESISTANCE,PENETRATION
    }

    public BaseHuman(Entity entity,int level,float exp,float maxExp,int levelClass,String occupation,LinkedHashMap<PlayerAttribute,Number> playerAttributes){
        this.entity = entity;
        this.level = level;
        this.exp = exp;
        this.maxExp = maxExp;
        this.levelClass = levelClass;
        this.occupation = occupation;
        this.playerAttributes = playerAttributes;
    }

    public Number getAttribute(PlayerAttribute attribute){
        if(playerAttributes.containsKey(attribute)){
            return playerAttributes.get(attribute);
        }
        return 0;
    }



    public void setAttribute(PlayerAttribute playerAttribute,Number number){
        playerAttributes.put(playerAttribute,number);
    }

    public static BaseHuman getBaseHumanByMap(Map map){
        int level = 1;
        float exp = 0.0F;
        float maxExp = 301.0F;
        int levelClass = 0;
        String occupation = null;
        LinkedList<TaskHandle> handles = new LinkedList<>();
        LinkedHashMap<PlayerAttribute,Number> attributes = new LinkedHashMap<>();
        if(map.containsKey("level")){
            level = Integer.parseInt(map.get("level").toString());
        }
        if(map.containsKey("exp")){
            exp = (float) Double.parseDouble(map.get("exp").toString());
        }
        if(map.containsKey("maxExp")){
            maxExp = (float) Double.parseDouble(map.get("maxExp").toString());
        }
        if(map.containsKey("buffer")){
            handles = getTaskHandleByMap((Map) map.get("buffer"));
        }
        if(map.containsKey("levelClass")){
            levelClass = Integer.parseInt(map.get("levelClass").toString());
        }
        if(map.containsKey("occupation")){
            occupation = map.get("occupation").toString();
        }
        if(map.containsKey("attribute")){
            attributes = getPlayerAttributeByMap((Map) map.get("attribute"));
        }
        BaseHuman data = new BaseHuman(null, level, exp, maxExp, levelClass, occupation, attributes){};
        data.setHandles(handles);
        return data;

    }



    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getOccupation() {
        return occupation;
    }

    public int getLevel() {
        return level;
    }


    public int getLevelClass() {
        return levelClass;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public void setPlayerAttributes(LinkedHashMap<PlayerAttribute, Number> playerAttributes) {
        this.playerAttributes = playerAttributes;
    }

    public void setHandles(LinkedList<TaskHandle> handles) {
        this.handles = handles;
    }

    public Entity getEntity() {
        return entity;
    }

    public LinkedList<TaskHandle> getHandles() {
        return handles;
    }

    public LinkedList<BaseBuffer> getBuffers(){
        LinkedList<BaseBuffer> baseBuffers = new LinkedList<>();
        for(TaskHandle handle:handles){
            if(!handle.isClose()){
                baseBuffers.add(handle.getBuffer());
            }
        }
        return baseBuffers;
    }

    public void addBuffers(BaseBuffer buffer){
        for(TaskHandle handle:handles){
            if(handle.getBuffer().equals(buffer)){
                return;
            }
        }
        handles.add(new TaskHandle(buffer));
    }

    public void addLevel(int level){
        this.level += level;

    }

    public void addExp(float exp){
        if(this.exp + exp >= this.maxExp){
            this.exp = 0;
            addExp((this.exp + exp) - this.maxExp);
        }else{
            this.exp += exp;
        }
    }


    public void setExp(float exp) {
        this.exp = exp;
        if(this.exp >= this.maxExp){
            this.addExp(this.exp - this.maxExp);
        }
    }

    public void setMaxExp(float maxExp) {
        this.maxExp = maxExp;
    }

    public float getExp() {
        return exp;
    }

    public float getMaxExp() {
        return maxExp;
    }

    public LinkedHashMap<String,Number> getAllAttributeToSave(){
        LinkedHashMap<String,Number> map = new LinkedHashMap<>();
        for(Map.Entry<PlayerAttribute,Number> entry: playerAttributes.entrySet()){
            map.put(entry.getKey().name(),entry.getValue());
        }
        return map;
    }

    public static LinkedHashMap<PlayerAttribute,Number> getPlayerAttributeByMap(Map map){
        LinkedHashMap<PlayerAttribute,Number> attributes = new LinkedHashMap<>();
        if(map == null){
            return attributes;
        }
        for(Object o: map.keySet()){
            attributes.put(PlayerAttribute.valueOf(o.toString()), (Number) map.get(o));
        }
        return attributes;

    }

    public static LinkedList<TaskHandle> getTaskHandleByMap(Map map){
        LinkedList<TaskHandle> attributes = new LinkedList<>();
        if(map == null){
            return attributes;
        }
        Map map1;
        for(Object o: map.keySet()){
            if(map.get(o) instanceof Map) {
                map1 = (Map) map.get(o);
                attributes.add(new TaskHandle(BaseBuffer.getBufferByMap(map1)));
            }
        }
        return attributes;
    }

    public LinkedList<Object> getAllBufferToSave(){
        LinkedList<Object> objects = new LinkedList<>();
        for(TaskHandle handler: handles){
            objects.add(handler.getBuffer().toSave());
        }
        return objects;
    }
}
