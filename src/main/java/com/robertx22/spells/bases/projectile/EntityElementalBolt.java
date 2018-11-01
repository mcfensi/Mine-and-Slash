package com.robertx22.spells.bases.projectile;

import com.robertx22.spells.bases.BaseSpellEffect;
import com.robertx22.spells.bases.DamageData;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityElementalBolt extends EntityThrowable {

	BaseSpellEffect effect;
	DamageData data;

	public EntityElementalBolt(World worldIn) {
		super(worldIn);

	}

	public void SetReady(BaseSpellEffect effect, DamageData data) {
		this.effect = effect;
		this.data = data;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.entityHit != null && result.entityHit instanceof EntityLivingBase && effect != null
				&& data != null) {

			if (!world.isRemote) {
				effect.Activate(data, (EntityLivingBase) result.entityHit);
			}

		}

		if (!this.world.isRemote) {
			this.world.setEntityState(this, (byte) 3);
			this.setDead();
		}

	}

	public void SpawnAndShoot(BaseSpellEffect effect, DamageData data, EntityLivingBase caster) {

		this.ignoreEntity = caster;
		Vec3d look = caster.getLookVec();

		SetReady(effect, data);
		setPosition(caster.posX + look.x, caster.posY + look.y + 1.3, caster.posZ + look.z);
		shoot(caster, caster.rotationPitch, caster.rotationYaw, 0.0F, 1.5F, 1.0F);

		world.spawnEntity(this);
	}

}