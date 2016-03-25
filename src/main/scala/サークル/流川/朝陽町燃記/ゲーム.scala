package サークル.流川.朝陽町燃記

import java.io.File

/**
 * @author ウルヰ
 */
class ゲーム(
    val ファイル名: String
    ) {
  val セーブディレクトリ = new File(s"saves/$ファイル名/")
  def セーブファイル(名: String) = new File(セーブディレクトリ, 名)
  def 初期化() {
    セーブディレクトリ.mkdirs()
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