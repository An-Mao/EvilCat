package anmao.idoll.evilcat.Entity.Custom.A;

import anmao.idoll.evilcat.Entity._Goal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class _EntityA extends Monster implements GeoEntity {
    protected static final RawAnimation ANIM_WALK = RawAnimation.begin().thenLoop("move.walk");
    protected static final RawAnimation ANIM_IDLE = RawAnimation.begin().thenLoop("idle.idle");
    protected static final RawAnimation ANIM_ATK = RawAnimation.begin().thenLoop("atk.attack");
    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Nullable
    Mob owner;
    @Nullable
    private BlockPos boundOrigin;
    private final Level level =this.level();

    public  _EntityA(EntityType<? extends Monster> pEntityType, Level pLevel) {

        super(pEntityType, pLevel);
        this.moveControl = new _EntityA.DMoveControl(this);
    }

    public static AttributeSupplier setAttributes(){
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH,35.00)
                .add(Attributes.ATTACK_DAMAGE,7.0f)
                .add(Attributes.ATTACK_SPEED,0.5f)
                .add(Attributes.MOVEMENT_SPEED,0.7f).build();
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0,new FloatGoal(this));
        this.goalSelector.addGoal(4,new _EntityA.AtkGoal());
        this.goalSelector.addGoal(8, new _EntityA.VexRandomMoveGoal(this));
        this.goalSelector.addGoal(9,new LookAtPlayerGoal(this, Player.class,3.0F,1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));


        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new _EntityA.DCopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3,new NearestAttackableTargetGoal<>(this, LivingEntity.class,true));
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "Flying", 5, this::flyAnimController));
    }
    protected <E extends _EntityA> PlayState flyAnimController(final AnimationState<E> event) {
        if (event.isMoving()){
            return event.setAndContinue(ANIM_WALK);
        }
        return event.setAndContinue(ANIM_IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    @Override
    public void aiStep() {
        if (!this.level.isClientSide){
            //
        }
        super.aiStep();
    }
    class AtkGoal extends Goal {
        public AtkGoal(){
            this.setFlags(EnumSet.of(Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            LivingEntity livingentity = _EntityA.this.getTarget();
            if (livingentity != null && livingentity.isAlive() && !_EntityA.this.getMoveControl().hasWanted()
                    && _EntityA.this.random.nextInt(reducedTickDelay(7)) == 0) {
                //
                return _EntityA.this.distanceToSqr(livingentity) > 14.0D;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return _EntityA.this.getMoveControl().hasWanted() && _EntityA.this.getTarget() != null && _EntityA.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            LivingEntity livingentity = _EntityA.this.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.getEyePosition();
                _EntityA.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
            }

            //EntityD.this.setIsCharging(true);
            _EntityA.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        @Override
        public void stop() {
            //EntityD.this.setIsCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = _EntityA.this.getTarget();
            if (livingentity != null) {
                if (_EntityA.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    _EntityA.this.doHurtTarget(livingentity);
                    //EntityD.this.setIsCharging(false);
                } else {
                    double d0 = _EntityA.this.distanceToSqr(livingentity);
                    if (d0 < 9.0D) {
                        Vec3 vec3 = livingentity.getEyePosition();
                        _EntityA.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                    }
                }

            }
        }
    }
    class DCopyOwnerTargetGoal extends TargetGoal {
        private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

        public DCopyOwnerTargetGoal(PathfinderMob pMob) {
            super(pMob, false);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return _EntityA.this.owner != null && _EntityA.this.owner.getTarget() != null && this.canAttack(_EntityA.this.owner.getTarget(), this.copyOwnerTargeting);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            _EntityA.this.setTarget(_EntityA.this.owner.getTarget());
            super.start();
        }
    }

    class DMoveControl extends MoveControl {
        public DMoveControl(Mob pMob) {
            super(pMob);
        }
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vec3 = new Vec3(this.wantedX - _EntityA.this.getX(), this.wantedY - _EntityA.this.getY(), this.wantedZ - _EntityA.this.getZ());
                double d0 = vec3.length();
                if (d0 < _EntityA.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    _EntityA.this.setDeltaMovement(_EntityA.this.getDeltaMovement().scale(0.5D));
                } else {
                    _EntityA.this.setDeltaMovement(_EntityA.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05D / d0)));
                    if (_EntityA.this.getTarget() == null) {
                        Vec3 vec31 = _EntityA.this.getDeltaMovement();
                        _EntityA.this.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180F / (float)Math.PI));
                    } else {
                        double d2 = _EntityA.this.getTarget().getX() - _EntityA.this.getX();
                        double d1 = _EntityA.this.getTarget().getZ() - _EntityA.this.getZ();
                        _EntityA.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
                    }
                    _EntityA.this.yBodyRot = _EntityA.this.getYRot();
                }

            }
        }
    }

    class VexRandomMoveGoal extends _Goal {
        public VexRandomMoveGoal(Mob e) {
            super(e);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            BlockPos blockpos = _EntityA.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = _EntityA.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(_EntityA.this.random.nextInt(15) - 7, _EntityA.this.random.nextInt(11) - 5, _EntityA.this.random.nextInt(15) - 7);
                if (_EntityA.this.level.isEmptyBlock(blockpos1)) {
                    _EntityA.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (_EntityA.this.getTarget() == null) {
                        _EntityA.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }
}
