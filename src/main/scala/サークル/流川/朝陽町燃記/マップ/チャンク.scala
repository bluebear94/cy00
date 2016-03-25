package サークル.流川.朝陽町燃記.マップ

import scala.collection.mutable.LongMap
import java.io.File

/**
 * @author ウルヰ
 */
class チャンク(
    val チャンク座標: 座標,
    val ノードデータ: Array[ノード]
    ) {
  val ブロック: Array[Int] = new Array[Int](16 * 16)
  val タイル: LongMap[タイルエンティティ] = new LongMap
  val エンティティ: LongMap[生物] = new LongMap
  def ブロック(チャンク内: 座標): Int =
    ブロック((チャンク内.行 << 4) + (チャンク内.列))
  def タイル(チャンク内: 座標): タイルエンティティ =
    タイル(チャンク内.行列)
  def エンティティ(チャンク内: 座標): 生物 =
    エンティティ(チャンク内.行列)
  private def 地域名 = {
    val xr = チャンク座標.列 >> 5
    val yr = チャンク座標.行 >> 5;
    val xnr = チャンク座標.列 & 31
    val ynr = チャンク座標.行 & 31;
    (s"region.$xr.$yr.dat", (xnr << 5) | ynr)
  }
  def ロード(セーブ名: String) = {
    val (region: String, reCoords: Int) = 地域名
    val ファイル = new File(s"saves/$セーブ名/world/$region")
    if (!ファイル.exists) 初期化()
    else if (ファイル.isDirectory)
      throw new RuntimeException(
          ファイル.toString + "はファイルじゃないでディレクトリです")
    else {
      
    }
  }
  def 初期化() {
    
  }
}
