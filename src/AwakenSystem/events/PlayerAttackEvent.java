package AwakenSystem.events;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerEvent;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class PlayerAttackEvent extends EntityDamageByEntityEvent{

    private float damageW,damageF;
    private float knockBack;
    private static final HandlerList handlers = new HandlerList();

    public PlayerAttackEvent(Entity damager, Entity entity, DamageCause cause, float damageW,float damageF,float knock) {
        super(damager, entity, cause, damageW+damageF,knock);
        this.damageF = damageF;
        this.damageW = damageW;
    }

    @Override
    public void setKnockBack(float knockBack) {
        super.setKnockBack(knockBack);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public Entity getDamager() {
        return super.getDamager();
    }

    @Override
    public Entity getEntity() {
        return super.getEntity();
    }


    public float getDamageW() {
        return damageW;
    }


    public float getDamageF() {
        return damageF;
    }

}
