package me.gorgeousone.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NeonColor {

	public static final Color BLUE = new Color(36, 140, 240);
	public static final Color YELLOW = new Color(255, 200, 0);
	public static final Color LIGHT_BLUE = new Color(50, 200, 255);
	public static final Color PINK = new Color(255, 50, 150);
	public static final Color GREEN = new Color(125, 174, 51);
	public static final Color ORANGE = new Color(246, 130, 34);
	public static final Color PURPLE = new Color(157, 63, 255);
	
	public static final Color LIGHT_GRAY = new Color(180, 180, 180);
	public static final Color GRAY = new Color(60, 60, 60);
	public static final Color MIDDLE_GRAY = new Color(55, 55, 55);
	public static final Color DARK_GRAY = new Color(50, 50, 50);
	
	private static final List<Color> NEON_COLORS = new ArrayList<>(Arrays.asList(
			BLUE,
			YELLOW,
			LIGHT_BLUE,
			PINK,
			GREEN,
			ORANGE,
			PURPLE));
	
	private NeonColor() {}
	
	public static Color rndNeonColor() {
		return NEON_COLORS.get((int) (Math.random() * NEON_COLORS.size()));
	}
	
	public static Color nextColor(Color previous) {
		return NEON_COLORS.get((NEON_COLORS.indexOf(previous) + 1) % NEON_COLORS.size());
	}
}
