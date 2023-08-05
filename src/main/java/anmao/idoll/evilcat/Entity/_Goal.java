package anmao.idoll.evilcat.Entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class _Goal extends Goal {
    protected final Mob e;
    protected _Goal(Mob e){
        this.e = e;
    }
    @Override
    public boolean canUse() {
        return e.getMoveControl().hasWanted() && e.getRandom().nextInt(reducedTickDelay(7)) == 0;
    }
}
