package com.robertx22.mine_and_slash.mmorpg.registers.common;

import com.robertx22.mine_and_slash.uncommon.capability.chunk.DungeonChunkCap;
import com.robertx22.mine_and_slash.uncommon.capability.entity.BossCap;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap;
import com.robertx22.mine_and_slash.uncommon.capability.player.*;
import com.robertx22.mine_and_slash.uncommon.capability.server_wide.PlayerCapBackupCap;
import com.robertx22.mine_and_slash.uncommon.capability.server_wide.TeamCap;
import com.robertx22.mine_and_slash.uncommon.capability.world.WorldMapCap;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityRegister {
    public static void register() {

        CapabilityManager.INSTANCE.register(
            EntityCap.UnitData.class, new EntityCap.Storage(), EntityCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(
            PlayerMapCap.IPlayerMapData.class, new PlayerMapCap.Storage(), PlayerMapCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(PlayerCapBackupCap.IPlayerCapBackupData.class,
            new PlayerCapBackupCap.Storage(), PlayerCapBackupCap.DefaultImpl::new
        );

        CapabilityManager.INSTANCE.register(
            ProfessionsCap.IProfessionsData.class, new ProfessionsCap.Storage(), ProfessionsCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(PlayerStatsPointsCap.IPlayerStatPointsData.class,
            new PlayerStatsPointsCap.Storage(), PlayerStatsPointsCap.DefaultImpl::new
        );

        CapabilityManager.INSTANCE.register(
            TeamCap.ITeamData.class, new TeamCap.Storage(), TeamCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(PlayerTalentsCap.IPlayerTalentsData.class, new PlayerTalentsCap.Storage(),
            PlayerTalentsCap.DefaultImpl::new
        );

        CapabilityManager.INSTANCE.register(
            QuestsCap.IQuestsData.class, new QuestsCap.Storage(), QuestsCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(
            PlayerSpellCap.ISpellsCap.class, new PlayerSpellCap.Storage(), PlayerSpellCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(BossCap.IBossData.class, new BossCap.Storage(), BossCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(
            WorldMapCap.IWorldMapData.class, new WorldMapCap.Storage(), WorldMapCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(
            DungeonChunkCap.IDungeonChunkData.class, new DungeonChunkCap.Storage(), DungeonChunkCap.DefaultImpl::new);

        CapabilityManager.INSTANCE.register(
            PlayerLastThrownItemCap.ILastThrownItem.class, new PlayerLastThrownItemCap.Storage(), PlayerLastThrownItemCap.DefaultImpl::new);

    }

}
