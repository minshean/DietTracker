package com.diettracker.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "food")
data class FoodProduct(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "cryptoxanthin") var cryptoxanthin: Float? = 0f,
    @ColumnInfo(name = "totalfolates") var totalfolates: Float? = 0f,
    @ColumnInfo(name = "ergocalciferol_d2") var ergocalciferol_d2: Float? = 0f,
    @ColumnInfo(name = "niacin_b3") var niacin_b3: Float? = 0f,
    @ColumnInfo(name = "cobalamin_b12") var cobalamin_b12: Float? = 0f,
    @ColumnInfo(name = "energy_without_dietary_fibre") var energy_without_dietary_fibre: Float? = 0f,
    @ColumnInfo(name = "carbs") var carbs: Float,
    @ColumnInfo(name = "fluoride") var fluoride: Float? = 0f,
    @ColumnInfo(name = "pantothenic_acid_b5") var pantothenic_acid_b5: Float? = 0f,
    @ColumnInfo(name = "thiamin_b1") var thiamin_b1: Float? = 0f,
    @ColumnInfo(name = "folicacid") var folicacid: Float? = 0f,
    @ColumnInfo(name = "retinol") var retinol: Float? = 0f,
    @ColumnInfo(name = "alpha_carotene") var alpha_carotene: Float? = 0f,
    @ColumnInfo(name = "pyridoxine_b6") var pyridoxine_b6: Float? = 0f,
    @ColumnInfo(name = "protein") var proteins: Float,
    @ColumnInfo(name = "fat") var fats: Float,
    @ColumnInfo(name = "tin") var tin: Float? = 0f,
    @ColumnInfo(name = "chloride") var chloride: Float? = 0f,
    @ColumnInfo(name = "omega_g") var omega_g: Float? = 0f,
    @ColumnInfo(name = "zinc") var zinc: Float? = 0f,
    @ColumnInfo(name = "o_poly_fats_g") var o_poly_fats_g: Float? = 0f,
    @ColumnInfo(name = "energy") var energy: Float? = 0f,
    @ColumnInfo(name = "molybdenum") var molybdenum: Float? = 0f,
    @ColumnInfo(name = "phosphorus") var phosphorus: Float? = 0f,
    @ColumnInfo(name = "provitamin_a") var provitamin_a: Float? = 0f,
    @ColumnInfo(name = "alcohol") var alcohol: Float? = 0f,
    @ColumnInfo(name = "total_dietary_fibre") var total_dietary_fibre: Float? = 0f,
    @ColumnInfo(name = "sat_fats_g") var sat_fats_g: Float? = 0f,
    @ColumnInfo(name = "vitamin_c") var vitamin_c: Float? = 0f,
    @ColumnInfo(name = "vitamin_e") var vitamin_e: Float? = 0f,
    @ColumnInfo(name = "magnesium") var magnesium: Float? = 0f,
    @ColumnInfo(name = "galactose") var galactose: Float? = 0f,
    @ColumnInfo(name = "moisture") var moisture: Float? = 0f,
    @ColumnInfo(name = "folatenatural") var folatenatural: Float? = 0f,
    @ColumnInfo(name = "sucrose") var sucrose: Float? = 0f,
    @ColumnInfo(name = "arsenic") var arsenic: Float? = 0f,
    @ColumnInfo(name = "omega") var omega: Float? = 0f,
    @ColumnInfo(name = "sodium") var sodium: Float? = 0f,
    @ColumnInfo(name = "beta_carotene") var beta_carotene: Float? = 0f,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "cadmium") var cadmium: Float? = 0f,
    @ColumnInfo(name = "vitamin_a_retinol_equivalents") var vitamin_a_retinol_equivalents: Float? = 0f,
    @ColumnInfo(name = "sugar") var sugar: Float? = 0f,
    @ColumnInfo(name = "o_poly_fats") var o_poly_fats: Float? = 0f,
    @ColumnInfo(name = "cholecalciferol_d3") var cholecalciferol_d3: Float? = 0f,
    @ColumnInfo(name = "potassium") var potassium: Float? = 0f,
    @ColumnInfo(name = "mercury") var mercury: Float? = 0f,
    @ColumnInfo(name = "dietary_folate_equivalents") var dietary_folate_equivalents: Float? = 0f,
    @ColumnInfo(name = "cobalt") var cobalt: Float? = 0f,
    @ColumnInfo(name = "lactose") var lactose: Float? = 0f,
    @ColumnInfo(name = "manganese") var manganese: Float? = 0f,
    @ColumnInfo(name = "biotin_b7") var biotin_b7: Float? = 0f,
    @ColumnInfo(name = "maltose") var maltose: Float? = 0f,
    @ColumnInfo(name = "maltotriose") var maltotriose: Float? = 0f,
    @ColumnInfo(name = "mono_fats") var mono_fats: Float? = 0f,
    @ColumnInfo(name = "selenium") var selenium: Float? = 0f,
    @ColumnInfo(name = "copper") var copper: Float? = 0f,
    @ColumnInfo(name = "iodine") var iodine: Float? = 0f,
    @ColumnInfo(name = "t_poly_fats_g") var t_poly_fats_g: Float? = 0f,
    @ColumnInfo(name = "nickel") var nickel: Float? = 0f,
    @ColumnInfo(name = "glucose") var glucose: Float? = 0f,
    @ColumnInfo(name = "chromium") var chromium: Float? = 0f,
    @ColumnInfo(name = "antimony") var antimony: Float? = 0f,
    @ColumnInfo(name = "calcium") var calcium: Float? = 0f,
    @ColumnInfo(name = "sulphur") var sulphur: Float? = 0f,
    @ColumnInfo(name = "nitrogen") var nitrogen: Float? = 0f,
    @ColumnInfo(name = "fructose") var fructose: Float? = 0f,
    @ColumnInfo(name = "lead") var lead: Float? = 0f,
    @ColumnInfo(name = "sat_fats") var sat_fats: Float? = 0f,
    @ColumnInfo(name = "mono_fats_g") var mono_fats_g: Float? = 0f,
    @ColumnInfo(name = "ash") var ash: Float? = 0f,
    @ColumnInfo(name = "aluminium") var aluminium: Float? = 0f,
    @ColumnInfo(name = "t_poly_fats") var t_poly_fats: Float? = 0f,
    @ColumnInfo(name = "iron") var iron: Float? = 0f,
    @ColumnInfo(name = "starch") var starch: Float? = 0f,
    @ColumnInfo(name = "riboflavin_b2") var riboflavin_b2: Float? = 0f,
    @ColumnInfo(name="calories") var calories: Float
){

    constructor() : this(
        id = 12,
        name = "",
        carbs = 0f,
        proteins = 0f,
        fats = 0f,
        calories = 0f
    )

    override fun toString(): String {
        return "name = $name id = $id protein = $proteins carbs = $carbs fats = $fats calories = $calories"
    }

}