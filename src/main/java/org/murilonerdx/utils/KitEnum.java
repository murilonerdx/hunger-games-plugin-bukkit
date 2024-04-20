package org.murilonerdx.utils;

import org.bukkit.ChatColor;

public enum KitEnum {
	KIT_RARO("Kit raro", ChatColor.GOLD),
	KIT_FULL("Kit full", ChatColor.AQUA),
	KIT_CURA("Kit cura", ChatColor.GREEN),
	KIT_BASICO("Kit básico",ChatColor.DARK_PURPLE);


	private String descricao;
	private ChatColor color;

	private KitEnum(String descricao, ChatColor color) {
		this.descricao = descricao;
		this.color = color;
	}

	public String getDescricao() {
		return descricao;
	}

	public ChatColor getColor() {
		return color;
	}
}
