package サークル.流川.朝陽町燃記.マップ

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import java.nio.ByteBuffer
import サークル.流川.朝陽町燃記._

class ワールドパラメター(
    ランダム: Random,
    セーブ名: String,
    げ: ゲーム
    ) {
  val 種: Long = ランダム.nextLong
  val ブロックノード数 = 40
  val ノードサイズ = 16
  // 65536x65536 / 1024x1024
  // 約2.6MB
  val ノードデータ: ByteBuffer =
    ByteBuffer.allocateDirect(64 * 64 * ブロックノード数 * ノードサイズ)
  val ノードカウント: ByteBuffer =
    ByteBuffer.allocateDirect(64 * 64)
  def ゲットノード(オフセット: Int): Option[ノード] = {
    val ノード行列 = ノードデータ.getLong(オフセット)
    if (ノード行列 == 0)
      None
    else Some(ノード(
        座標(ノードデータ.getLong(オフセット)),
        ノードデータ.getShort(オフセット + 8),
        ノードデータ.getShort(オフセット + 10),
        ノードデータ.get(オフセット + 12),
        ノードデータ.get(オフセット + 13),
        ノードデータ.get(オフセット + 14)
    ))
  }
  def セットノード(オフセット: Int, の: ノード): Unit = の match {
    case ノード(座標(gr), tm, hu, vox, voy, vs) => {
      ノードデータ.putLong(オフセット, gr)
      ノードデータ.putShort(オフセット + 8, tm)
      ノードデータ.putShort(オフセット + 10, hu)
      ノードデータ.put(オフセット + 12, vox)
      ノードデータ.put(オフセット + 13, voy)
      ノードデータ.put(オフセット + 14, vs)
    }
  }
  private def チェックインデックス(ここ: 座標): Long = {
    val 行 = (ここ.行 >> 10) + 32
    val 列 = (ここ.列 >> 10) + 32
    val 残行 = ここ.行 & 1023
    val 残列 = ここ.列 & 1023
    val ここのインデックス = (行 << 6) | 列
    // インデックスは４追加必要で
    // １つのインデックスを
    // １６ビットに保存できるから
    // Longを使える
    var インデックスリスト =
      ここのインデックス | 0xFFFFFFFFFFFF0000L
    var i = 1
    def 追加(インデックス: Int) {
      if (i != 4) {
        // 満員じゃないです
        インデックスリスト <<= 16
        インデックスリスト |= インデックス
        i += 1
      }
    }
    val 左 = 残列 < 256 && 列 != 0
    val 右 = 残列 >= 768 && 列 != 63
    val 上 = 残行 < 256 && 行 != 0
    val 下 = 残行 >= 768 && 行 != 63
    if (左) {
      追加(ここのインデックス - 1)
      if (上) 追加(ここのインデックス - 65)
      else if (下) 追加(ここのインデックス + 63)
    }
    else if (右) {
      追加(ここのインデックス + 1)
      if (上) 追加(ここのインデックス - 63)
      else if (下) 追加(ここのインデックス + 65)
    }
    if (上) 追加(ここのインデックス - 64)
    else if (下) 追加(ここのインデックス + 64)
    インデックスリスト
  }
  def 近くノードあり(ここ: 座標): Boolean = {
    var インデックスリスト = チェックインデックス(ここ)
    var i = 0
    while (i < 4) {
      val インデックス = インデックスリスト.toShort
      if (インデックス != -1) {
        var オフセット = ブロックノード数 * ノードサイズ * インデックス
        var 完成 = false
        while (!完成) {
          val ノード列 =
            ノードデータ.getInt(オフセット) + 0x80000000
          if (ノード列 == -0x80000000) {
            完成 = true
          } else {
            val ノード行 =
              ノードデータ.getInt(オフセット + 4) + 0x80000000
            val dx = ここ.列 - ノード列
            val dy = ここ.行 - ノード行
            if (dx * dx + dy * dy < 65536)
              return true
          }
          オフセット += ノードサイズ
        }
      }
      インデックスリスト >>= 16
      i += 1
    }
    return false
  }
  def ノード実装(): Unit = {
    var 失敗数 = 0
    var ノード数 = 0
    while (失敗数 < 10) {
      val 行 = ランダム.nextInt(65536) - 32768
      val 列 = ランダム.nextInt(65536) - 32768
      val インデックス行 = (行 >> 10) + 32
      val インデックス列 = (列 >> 10) + 32
      val インデックス = (インデックス行 << 6) | インデックス列
      val カウント = ノードカウント.get(インデックス)
      if (カウント < ブロックノード数 &&
          !近くノードあり(座標(列, 行))) {
        val オフセット = インデックス * ブロックノード数 * ノードサイズ +
            カウント * ノードサイズ
        // TODO
        セットノード(オフセット, ノード(
            座標(列, 行),
            (ランダム.nextInt(256) + (行 >> 1) + 16384).toShort,
            ランダム.nextInt(32768).toShort,
            0, 0, 64
            ))
        ノードカウント.put(インデックス, (カウント + 1).toByte)
        ノード数 += 1
        失敗数 = 0
      } else {
        失敗数 += 1
      }
    }
    println(ノード数 + "個のノードが実装された。")
  }
}