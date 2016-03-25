package サークル.流川.朝陽町燃記.マップ

/**
 * @author ウルヰ
 */
case class 座標(行列: Long) extends AnyVal {
  def 列 = (行列 & 座標.マスク).toInt - 座標.偏り
  def 行 = (行列 >>> 32).toInt - 座標.偏り
  def 列化(列: Int) = 座標(
    (行列 & ~座標.マスク) |
      ((列 + 座標.偏り).toLong & 座標.マスク))
  def 行化(行: Int) = 座標(
    (行列 & 座標.マスク) |
      ((行 + 座標.偏り).toLong << 32))
  def 左(列: Int = 1) = 座標(行列 - 列)
  def 右(列: Int = 1) = 座標(行列 + 列)
  def 上(行: Int = 1) = 座標(行列 - 座標.上下 * 行)
  def 下(行: Int = 1) = 座標(行列 + 座標.上下 * 行)
}

object 座標 {
  val 偏り = 0x80000000
  val 上下 = 0x100000000L
  val マスク = 0xFFFFFFFFL
  def apply(列: Int, 行: Int): 座標 =
    座標(((行 - 偏り).toLong << 32) |
        ((列 - 偏り).toLong) & マスク)
}