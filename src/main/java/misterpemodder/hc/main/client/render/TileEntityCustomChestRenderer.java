package misterpemodder.hc.main.client.render;

import misterpemodder.hc.main.blocks.BlockCustomChest;
import misterpemodder.hc.main.tileentity.TileEntityCustomChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public abstract class TileEntityCustomChestRenderer<TE extends TileEntityCustomChest> extends TileEntitySpecialRenderer<TE> {

	private final ModelChest simpleChest = new ModelChest();
	
	public TileEntityCustomChestRenderer() {}
	
	protected abstract ResourceLocation getTexture();
	
	protected ModelChest getModel() {
		return simpleChest;
	}
	
	@Override
	public void renderTileEntityAt(TE te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        IBlockState state = null;
        if (te.hasWorld()) {
            Block block = te.getBlockType();
            state = block.getStateFromMeta(te.getBlockMetadata());
        }
        ModelChest model = this.simpleChest;

        if (destroyStage >= 0) {
        	this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
        	this.bindTexture(getTexture());
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0)
        	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int j = 0;

        if(state != null) {
        	EnumFacing facing = state.getValue(BlockCustomChest.FACING);
        	j = (int) facing.getHorizontalAngle();
        }
            
        GlStateManager.rotate(j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

        f = 1.0F - f;
        f = 1.0F - f * f * f;
        model.chestLid.rotateAngleX = -(f * ((float)Math.PI / 2F));
        model.renderAll();
        
        if(f== 0 && !te.getLockItemHandler().getStackInSlot(0).isEmpty() && destroyStage < 0) {
        	
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            GlStateManager.translate(0.5F, 0.6F, 0.0625F/1.5);
            GlStateManager.scale(0.5F, -0.5F, -0.5F);
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(te.getLockItemHandler().getStackInSlot(0), TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();

        }
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
        	GlStateManager.matrixMode(5890);
        	GlStateManager.popMatrix();
        	GlStateManager.matrixMode(5888);
        }
	}
	
}
