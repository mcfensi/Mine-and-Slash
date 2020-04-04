package com.robertx22.mine_and_slash.database.spells.spell_classes.bases.cast_types;

import com.google.common.base.Preconditions;
import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.SpellCastContext;
import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.SpellCastType;
import com.robertx22.mine_and_slash.potion_effects.bases.PotionEffectUtils;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.SoundUtils;

public class CastGiveEffect extends SpellCastType {
    @Override
    public boolean cast(SpellCastContext ctx) {
        try {

            Preconditions.checkNotNull(ctx.config.potionEffect);

            PotionEffectUtils.applyToSelf(ctx.config.potionEffect, ctx.caster);

            SoundUtils.playSound(ctx.caster, ctx.config.sound, 1, 1);

            ctx.spell.spawnParticles(ctx);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
