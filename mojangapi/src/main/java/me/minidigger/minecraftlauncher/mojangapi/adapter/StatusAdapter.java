package me.minidigger.minecraftlauncher.mojangapi.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import me.minidigger.minecraftlauncher.mojangapi.model.Status;

public class StatusAdapter {

	@ToJson
	String toJson(Status status) {
		return status.name().toLowerCase();
	}

	@FromJson
	Status fromJson(String status) {
		return Status.valueOf(status.toUpperCase());
	}
}