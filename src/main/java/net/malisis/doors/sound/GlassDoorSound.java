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

package net.malisis.doors.sound;

import net.malisis.core.MalisisRegistry;
import net.malisis.doors.DoorState;
import net.malisis.doors.MalisisDoors;
import net.minecraft.util.SoundEvent;

/**
 * @author Ordinastie
 *
 */
public class GlassDoorSound implements IDoorSound
{
	private SoundEvent slidingDoorSound;

	@Override
	public void register()
	{
		slidingDoorSound = MalisisRegistry.registerSound(MalisisDoors.modid, "slidingdooro");
	}

	@Override
	public SoundEvent getSound(DoorState state)
	{
		if (state == DoorState.OPENING || state == DoorState.CLOSING)
			return slidingDoorSound;

		return null;
	}

}
