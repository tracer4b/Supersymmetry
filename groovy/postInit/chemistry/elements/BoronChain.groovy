import static globals.Globals.*
import static globals.SinteringGlobals.*

BATCH_REACTOR = recipemap('batch_reactor')
ROASTER = recipemap('roaster')
MACERATOR = recipemap('macerator')
ELECTROLYTIC_CELL = recipemap('electrolytic_cell')
DISTILLERY = recipemap('distillery')
ARC_FURNACE = recipemap('arc_furnace')
EXTRUDER = recipemap('extruder')
FLUIDIZEDBR = recipemap('fluidized_bed_reactor')
FORMINGPRESS = recipemap('forming_press')

//BORAX CALCINATION
ROASTER.recipeBuilder()
	.inputs(ore('dustBorax') * 4)
	.outputs(metaitem('dustSodiumTetraborate') * 13)
	.fluidOutputs(fluid('steam') * 10000)
	.duration(200)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()	

//DISSOLUTION OF TETRABORATE
BATCH_REACTOR.recipeBuilder()
	.inputs(ore('dustSodiumTetraborate') * 13)
	.fluidInputs(fluid('distilled_water') * 2000)
	.outputs(metaitem('dustTinyClay'))
	.fluidOutputs(fluid('borate_liquor') * 2000)
	.duration(200)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

//PRECIPITATION OF BORIC ACID
BATCH_REACTOR.recipeBuilder()
	.fluidInputs(fluid('borate_liquor') * 2000)
	.fluidInputs(fluid('hydrochloric_acid') * 2000)
	.outputs(metaitem('dustBoricAcid') * 28)
	.fluidOutputs(fluid('salt_water') * 2000)
	.duration(200)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

//CALCINATION OF BORIC ACID
ROASTER.recipeBuilder()
	.inputs(ore('dustBoricAcid') * 14)
	.outputs(metaitem('dustBoronTrioxide') * 5)
	.fluidOutputs(fluid('steam') * 3000)
	.duration(100)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

//REDUCTION OF BORON TRIOXIDE
ROASTER.recipeBuilder()
	.inputs(ore('dustBoronTrioxide') * 5)
	.inputs(ore('dustMagnesium') * 3)
	.outputs(metaitem('dustAmorphousBoron') * 2)
	.outputs(metaitem('dustMagnesia') * 6)
	.duration(100)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

MACERATOR.recipeBuilder()
	.inputs(ore('dustAmorphousBoron') * 2)
	.outputs(metaitem('dustBoron'))
	.duration(100)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

//MAGNESIUM REGERNATION
ELECTROLYTIC_CELL.recipeBuilder()
	.inputs(ore('dustMagnesia') * 2)
	.notConsumable(ore('stickTantalum'))
	.notConsumable(metaitem('graphite_electrode'))
	.notConsumable(fluid('rock_salt') * 288)
	.notConsumable(fluid('magnesium_chloride') * 864)
	.duration(200)
	.outputs(metaitem('dustMagnesium'))
	.fluidOutputs(fluid('oxygen') * 1000)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

//PRODUCTION OF KBF4
BATCH_REACTOR.recipeBuilder()
	.inputs(ore('dustBoricAcid') * 7)
    .fluidInputs(fluid('hydrofluoric_acid') * 4000)
	.fluidOutputs(fluid('tetrafluoroboric_acid') * 1000)
	.duration(600)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

BATCH_REACTOR.recipeBuilder()
	.inputs(ore('dustPotassiumCarbonate') * 6)
	.fluidInputs(fluid('tetrafluoroboric_acid') * 1000)
	.fluidOutputs(fluid('potassium_tetrafluoroborate_solution') * 1000)
	.fluidOutputs(fluid('carbon_dioxide') * 1000)
	.duration(200)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

DISTILLERY.recipeBuilder()
	.fluidInputs(fluid('potassium_tetrafluoroborate_solution') * 1000)
	.outputs(metaitem('dustPotassiumTetrafluoroborate') * 12)
	.fluidOutputs(fluid('water') * 7000)
	.duration(100)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

//ELECTROLYSIS ELECTRODE PRODUCTION
ARC_FURNACE.recipeBuilder()
	.inputs(ore('dustBoronTrioxide') * 10)
	.inputs(ore('dustCarbon') * 7)
	.outputs(metaitem('dustBoronCarbide') * 5)
	.fluidOutputs(fluid('carbon_monoxide') * 6000)
	.duration(300)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

def SINTERING_RECIPES = recipemap("sintering_oven")
for (blanket in sintering_blankets) {
    SINTERING_RECIPES.recipeBuilder()
        .inputs(ore('dustBoronCarbide'))
		.notConsumable(metaitem('shape.mold.rod'))
        .fluidInputs(fluid(blanket.name) * blanket.amountRequired)
        .outputs(metaitem('stickBoronCarbide'))
        .duration(blanket.duration)
        .EUt(Globals.voltAmps[2])
       	.buildAndRegister()
}

FORMINGPRESS.recipeBuilder()
	.inputs(ore('dustBoronCarbide'))
	.notConsumable(metaitem('shape.mold.rod'))
	.outputs(metaitem('stickBoronCarbide'))
	.duration(120)
	.EUt(Globals.voltAmps[3])
	.buildAndRegister()

//ELECTROLYSIS OF B2O3
ELECTROLYTIC_CELL.recipeBuilder()
	.inputs(ore('dustBoronTrioxide') * 5)
	.notConsumable(metaitem('graphite_electrode'))
	.notConsumable(metaitem('stickBoronCarbide'))
	.notConsumable(fluid('sodium_fluoride') * (13 * 144))
	.notConsumable(fluid('potassium_tetrafluoroborate') * (6 * 144))
	.outputs(metaitem('dustElectrolyticBoron') * 2)
	.fluidOutputs(fluid('oxygen') * 3000)
	.duration(120)
	.EUt(120)
	.buildAndRegister()

MACERATOR.recipeBuilder()
	.inputs(ore('dustElectrolyticBoron') * 4)
	.outputs(metaitem('dustBoron') * 3)
	.duration(100)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

//PURIFICATION OF SEMICONDUCTOR-GRADE BORON
FLUIDIZEDBR.recipeBuilder()
	.inputs(ore('dustAmorphousBoron') * 1)
	.fluidInputs(fluid('chlorine') * 4000)
	.outputs(metaitem('dustTinyMagnesiumChloride'))
	.fluidOutputs(fluid('boron_trichloride') * 1000)
	.duration(200)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

FLUIDIZEDBR.recipeBuilder()
	.inputs(ore('dustElectrolyticBoron') * 1)
	.fluidInputs(fluid('chlorine') * 300)
	.outputs(metaitem('dustTinyMagnesiumChloride'))
	.fluidOutputs(fluid('boron_trichloride') * 1000)
	.duration(100)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()

BATCH_REACTOR.recipeBuilder()
	.fluidInputs(fluid('boron_trichloride') * 1000)
	.fluidInputs(fluid('hydrogen') * 3000)
	.outputs(metaitem('dustHighPurityBoron'))
	.fluidOutputs(fluid('hydrogen_chloride') * 3000)
	.duration(100)
	.EUt(Globals.voltAmps[1])
	.buildAndRegister()