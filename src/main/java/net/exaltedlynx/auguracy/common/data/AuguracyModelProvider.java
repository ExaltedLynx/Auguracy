package net.exaltedlynx.auguracy.common.data;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.setup.AuguracyBlocks;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class AuguracyModelProvider extends ModelProvider
{
    public AuguracyModelProvider(PackOutput output) {
        super(output, Auguracy.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {


        createCrossBlockWithDefaultItem(blockModels, AuguracyBlocks.MANA_FLOWER.get(), BlockModelGenerators.PlantType.NOT_TINTED,"cutout");

        itemModels.generateFlatItem(AuguracyItems.MANA_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AuguracyItems.DIVINE_WAND.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AuguracyItems.SPELL_SCROLL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AuguracyItems.SPELL_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
    }

    //credit to ZestyBlaze on the NeoForge Discord
    private void createCrossBlockWithDefaultItem(BlockModelGenerators blockModels, Block block, BlockModelGenerators.PlantType plantType, String renderType)
    {
        blockModels.registerSimpleFlatItemModel(block);
        this.createCrossBlock(blockModels, block, plantType, renderType);
    }

    private void createCrossBlock(BlockModelGenerators blockModels, Block block, BlockModelGenerators.PlantType plantType, String renderType)
    {
        TextureMapping textureMapping = plantType.getTextureMapping(block);
        ResourceLocation resourceLocation = plantType.getCross().extend().renderType(renderType).build().create(block, textureMapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, resourceLocation));
    }

}
