package wtfcrops.blocks.customcrops.cropbases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.ForgeEventFactory;
import wtfcore.WTFCore;
import wtfcrops.Crops;
import wtfcrops.WTFCropsConfig;
import wtfcrops.renderers.ICropRenderInfo;
import wtfcrops.renderers.RenderIDs;
import wtfcrops.utilities.BlockAndMeta;


public class CustomCrop extends BlockCrops implements ICropRenderInfo{
	
	public Item seed;
	public Item food;
	public int maxGrowth = 7;
	public int baseGrowthRate;
	public Random random = new Random();
	

	public HashMap<Type, Float> biomeMods = new HashMap<Type, Float>();
	public HashMap<BlockAndMeta, Float> farmlandModifier = new HashMap<BlockAndMeta, Float>();
	public int cultivar;

	public CustomCrop(int cultivar, Item seed, Item food){
		//this.setCreativeTab(WTFCrops.WTFCrops);
		this.seed=seed;
		this.food=food;
		this.loadBiomeHashMap();
		this.loadFarmlandHashMap();
		this.cultivar = cultivar;
	}

	@Override
	public boolean canPlaceBlockOn(Block block)
	{
		return farmlandModifier.containsKey(new BlockAndMeta(block, 0));
	}
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return canPlaceBlockOn(world.getBlock(x,y,z));
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return canPlaceBlockOn(world.getBlock(x,y-1,z));
	}
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return null;
	}

	@Override
	public int getRenderType()
	{
		return RenderIDs.customCropRenderer;
	}



	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
	{
		//super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
		this.checkAndDropBlock(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
	}

	@Override
	protected void checkAndDropBlock(World world, int x, int y, int z)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote){
			this.checkAndDropBlock(world, x, y, z);

			if (world.getBlockLightValue(x, y + 1, z) >= 7) {
				int meta = world.getBlockMetadata(x, y, z);
				if (meta < 7) {
					//ShouldGrow contains the method to kill the plant in poor conditions
					if (shouldGrow(world, random, x, y, z)) {
						growCrop(world, x, y, z, meta);
					}
				}
			}
		}
	}

	public void growCrop(World world, int x, int y, int z, int meta){
		world.setBlockMetadataWithNotify(x, y, z, meta+1, 2);
	}

	public boolean shouldGrow(World world, Random random, int x, int y, int z){
		float growthModifier = getGrowthModifier(world, x, y, z);
		//Growth is determined by whether a random number is beneath the config set threshold * cultivar modifier
		//so, making randomGrowth range smaller, increases the chance of plant growth
		int randomGrowth = MathHelper.ceiling_float_int(500/growthModifier);
		//WTFCore.log.info("Random growth rate " + randomGrowth));
		if (random.nextInt(randomGrowth) < baseGrowthRate*getCultivarModifier()){
			return true;
		}
		//if a random float is less than 2x the growth modifier, kill the plant
		else if (WTFCropsConfig.killPlants && random.nextFloat() > growthModifier*3){
			world.setBlockToAir(x,y,z);
		}
		return false;
	}
	
	public float getGrowthModifier(World world, int x, int y, int z){
		float modifier = 1;
		modifier += getBiomeModifier(world, x, z);
		modifier += getFarmlandModifier(world, x, y, z);
		modifier += getLightModifier(world, x, y, z);
		//elevation
		//Scarecrow- get distance to nearest scarecrow
		//if IsRaioning
		//if isSnowing
		//if dwarf cultifar and modifier is negative, halve modifier
		if (cultivar == Crops.dwarf && modifier < 0){modifier = modifier/2;}

		if (modifier < 1){
			modifier = 1/(modifier-1) * -1;
		}
		//WTFCore.log.info("growth modifier " + modifier);
		return modifier;
	}
	public float getSplitGrowthModifier(World world, int x, int ybase, int ytop, int z){
		float modifier = 1;
		modifier += getBiomeModifier(world, x, z);
		modifier += getFarmlandModifier(world, x, ybase, z);
		modifier += getLightModifier(world, x, ytop, z);
		//elevation
		//Scarecrow- get distance to nearest scarecrow
		//if IsRaioning
		//if isSnowing
		//if dwarf cultifar and modifier is negative, halve modifier
		if (cultivar == Crops.dwarf && modifier < 0){modifier = modifier/2;}

		if (modifier < 1){
			modifier = 1/(modifier-1) * -1;
		}
		//WTFCore.log.info("growth modifier " + modifier);
		return modifier;
	}
	

	public float getLightModifier(World world, int x, int y, int z){
		int lightValue = world.getBlockLightValue(x, y, z);
		return (lightValue - 7)/7;
	}

	public float getBiomeModifier(World world, int x, int z){
		float totalModifier = 0;
		float modifier;
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		Type[] biomeTypes =BiomeDictionary.getTypesForBiome(biome);
		for (int loop = 0; loop < biomeTypes.length; loop++){
			if (biomeMods.containsKey(biomeTypes[loop])){
				modifier = biomeMods.get(biomeTypes[loop]);
				totalModifier+= modifier;
			}
		}
		return totalModifier;
	}

	public float getFarmlandModifier(World world, int x, int y, int z){
		BlockAndMeta block = new BlockAndMeta(world, x, y-1, z);
		if (farmlandModifier.containsKey(block)){
			float modifier = farmlandModifier.get(block);
			return modifier;
		}
		else {
			return -5F;
		}

	}

	public float getCultivarModifier(){
		switch (cultivar){
		case Crops.wild://Wild: grows at 3x standard rate, drops: 1-3 seeds, 0/0/1 wheat
			return 3F;
		case Crops.simple://simple: grows at standard rate: 1-3 seeds, 1.5 wheat
			return 1F;
		case Crops.giant://giant grows at 1/3 standard rate, negative modifiers x2, 2x 1-2 seeds, 2-4 wheat
			return 0.5F;
		case Crops.dwarf://dwarf: grows at standard rate, negative modifiers 1/2, 1-2 seeds, 1.5 wheat
			return 1F;
		case Crops.golden://golden: grows at 1/10th standard rate, 10x as much food or golden version
			return 0.1F;
		}
		WTFCore.log.info("Found unexpected cultivar integer");
		return 1F;
	}

	public int getSeedMetaToDrop(int cultivar, Random random, float growthModifier){

		switch (cultivar){
		case Crops.wild:
			if (growthModifier > 1F && random.nextInt(MathHelper.ceiling_double_int(100/growthModifier))==0){return Crops.simple;}
			else {return Crops.wild;}
		case Crops.simple:
			if (growthModifier > 2.5F && random.nextInt(MathHelper.ceiling_double_int(300/growthModifier))==0){return Crops.giant;}
			else if (growthModifier < 1F && random.nextInt(MathHelper.ceiling_double_int(100*growthModifier))==0){return Crops.giant;}
			else {return Crops.simple;}
			//Add a check for a gold block, or gold ore
		case Crops.giant:
			if (growthModifier < 2.5F && random.nextBoolean()){return Crops.simple;}
			else {return Crops.giant;}
		case Crops.dwarf:
			if (growthModifier > 2.5F && random.nextBoolean()){return Crops.simple;}
			else {return Crops.dwarf;}
		case Crops.golden:
			if (growthModifier > 2.5 &&  random.nextBoolean()){return Crops.golden;}
			else {return Crops.simple;}
		}
		if (this.cultivar > 4|| this.cultivar < 0){
			WTFCore.log.info("Plant has cultivar type outside accepted range" + this.getUnlocalizedName() + " " + this.cultivar);
		}
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		WTFCore.log.info("Dropping for " + food.getUnlocalizedName());
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (metadata >= this.maxGrowth)//this is using 7 instead of max growth- if I use max growth later I need to change this
		{
			float growthModifier = getGrowthModifier(world, x, y, z);
			//adds the replacement seeds
			drops.add(new ItemStack(seed, 1, getSeedMetaToDrop(cultivar, random, growthModifier)));
			
			switch (cultivar){
			case Crops.wild://Wild: grows at 3x standard rate, drops: 1-3 seeds, 0/0/1 wheat
				drops = this.getWildDrops(drops, growthModifier);
				break;
			case Crops.simple://simple: grows at standard rate: 1-3 seeds, 1 wheat
				drops = this.getSimpleDrops(drops, growthModifier);
				break;
			case Crops.giant://giant grows at 1/3 standard rate, negative modifiers x2, 2x 1-2 seeds, 2-4 wheat
				drops = this.getGiantDrops(drops, growthModifier);
				break;
			case Crops.dwarf://dwarf: grows at standard rate, negative modifiers 1/2, 1-2 seeds, 1 wheat
				drops = this.getDwarfDrops(drops, growthModifier);
				break;
			case Crops.golden://golden: grows at 1/10th standard rate, 10x as much food or golden version
				drops = this.getGoldenDrops(drops, growthModifier);
				break;
			}
		}
		return drops;
	}
	//Wild drops- plus 1-2 seeds, and a food if seed!= food
	public ArrayList<ItemStack> getWildDrops(ArrayList<ItemStack> drops, float growthModifier){
		for(int loop = random.nextInt(2); loop < 2; loop++){
			drops.add(new ItemStack(seed, 1, getSeedMetaToDrop(cultivar, random, growthModifier)));
		}
		if (food != seed && random.nextBoolean()){
			drops.add(new ItemStack(food, 1, 0));
		}
		return drops;
	}

	//Simple drops
	public ArrayList<ItemStack> getSimpleDrops(ArrayList<ItemStack> drops, float growthModifier){
		if (random.nextBoolean()){ //adds a 2nd seed 50% of the time
			drops.add(new ItemStack(seed, 1, getSeedMetaToDrop(cultivar, random, growthModifier)));
		}
		drops.add(new ItemStack(food, 1, 0));
		if (food != seed){
			drops.add(new ItemStack(food, random.nextInt(1)+1, 0));
		}
		return drops;
	}

	//Dwarf Drops
	public ArrayList<ItemStack> getDwarfDrops(ArrayList<ItemStack> drops, float growthModifier){
		if (random.nextBoolean()){ //adds a 2nd seed 50% of the time
			drops.add(new ItemStack(seed, 1, getSeedMetaToDrop(cultivar, random, growthModifier)));
		}
		drops.add(new ItemStack(food, 1, 0));
		return drops;
	}
	
	//Giant drops
	public ArrayList<ItemStack> getGiantDrops(ArrayList<ItemStack> drops, float growthModifier){
		if (random.nextBoolean()){ //adds a 2nd seed 50% of the time
			drops.add(new ItemStack(seed, 1, getSeedMetaToDrop(cultivar, random, growthModifier)));
		}
		drops.add(new ItemStack(food, random.nextInt(2)+2, 0));
		return drops;
	}
	
	//Golden drops
	public ArrayList<ItemStack> getGoldenDrops(ArrayList<ItemStack> drops, float growthModifier){
		if (random.nextBoolean()){
			drops.add(new ItemStack(seed, 1, getSeedMetaToDrop(cultivar, random, growthModifier)));
		}
		//for high food drops without golden blocks
		for (int loop = random.nextInt(8)-5; loop < 10; loop++){
			drops.add(new ItemStack(food, 1, 0));
		}
		return drops;
	}
   
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
        player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);
        
        harvesters.set(player);
        int i1 = EnchantmentHelper.getFortuneModifier(player);
        this.dropBlockAsItem(world, x, y, z, metadata, i1);
        harvesters.set(null);
        
        if (world.getBlock(x, y-1, z) == Crops.fertileFarmland){
			world.setBlock(x, y-1, z, Crops.customFarmland);
		}
    }
	
	
	

	//These three functions are responsible for bonemeal
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_){
		return world.getBlock(x, y-1, z) == Blocks.farmland;
	}
	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_){
		return true;
	}
	@Override
	public
	void func_149853_b(World world, Random random, int x, int y, int z){
		world.setBlock(x,y-1,z,Crops.fertileFarmland, world.getBlockMetadata(x, y, z), 3);
	}

	@Override
	public void func_149863_m(World p_149863_1_, int p_149863_2_, int p_149863_3_, int p_149863_4_){
		//do nothing- this is the normal growth function, which I've disabled
	}

	protected void loadFarmlandHashMap(){
		WTFCore.log.info("Crop farmlands hashmap empty " + this.getUnlocalizedName());
	}

	protected void loadBiomeHashMap() {
		WTFCore.log.info("Load Biome Hashmap Empty " + this.getUnlocalizedName());
	}



	@Override
	public int getCustomRenderType() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public float getRenderSizer(IBlockAccess world, int x, int y, int z) {
		if (this.cultivar == Crops.dwarf){
			return 0.5F;
		}
		else if (this.cultivar == Crops.giant){
			return 1.5F;
		}
		return 1F;
	}





}
