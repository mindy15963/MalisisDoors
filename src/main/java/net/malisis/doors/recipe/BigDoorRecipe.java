/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.malisis.doors.recipe;

import javax.annotation.Nullable;

import net.malisis.core.util.ItemUtils;
import net.malisis.core.util.ItemUtils.ItemStackSplitter;
import net.malisis.core.util.MBlockState;
import net.malisis.doors.MalisisDoors.Blocks;
import net.malisis.doors.block.BigDoor;
import net.malisis.doors.block.BigDoor.Type;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * @author Ordinastie
 *
 */
public class BigDoorRecipe implements IRecipe
{
	private BigDoor.Type type;
	private ResourceLocation registryName;

	public BigDoorRecipe(BigDoor.Type type)
	{
		this.type = type;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		return !getMatching(inv).isEmpty();
	}

	public ItemStack getMatching(InventoryCrafting inv)
	{
		boolean doorMatch = false;
		ItemStack frame = ItemStack.EMPTY;
		int frameSize = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack itemStack = inv.getStackInSlot(i);
			if (itemStack.isEmpty())
				continue;
			if (itemStack.getItem() == type.door)
			{
				if (doorMatch == true) //two doors
					return ItemStack.EMPTY;
				doorMatch = true;
				continue;
			}

			Block block = Block.getBlockFromItem(itemStack.getItem());
			if (block == net.minecraft.init.Blocks.AIR || (!frame.isEmpty() && !ItemUtils.areItemStacksStackable(frame, itemStack)))
				return ItemStack.EMPTY;
			frame = itemStack;
			frameSize += itemStack.getCount();
		}

		return !doorMatch || frameSize < 5 ? ItemStack.EMPTY : frame;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack frame = getMatching(inv);
		if (frame.isEmpty())
			return ItemStack.EMPTY;

		ItemStack itemStack = new ItemStack(type == Type.CARRIAGE ? Blocks.carriageDoor : Blocks.medievalDoor);
		NBTTagCompound nbt = new NBTTagCompound();
		MBlockState.toNBT(nbt, ItemUtils.getStateFromItemStack(frame));
		itemStack.setTagCompound(nbt);
		return itemStack;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return new ItemStack(type == Type.CARRIAGE ? Blocks.carriageDoor : Blocks.medievalDoor);
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width <= 2 && height <= 2;
	}

	@Override
	public IRecipe setRegistryName(ResourceLocation name)
	{
		registryName = name;
		return this;
	}

	@Override
	@Nullable
	public ResourceLocation getRegistryName()
	{
		return registryName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IRecipe> getRegistryType()
	{
		return (Class<IRecipe>) this.getClass();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		NonNullList<ItemStack> itemStacks = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		int left = 5;
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack itemStack = inv.getStackInSlot(i);
			inv.setInventorySlotContents(i, ItemStack.EMPTY);
			if (itemStack.isEmpty())
				continue;

			ItemStackSplitter iss = new ItemStackSplitter(itemStack);
			iss.split(itemStack.getItem() == type.door ? 1 : left);
			itemStacks.set(i, iss.source);
			if (itemStack.getItem() != type.door)
				left -= iss.amount;
		}

		return itemStacks;
	}

}
