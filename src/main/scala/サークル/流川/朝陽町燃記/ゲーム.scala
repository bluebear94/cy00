package サークル.流川.朝陽町燃記

import scala.util.Random
import java.io.File
import サークル.流川.朝陽町燃記.マップ._
import サークル.流川.朝陽町燃記.引用仕様._

/**
 * @author ウルヰ
 */
class ゲーム(
    val ファイル名: String,
    val い: いんようしよう
    ) {
  val セーブディレクトリ = new File(s"saves/$ファイル名/")
  def セーブファイル(名: String) = new File(セーブディレクトリ, 名)
  var パラ: ワールドパラメター = null
  def 初期化() {
    セーブディレクトリ.mkdirs()
    パラ = new ワールドパラメター(
      new Random,
      ファイル名,
      this
      )
    パラ.ノード実装()
  }
  def ロード() {
    if (!セーブディレクトリ.exists()) 初期化()
    else if (!セーブディレクトリ.isDirectory())
      throw new RuntimeException(
          セーブディレクトリ.toString + "はディレクトリじゃないでファイルです。")
    else {
      // メインコード
    }
  }
}