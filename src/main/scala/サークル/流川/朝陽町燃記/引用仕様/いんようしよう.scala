package サークル.流川.朝陽町燃記.引用仕様

import scopt._
import サークル.流川.朝陽町燃記._
import サークル.流川.朝陽町燃記.言語._

/**
 * @author ウルヰ
 */
trait いんようしよう {
  def スタート(せ: 設定): Unit
  var 現代言語: げんご = null
  var げ: ゲーム = null
  def main(引数: Array[String]) {
    現代言語 = new 日本語()
    いんようしよう.パーサー.parse(引数, 設定()) match {
      case Some(s) => {
        val 百万 = BigInt(1000000)
        s.カンスト match {
          case None => {
            げ = new ゲーム("test", this)
            スタート(s)
          }
          case Some(`百万`) => println("海外MMOじゃないですよ")
          case Some(_) => println("でもカンスト欲しくない！！！")
        }
      }
      case None => println("馬鹿ですよ")
    }
  }
  def ロードバー表示(): Unit
  def ロードバー非表示(): Unit
  def ロードバー進捗(分数: Double): Unit
  def プリント(メッセージ: String): Unit
}

object いんようしよう {
  val パーサー = new OptionParser[設定]("cy00") {
    opt[BigInt]('カ', "カンスト") action { (し, ち) =>
      ち.copy(カンスト = Some(し))
    } text("加えることできる最大ダメージ")
  }
}