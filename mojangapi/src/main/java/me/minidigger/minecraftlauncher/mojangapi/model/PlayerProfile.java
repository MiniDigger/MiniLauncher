package me.minidigger.minecraftlauncher.mojangapi.model;

import java.util.List;
import java.util.UUID;

public class PlayerProfile {

	private UUID id;
	private String name;
	// optional
	private boolean legacy;
	// optional
	private boolean demo;
	// only via session server
	private List<ProfileProperty> properties;

	//TODO more stuff, do we really need it tho? https://hasteb.in/axukafab.rb
	
	public UUID getUuid() {
		return id;
	}

	public String getUsername() {
		return name;
	}

	public boolean isLegacy() {
		return legacy;
	}

	public boolean isDemo() {
		return demo;
	}

	//TODO allow decoding textures profile property
	public List<ProfileProperty> getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return "PlayerProfile [id=" + id + ", name=" + name + ", legacy=" + legacy + ", demo=" + demo + ", properties="
				+ properties + "]";
	}
}
