package サークル.流川.朝陽町燃記.引用仕様

import scopt._
import サークル.流川.朝陽町燃記.言語._

/**
 * @author ウルヰ
 */
trait いんようしよう {
  def スタート(せ: 設定): Unit
  var 現代言語: げんご = null
  def main(引数: Array[String]) {
    現代言語 = new 日本語()
    いんようしよう.パーサー.parse(引数, 設定()) match {
      case Some(s) => {
        val 百万 = BigInt(1000000)
        s.カンスト match {
          case None => スタート(s)
          case Some(`百万`) => println("海外MMOじゃないですよ")
          case Some(_) => println("でもカンスト欲しくない！！！")
        }
      }
      case None => println("馬鹿ですよ")
    }
  }
}

object いんようしよう {
  val パーサー = new OptionParser[設定]("cy00") {
    opt[BigInt]('カ', "カンスト") action { (し, ち) =>
      ち.copy(カンスト = Some(し))
    } text("加えることできる最大ダメージ")
  }
}