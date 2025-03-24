package net.exaltedlynx.auguracy.common.data;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.setup.AuguracyBlocks;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class AuguracyModelProvider extends ModelProvider
{
    public AuguracyModelProvider(PackOutput output) {
        super(output, Auguracy.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        blockModels.createCrossBlockWithDefaultItem(AuguracyBlocks.MANA_FLOWER.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        itemModels.generateFlatItem(AuguracyItems.MANA_SHARD.get(), ModelTemplates.FLAT_ITEM);
    }
}
