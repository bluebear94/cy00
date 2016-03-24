package サークル.流川
import scala.language.implicitConversions
import scala.util.Random

/**
 * @author ウルヰ
 */
package object 朝陽町燃記 {
  val ランダム: Random = new Random
  case class 整数(い: BigInt) {
    def ~(ろ: BigInt) = {
      val に = ろ - い
      val ビット数 = (に).bitCount
      var は = BigInt(0)
      do {
        は = BigInt(ビット数, ランダム)
      } while (は >= に)
      は + い
    }
  }
  case class せいすう(い: Int) {
    def ~(ろ: Int) = ランダム.nextInt(ろ - い) + い
  }
  implicit def BigIntから整数まで(い: BigInt) = 整数(い)
  implicit def Intからせいすうまで(い: Int) = せいすう(い)
}