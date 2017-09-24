package com.blogspot.jabelarminecraft.examplemod.items.fluidcontainers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.examplemod.blocks.fluids.FluidHandlerSlimeBag;
import com.blogspot.jabelarminecraft.examplemod.utilities.Utilities;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemSlimeBag extends ItemFluidContainer
{
	public ItemSlimeBag() 
	{
		super(Fluid.BUCKET_VOLUME);
		Utilities.setItemName(this, "slime_bag");
		setCreativeTab(CreativeTabs.MISC);
		
		// DEBUG
		System.out.println("Constructing ItemSlimeBag");
	}
	
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt)
    {
    	// DEBUG
    	System.out.println("initCapabilities for ItemSlimeBag");
    	
        return new FluidHandlerSlimeBag(stack);
    }
    
    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World parWorld, @Nonnull EntityPlayer parPlayer, @Nonnull EnumHand parHand)
    {
    	// DEBUG
    	System.out.println("onItemRightClick for ItemSlimeBag");
    
        ItemStack itemStack = parPlayer.getHeldItem(parHand);
        
        FluidStack fluidStack = getFluid(itemStack);
        
        // DEBUG
        System.out.println("finding FluidStack from ItemStack "+fluidStack);
        
//        // empty bucket shouldn't exist, do nothing since it should be handled by the bucket event
//        if (fluidStack == null)
//        {
//        	// DEBUG
//        	System.out.println("Can't use item because fluid stack is null");
//        	
//            return ActionResult.newResult(EnumActionResult.PASS, itemStack);
//        }

        // clicked on a block?
        RayTraceResult mop = this.rayTrace(parWorld, parPlayer, false);

        if(mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return ActionResult.newResult(EnumActionResult.PASS, itemStack);
        }

        // DEBUG
        System.out.println("Slime bag used");
        
        BlockPos clickPos = mop.getBlockPos();
    	
        // can we place liquid there?
        if (parWorld.isBlockModifiable(parPlayer, clickPos))
        {
            // the block adjacent to the side we clicked on
            BlockPos targetPos = clickPos.offset(mop.sideHit);

            // can the player place there?
            if (parPlayer.canPlayerEdit(targetPos, mop.sideHit, itemStack))
            {
                if (fluidStack == null)
                {
                	// DEBUG
                	System.out.println("Fluid stack is empty so try to fill");
                	
                	return tryFill(parWorld, parPlayer, mop, itemStack);
                }
                else
                {
	                // try placing liquid
	                FluidActionResult result = FluidUtil.tryPlaceFluid(parPlayer, parWorld, targetPos, itemStack, fluidStack);
	                
	                // DEBUG
	                System.out.println("Tried to place fluid with result = "+result);
	                
	                if (result.isSuccess() && !parPlayer.capabilities.isCreativeMode)
	                {
	                    // DEBUG
	                    System.out.println("Not in creative so draining containier");
	
	                    // success!
	                    parPlayer.addStat(StatList.getObjectUseStats(this));
	
	                    itemStack.shrink(1);
	                    ItemStack drained = result.getResult();
	                    ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : new ItemStack(this);
	
	                    // DEBUG
	                	System.out.println("Adding empty slime bag to player inventory = "+emptyStack);
	                	
	                    // add empty bucket to player inventory
	                    ItemHandlerHelper.giveItemToPlayer(parPlayer, emptyStack);
	                    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
	                }
                }
            }
         }
        
        // DEBUG
        System.out.println("Failed to place fluid");

        // couldn't place liquid there
        return ActionResult.newResult(EnumActionResult.FAIL, itemStack);
    }
    
    public ActionResult<ItemStack> tryFill(World parWorld, EntityPlayer parPlayer, RayTraceResult parRayTraceTarget, ItemStack parStack)
    {
    	BlockPos pos = parRayTraceTarget.getBlockPos();
        ItemStack singleBucket = parStack.copy();
        singleBucket.setCount(1);

        FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleBucket, parPlayer, parWorld, pos, parRayTraceTarget.sideHit);
        if (filledResult.isSuccess())
        {
        	// DEBUG
        	System.out.println("Successful at picking up fluid item stack = "+filledResult.getResult());
        	
            ItemHandlerHelper.giveItemToPlayer(parPlayer, filledResult.getResult());
            return ActionResult.newResult(EnumActionResult.SUCCESS, filledResult.getResult());
        }
        else
        {
            // DEBUG
        	System.out.println("Not successful at picking up fluid");
            return ActionResult.newResult(EnumActionResult.FAIL, parStack);
        }
    }

    
    @Nullable
	public FluidStack getFluid(final ItemStack container) {
		return FluidUtil.getFluidContained(container);
	}
 }