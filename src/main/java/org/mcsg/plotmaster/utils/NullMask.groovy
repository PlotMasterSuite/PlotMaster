package org.mcsg.plotmaster.utils;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;


public class NullMask implements Mask {
	
	
	boolean test(Vector vector){
		return false
	}
	
	Mask2D toMask2D() {
		return null
	}


}
