package org.hexcraft.hexattributes;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.gson.annotations.SerializedName;

public class Config  {
	
	// -- General Config
	@SerializedName("configVersion") public String configVersion;
	@SerializedName("bUsePerms") public boolean bUsePerms;
	@SerializedName("bDelayRespecAfterDamage") public boolean bDelayRespecAfterDamage;
	@SerializedName("timeRespecAfterDamage") public int timeRespecAfterDamage;
	@SerializedName("bHideAttributesWithNoPerms") public boolean bHideAttributesWithNoPerms;
	@SerializedName("bShowPlayerNoAttributesMessage") public boolean bShowPlayerNoAttributesMessage;
	@SerializedName("bShowWeblink") public boolean bShowWeblink;
	//@SerializedName("WebLinkString") public String WebLinkString;
	
	@SerializedName("meleeStrikeChance") public float meleeStrikeChance;
	@SerializedName("rangedStrikeChance") public float rangedStrikeChance;
	@SerializedName("meleeReflectChance") public float meleeReflectChance;
	@SerializedName("rangedReflectChance") public float rangedReflectChance;
	
	@SerializedName("bModifyStrikeChanceBasedOnSwingCooldown") public boolean bModifyStrikeChanceBasedOnSwingCooldown;
	
	@SerializedName("slotsMax") public int slotsMax;
	@SerializedName("pointsBase") public int pointsBase;
	
	
	// -- MultiWorld
	@SerializedName("worldWhitelist") public List<String> worldWhitelist;
	@SerializedName("bUseWorldWhiteList") public boolean bUseWorldWhiteList;
	
	// -- we need weapon categories
	@SerializedName("Axes") public List<Material> Axes;
	@SerializedName("Swords") public List<Material> Swords;
	@SerializedName("Projectiles") public List<Material> Projectiles;
	@SerializedName("Armors") public List<Material> Armors;
	
	// -- MobTypes
	@SerializedName("Mobs") public List<EntityType> Mobs;
}
