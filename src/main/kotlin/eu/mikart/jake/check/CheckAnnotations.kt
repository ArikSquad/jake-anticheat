package eu.mikart.jake.check

@Target(AnnotationTarget.CLASS)
annotation class CheckInfo(
	val name: String,
	val type: String,
	val complexType: String = "",
	val category: CheckCategory,
	val description: String = "",
	val setback: Boolean = false
)

