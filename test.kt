import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon

fun main() {
    val morph = Morph(RoundedPolygon(4), RoundedPolygon(8))
    morph.javaClass.methods.forEach { println(it.name) }
}
